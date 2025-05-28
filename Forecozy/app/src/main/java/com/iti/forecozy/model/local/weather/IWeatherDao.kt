package com.iti.forecozy.model.local.weather

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.iti.forecozy.model.dto.weather.CurrentWeatherResponse
import com.iti.forecozy.model.dto.weather.ForecastItem
import com.iti.forecozy.model.dto.weather.WeatherWithForecast

@Dao
interface IWeatherDao {
    // Current Weather Operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(weather: CurrentWeatherResponse)

    @Query("SELECT * FROM current_weather WHERE locationKey = :locationKey")
    suspend fun getCurrentWeather(locationKey: String): CurrentWeatherResponse?

    // Forecast Operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(forecast: List<ForecastItem>)

    @Query("SELECT * FROM forecast WHERE locationKey = :locationKey ORDER BY timestamp ASC")
    suspend fun getForecast(locationKey: String): List<ForecastItem>

    // Combined Operations
    @Transaction
    @Query("SELECT * FROM current_weather WHERE locationKey = :locationKey")
    suspend fun getWeatherWithForecast(locationKey: String): WeatherWithForecast

    @Transaction
    @Query("DELETE FROM forecast WHERE locationKey = :locationKey")
    suspend fun clearOldForecast(locationKey: String)
}