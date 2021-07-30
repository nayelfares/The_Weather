package com.example.theweather.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.theweather.R
import com.example.theweather.WeatherViewModel
import com.example.theweather.base.DataState
import com.example.theweather.intent.WeatherIntent
import com.example.theweather.model.CurrentWeatherResponse
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
    @Inject
    lateinit var weatherViewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            location = it.getString("location")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        lifecycleScope.launch {
            weatherViewModel.userIntent.send(WeatherIntent.GetCurrentWeather(location!!))
        }
    }

    private fun subscribeObservers() {
        weatherViewModel.currentWeatherDataState.observe(
            viewLifecycleOwner, {
                when (it) {
                    is DataState.Success<CurrentWeatherResponse> -> {
                        status.text = it.data.current.condition.text
                        tempratureC.text = it.data.current.temp_c.toString()
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
        fun newInstance(location: String) =
            CurrentWeatherFragment().apply {
                arguments = Bundle().apply {
                    putString("location", location)
                }
            }
    }
}