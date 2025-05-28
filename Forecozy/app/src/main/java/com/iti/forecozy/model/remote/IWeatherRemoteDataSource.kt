package com.iti.forecozy.model.remote

import com.iti.forecozy.model.dto.weather.CurrentWeatherResponse
import com.iti.forecozy.model.dto.weather.ForecastResponse
import com.iti.forecozy.utils.types.Result

interface IWeatherRemoteDataSource {
    suspend fun getCurrentWeather(lat: Double, lon: Double): Result<CurrentWeatherResponse>
    suspend fun getWeatherForecast(lat: Double, lon: Double): Result<ForecastResponse>

}