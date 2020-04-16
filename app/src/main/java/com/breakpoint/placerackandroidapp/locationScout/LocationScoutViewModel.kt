/*
 * Copyright (c) 2020. Breakpoint Software Inc.
 */

package com.breakpoint.placerackandroidapp.screens

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.breakpoint.placerackandroidapp.locationScout.LocationInterface
import com.breakpoint.placerackandroidapp.locationScout.LocationModel
import com.breakpoint.placerackandroidapp.connectionScout.Network
import com.breakpoint.placerackandroidapp.connectionScout.Repository
import com.google.android.gms.location.*
import kotlinx.coroutines.*
import java.io.IOException
import java.util.*


class LocationScoutViewModel(application: Application) : AndroidViewModel(application),
    LocationInterface {

    companion object {
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(application.applicationContext)

    private lateinit var locationCallback: LocationCallback

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _getLatLng = MutableLiveData<LocationModel>()
    val getLatLng: LiveData<LocationModel>
        get() = _getLatLng

    private val _getAddress = MutableLiveData<String>()
    val getAddress: LiveData<String>
        get() = _getAddress

    private val _locationUpdateIsActive = MutableLiveData<Boolean>()
    val locationUpdateIsActive : LiveData<Boolean>
        get() = _locationUpdateIsActive

    private val repository = Repository()

    init {
        Log.i("LocationScoutViewModel","LocationScoutViewModel created")
        _locationUpdateIsActive.value = false
        initLocationCallback()

        viewModelScope.launch {
            repository.getFromURL()
        }
    }

    private fun getLastKnownLocation(){
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                if (location != null) {
                    setLocationData(location)
                    Log.i("LastKnownLocation",""+ location.latitude + ", " + location.longitude)
                }
            }
    }

    private fun initLocationCallback(){
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    Log.d("location",locationResult.locations.toString())
                    setLocationData(location)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun startLocationUpdates() {
        _locationUpdateIsActive.value = true
        getLastKnownLocation()
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    override fun setLocationData(location: Location) {
        _getLatLng.value =
            LocationModel(
                longitude = location.longitude,
                latitude = location.latitude
            )
        _getAddress.value = getAddress(getApplication(),location.latitude,location.longitude)
        if(_getAddress.value == null){
            _getAddress.value = "Unable to Fetch Address"
        }
    }

    fun isLocationUpdateActive() = _locationUpdateIsActive.value

    override fun onCleared() {
        super.onCleared()
        _locationUpdateIsActive.value = false
        Log.i("LocationScoutViewModel","LocationScoutViewModel destroyed")
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    fun getAddress(context: Context?, lat: Double, lng: Double): String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        return try {
            val addresses: List<Address> = geocoder.getFromLocation(lat, lng, 1)
            val obj: Address = addresses[0]
            var add: String = obj.getAddressLine(0)
            add = add + "," + obj.getAdminArea()
            add = add + "," + obj.getCountryName()
            add
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }




}