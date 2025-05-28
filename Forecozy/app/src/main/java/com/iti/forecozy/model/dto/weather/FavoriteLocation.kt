package com.iti.forecozy.model.dto.weather

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_locations")
data class FavoriteLocation(
    @PrimaryKey
    val locationKey: String,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val country: String,
    val timestamp: Long = System.currentTimeMillis()
)