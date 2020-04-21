/*
 * Copyright (c) 2020. Breakpoint Software Inc.
 */

package com.breakpoint.placerackandroidapp.connectionScout
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceRackNetworkAPi {

//    @GET("5e9dc7a3340000b2606ee974")
//    suspend fun getFeed(): Call<ResponseBody>

    @GET("PlaceRackServlets/GPlacemark")
    suspend fun getPlaces(@Query("uid") uid : String, @Query("lat") lat : Double,  @Query("lng") lng : Double): Response<String>
}