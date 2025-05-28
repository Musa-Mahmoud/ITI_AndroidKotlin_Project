package com.iti.forecozy.utils.location

class LocationKey {
    companion object {
        fun getLocationKey(lat: Double, long: Double): String = "$lat,$long"
    }
}