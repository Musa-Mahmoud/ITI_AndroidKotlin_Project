package com.iti.forecozy.model.remote

import com.iti.forecozy.BuildConfig
import com.iti.forecozy.model.dto.weather.CurrentWeatherResponse
import com.iti.forecozy.model.dto.weather.ForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface IWeatherService {
    companion object {
        const val UNIT_METRIC = "metric"
        const val DEFAULT_LANGUAGE = "en"
    }

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String = UNIT_METRIC,
        @Query("lang") language: String = DEFAULT_LANGUAGE,
        @Query("appid") apiKey: String = BuildConfig.OPENWEATHER_API_KEY
    ): CurrentWeatherResponse

    @GET("forecast/hourly")
    suspend fun getForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String = UNIT_METRIC,
        @Query("lang") language: String = DEFAULT_LANGUAGE,
        @Query("appid") apiKey: String = BuildConfig.OPENWEATHER_API_KEY
    ): ForecastResponse

}