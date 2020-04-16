/*
 * Copyright (c) 2020. Breakpoint Software Inc.
 */

package com.breakpoint.placerackandroidapp.screens

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocationScoutViewModelTest{

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun location_test(){
        // Given a fresh TasksViewModel
        val locationScoutViewModel = LocationScoutViewModel(ApplicationProvider.getApplicationContext())


        // When

        // Then

        //assertThat(value.getContentIfNotHandled(), (CoreMatchers.not(CoreMatchers.nullValue())))
    }

}