package com.sv.scanngo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.sv.scanngo.api.RetrofitInstance
import com.sv.scanngo.api.ScanNgoApi
import com.sv.scanngo.model.login
import com.sv.scanngo.model.signup
import com.sv.scanngo.model.token
import com.sv.scanngo.databinding.ActivitySignupBinding
import com.sv.scanngo.model.logout
import retrofit2.Call
import retrofit2.Response

class Signup : AppCompatActivity() {

    val TAG = "dbtest"
    private lateinit var binding:ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupBtn.setOnClickListener {
            SignUp()
        }
        binding.signinBtn.setOnClickListener {
            startActivity(Intent(this,Login::class.java))
        }

    }

   /* private fun LogOut() {
            var tokenFile=getSharedPreferences("tokenfile",Context.MODE_PRIVATE)
        val default = resources.getString(R.string.app_name)
        val text = tokenFile.getString("token",default).toString()
        val tkn = "Bearer_$text"
           // val headerMap= mutableMapOf<String,String>()
           // headerMap["token"] = text.toString()
        val scanNgoApi = RetrofitInstance.buildService(ScanNgoApi::class.java)
        val requestCall=scanNgoApi.logout(tkn)
        requestCall.enqueue(object :retrofit2.Callback<logout>{
            override fun onResponse(call: Call<logout>, response: Response<logout>) {
                //tokenFile.edit().putString("token","").apply()
                if(response.isSuccessful){
                    Toast.makeText(this@Signup, "Success",Toast.LENGTH_LONG).show()
                }
                else {
                    Toast.makeText(this@Signup, "Failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<logout>, t: Throwable) {
                Toast.makeText(this@Signup, "Failed to sign up", Toast.LENGTH_SHORT).show()

            }

        })
    }*/

    private fun SignUp() {
        val tokenFile:SharedPreferences =getSharedPreferences("tokenfile",Context.MODE_PRIVATE)
        val newsignup = signup()
        newsignup.email=binding.email.editText.toString()
        newsignup.password=binding.pass.editText.toString()
        newsignup.phonenumber=binding.phone.editText.toString()
        newsignup.username=binding.username.editText.toString()
        //Log.i(TAG, "SignUp: fails at service build")
        val scanNgoApi = RetrofitInstance.buildService(ScanNgoApi::class.java)
        //Log.i(TAG, "SignUp: failed at request call")
        val requestCall = scanNgoApi.signup(newsignup)
        //Log.i(TAG, "SignUp: failed after request call")
        requestCall.enqueue(object: retrofit2.Callback<token>{
            override fun onResponse(call: Call<token>,response: Response<token>){
                if (response.isSuccessful){
                   val Token:token?=response.body()
                    tokenFile.edit().putString("token",Token!!.token)
                        .apply()
                    //Toast.makeText(this@Signup, "Success",Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@Signup,MainActivity::class.java))
                }
                else{
                    Toast.makeText(this@Signup, "Failed to Sign Up", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<token>, t: Throwable) {
                Toast.makeText(this@Signup, "Could not connect to Server", Toast.LENGTH_SHORT).show()
            }
        })
    }
}