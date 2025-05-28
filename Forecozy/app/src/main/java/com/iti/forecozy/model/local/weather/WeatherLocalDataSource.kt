package com.iti.forecozy.model.local.weather

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.iti.forecozy.model.dto.weather.AlertStatus
import com.iti.forecozy.model.dto.weather.CurrentWeatherResponse
import com.iti.forecozy.model.dto.weather.FavoriteLocation
import com.iti.forecozy.model.dto.weather.ForecastItem
import com.iti.forecozy.model.dto.weather.WeatherAlert
import com.iti.forecozy.model.dto.weather.WeatherWithForecast

class WeatherLocalDataSource private constructor(
    private val weatherDao: IWeatherDao,
    private val alertDao: IAlertDao,
    private val favoritesDao: IFavoriteDao
) : IWeatherLocalDataSource {
    // Weather operations
    override suspend fun saveCurrentWeather(weather: CurrentWeatherResponse) =
        withContext(Dispatchers.IO) {
            weatherDao.insertCurrentWeather(weather)
        }

    override suspend fun getCurrentWeather(locationKey: String): CurrentWeatherResponse? =
        withContext(Dispatchers.IO) {
            weatherDao.getCurrentWeather(locationKey)
        }

    override suspend fun saveForecast(forecastItems: List<ForecastItem>) =
        withContext(Dispatchers.IO) {
            weatherDao.insertForecast(forecastItems)
        }

    override suspend fun getForecast(locationKey: String): List<ForecastItem> =
        withContext(Dispatchers.IO) {
            weatherDao.getForecast(locationKey)
        }

    override suspend fun getWeatherWithForecast(locationKey: String): WeatherWithForecast? =
        withContext(Dispatchers.IO) {
            weatherDao.getWeatherWithForecast(locationKey)
        }

    override suspend fun clearOldForecast(locationKey: String) =
        withContext(Dispatchers.IO) {
            weatherDao.clearOldForecast(locationKey)
        }


    // Alert operations
    override suspend fun saveWeatherAlert(alert: WeatherAlert) = withContext(Dispatchers.IO) {
        alertDao.insertAlert(alert)
    }

    override suspend fun dismissWeatherAlert(alertId: Long) = withContext(Dispatchers.IO) {
        alertDao.updateAlertStatus(alertId, AlertStatus.DISMISSED)
    }

    override suspend fun deleteAlert(alertId: Long) = withContext(Dispatchers.IO) {
        alertDao.deleteAlert(alertId)
    }

    override suspend fun deleteAlert(alert: WeatherAlert) = withContext(Dispatchers.IO) {
        alertDao.deleteAlert(alert)
    }

    override suspend fun getWeatherAlerts(): List<WeatherAlert> = withContext(Dispatchers.IO) {
        alertDao.getActiveAlerts(System.currentTimeMillis())
    }

    // Favorites operations
    override suspend fun addFavoriteLocation(
        lat: Double,
        lon: Double,
        name: String,
        country: String
    ) = withContext(Dispatchers.IO) {
        val locationKey = "$lat,$lon"
        val favorite = FavoriteLocation(
            locationKey = locationKey,
            latitude = lat,
            longitude = lon,
            name = name,
            country = country
        )
        favoritesDao.addFavorite(favorite)
    }

    override suspend fun removeFavorite(locationKey: String) = withContext(Dispatchers.IO) {
        val favorite = favoritesDao.getFavoriteByLocation(locationKey)
        if (favorite != null) {
            favoritesDao.removeFavorite(favorite)
        }
    }

    override suspend fun saveFavoriteLocation(location: FavoriteLocation) =
        withContext(Dispatchers.IO) {
            favoritesDao.addFavorite(location)
        }

//    override suspend fun deleteFavoriteLocation(locationId: Int) {
//        favoritesDao.getFavoriteById(locationId)?.let {
//            favoritesDao.removeFavorite(it)
//        }
//    }

    override suspend fun getFavoriteLocations(): List<FavoriteLocation> =
        withContext(Dispatchers.IO) {
            favoritesDao.getAllFavorites()
        }

    override suspend fun isFavorite(locationKey: String): Boolean = withContext(Dispatchers.IO) {
        favoritesDao.getFavoriteByLocation(locationKey) != null
    }

    companion object {
        @Volatile
        private var instance: WeatherLocalDataSource? = null

        fun getInstance(context: Context): WeatherLocalDataSource {
            return instance ?: synchronized(this) {
                val db = WeatherDatabase.getInstance(context)
                WeatherLocalDataSource(
                    db.weatherDao(),
                    db.alertDao(),
                    db.favoritesDao()
                ).also { instance = it }
            }
        }
    }
}