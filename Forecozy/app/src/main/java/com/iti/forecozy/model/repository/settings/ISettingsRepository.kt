package com.iti.forecozy.model.repository.settings

import com.iti.forecozy.model.dto.settings.AppLanguage
import com.iti.forecozy.model.dto.settings.LocationMethod
import com.iti.forecozy.model.dto.settings.Settings
import com.iti.forecozy.model.dto.settings.TemperatureUnit
import com.iti.forecozy.model.dto.settings.WindSpeedUnit

interface ISettingsRepository {
    suspend fun getAppSettings(): Settings
    suspend fun updateTemperatureUnit(unit: TemperatureUnit)
    suspend fun updateLocationMethod(method: LocationMethod)
    suspend fun updateWindSpeedUnit(unit: WindSpeedUnit)
    suspend fun updateAppLanguage(language: AppLanguage)
    suspend fun shouldShowOnboarding(): Boolean
    suspend fun completeOnboarding()
}