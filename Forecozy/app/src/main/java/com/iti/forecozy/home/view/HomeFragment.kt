package com.iti.forecozy.home.view

import HomeViewModel
import android.Manifest
import android.content.pm.PackageManager
import com.iti.forecozy.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.iti.forecozy.databinding.FragmentHomeBinding
import com.iti.forecozy.home.viewmodel.HomeViewModelFactory
import com.iti.forecozy.model.dto.settings.TemperatureUnit
import com.iti.forecozy.model.local.settings.SettingsLocalDataStore
import com.iti.forecozy.model.local.weather.WeatherLocalDataSource
import com.iti.forecozy.model.remote.WeatherRemoteDataSource
import com.iti.forecozy.model.repository.settings.SettingsRepository
import com.iti.forecozy.model.repository.weather.WeatherRepository
import com.iti.forecozy.model.dto.weather.WeatherWithForecast
import com.iti.forecozy.utils.types.LocationState
import com.iti.forecozy.utils.types.Result
import com.iti.forecozy.utils.unitconverter.UnitConverter

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var dailyAdapter: DailyForecastAdapter
    private lateinit var hourlyAdapter: HourlyForecastAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        homeViewModel.handleLocationPermissionResult(granted)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupLocationServices()
        setupRecyclerViews()
        setupObservers()
        setupClickListeners()
    }

    private fun setupViewModel() {
        val factory = HomeViewModelFactory(
            WeatherRepository.getInstance(
                WeatherLocalDataSource.getInstance(requireContext()),
                WeatherRemoteDataSource.getInstance(requireContext())
            ),
            SettingsRepository.getInstance(SettingsLocalDataStore(requireContext()))
        )
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }

    private fun setupLocationServices() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        homeViewModel.setupLocationCallback { locationResult ->
            locationResult.lastLocation?.let { location ->
                homeViewModel.fetchWeatherData(location.latitude, location.longitude)
            }
        }
    }

    private fun setupRecyclerViews() {
        binding.apply {
            dailyAdapter = DailyForecastAdapter(UnitConverter, homeViewModel.temperatureUnit.value ?: TemperatureUnit.CELSIUS)
            hourlyAdapter = HourlyForecastAdapter(UnitConverter, homeViewModel.temperatureUnit.value ?: TemperatureUnit.CELSIUS)

            fourDayRecycler.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = dailyAdapter
            }

            hourlyRecycler.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = hourlyAdapter
            }
        }
    }

    private fun setupObservers() {
        homeViewModel.weatherState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    showWeatherData(result.data)
                    hideLoadingState()
                }
                is Result.Error -> {
                    showError(result.exception.message ?: getString(R.string.unknown_error))
                    hideLoadingState()
                }
                is Result.Loading -> showLoadingState()
            }
        }

        homeViewModel.locationState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LocationState.Success -> {
                    hideLoadingState()
                }
                is LocationState.PermissionRequired -> {
                    checkLocationPermission()
                }
                is LocationState.Error -> {
                    showError(state.message)
                    hideLoadingState()
                }
                is LocationState.LocationDisabled -> {
                    showLocationDisabledError()
                    hideLoadingState()
                }
                LocationState.Loading -> {
                    showLoadingState()
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.retryButton.setOnClickListener {
            homeViewModel.retryLastAction()
        }

//        binding.settingsButton.setOnClickListener {
//            findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
        }

    private fun showWeatherData(weatherData: WeatherWithForecast) {
        binding.apply {
            weatherContentGroup.visibility = View.VISIBLE
            errorLayout.visibility = View.GONE

            temperatureText.text = UnitConverter.convertTemperature(
                weatherData.currentWeather.main.temp,
                homeViewModel.temperatureUnit.value ?: TemperatureUnit.CELSIUS
            )
            conditionText.text = weatherData.currentWeather.weather[0].description

            dailyAdapter.submitList(weatherData.forecast)
            hourlyAdapter.submitList(weatherData.forecast)
        }
    }

    private fun showError(message: String) {
        binding.apply {
            weatherContentGroup.visibility = View.GONE
            errorLayout.visibility = View.VISIBLE
            errorText.text = message
        }
    }

    private fun showLoadingState() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            weatherContentGroup.visibility = View.GONE
            errorLayout.visibility = View.GONE
        }
    }

    private fun hideLoadingState() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showLocationDisabledError() {
        showError(getString(R.string.location_disabled_error))
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermissions() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun showLocationPermissionRationale() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.location_permission_title))
            .setMessage(getString(R.string.location_permission_rationale))
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                requestLocationPermissions()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun checkLocationPermission() {
        homeViewModel.checkAndRequestLocationPermission(
            hasPermission = hasLocationPermission(),
            shouldShowRationale = shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION),
            onRequestPermission = {
                requestLocationPermissions()
            },
            onShowRationale = {
                showLocationPermissionRationale()
            }
        )
    }
}