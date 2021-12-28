package com.sv.scanngo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.sv.scanngo.api.RetrofitInstance
import com.sv.scanngo.api.ScanNgoApi
import com.sv.scanngo.databinding.FragmentHomeBinding
import com.sv.scanngo.model.logout
import com.sv.scanngo.model.token
import retrofit2.Call
import retrofit2.Response


class Home : Fragment() {

    private var _binding:FragmentHomeBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.qrbtn.setOnClickListener {
            findNavController().navigate(R.id.action_home2_to_qrscan)
        }
        binding.signAct.setOnClickListener {
            startActivity(Intent(context,Signup::class.java))
        }
        binding.logoutBtn.setOnClickListener {
            Logout()
        }
    }

    private fun Logout() {
        var tokenFile=activity?.getSharedPreferences("tokenfile", Context.MODE_PRIVATE)
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
                    startActivity(Intent(context,Signup::class.java))
                }
                else {
                    Toast.makeText(activity, "Log Out Failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<logout>, t: Throwable) {
                Toast.makeText(activity, "Could not connect to Server", Toast.LENGTH_SHORT).show()

            }
        })
    }

}