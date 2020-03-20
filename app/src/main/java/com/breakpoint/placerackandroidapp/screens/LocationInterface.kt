/*
 * Copyright (c) 2020. Breakpoint Software Inc.
 */

package com.breakpoint.placerackandroidapp.screens

import android.location.Location

interface LocationInterface {
    fun setLocationData(location: Location)
    fun startLocationUpdates()
}