/*
 * Copyright (c) 2020. Breakpoint Software Inc.
 */

package com.breakpoint.placerackandroidapp.connectionScout

import com.breakpoint.placerackandroidapp.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

/**
 * A retrofit service to fetch a devbyte playlist.
 */

/**
 * Main entry point for network access.
 */
object Network{
    // Configure retrofit to parse JSON and use coroutines
    private val httpClient = OkHttpClient.Builder()

    init {
        if (BuildConfig.DEBUG)
        {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)
        }
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.placerack.com:8443/")
        .addConverterFactory(SimpleXmlConverterFactory.create())
        .client(httpClient.build())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val devbytes = retrofit.create(
        PlaceRackNetworkAPi::class.java)
}