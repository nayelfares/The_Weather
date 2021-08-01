package com.example.theweather.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.theweather.model.City
import java.io.Serializable

@Entity(tableName = "cities")
data class CityCacheEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "name")
    val name:String,

    @ColumnInfo(name = "region")
    val region:String,

    @ColumnInfo(name = "country")
    val country:String,

    @ColumnInfo(name = "lat")
    val lat:Float,

    @ColumnInfo(name = "lon")
    val lon:Float,

    @ColumnInfo(name = "url")
    val url:String,
):Serializable


fun City.toCityCacheEntity():CityCacheEntity {
    return CityCacheEntity(
        id      = this.id,
        name    = this.name,
        region  = this.region,
        country = this.country,
        lat     = this.lat,
        lon     = this.lon,
        url     = this.url
    )
}
