import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.iti.forecozy.model.dto.settings.LocationMethod
import com.iti.forecozy.model.dto.settings.TemperatureUnit
import com.iti.forecozy.model.dto.settings.WindSpeedUnit
import com.iti.forecozy.model.dto.weather.WeatherWithForecast
import com.iti.forecozy.model.repository.settings.ISettingsRepository
import com.iti.forecozy.model.repository.weather.IWeatherRepository
import com.iti.forecozy.utils.types.LocationState
import com.iti.forecozy.utils.types.Result
import kotlinx.coroutines.launch

class HomeViewModel(
    private val weatherRepository: IWeatherRepository,
    private val settingsRepository: ISettingsRepository
) : ViewModel() {

    private val _weatherState = MutableLiveData<Result<WeatherWithForecast>>(Result.Loading)
    val weatherState: LiveData<Result<WeatherWithForecast>> = _weatherState

    private val _locationState = MutableLiveData<LocationState>(LocationState.Loading)
    val locationState: LiveData<LocationState> = _locationState

    private val _networkState = MutableLiveData(true)
    val networkState: LiveData<Boolean> = _networkState

    private val _temperatureUnit = MutableLiveData<TemperatureUnit>()
    val temperatureUnit: LiveData<TemperatureUnit> = _temperatureUnit

    private val _windSpeedUnit = MutableLiveData<WindSpeedUnit>()
    val windSpeedUnit: LiveData<WindSpeedUnit> = _windSpeedUnit

    private val _locationPermissionState = MutableLiveData<Boolean>()
    val locationPermissionState: LiveData<Boolean> = _locationPermissionState

    private var currentLocation: Pair<Double, Double>? = null
    private var locationCallback: LocationCallback? = null
    private var lastAction: (() -> Unit)? = null

    init {
        viewModelScope.launch {
            loadSettings()
            checkLocationMethod()
//            checkLocationPermissionState()
        }
    }

    fun setupLocationCallback(onLocationResult: (LocationResult) -> Unit) {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    setLocationState(
                        LocationState.Success(
                            latitude = location.latitude,
                            longitude = location.longitude
                        )
                    )
                    onLocationResult(result)
                }
            }
        }
    }

    private fun checkLocationPermissionState() {
        _locationState.value = LocationState.PermissionRequired
        lastAction = {
            currentLocation?.let { (lat, lon) ->
                fetchWeatherData(lat, lon)
            }
        }
    }

    fun retryLastAction() {
        lastAction?.invoke()
    }

    fun checkAndRequestLocationPermission(
        hasPermission: Boolean,
        shouldShowRationale: Boolean,
        onRequestPermission: () -> Unit,
        onShowRationale: () -> Unit
    ) {
        when {
            hasPermission -> {
                _locationPermissionState.value = true
            }

            shouldShowRationale -> {
                onShowRationale()
            }

            else -> {
                onRequestPermission()
            }
        }
    }

    fun handleLocationPermissionResult(granted: Boolean) {
        _locationPermissionState.value = granted
        if (!granted) {
            setLocationState(LocationState.PermissionRequired)
        }
    }

    private suspend fun loadSettings() {
        settingsRepository.getAppSettings().let { settings ->
            _temperatureUnit.value = settings.temperatureUnit
            _windSpeedUnit.value = settings.windSpeedUnit
        }
    }

    fun updateNetworkState(isAvailable: Boolean) {
        _networkState.value = isAvailable
        if (isAvailable) {
            refreshWeatherData()
        }
    }

    fun setLocationState(state: LocationState) {
        _locationState.value = state
        when (state) {
            is LocationState.Success -> {
                currentLocation = state.latitude to state.longitude
                fetchWeatherData(state.latitude, state.longitude)
            }

            is LocationState.Error -> {
                _weatherState.value = Result.Error(Exception(state.message))
                currentLocation = null
            }

            is LocationState.PermissionRequired -> {
                _weatherState.value = Result.Error(Exception("Location permission required"))
                currentLocation = null
            }

            else -> {
                currentLocation = null
            }
        }
    }

    fun fetchWeatherData(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _weatherState.value = Result.Loading
            try {
                val result = weatherRepository.getWeatherWithForecast(latitude, longitude)
                _weatherState.value = result
            } catch (e: Exception) {
                _weatherState.value = Result.Error(e)
            }
        }
    }

    private suspend fun checkLocationMethod() {
        when (settingsRepository.getAppSettings().locationMethod) {
            LocationMethod.GPS -> _locationState.value = LocationState.PermissionRequired
            LocationMethod.MAP -> _locationState.value =
                LocationState.Error("Please select location from map")
        }
    }

    fun refreshWeatherData() {
        currentLocation?.let { (lat, lon) ->
            fetchWeatherData(lat, lon)
        }
    }

    fun getLocationCallback(): LocationCallback? = locationCallback

    override fun onCleared() {
        super.onCleared()
        locationCallback = null
    }
}