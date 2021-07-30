package com.example.theweather.network

import com.example.theweather.WeatherViewModel
import com.example.theweather.repository.WeatherRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import retrofit2.Retrofit
import javax.inject.Singleton

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
object WeatherModule {
    @Singleton
    @Provides
    fun provideRecordingApi(retrofit: Retrofit.Builder): WeatherApi{
        return retrofit.build()
            .create(WeatherApi::class.java)
    }

    @Singleton
    @Provides
    fun provideWeatherRepo(
        weatherApi: WeatherApi
    ): WeatherRepo {
        return WeatherRepo(weatherApi)
    }

    @Singleton
    @Provides
    fun provideWeatherViewModel(
        weatherRepo: WeatherRepo
    ): WeatherViewModel {
        return WeatherViewModel(weatherRepo)
    }

}