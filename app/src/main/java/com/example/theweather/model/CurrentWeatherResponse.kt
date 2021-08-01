package com.example.theweather.model


data class CurrentWeatherResponse(
    val location    : Location,
    val current     : Current,
    val humidity    : Int,
    val feelslike_c : Float,
    val feelslike_f : Float,
    val wind_kph    : Float
)
data class Location (
            val name            : String,
            val region          : String,
            val country         : String,
            val lat             : Float,
            val lon             : Float,
            val tz_id           : String,
            val localtime_epoch : String,
            val localtime       : String
        )

data class Current(
    val last_updated_epoch : Long,
    val last_updated       : String,
    val temp_c             : Float,
    val temp_f             : Float,
    val is_day             : Int,
    val condition          : Condition
)


data class Condition(
    val text : String,
    val icon :String,
    val code:Long
)


