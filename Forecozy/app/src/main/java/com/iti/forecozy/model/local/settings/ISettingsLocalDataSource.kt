package com.iti.forecozy.model.local.settings

import com.iti.forecozy.model.dto.settings.AppLanguage
import com.iti.forecozy.model.dto.settings.LocationMethod
import com.iti.forecozy.model.dto.settings.Settings
import com.iti.forecozy.model.dto.settings.TemperatureUnit
import com.iti.forecozy.model.dto.settings.WindSpeedUnit

interface ISettingsLocalDataSource {
    suspend fun saveAppSettings(settings: Settings)
    suspend fun getAppSettings(): Settings

    suspend fun getTemperatureUnit(): TemperatureUnit
    suspend fun setTemperatureUnit(unit: TemperatureUnit)

    suspend fun getWindSpeedUnit(): WindSpeedUnit
    suspend fun setWindSpeedUnit(unit: WindSpeedUnit)

    suspend fun getAppLanguage(): AppLanguage
    suspend fun setAppLanguage(language: AppLanguage)

    suspend fun getLocationMethod(): LocationMethod
    suspend fun setLocationMethod(method: LocationMethod)

    suspend fun isFirstRun(): Boolean
    suspend fun setFirstRunCompleted()
}