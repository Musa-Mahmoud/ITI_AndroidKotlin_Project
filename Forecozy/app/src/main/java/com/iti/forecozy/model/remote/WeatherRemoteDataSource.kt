package com.iti.forecozy.model.remote

import android.content.Context
import com.iti.forecozy.model.dto.weather.CurrentWeatherResponse
import com.iti.forecozy.model.dto.weather.ForecastResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.iti.forecozy.utils.types.Result

class WeatherRemoteDataSource private constructor(
    private val weatherService: IWeatherService
) : IWeatherRemoteDataSource {

    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double
    ): Result<CurrentWeatherResponse> =
        withContext(Dispatchers.IO) {
            try {
                Result.Success(weatherService.getCurrentWeather(lat, lon))
            } catch (e: Exception) {
                Result.Error(e)
            }
        }


    override suspend fun getWeatherForecast(lat: Double, lon: Double): Result<ForecastResponse> =
        withContext(Dispatchers.IO) {
            try {
                Result.Success(weatherService.getForecast(lat, lon))
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    companion object {
        private const val BASE_URL = "https://pro.openweathermap.org/data/2.5/"

        @Volatile
        private var instance: WeatherRemoteDataSource? = null

        fun getInstance(context: Context): WeatherRemoteDataSource {
            return instance ?: synchronized(this) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val service = retrofit.create(IWeatherService::class.java)
                WeatherRemoteDataSource(service).also { instance = it }
            }
        }
    }
}