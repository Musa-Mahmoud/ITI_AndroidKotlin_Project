package com.iti.forecozy.model.repository.settings

import com.iti.forecozy.model.dto.settings.AppLanguage
import com.iti.forecozy.model.dto.settings.LocationMethod
import com.iti.forecozy.model.dto.settings.Settings
import com.iti.forecozy.model.dto.settings.TemperatureUnit
import com.iti.forecozy.model.dto.settings.WindSpeedUnit
import com.iti.forecozy.model.local.settings.ISettingsLocalDataSource

class SettingsRepository private constructor(private val localDataSource: ISettingsLocalDataSource) :
    ISettingsRepository {

    override suspend fun getAppSettings(): Settings {
        return Settings(
            temperatureUnit = localDataSource.getTemperatureUnit(),
            windSpeedUnit = localDataSource.getWindSpeedUnit(),
            appLanguage = localDataSource.getAppLanguage(),
            locationMethod = localDataSource.getLocationMethod(),
            isFirstRun = localDataSource.isFirstRun()
        )
    }

    override suspend fun updateTemperatureUnit(unit: TemperatureUnit) {
        localDataSource.setTemperatureUnit(unit)
        updateSettings()
    }

    override suspend fun updateLocationMethod(method: LocationMethod) {
        localDataSource.setLocationMethod(method)
        updateSettings()
    }

    override suspend fun updateWindSpeedUnit(unit: WindSpeedUnit) {
        localDataSource.setWindSpeedUnit(unit)
        updateSettings()
    }

    override suspend fun updateAppLanguage(language: AppLanguage) {
        localDataSource.setAppLanguage(language)
        updateSettings()
    }

    override suspend fun shouldShowOnboarding(): Boolean = localDataSource.isFirstRun()

    override suspend fun completeOnboarding() {
        localDataSource.setFirstRunCompleted()
        updateSettings()
    }

    private suspend fun updateSettings() {
        val currentSettings = getAppSettings()
        localDataSource.saveAppSettings(currentSettings)
    }

    companion object {
        @Volatile
        private var instance: SettingsRepository? = null

        fun getInstance(localDataSource: ISettingsLocalDataSource): SettingsRepository =
            instance ?: synchronized(this) {
                instance ?: SettingsRepository(localDataSource).also { instance = it }
            }
    }
}