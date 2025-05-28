package com.iti.forecozy.model.repository.weather

import com.iti.forecozy.model.dto.weather.CurrentWeatherResponse
import com.iti.forecozy.model.dto.weather.FavoriteLocation
import com.iti.forecozy.model.dto.weather.ForecastItem
import com.iti.forecozy.model.dto.weather.WeatherAlert
import com.iti.forecozy.model.dto.weather.WeatherWithForecast
import com.iti.forecozy.utils.types.Result

interface IWeatherRepository {
    // Weather operations
    suspend fun getCurrentWeather(lat: Double, lon: Double): Result<CurrentWeatherResponse>
    suspend fun getForecast(lat: Double, lon: Double): Result<List<ForecastItem>>
    suspend fun getWeatherWithForecast(lat: Double, lon: Double): Result<WeatherWithForecast>
    suspend fun refreshWeatherData(lat: Double, lon: Double): Result<Unit>

    // Alert operations
    suspend fun saveAlert(alert: WeatherAlert): Result<Unit>
    suspend fun dismissAlert(alertId: Long): Result<Unit>
    suspend fun deleteAlert(alertId: Long): Result<Unit>
    suspend fun getActiveAlerts(): Result<List<WeatherAlert>>

    // Favorite locations operations
    suspend fun addFavoriteLocation(
        lat: Double,
        lon: Double,
        name: String,
        country: String
    ): Result<Unit>

    suspend fun removeFavoriteLocation(locationKey: String): Result<Unit>
    suspend fun getFavoriteLocations(): Result<List<FavoriteLocation>>
    suspend fun isFavoriteLocation(locationKey: String): Result<Boolean>
}