package com.sv.scanngo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.sv.scanngo.api.RetrofitInstance
import com.sv.scanngo.api.ScanNgoApi
import com.sv.scanngo.data.signup
import com.sv.scanngo.data.token
import com.sv.scanngo.databinding.ActivitySignupBinding
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback
import kotlin.math.log

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
            SignIn()
        }
    }

    private fun SignIn() {
        val newsignin = signup()
        newsignin.email=binding.email.text.toString()
        newsignin.password=binding.pass.text.toString()
        val scanNgoApi=RetrofitInstance.buildService(ScanNgoApi::class.java)
        val requestCall = scanNgoApi.signin(newsignin)
        requestCall.enqueue(object :retrofit2.Callback<token>{
            override fun onResponse(call: Call<token>, response: Response<token>) {
                if (response.isSuccessful){
                    val login:token? = response.body()
                    Toast.makeText(this@Signup,login!!.token,Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@Signup,"Failed",Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<token>, t: Throwable) {
                Toast.makeText(this@Signup,"Failed",Toast.LENGTH_LONG).show()

            }
        })

    }

    private fun SignUp() {
        val newsignup = signup()
        newsignup.email=binding.email.text.toString()
        newsignup.password=binding.pass.text.toString()
        //Log.i(TAG, "SignUp: fails at service build")
        val scanNgoApi = RetrofitInstance.buildService(ScanNgoApi::class.java)
        //Log.i(TAG, "SignUp: failed at request call")
        val requestCall = scanNgoApi.signup(newsignup)
        //Log.i(TAG, "SignUp: failed after request call")
        requestCall.enqueue(object: retrofit2.Callback<token>{
            override fun onResponse(call: Call<token>,response: Response<token>){
                if (response.isSuccessful){
                    Toast.makeText(this@Signup, "Success",Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@Signup, "Failed to sign up", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<token>, t: Throwable) {
                Toast.makeText(this@Signup, "Failed to sign up", Toast.LENGTH_SHORT).show()
            }
        } )
    }
}