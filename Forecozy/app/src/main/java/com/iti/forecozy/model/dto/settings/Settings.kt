package com.iti.forecozy.model.dto.settings

data class Settings(
    val temperatureUnit: TemperatureUnit,
    val windSpeedUnit: WindSpeedUnit,
    val appLanguage: AppLanguage,
    val locationMethod: LocationMethod,
    val isFirstRun: Boolean
)

enum class TemperatureUnit { KELVIN, CELSIUS, FAHRENHEIT }
enum class WindSpeedUnit { METER_SEC, MILE_HOUR }
enum class AppLanguage { ENGLISH, ARABIC }
enum class LocationMethod { GPS, MAP }
