package com.iti.forecozy.model.dto.weather

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.iti.forecozy.model.local.weather.WeatherConverters

@Entity(tableName = "current_weather")
data class CurrentWeatherResponse(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id") val cityId: Long,
    @TypeConverters(WeatherConverters::class)
    @SerializedName("coord") val coordinates: Coordination,
    @TypeConverters(WeatherConverters::class)
    @SerializedName("weather") val weather: List<Weather>,
    @TypeConverters(WeatherConverters::class)
    @SerializedName("main") val main: Main,
    @SerializedName("visibility") val visibility: Int,
    @TypeConverters(WeatherConverters::class)
    @SerializedName("wind") val wind: Wind,
    @TypeConverters(WeatherConverters::class)
    @SerializedName("clouds") val clouds: Clouds,
    @SerializedName("dt") val timestamp: Long,
    @TypeConverters(WeatherConverters::class)
    @SerializedName("sys") val sys: Sys,
    @SerializedName("timezone") val timezone: Int,
    @SerializedName("name") val cityName: String,
    @SerializedName("cod") val code: Int
) {
    @ColumnInfo(index = true)
    var locationKey: String = ""
        get() = "${coordinates.latitude},${coordinates.longitude}"
}

