package com.breakpoint.placerackandroidapp.screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.breakpoint.placerackandroidapp.R
import com.breakpoint.placerackandroidapp.databinding.LocationScoutBinding
import com.breakpoint.placerackandroidapp.utils.GpsUtils

class LocationScoutFragment : Fragment(){

    private lateinit var binding: LocationScoutBinding

    private lateinit var viewModel: LocationScoutViewModel

    private lateinit var viewModelFactory: LocationScoutViewModelFactory

    private var isGPSEnabled = true

    private lateinit var application: Application



    @SuppressLint("SetTextI18n")
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
        viewModelFactory = LocationScoutViewModelFactory(application)
        Log.i("LocationScoutViewModel","called viewmodel providers.!")


//        GpsUtils(application).turnGPSOn(object : GpsUtils.OnGpsListener {
//
//            override fun gpsStatus(isGPSEnable: Boolean) {
//                this@LocationScoutFragment.isGPSEnabled = isGPSEnable
//            }
//        })

        //for viewmodel
//        binding.lifecycleOwner = this// for livedata
//        viewModel.getLatLng.observe(viewLifecycleOwner, Observer {
//            binding.locationString.text =  getString(R.string.latLong, it.longitude, it.latitude)
//        })

        invokeLocationAction()


        return binding.root
    }

    private fun invokeLocationAction() {
        when {
            !isGPSEnabled -> binding.locationString.text = getString(R.string.enable_gps)

            isPermissionsGranted() -> startLocationUpdate()

            shouldShowRequestPermissionRationale() -> binding.locationString.text = getString(R.string.permission_request)

            else -> requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_REQUEST
            )
        }
    }

    private fun startLocationUpdate() {
        Log.d("startLocationUpdate","here")
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(LocationScoutViewModel::class.java)
        binding.locationScoutViewModel = viewModel
            viewModel.getLatLng.observe(viewLifecycleOwner, Observer {
            binding.locationString.text =  getString(R.string.latLong, it.longitude, it.latitude)
        })
    }

    private fun isPermissionsGranted() =
        ActivityCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    application,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    private fun shouldShowRequestPermissionRationale() =
        shouldShowRequestPermissionRationale(
            Manifest.permission.ACCESS_FINE_LOCATION
        ) && shouldShowRequestPermissionRationale(
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    @SuppressLint("MissingPermission", "LongLogTag")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST -> {
                Log.d("onRequestPermissionsResult",""+isGPSEnabled)
                invokeLocationAction()
            }
        }
    }

}
const val LOCATION_REQUEST = 100
const val GPS_REQUEST = 101