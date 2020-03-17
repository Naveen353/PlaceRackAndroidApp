package com.breakpoint.placerackandroidapp.screens

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LocationScoutViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationScoutViewModel::class.java)) {
            return LocationScoutViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}