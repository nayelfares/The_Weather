package com.example.theweather.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CurrentWeatherCacheEntity::class], version = 1)
abstract class TheWeatherDatabase: RoomDatabase(){
    abstract fun currentWeatherDao():CurrentWeatherDao
}