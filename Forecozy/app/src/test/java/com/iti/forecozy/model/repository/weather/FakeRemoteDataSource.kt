package com.iti.forecozy.model.repository.weather

import com.iti.forecozy.model.dto.weather.CurrentWeatherResponse
import com.iti.forecozy.model.dto.weather.ForecastResponse
import com.iti.forecozy.model.remote.IWeatherRemoteDataSource
import com.iti.forecozy.utils.types.Result

class FakeRemoteDataSource : IWeatherRemoteDataSource {
    var currentWeatherResponse: Result<CurrentWeatherResponse> = Result.Error(Exception("Not initialized"))
    var forecastResponse: Result<ForecastResponse> = Result.Error(Exception("Not initialized"))

    override suspend fun getCurrentWeather(lat: Double, lon: Double): Result<CurrentWeatherResponse> {
        return currentWeatherResponse
    }

    override suspend fun getWeatherForecast(lat: Double, lon: Double): Result<ForecastResponse> {
        return forecastResponse
    }
}