package com.iti.forecozy.model.dto.weather

import com.google.gson.annotations.SerializedName

data class Wind(
    @SerializedName("speed") val speed: Double,
    @SerializedName("deg") val degrees: Int,
    @SerializedName("gust") val gust: Double? = null
)