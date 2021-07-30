package com.example.theweather.model

data class City (
    val id: Long,
    val name:String,
    val region : String,
    val country: String,
    val lat:Float,
    val lon :Float,
    val url : String
)