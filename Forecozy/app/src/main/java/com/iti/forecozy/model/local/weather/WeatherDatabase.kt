package com.iti.forecozy.model.local.weather

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.iti.forecozy.model.dto.weather.CurrentWeatherResponse
import com.iti.forecozy.model.dto.weather.FavoriteLocation
import com.iti.forecozy.model.dto.weather.ForecastItem
import com.iti.forecozy.model.dto.weather.WeatherAlert

@Database(
    entities = [
        CurrentWeatherResponse::class,
        ForecastItem::class,
        WeatherAlert::class,
        FavoriteLocation::class
    ],
    version = 1,
)
@TypeConverters(
    WeatherConverters::class,
    AlertTypeConverter::class,
    AlertStatusConverter::class
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): IWeatherDao
    abstract fun alertDao(): IAlertDao
    abstract fun favoritesDao(): IFavoriteDao

    companion object {
        @Volatile
        private var instance: WeatherDatabase? = null

        fun getInstance(context: Context): WeatherDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDatabase::class.java,
                    "weather.db"
                ).build().also { instance = it }
            }
        }
    }
}
