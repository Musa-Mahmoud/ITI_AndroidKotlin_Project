package com.iti.forecozy.model.repository.weather

import com.iti.forecozy.model.dto.weather.CurrentWeatherResponse
import com.iti.forecozy.model.dto.weather.FavoriteLocation
import com.iti.forecozy.model.dto.weather.ForecastItem
import com.iti.forecozy.model.dto.weather.WeatherAlert
import com.iti.forecozy.model.dto.weather.WeatherWithForecast
import com.iti.forecozy.model.local.weather.IWeatherLocalDataSource

class FakeLocalDataSource : IWeatherLocalDataSource {
    private val currentWeatherMap = mutableMapOf<String, CurrentWeatherResponse>()
    private val forecastMap = mutableMapOf<String, MutableList<ForecastItem>>()
    private val weatherWithForecastMap = mutableMapOf<String, WeatherWithForecast>()

    override suspend fun getCurrentWeather(locationKey: String): CurrentWeatherResponse? {
        return currentWeatherMap[locationKey]
    }

    override suspend fun saveCurrentWeather(weather: CurrentWeatherResponse) {
        val key = "${weather.coordinates.latitude},${weather.coordinates.longitude}"
        currentWeatherMap[key] = weather
    }

    override suspend fun getForecast(locationKey: String): List<ForecastItem> {
        return forecastMap[locationKey] ?: emptyList()
    }

    override suspend fun saveForecast(forecasts: List<ForecastItem>) {
        forecasts.forEach { forecast ->
            val key = forecast.locationKey
            val list = forecastMap.getOrPut(key) { mutableListOf() }
            list.add(forecast)
        }
    }

    override suspend fun getWeatherWithForecast(locationKey: String): WeatherWithForecast? {
        return weatherWithForecastMap[locationKey]
    }

    // Implement other methods with default behavior
    override suspend fun clearOldForecast(locationKey: String) = Unit
    override suspend fun saveWeatherAlert(alert: WeatherAlert) = Unit
    override suspend fun dismissWeatherAlert(alertId: Long) = Unit
    override suspend fun deleteAlert(alertId: Long) = Unit
    override suspend fun deleteAlert(alert: WeatherAlert) = Unit

    override suspend fun getWeatherAlerts(): List<WeatherAlert> = emptyList()
    override suspend fun addFavoriteLocation(lat: Double, lon: Double, name: String, country: String) = Unit
    override suspend fun removeFavorite(locationKey: String) = Unit
    override suspend fun saveFavoriteLocation(location: FavoriteLocation) = Unit

    override suspend fun getFavoriteLocations(): List<FavoriteLocation> = emptyList()
    override suspend fun isFavorite(locationKey: String): Boolean = false
}