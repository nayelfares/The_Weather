package com.example.theweather.intent

sealed class WeatherIntent {
    class GetSuggestions(val query: String) : WeatherIntent()
}