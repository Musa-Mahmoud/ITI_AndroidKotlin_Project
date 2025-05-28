package com.iti.forecozy.utils.types

sealed class LocationState {
    object Loading : LocationState()
    data class Success(val latitude: Double, val longitude: Double) : LocationState()
    data class Error(val message: String) : LocationState()
    object PermissionRequired : LocationState()
    object LocationDisabled : LocationState()
}
