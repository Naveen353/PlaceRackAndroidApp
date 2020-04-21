/*
 * Copyright (c) 2020. Breakpoint Software Inc.
 */

package com.breakpoint.placerackandroidapp.connectionScout
import android.app.Application
import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(val application: Application) {

    suspend fun getFromURL(lat: Double, lng: Double) {
        withContext(Dispatchers.IO) {
            val response = Network.devbytes.getPlaces(uid = "thisisjadoo",lat = lat,lng = lng)
            //val feed = Network.devbytes.getFeed()
            if(response.isSuccessful){
                Log.d("raw_response",response.body().toString())
                writePostData(response.raw().toString())
            } else{
                Log.d("Error_response",response.message())
            }
        }
    }

    fun writePostData(file_data:String){
        val filename = "placeRackPlaces"
        //val file = File(context.filesDir, filename)

        application.openFileOutput(filename, Context.MODE_PRIVATE).use {

            it.write(file_data.toByteArray())
            Log.d("file_update","successful")
        }

        application.openFileInput(filename).bufferedReader().useLines { lines ->
            lines.fold("") { some, text ->
                Log.d("lines","$some\n$text")
                ""
            }
        }
    }

    fun getBinaryFromURL(){

    }

    fun postToURL(){

    }

    fun postBinaryFromURL(){

    }




}