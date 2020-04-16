/*
 * Copyright (c) 2020. Breakpoint Software Inc.
 */

package com.breakpoint.placerackandroidapp.connectionScout
import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository {
    suspend fun getFromURL() {
        withContext(Dispatchers.IO) {
            val feed = Network.devbytes.getPlaces(uid = "thisisjadoo",lat = 37.785834,lng = -122.406417)
            //val feed = Network.devbytes.getFeed()
            if(feed.isSuccessful){
                writePostData(feed.toString())
            } else{
                Log.d("Error_response",feed.message())
            }
        }
    }

    fun writePostData(file_data:String){

    }

    fun getBinaryFromURL(){

    }

    fun postToURL(){

    }

    fun postBinaryFromURL(){

    }




}