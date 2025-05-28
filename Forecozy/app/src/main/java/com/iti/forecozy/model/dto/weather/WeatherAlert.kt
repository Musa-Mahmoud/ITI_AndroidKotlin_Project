package com.iti.forecozy.model.dto.weather

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.iti.forecozy.model.local.weather.AlertStatusConverter
import com.iti.forecozy.model.local.weather.AlertTypeConverter

@Entity(tableName = "weather_alerts")
data class WeatherAlert(
    @PrimaryKey(autoGenerate = true)
    val alertId: Long = 0,
    val locationKey: String, // "lat,lon"
    val startTime: Long, // UTC timestamp
    val endTime: Long,
    @TypeConverters(AlertTypeConverter::class)
    val alertType: AlertType,
    @TypeConverters(AlertStatusConverter::class)
    var status: AlertStatus = AlertStatus.ACTIVE,
    val notificationTitle: String,
    val notificationMessage: String
)

enum class AlertType {
    NOTIFICATION_ONLY, SOUND_ALARM
}

enum class AlertStatus {
    ACTIVE, DISMISSED
}