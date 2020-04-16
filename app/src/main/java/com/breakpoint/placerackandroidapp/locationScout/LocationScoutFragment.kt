/*
 * Copyright (c) 2020. Breakpoint Software Inc.
 */

package com.breakpoint.placerackandroidapp.locationScout

import android.Manifest
import android.annotation.TargetApi
import android.app.Application
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.breakpoint.placerackandroidapp.BuildConfig
import com.breakpoint.placerackandroidapp.R
import com.breakpoint.placerackandroidapp.databinding.LocationScoutBinding
import com.breakpoint.placerackandroidapp.screens.LocationScoutViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.snackbar.Snackbar

class LocationScoutFragment : Fragment(){

    private lateinit var binding: LocationScoutBinding

    private lateinit var viewModel: LocationScoutViewModel

    private lateinit var viewModelFactory: LocationScoutViewModelFactory

    private val runningQOrLater =
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q

    private lateinit var application: Application

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {

        binding = DataBindingUtil.inflate(
                inflater,
        R.layout.location_scout,
        container,
        false
        )
        application = requireNotNull(this.activity).application
        viewModelFactory =
            LocationScoutViewModelFactory(
                application
            )

        viewModel = ViewModelProviders.of(this,viewModelFactory).get(LocationScoutViewModel::class.java)
        binding.locationScoutViewModel = viewModel

        //Updates Lat Lng to UI
        viewModel.getLatLng.observe(viewLifecycleOwner, Observer {
            binding.locationString.text =  getString(R.string.latLong, it.longitude, it.latitude)
        })

        //Updates Address to UI
        viewModel.getAddress.observe(viewLifecycleOwner, Observer {
            binding.locationAddress.text =  getString(R.string.address,it)
        })

        return binding.root
    }

    private fun updateLabelWithLocation() {
        viewModel.startLocationUpdates()
    }

    private fun updateTextFieldWithLocation(){
        // updateLabelWithLocation updates LatLng and corresponding address to the Views.
    }

    override fun onStart() {
        super.onStart()
        checkPermissionsAndStartLocationUpdates()
    }

    private fun checkPermissionsAndStartLocationUpdates() {
        if(viewModel.isLocationUpdateActive()!!)return
        if (LocationScoutHelper(
                application
            ).foregroundAndBackgroundLocationPermissionApproved()) {
            checkDeviceLocationSettingsAndStartLocationUpdates()
        } else {
            requestForegroundAndBackgroundLocationPermissions()
        }
    }

    /*
 *  Requests ACCESS_FINE_LOCATION and (on Android 10+ (Q) ACCESS_BACKGROUND_LOCATION.
 */
    @TargetApi(29 )
    private fun requestForegroundAndBackgroundLocationPermissions() {
        if (LocationScoutHelper(
                application
            ).foregroundAndBackgroundLocationPermissionApproved())
            return

        // Else request the permission
        // this provides the result[LOCATION_PERMISSION_INDEX]
        var permissionsArray = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

        val resultCode = when {
            runningQOrLater -> {
                // this provides the result[BACKGROUND_LOCATION_PERMISSION_INDEX]
                permissionsArray += Manifest.permission.ACCESS_BACKGROUND_LOCATION
                REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE
            }
            else -> REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
        }

        Log.d(TAG, "Request foreground only location permission")
        requestPermissions(
            permissionsArray,
            resultCode
        )
    }

    /*
 * In all cases, we need to have the location permission.  On Android 10+ (Q) we need to have
 * the background permission as well.
 */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d(TAG, "onRequestPermissionResult")

        if (
            grantResults.isEmpty() ||
            grantResults[LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED ||
            (requestCode == REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE &&
                    grantResults[BACKGROUND_LOCATION_PERMISSION_INDEX] ==
                    PackageManager.PERMISSION_DENIED))
        {
            // Permission denied.
            Snackbar.make(
                binding.locationLayout,
                R.string.permission_denied_explanation, Snackbar.LENGTH_INDEFINITE
            )
                .setAction(R.string.settings) {
                    // Displays App settings screen.
                    startActivity(Intent().apply {
                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                }.show()
        } else {
            checkDeviceLocationSettingsAndStartLocationUpdates()
        }
    }

    /*
 *  When we get the result from asking the user to turn on device location, we call
 *  checkDeviceLocationSettingsAndStartGeofence again to make sure it's actually on, but
 *  we don't resolve the check to keep the user from seeing an endless loop.
 */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TURN_DEVICE_LOCATION_ON) {
            // We don't rely on the result code, but just check the location setting again
            checkDeviceLocationSettingsAndStartLocationUpdates(false)
        }
    }
    /*
 *  Uses the Location Client to check the current state of location settings, and gives the user
 *  the opportunity to turn on location services within our app.
 */
    private fun checkDeviceLocationSettingsAndStartLocationUpdates(resolve:Boolean = true) {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_LOW_POWER
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val settingsClient = LocationServices.getSettingsClient(application)
        val locationSettingsResponseTask =
            settingsClient.checkLocationSettings(builder.build())

        locationSettingsResponseTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException && resolve){
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    startIntentSenderForResult(exception.resolution.intentSender,
                        REQUEST_TURN_DEVICE_LOCATION_ON, null, 0, 0, 0, null);
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.d(TAG, "Error getting location settings resolution: " + sendEx.message)
                }
            } else {
                Snackbar.make(
                    binding.locationLayout,
                    R.string.location_required_error, Snackbar.LENGTH_INDEFINITE
                ).setAction(android.R.string.ok) {
                    checkDeviceLocationSettingsAndStartLocationUpdates()
                }.show()
            }
        }
        locationSettingsResponseTask.addOnCompleteListener {
            if ( it.isSuccessful ) {
                updateLabelWithLocation()
            }
        }
    }
}
 const val REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE = 33
 const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
 const val REQUEST_TURN_DEVICE_LOCATION_ON = 29
 const val TAG = "LocationScoutFragment"
 const val LOCATION_PERMISSION_INDEX = 0
 const val BACKGROUND_LOCATION_PERMISSION_INDEX = 1
private const val LOCATION_REQUEST = 100
const val GPS_REQUEST = 101