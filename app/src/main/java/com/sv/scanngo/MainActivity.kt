package com.sv.scanngo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tokenFile:SharedPreferences=getSharedPreferences("tokenfile", Context.MODE_PRIVATE)
        val default = ""
        val token=tokenFile.getString("token",default)
        if(token==""){
            startActivity(Intent(this,Signup::class.java))
        }
    }
}