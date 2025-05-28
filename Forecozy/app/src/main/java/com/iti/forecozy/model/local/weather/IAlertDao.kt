package com.iti.forecozy.model.local.weather

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.iti.forecozy.model.dto.weather.AlertStatus
import com.iti.forecozy.model.dto.weather.WeatherAlert

@Dao
interface IAlertDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: WeatherAlert)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAlert(alert: WeatherAlert)

    @Query("DELETE FROM weather_alerts WHERE alertId = :alertId")
    suspend fun deleteAlert(alertId: Long)

    @Delete
    suspend fun deleteAlert(alert: WeatherAlert)

    @Query("SELECT * FROM weather_alerts WHERE status = 'ACTIVE' AND endTime > :currentTime")
    suspend fun getActiveAlerts(currentTime: Long): List<WeatherAlert>

    @Query("SELECT * FROM weather_alerts")
    suspend fun getAllAlerts(): List<WeatherAlert>

    @Query("UPDATE weather_alerts SET status = :newStatus WHERE alertId = :alertId")
    suspend fun updateAlertStatus(alertId: Long, newStatus: AlertStatus)
}