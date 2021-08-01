package com.example.theweather.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.theweather.MainActivity
import com.example.theweather.R
import com.example.theweather.WeatherViewModel
import com.example.theweather.base.DataState
import com.example.theweather.intent.WeatherIntent
import com.example.theweather.model.CurrentWeatherResponse
import com.example.theweather.room.CurrentWeatherCacheEntity
import com.example.theweather.room.CurrentWeatherDao
import com.example.theweather.room.toCurrentWeatherCacheEntity
import com.example.theweather.service.DailyNotificationService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_current_weather.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class CurrentWeatherFragment : Fragment(R.layout.fragment_current_weather) {
    private var location: String? = null
    private var currentWeather:CurrentWeatherCacheEntity?=null
    private var currentWeatherResponse:CurrentWeatherResponse?=null
    @Inject
    lateinit var weatherViewModel: WeatherViewModel

    @Inject
    lateinit var currentWeatherDao: CurrentWeatherDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            location = it.getString("location")
            currentWeather = it.getSerializable("currentWeather") as CurrentWeatherCacheEntity?
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        celsius.isSelected = true
        fahrenheit.isSelected = false
        celsius.setOnClickListener {
            celsius.isSelected = true
            fahrenheit.isSelected = false
            if (currentWeather!=null)
                tempratureC.text = currentWeather!!.temp_c
            else
                tempratureC.text = currentWeatherResponse!!.current.temp_c.toString()
            unit.text = "c"
        }
        fahrenheit.setOnClickListener {
            celsius.isSelected = false
            fahrenheit.isSelected = true
            if (currentWeather!=null)
                tempratureC.text = (currentWeather!!.temp_c.toFloat()*1.8+32).toInt().toString()
            else
                tempratureC.text = (currentWeatherResponse!!.current.temp_c*1.8+32).toInt().toString()
            unit.text = "f"
        }
        // Search for new location
        addCity.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragmentFromMain(SearchFragment.newInstance())
        }
        //Show Bottom sheet that caonains favorite cities
        selectCity.setOnClickListener {
            FavoritCities.newInstance().show(requireActivity().supportFragmentManager,"Favorite Cities")
        }
        swipToRefresh.setOnRefreshListener {
            //Pull to refresh implementation
            if (currentWeather==null)
                lifecycleScope.launch {
                    weatherViewModel.userIntent.send(WeatherIntent.GetCurrentWeather(location!!))
                }
            else{
                lifecycleScope.launch {
                    weatherViewModel.userIntent.send(WeatherIntent.GetCurrentWeather(currentWeather!!.url))
                }
            }
        }
        if (currentWeather==null)
            lifecycleScope.launch {
                weatherViewModel.userIntent.send(WeatherIntent.GetCurrentWeather(location!!))
            }
        else {
            //Show current weather data from cache
            Glide.with(requireActivity())
                .load("http:/"+currentWeather!!.icon)
                .into(icon)
            status.text = currentWeather!!.text
            tempratureC.text = currentWeather!!.temp_c
            city.text  = currentWeather!!.name
            humidity.text = currentWeather!!.humidity
            wind.text= currentWeather!!.wind_kph
        }
    }
// receiving data from API
    private fun subscribeObservers() {
        weatherViewModel.currentWeatherDataState.observe(
            viewLifecycleOwner, {
                when (it) {
                    is DataState.Success<CurrentWeatherResponse> -> {
                        currentWeatherResponse = it.data
                        //Caching current weather
                        lifecycleScope.launch {
                            currentWeatherDao.insert(it.data.toCurrentWeatherCacheEntity(location!!))
                        }
                        //Show current weather data
                        Glide.with(requireActivity())
                            .load("http:/"+it.data.current.condition.icon)
                            .into(icon)
                        status.text = it.data.current.condition.text
                        tempratureC.text = it.data.current.temp_c.toString()
                        city.text  = it.data.location.name
                        humidity.text = it.data.humidity.toString()
                        wind.text= it.data.wind_kph.toString()
                        loading.visibility = View.GONE
                        //Create Daily Notification
                        val alarmManager =
                            requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                        val pendingIntent = PendingIntent.getService(
                            requireContext(), 0,
                            Intent(requireContext(), DailyNotificationService::class.java),
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )
                        val calendar = Calendar.getInstance()
                        calendar.set(Calendar.SECOND, 0)
                        calendar.set(Calendar.MINUTE, 0)
                        calendar.set(Calendar.HOUR_OF_DAY, 6)
                        alarmManager.setInexactRepeating(
                            AlarmManager.RTC_WAKEUP,
                            calendar.timeInMillis, 0, pendingIntent
                        )
                    }
                    is DataState.Error -> {
                        loading.visibility = View.GONE
                    }
                    else -> {
                        swipToRefresh.isRefreshing = false
                        loading.visibility = View.VISIBLE
                    }
                }
            })
    }

    companion object {

        @JvmStatic
        fun newInstance(location: String,currentWeatherCacheEntity: CurrentWeatherCacheEntity?=null) =
            CurrentWeatherFragment().apply {
                arguments = Bundle().apply {
                    putString("location", location)
                    putSerializable("currentWeather",currentWeatherCacheEntity)
                }
            }
    }
}