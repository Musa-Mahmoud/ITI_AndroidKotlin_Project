package com.iti.forecozy.model.local.weather

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.iti.forecozy.model.dto.weather.AlertStatus
import com.iti.forecozy.model.dto.weather.AlertType
import com.iti.forecozy.model.dto.weather.Clouds
import com.iti.forecozy.model.dto.weather.Coordination
import com.iti.forecozy.model.dto.weather.Main
import com.iti.forecozy.model.dto.weather.Sys
import com.iti.forecozy.model.dto.weather.Weather
import com.iti.forecozy.model.dto.weather.Wind

class WeatherConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromCoord(coord: Coordination): String = gson.toJson(coord)
    @TypeConverter
    fun toCoord(coordString: String): Coordination = gson.fromJson(coordString, Coordination::class.java)

    @TypeConverter
    fun fromWeatherList(weather: List<Weather>): String = gson.toJson(weather)
    @TypeConverter
    fun toWeatherList(weatherString: String): List<Weather> =
        gson.fromJson(weatherString, object : TypeToken<List<Weather>>() {}.type)

    @TypeConverter
    fun fromMain(main: Main): String = gson.toJson(main)
    @TypeConverter
    fun toMain(mainString: String): Main = gson.fromJson(mainString, Main::class.java)

    @TypeConverter
    fun fromWind(wind: Wind): String = gson.toJson(wind)
    @TypeConverter
    fun toWind(windString: String): Wind = gson.fromJson(windString, Wind::class.java)

    @TypeConverter
    fun fromClouds(clouds: Clouds): String = gson.toJson(clouds)
    @TypeConverter
    fun toClouds(cloudsString: String): Clouds = gson.fromJson(cloudsString, Clouds::class.java)

    @TypeConverter
    fun fromSys(sys: Sys): String = gson.toJson(sys)
    @TypeConverter
    fun toSys(sysString: String): Sys = gson.fromJson(sysString, Sys::class.java)
}

class AlertTypeConverter {
    @TypeConverter
    fun fromAlertType(type: AlertType): String = type.name

    @TypeConverter
    fun toAlertType(value: String): AlertType = enumValueOf(value)
}


class AlertStatusConverter {
    @TypeConverter
    fun fromStatus(status: AlertStatus): String = status.name

    @TypeConverter
    fun toStatus(value: String): AlertStatus = enumValueOf(value)
}