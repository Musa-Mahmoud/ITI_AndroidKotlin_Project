package com.iti.forecozy.model.local.weather

import com.iti.forecozy.model.dto.weather.CurrentWeatherResponse
import com.iti.forecozy.model.dto.weather.FavoriteLocation
import com.iti.forecozy.model.dto.weather.ForecastItem
import com.iti.forecozy.model.dto.weather.WeatherAlert
import com.iti.forecozy.model.dto.weather.WeatherWithForecast

interface IWeatherLocalDataSource {
    // Weather
    suspend fun saveCurrentWeather(currentWeather: CurrentWeatherResponse)
    suspend fun getCurrentWeather(locationKey: String): CurrentWeatherResponse?
    suspend fun saveForecast(forecastItems: List<ForecastItem>)
    suspend fun getForecast(locationKey: String): List<ForecastItem>
    suspend fun getWeatherWithForecast(locationKey: String): WeatherWithForecast?
    suspend fun clearOldForecast(locationKey: String)


    // WeatherAlerts
    suspend fun saveWeatherAlert(alert: WeatherAlert)
    suspend fun dismissWeatherAlert(alertId: Long)
    suspend fun deleteAlert(alertId: Long)
    suspend fun deleteAlert(alert: WeatherAlert)
    suspend fun getWeatherAlerts(): List<WeatherAlert>

    // Favorites
    suspend fun addFavoriteLocation(lat: Double, lon: Double, name: String, country: String)
    suspend fun removeFavorite(locationKey: String)
    suspend fun saveFavoriteLocation(location: FavoriteLocation)
//    suspend fun deleteFavoriteLocation(locationId: Int)
    suspend fun getFavoriteLocations(): List<FavoriteLocation>
    suspend fun isFavorite(locationKey: String): Boolean
}