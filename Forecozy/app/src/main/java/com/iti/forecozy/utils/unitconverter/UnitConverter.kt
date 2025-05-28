package com.iti.forecozy.utils.unitconverter

import com.iti.forecozy.model.dto.settings.TemperatureUnit
import com.iti.forecozy.model.dto.settings.WindSpeedUnit

object UnitConverter {
    fun convertTemperature(value: Double, from: TemperatureUnit): String {
        return when (from) {
            TemperatureUnit.CELSIUS -> "${value.toInt()}°C"
            TemperatureUnit.FAHRENHEIT -> "${(value * 9/5 + 32).toInt()}°F"
            TemperatureUnit.KELVIN -> "${(value + 273.15).toInt()}K"
        }
    }

    fun convertWindSpeed(value: Double, to: WindSpeedUnit): String {
        return when (to) {
            WindSpeedUnit.METER_SEC -> "${value.toInt()} m/s"
            WindSpeedUnit.MILE_HOUR -> "${(value * 2.237).toInt()} mph"
        }
    }
}