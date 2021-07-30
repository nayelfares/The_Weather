package com.example.theweather.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currentWeatherCacheEntity: CurrentWeatherCacheEntity):Long

    @Query("SELECT * FROM current_weather")
    suspend fun get():List<CurrentWeatherCacheEntity>
}