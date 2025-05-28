package com.iti.forecozy.model.dto.weather

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    @SerializedName("cod") val code: String,
    @SerializedName("message") val message: Int,
    @SerializedName("cnt") val count: Int,
    @SerializedName("list") val forecastItems: List<ForecastItem>,
    @SerializedName("city") val city: City
)