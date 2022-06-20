package com.sv.scanngo.api

import android.os.Build
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

object RetrofitInstance {

                private const val URL = "https://scan-n-go-api.herokuapp.com/customer/"

                private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

                val headerInterceptor = object :Interceptor{
                    override fun intercept(chain: Interceptor.Chain): Response {
                        var request = chain.request()

                        request=request.newBuilder()
                            .addHeader("x-device-type", Build.DEVICE)
                            .addHeader("Accept-Langugae", Locale.getDefault().language)
                            .build()
                        val response = chain.proceed(request)
                        return response
                    }
                }
                private val okHttp:OkHttpClient.Builder=OkHttpClient.Builder()
                    .callTimeout(5,TimeUnit.SECONDS)
                    .addInterceptor(headerInterceptor)
                    .addInterceptor(logger)
                private val builder: Retrofit.Builder = Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttp.build())

        private val retrofit= builder.build()

        fun<T> buildService(serviceType: Class<T>): T{
            return retrofit.create(serviceType)
        }

}