package com.example.theweather.ui

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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_current_weather.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class CurrentWeatherFragment : Fragment(R.layout.fragment_current_weather) {
    private var location: String? = null
    private var currentWeather:CurrentWeatherCacheEntity?=null
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
        addCity.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragmentFromMain(SearchFragment.newInstance())
        }
        selectCity.setOnClickListener {
            FavoritCities.newInstance().show(requireActivity().supportFragmentManager,"Favorite Cities")
        }
        if (currentWeather==null)
            lifecycleScope.launch {
                weatherViewModel.userIntent.send(WeatherIntent.GetCurrentWeather(location!!))
            }
        else {
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

    private fun subscribeObservers() {
        weatherViewModel.currentWeatherDataState.observe(
            viewLifecycleOwner, {
                when (it) {
                    is DataState.Success<CurrentWeatherResponse> -> {
                        lifecycleScope.launch {
                            currentWeatherDao.insert(it.data.toCurrentWeatherCacheEntity(location!!))
                        }
                        Glide.with(requireActivity())
                            .load("http:/"+it.data.current.condition.icon)
                            .into(icon)
                        status.text = it.data.current.condition.text
                        tempratureC.text = it.data.current.temp_c.toString()
                        city.text  = it.data.location.name
                        humidity.text = it.data.humidity.toString()
                        wind.text= it.data.wind_kph.toString()
                        loading.visibility = View.GONE

                    }
                    is DataState.Error -> {
                        loading.visibility = View.GONE
                    }
                    else -> {
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