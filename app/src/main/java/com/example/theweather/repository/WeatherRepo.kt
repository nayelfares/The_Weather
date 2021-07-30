package com.example.theweather.repository

import android.util.Log
import com.example.theweather.BuildConfig
import com.example.theweather.base.DataState
import com.example.theweather.model.City
import com.example.theweather.model.CurrentWeatherResponse
import com.example.theweather.network.WeatherApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepo
@Inject
constructor(private  val weatherApi: WeatherApi) {
    fun getSuggestions(query: String): Flow<DataState<ArrayList<City>>> = flow {
        emit(DataState.Loading)
        try {
            val data: ArrayList<City> = weatherApi.getSuggestions(BuildConfig.KEY,query)
            emit(DataState.Success(data))
        } catch (e: Exception) {
            Log.i("Excep", e.toString())
            emit(DataState.Error(e))
        }
    }

    fun getCurrentWeather(query: String): Flow<DataState<CurrentWeatherResponse>> = flow {
        emit(DataState.Loading)
        try {
            val data: CurrentWeatherResponse = weatherApi.getSCurrentWeather(BuildConfig.KEY,query)
            emit(DataState.Success(data))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }
}
