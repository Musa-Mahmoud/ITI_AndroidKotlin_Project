package com.iti.forecozy.model.repository.weather

import com.iti.forecozy.model.dto.weather.Coordination
import com.iti.forecozy.model.dto.weather.CurrentWeatherResponse
import com.iti.forecozy.model.dto.weather.ForecastItem
import com.iti.forecozy.model.dto.weather.WeatherWithForecast
import kotlinx.coroutines.runBlocking
import org.mockito.Mockito.*
import com.iti.forecozy.utils.types.Result
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Test

class WeatherRepositoryTest {
    private lateinit var repository: WeatherRepository
    private lateinit var fakeLocal: FakeLocalDataSource
    private lateinit var fakeRemote: FakeRemoteDataSource

    @Before
    fun setUp() {
        // Reset singleton instance
        resetSingleton()

        // Create fake data sources
        fakeLocal = FakeLocalDataSource()
        fakeRemote = FakeRemoteDataSource()

        // Create real repository with fake dependencies
        repository = WeatherRepository.getInstance(fakeLocal, fakeRemote)
    }

    private fun resetSingleton() {
        try {
            val instanceField = WeatherRepository::class.java.getDeclaredField("instance")
            instanceField.isAccessible = true
            instanceField.set(null, null)
        } catch (e: Exception) {
            throw RuntimeException("Failed to reset singleton", e)
        }
    }

    @Test
    fun getCurrentWeather_latlon12_34_56_78_returnsSuccessLatlon() = runTest {
        // Given the repo
        // When
        val result = repository.getCurrentWeather(12.34, 56.78) as Result.Success

        // Then
        assertThat(result.data.locationKey, IsEqual("12.34,56.78"))
    }

    @Test
    fun getForecast_latlon12_34_56_78_returnsSuccessLatlon() = runTest {
        // Given the repo
        // When
        val result = repository.getForecast(12.34, 56.78) as Result.Success

        // Then
        assertThat(result.data[0].locationKey, IsEqual("12.34,56.78"))
    }
}