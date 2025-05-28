package com.iti.forecozy.model.repository.weather

import android.content.Context
import androidx.annotation.OpenForTesting
import androidx.annotation.VisibleForTesting
import com.iti.forecozy.model.dto.weather.CurrentWeatherResponse
import com.iti.forecozy.model.dto.weather.FavoriteLocation
import com.iti.forecozy.model.dto.weather.ForecastItem
import com.iti.forecozy.model.dto.weather.WeatherAlert
import com.iti.forecozy.model.dto.weather.WeatherWithForecast
import com.iti.forecozy.model.local.weather.IWeatherLocalDataSource
import com.iti.forecozy.model.remote.IWeatherRemoteDataSource
import com.iti.forecozy.utils.location.LocationKey
import com.iti.forecozy.utils.types.Result

@OpenForTesting
class WeatherRepository private constructor(
    private val localDataSource: IWeatherLocalDataSource,
    private val remoteDataSource: IWeatherRemoteDataSource
) : IWeatherRepository {

    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double
    ): Result<CurrentWeatherResponse> {
        return try {
            val locationKey = LocationKey.getLocationKey(lat, lon)
            val localWeather = localDataSource.getCurrentWeather(locationKey)

            if (localWeather != null) {
                Result.Success(localWeather)
            } else {
                remoteDataSource.getCurrentWeather(lat, lon).also { result ->
                    if (result is Result.Success) {
                        localDataSource.saveCurrentWeather(result.data)
                    }
                }
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getForecast(lat: Double, lon: Double): Result<List<ForecastItem>> {
        return try {
            val locationKey = LocationKey.getLocationKey(lat, lon)
            val localForecast = localDataSource.getForecast(locationKey)

            if (localForecast.isNotEmpty()) {
                Result.Success(localForecast)
            } else {
                when (val remoteResult = remoteDataSource.getWeatherForecast(lat, lon)) {
                    is Result.Success -> {
                        val forecastWithKeys = remoteResult.data.forecastItems.map { item ->
                            item.apply { this.locationKey = locationKey }
                        }

                        localDataSource.saveForecast(forecastWithKeys)
                        Result.Success(forecastWithKeys)
                    }

                    is Result.Error -> Result.Error(remoteResult.exception)
                    is Result.Loading -> Result.Loading
                }
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getWeatherWithForecast(
        lat: Double,
        lon: Double
    ): Result<WeatherWithForecast> {
        return try {
            val locationKey = LocationKey.getLocationKey(lat, lon)
            val weatherWithForecast = localDataSource.getWeatherWithForecast(locationKey)

            if (weatherWithForecast != null && weatherWithForecast.forecast.isNotEmpty()) {
                Result.Success(weatherWithForecast)
            } else {
                // If we don't have the data locally, refresh it first
                when (val refreshResult = refreshWeatherData(lat, lon)) {
                    is Result.Success -> {
                        val freshWeatherWithForecast =
                            localDataSource.getWeatherWithForecast(locationKey)
                        if (freshWeatherWithForecast != null) {
                            Result.Success(freshWeatherWithForecast)
                        } else {
                            Result.Error(Exception("Failed to get weather with forecast after refresh"))
                        }
                    }

                    is Result.Error -> Result.Error(refreshResult.exception)
                    is Result.Loading -> Result.Loading
                }
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun refreshWeatherData(lat: Double, lon: Double): Result<Unit> {
        return try {
            val locationKey = LocationKey.getLocationKey(lat, lon)

            // Clear old forecast data before fetching new data
            localDataSource.clearOldForecast(locationKey)

            // Fetch current weather
            val currentWeatherResult = remoteDataSource.getCurrentWeather(lat, lon)
            when (currentWeatherResult) {
                is Result.Success -> {
                    localDataSource.saveCurrentWeather(currentWeatherResult.data)
                }

                is Result.Error -> return Result.Error(currentWeatherResult.exception)
                is Result.Loading -> return Result.Loading
            }

            // Fetch forecast
            when (val forecastResult = remoteDataSource.getWeatherForecast(lat, lon)) {
                is Result.Success -> {
                    val forecastWithKeys = forecastResult.data.forecastItems.map { forecastItem ->
                        setForecastLocationKey(forecastItem, currentWeatherResult.data)
                    }

                    localDataSource.saveForecast(forecastWithKeys)
                    Result.Success(Unit)
                }

                is Result.Error -> Result.Error(forecastResult.exception)
                is Result.Loading -> Result.Loading
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun saveAlert(alert: WeatherAlert): Result<Unit> {
        return try {
            localDataSource.saveWeatherAlert(alert)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun dismissAlert(alertId: Long): Result<Unit> {
        try {
            localDataSource.dismissWeatherAlert(alertId)
            return Result.Success(Unit)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    override suspend fun deleteAlert(alertId: Long): Result<Unit> {
        try {
            localDataSource.deleteAlert(alertId)
            return Result.Success(Unit)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    override suspend fun getActiveAlerts(): Result<List<WeatherAlert>> {
        try {
            return Result.Success(localDataSource.getWeatherAlerts())
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    override suspend fun addFavoriteLocation(
        lat: Double,
        lon: Double,
        name: String,
        country: String
    ): Result<Unit> {
        try {
            localDataSource.addFavoriteLocation(lat, lon, name, country)
            return Result.Success(Unit)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    override suspend fun removeFavoriteLocation(locationKey: String): Result<Unit> {
        try {
            localDataSource.removeFavorite(locationKey)
            return Result.Success(Unit)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    override suspend fun getFavoriteLocations(): Result<List<FavoriteLocation>> {
        try {
            return Result.Success(localDataSource.getFavoriteLocations())
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    override suspend fun isFavoriteLocation(locationKey: String): Result<Boolean> {
        try {
            return Result.Success(localDataSource.isFavorite(locationKey))
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    private fun setForecastLocationKey(
        forecast: ForecastItem,
        currentWeather: CurrentWeatherResponse
    ): ForecastItem {
        return forecast.copy(
            locationKey = "${currentWeather.coordinates.latitude},${currentWeather.coordinates.longitude}"
        )
    }


    companion object {
        @Volatile
        private var instance: WeatherRepository? = null

        @VisibleForTesting
        fun resetInstance() {
            instance = null
        }

        fun getInstance(
            localDataSource: IWeatherLocalDataSource,
            remoteDataSource: IWeatherRemoteDataSource,
        ): WeatherRepository {
            return instance ?: synchronized(this) {
                instance ?: WeatherRepository(
                    localDataSource,
                    remoteDataSource,
                )
            }
        }
    }
}