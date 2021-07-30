package com.example.theweather.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cityCacheEntity: CityCacheEntity):Long

    @Query("SELECT * FROM cities")
    suspend fun get():List<CityCacheEntity>
}