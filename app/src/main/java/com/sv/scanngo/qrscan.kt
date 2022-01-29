package com.sv.scanngo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.zxing.integration.android.IntentIntegrator
import com.sv.scanngo.api.RetrofitInstance
import com.sv.scanngo.api.ScanNgoApi
import com.sv.scanngo.databinding.FragmentQrscanBinding
import com.sv.scanngo.model.item
import com.sv.scanngo.model.product
import com.sv.scanngo.viewModels.cartViewModel
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class qrscan : Fragment() {

   // private var owner_id:String?=null
    private var _binding:FragmentQrscanBinding?=null
    private val viewModel:cartViewModel by activityViewModels()
    private val binding get() = _binding!!
    private lateinit var qrScanIntegrator: IntentIntegrator
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentQrscanBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupScanner()
        binding.scanbtn.setOnClickListener { performAction() }
        if(viewModel.owner_Id==null){
            //findNavController().navigate(R.id.action_qrscan_to_home2)
            performAction()
        }
        else {
            binding.txt1.text=viewModel.owner_Id
        }
        binding.getItemBtn.setOnClickListener {
            //val action=qrscanDirections.actionQrscanToCart(ownerId = viewModel.owner_Id!!)
            findNavController().navigate(R.id.action_qrscan_to_cart)
        }
    }

    private fun setupScanner() {
        qrScanIntegrator=IntentIntegrator.forSupportFragment(this)
        qrScanIntegrator.setOrientationLocked(false)
    }

    private fun performAction(){
            qrScanIntegrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(activity, "Result Not Found", Toast.LENGTH_LONG).show()
            } else {
                try {
                    //owner_id=result.contents
                    viewModel.owner_Id=result.contents
                    binding.txt1.text=viewModel.owner_Id
                   // getItem(owner_id)
                    //val obj = JSONObject(result.contents)
                    //binding.txt1.text = obj.getString("name")
                    //binding.txt2.text = obj.getString("site_name")
                } catch (e: JSONException) {
                    e.printStackTrace()
                    //owner_id=result.contents
                    viewModel.owner_Id=result.contents
                    //getItem(owner_id)
                    binding.txt1.text=viewModel.owner_Id
                    Toast.makeText(activity, result.contents, Toast.LENGTH_LONG).show()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    private fun getItem(owner_id:String){
        var tokenFile: SharedPreferences? =activity?.getSharedPreferences("token",Context.MODE_PRIVATE)
        val default = resources.getString(R.string.app_name)
        val text = tokenFile?.getString("token",default).toString()
        var token = "Bearer_$text"
        val product=product()
        product.uid="22222222"
        product.owner_id=owner_id
        val scanNgoApi= RetrofitInstance.buildService(ScanNgoApi::class.java)
        val requestCall=scanNgoApi.getItem(product.uid.toString(),product.owner_id.toString())
        requestCall.enqueue(object :Callback<item>{
            override fun onResponse(call: Call<item>, response: Response<item>) {
                if (response.isSuccessful){
                    val Item:item?=response.body()
                    binding.txt1.text=Item!!.price
                   // binding.txt2.text= Item!!.quantity.toString()
                }
                else{
                    Toast.makeText(context,"Failed",Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<item>, t: Throwable) {
                Toast.makeText(context,"Failed to connect",Toast.LENGTH_LONG).show()
            }

        })

    }
}