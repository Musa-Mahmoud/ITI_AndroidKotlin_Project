package com.iti.forecozy.model.dto.weather

import com.google.gson.annotations.SerializedName

data class Coordination(
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lon") val longitude: Double
)