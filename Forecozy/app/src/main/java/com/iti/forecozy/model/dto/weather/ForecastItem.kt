package com.iti.forecozy.model.dto.weather

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.iti.forecozy.model.local.weather.WeatherConverters

@Entity(tableName = "forecast")
data class ForecastItem(
    @PrimaryKey
    @SerializedName("dt") val timestamp: Long,
    @TypeConverters(WeatherConverters::class)
    @SerializedName("main") val main: Main,
    @TypeConverters(WeatherConverters::class)
    @SerializedName("weather") val weather: List<Weather>,
    @TypeConverters(WeatherConverters::class)
    @SerializedName("clouds") val clouds: Clouds,
    @TypeConverters(WeatherConverters::class)
    @SerializedName("wind") val wind: Wind,
    @SerializedName("visibility") val visibility: Int,
    @SerializedName("pop") val precipitationProbability: Float,
    @TypeConverters(WeatherConverters::class)
    @SerializedName("sys") val sys: Sys,
    @SerializedName("dt_txt") val dateText: String,
    // Foreign key to current weather
    @ColumnInfo(index = true)
    var locationKey: String = ""
)
