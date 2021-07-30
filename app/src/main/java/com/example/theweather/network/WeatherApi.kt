package com.example.theweather.network

import com.example.theweather.model.City
import com.example.theweather.model.CurrentWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.ArrayList

interface WeatherApi {
        @GET("search.json")
        suspend fun getSuggestions(@Query("key") key:String, @Query("q") q:String
        ): ArrayList<City>

        @GET("current.json")
        suspend fun getSCurrentWeather(@Query("key") key:String, @Query("q") q:String
        ): CurrentWeatherResponse
}