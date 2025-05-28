package com.iti.forecozy.home.viewmodel

import HomeViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.forecozy.model.repository.settings.ISettingsRepository
import com.iti.forecozy.model.repository.weather.IWeatherRepository

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(
    private val weatherRepository: IWeatherRepository,
    private val settingsRepository: ISettingsRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(weatherRepository, settingsRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel class NOT found")
        }
    }
}