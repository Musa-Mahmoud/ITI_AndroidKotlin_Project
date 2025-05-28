package com.iti.forecozy.model.dto.weather

import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("type") val type: Int? = null,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("country") val country: String? = null,
    @SerializedName("sunrise") val sunrise: Long? = null,
    @SerializedName("sunset") val sunset: Long? = null,
    @SerializedName("pod") val partOfDay: String? = null
)