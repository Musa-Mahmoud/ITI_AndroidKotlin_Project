package com.iti.forecozy

import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.iti.forecozy.databinding.ActivityMainBinding
import com.iti.forecozy.model.local.weather.WeatherLocalDataSource
import com.iti.forecozy.model.remote.IWeatherRemoteDataSource
import com.iti.forecozy.model.remote.WeatherRemoteDataSource
import com.iti.forecozy.model.repository.weather.IWeatherRepository
import com.iti.forecozy.model.repository.weather.WeatherRepository
import kotlinx.coroutines.launch
import com.iti.forecozy.utils.types.Result

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var weatherRepository: IWeatherRepository
    private val TAG = "WeatherRepositoryTest"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize repository
        val localDataSource = WeatherLocalDataSource.getInstance(applicationContext)
        val remoteDataSource = WeatherRemoteDataSource.getInstance(applicationContext)
        weatherRepository = WeatherRepository.getInstance(localDataSource, remoteDataSource)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Test repository functions
//        lifecycleScope.launch {
//            testWeatherRepository()
////            testWeatherRemoteDataSource(remoteDataSource)
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private suspend fun testWeatherRemoteDataSource(remoteDataSource: IWeatherRemoteDataSource) {
        val lat = 29.95
        val lon = 31.53

        // Test getCurrentWeather
        Log.i(TAG, "Testing getCurrentWeather...")
        when (val result = remoteDataSource.getCurrentWeather(lat, lon)) {
            is Result.Success -> {
                Log.i(TAG, "Current weather success: ${result.data}")
                Log.i(TAG, "Temperature: ${result.data.main.temp}°C")
                Log.i(TAG, "Weather: ${result.data.weather.firstOrNull()?.description}")
            }
            is Result.Error -> Log.i(TAG, "Current weather error: ${result.exception.message}")
            is Result.Loading -> Log.i(TAG, "Current weather loading...")
        }
        // Test getForecast
        Log.i(TAG, "\nTesting getWeatherForecast...")
        when (val result = remoteDataSource.getWeatherForecast(lat, lon)) {
            is Result.Success -> {
                Log.i(TAG, "Forecast success: Got ${result.data.count} items")
                result.data.forecastItems.forEach { forecast ->
                    Log.i(TAG, "Forecast for ${forecast.dateText}: " +
                            "Temp: ${forecast.main.temp}°C, " +
                            "Weather: ${forecast.weather.firstOrNull()?.description}")
                }
            }
            is Result.Error -> Log.i(TAG, "Forecast error: ${result.exception.message}")
            is Result.Loading -> Log.i(TAG, "Forecast loading...")
        }
    }

    private suspend fun testWeatherRepository() {
        // Test coordinates (example for Cairo)
        val lat = 29.95
        val lon = 31.53

        // Test getCurrentWeather
        Log.i(TAG, "Testing getCurrentWeather...")
        when (val result = weatherRepository.getCurrentWeather(lat, lon)) {
            is Result.Success -> {
                Log.i(TAG, "Current weather success: ${result.data}")
                Log.i(TAG, "Temperature: ${result.data.main.temp}°C")
                Log.i(TAG, "Weather: ${result.data.weather.firstOrNull()?.description}")
            }
            is Result.Error -> Log.i(TAG, "Current weather error: ${result.exception.message}")
            is Result.Loading -> Log.i(TAG, "Current weather loading...")
        }

        // Test getForecast
        Log.i(TAG, "\nTesting getForecast...")
        when (val result = weatherRepository.getForecast(lat, lon)) {
            is Result.Success -> {
                Log.i(TAG, "Forecast success: Got ${result.data.size} items")
                result.data.forEach { forecast ->
                    Log.i(TAG, "Forecast for ${forecast.dateText}: " +
                            "Temp: ${forecast.main.temp}°C, " +
                            "Weather: ${forecast.weather.firstOrNull()?.description}")
                }
            }
            is Result.Error -> Log.i(TAG, "Forecast error: ${result.exception.message}")
            is Result.Loading -> Log.i(TAG, "Forecast loading...")
        }

        // Test getWeatherWithForecast
        Log.i(TAG, "\nTesting getWeatherWithForecast...")
        when (val result = weatherRepository.getWeatherWithForecast(lat, lon)) {
            is Result.Success -> {
                Log.i(TAG, "Weather with forecast success:")
                Log.i(TAG, "Current weather: ${result.data.currentWeather.cityName}")
                Log.i(TAG, "Forecast count: ${result.data.forecast.size}")
            }
            is Result.Error -> Log.i(TAG, "Weather with forecast error: ${result.exception.message}")
            is Result.Loading -> Log.i(TAG, "Weather with forecast loading...")
        }

        // Test refreshWeatherData
        Log.i(TAG, "\nTesting refreshWeatherData...")
        when (val result = weatherRepository.refreshWeatherData(lat, lon)) {
            is Result.Success -> Log.i(TAG, "Refresh successful")
            is Result.Error -> Log.i(TAG, "Refresh error: ${result.exception.message}")
            is Result.Loading -> Log.i(TAG, "Refresh loading...")
        }
    }
}