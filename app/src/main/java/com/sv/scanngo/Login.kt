package com.sv.scanngo
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.sv.scanngo.api.RetrofitInstance
import com.sv.scanngo.api.ScanNgoApi
import com.sv.scanngo.databinding.ActivityLoginBinding
import com.sv.scanngo.model.login
import com.sv.scanngo.model.token
import retrofit2.Call
import retrofit2.Response

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginBtn.setOnClickListener {
            LogIn()
        }

    }

    private fun LogIn() {
        val newlogin = login()
        newlogin.email=binding.email.text.toString()
        newlogin.password=binding.pass.text.toString()
        val scanNgoApi= RetrofitInstance.buildService(ScanNgoApi::class.java)
        val requestCall = scanNgoApi.login(newlogin)
        requestCall.enqueue(object :retrofit2.Callback<token>{
            override fun onResponse(call: Call<token>, response: Response<token>) {
                if (response.isSuccessful){
                    val tokenFile:SharedPreferences =getSharedPreferences("tokenfile",Context.MODE_PRIVATE)
                    val login: token? = response.body()
                    tokenFile.edit().putString("token",login!!.token).apply()
                    //Toast.makeText(this@Login,"Logged In", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@Login,MainActivity::class.java))
                }
                else{
                    Toast.makeText(this@Login,"Log In Failed", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<token>, t: Throwable) {
                Toast.makeText(this@Login,"Could not connect to Server", Toast.LENGTH_LONG).show()

            }
        })
    }
}