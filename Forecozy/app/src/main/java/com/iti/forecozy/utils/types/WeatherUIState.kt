package com.iti.forecozy.utils.types

import com.iti.forecozy.model.dto.weather.WeatherWithForecast

sealed class WeatherUIState {
    object Loading : WeatherUIState()
    data class Success(val weatherData: WeatherWithForecast) : WeatherUIState()
    data class Error(val message: String) : WeatherUIState()
}
