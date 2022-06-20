package com.sv.scanngo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.sv.scanngo.api.RetrofitInstance
import com.sv.scanngo.api.ScanNgoApi
import com.sv.scanngo.databinding.ActivityMainBinding
import com.sv.scanngo.model.logout
import retrofit2.Call
import retrofit2.Response

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
        binding.topAppBar.setOnMenuItemClickListener { menuItem->
            when(menuItem.itemId){
                R.id.takeout ->{
                    true
                }
                R.id.storeLoc ->{
                    true
                }
                R.id.searchProd ->{
                    true
                }
                R.id.shopNearme ->{
                    true
                }
                R.id.purchases ->{
                    true
                }
                R.id.receipt ->{
                    true
                }
                R.id.help ->{
                    true
                }
                R.id.tNc ->{
                    true
                }
                R.id.privacyPol ->{
                    true
                }
                R.id.LogOut ->{
                    Logout()
                    true
                }
                else -> false
            }
        }


    }
    private fun Logout() {
        var tokenFile=this.getSharedPreferences("tokenfile", Context.MODE_PRIVATE)
        val default = resources.getString(R.string.app_name)
        val text = tokenFile?.getString("token",default).toString()
        var token = "Bearer_$text"
        val scanNgoApi = RetrofitInstance.buildService(ScanNgoApi::class.java)
        val requestCall=scanNgoApi.logout(token)
        requestCall.enqueue(object :retrofit2.Callback<logout>{
            override fun onResponse(call: Call<logout>, response: Response<logout>) {
                tokenFile!!.edit().putString("token","").apply()
                if(response.isSuccessful){
                    // Toast.makeText(activity, "Success", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@MainActivity,Signup::class.java))
                }
                else {
                    Toast.makeText(this@MainActivity, "Log Out Failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<logout>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Could not connect to Server", Toast.LENGTH_SHORT).show()

            }
        })
    }

}