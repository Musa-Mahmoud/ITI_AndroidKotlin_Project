package com.iti.forecozy.model.local.settings

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.google.gson.Gson
import com.iti.forecozy.model.dto.settings.AppLanguage
import com.iti.forecozy.model.dto.settings.LocationMethod
import com.iti.forecozy.model.dto.settings.Settings
import com.iti.forecozy.model.dto.settings.TemperatureUnit
import com.iti.forecozy.model.dto.settings.WindSpeedUnit

class SettingsLocalDataStore(context: Context) : ISettingsLocalDataSource{
    private val sharedPrefs = context.getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)
    private lateinit var settings: Settings

    override suspend fun saveAppSettings(settings: Settings) = withContext(Dispatchers.IO) {
        this@SettingsLocalDataStore.settings = settings
    }

    override suspend fun getAppSettings(): Settings = withContext(Dispatchers.IO) {
        settings
    }

    // Temperature Unit
    override suspend fun getTemperatureUnit(): TemperatureUnit = withContext(Dispatchers.IO) {
        TemperatureUnit.valueOf(
            sharedPrefs.getString(TEMP_UNIT_KEY, TemperatureUnit.CELSIUS.name)
                ?: TemperatureUnit.CELSIUS.name
        )
    }

    override suspend fun setTemperatureUnit(unit: TemperatureUnit) = withContext(Dispatchers.IO) {
        sharedPrefs.edit().putString(TEMP_UNIT_KEY, unit.name).apply()
    }

    // Wind Speed Unit
    override suspend fun getWindSpeedUnit(): WindSpeedUnit = withContext(Dispatchers.IO) {
        WindSpeedUnit.valueOf(
            sharedPrefs.getString(WIND_SPEED_KEY, WindSpeedUnit.METER_SEC.name)
                ?: WindSpeedUnit.METER_SEC.name
        )
    }

    override suspend fun setWindSpeedUnit(unit: WindSpeedUnit) = withContext(Dispatchers.IO) {
        sharedPrefs.edit().putString(WIND_SPEED_KEY, unit.name).apply()
    }

    // App Language
    override suspend fun getAppLanguage(): AppLanguage = withContext(Dispatchers.IO) {
        AppLanguage.valueOf(
            sharedPrefs.getString(LANGUAGE_KEY, AppLanguage.ENGLISH.name)
                ?: AppLanguage.ENGLISH.name
        )
    }

    override suspend fun setAppLanguage(language: AppLanguage) = withContext(Dispatchers.IO) {
        sharedPrefs.edit().putString(LANGUAGE_KEY, language.name).apply()
    }

    // Location Method
    override suspend fun getLocationMethod(): LocationMethod = withContext(Dispatchers.IO) {
        LocationMethod.valueOf(
            sharedPrefs.getString(LOCATION_METHOD_KEY, LocationMethod.GPS.name)
                ?: LocationMethod.GPS.name
        )
    }

    override suspend fun setLocationMethod(method: LocationMethod) = withContext(Dispatchers.IO) {
        sharedPrefs.edit().putString(LOCATION_METHOD_KEY, method.name).apply()
    }

    // First Run
    override suspend fun isFirstRun(): Boolean = withContext(Dispatchers.IO) {
        sharedPrefs.getBoolean(FIRST_RUN_KEY, true)
    }

    override suspend fun setFirstRunCompleted() = withContext(Dispatchers.IO) {
        sharedPrefs.edit().putBoolean(FIRST_RUN_KEY, false).apply()
    }

    companion object {
        private const val TEMP_UNIT_KEY = "temp_unit"
        private const val WIND_SPEED_KEY = "wind_speed_unit"
        private const val LANGUAGE_KEY = "app_language"
        private const val LOCATION_METHOD_KEY = "location_method"
        private const val FIRST_RUN_KEY = "first_run"
    }
}