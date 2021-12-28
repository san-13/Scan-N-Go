package com.sv.scanngo.api

import android.content.Context
import android.content.SharedPreferences
import com.sv.scanngo.model.*
import retrofit2.Call
import retrofit2.http.*

interface ScanNgoApi {


    @POST("signup")
    fun signup(@Body sgnup:signup): Call<token>

    @POST("login")
    fun login(@Body sgnin:login): Call<token>

    @POST("logout")
    fun logout(@Header("Authorization") token:String):Call<logout>

    @GET("product")
    fun getItem(@Query("uid") uid:String,@Query("owner_id") ownerid:String):Call<item>

}