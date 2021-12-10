package com.sv.scanngo.api

import com.sv.scanngo.data.signup
import com.sv.scanngo.data.token
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ScanNgoApi {
    @POST("signup")
    fun signup(@Body sgnup:signup): Call<token>

    @POST("login")
    fun signin(@Body sgnin:signup): Call<token>
}