package com.sv.scanngo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import com.sv.scanngo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var toggle:ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val tokenFile:SharedPreferences=getSharedPreferences("tokenfile", Context.MODE_PRIVATE)
        val default = ""
        val token=tokenFile.getString("token",default)
        if(token==""){
            startActivity(Intent(this,Signup::class.java))
        }
        binding.topAppBar.setNavigationOnClickListener{
            binding.drawerLayout.open()
        }


    }
}