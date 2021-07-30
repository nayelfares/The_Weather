package com.example.theweather.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.theweather.model.CurrentWeatherResponse
import java.io.Serializable

@Entity(tableName = "current_weather")
data class CurrentWeatherCacheEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "icon")
    val icon:String,

    @ColumnInfo(name = "text")
    val text:String,

    @ColumnInfo(name = "temp_c")
    val temp_c:String,

    @ColumnInfo(name = "name")
    val name:String,

    @ColumnInfo(name = "humidity")
    val humidity:String,

    @ColumnInfo(name = "wind_kph")
    val wind_kph:String,
):Serializable


fun CurrentWeatherResponse.toCurrentWeatherCacheEntity(id:String):CurrentWeatherCacheEntity {
    return CurrentWeatherCacheEntity(
        url = id,
        icon = this.current.condition.icon,
        text = this.current.condition.text,
        temp_c = this.current.temp_c.toString(),
        name = this.location.name,
        humidity = this.humidity.toString(),
        wind_kph = this.wind_kph.toString()
    )
}
