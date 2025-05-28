package com.iti.forecozy.model.dto.weather

import androidx.room.Embedded
import androidx.room.Relation

data class WeatherWithForecast(
    @Embedded val currentWeather: CurrentWeatherResponse,
    @Relation(parentColumn = "locationKey", entityColumn = "locationKey")
    val forecast: List<ForecastItem>
)