package com.iti.forecozy.model.dto.weather

import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all") val cloudiness: Int
)