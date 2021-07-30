package com.example.theweather.network

import android.content.Context
import androidx.room.Room
import com.example.theweather.room.CurrentWeatherDao
import com.example.theweather.room.TheWeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Module

@InstallIn(SingletonComponent::class)
object RoomModule {
    const val DATABASE_NAME =  "the_weather_db"

    @Singleton
    @Provides
    fun provideArtistDB(@ApplicationContext context: Context): TheWeatherDatabase {
        return Room.databaseBuilder(
            context,TheWeatherDatabase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    @Singleton
    @Provides
    fun provideArtistDao(theWeatherDatabase: TheWeatherDatabase): CurrentWeatherDao {
        return theWeatherDatabase.currentWeatherDao()
    }

}