/*
 * Copyright (c) 2020. Breakpoint Software Inc.
 */

package com.breakpoint.placerackandroidapp.connectionScout

import com.breakpoint.placerackandroidapp.network.Book
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceRackNetworkAPi {

    @GET("5e8b352a2d0000271a1a4d5c")
    suspend fun getFeed(): Response<Book>

    @GET("PlaceRackServlets/GPlacemark")
    suspend fun getPlaces(@Query("uid") uid : String, @Query("lat") lat : Double,  @Query("lng") lng : Double): Response<String>

    //@QueryMap Map<String, String> queryMap
}