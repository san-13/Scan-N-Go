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
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.zxing.integration.android.IntentIntegrator
import com.sv.scanngo.adapters.itemsadapter
import com.sv.scanngo.api.RetrofitInstance
import com.sv.scanngo.api.ScanNgoApi
import com.sv.scanngo.databinding.FragmentCartBinding
import com.sv.scanngo.model.item
import com.sv.scanngo.model.product
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Cart : Fragment() {
    private val navigationArgs:CartArgs by navArgs()
    private var _binding:FragmentCartBinding?=null
    private val binding get() = _binding!!
    private lateinit var qrScanIntegrator: IntentIntegrator
    var uid="0"
    private var ItemList:MutableList<item> = mutableListOf<item>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            _binding= FragmentCartBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupScanner()
        //ItemList= arrayListOf<item>()
        binding.addItem.setOnClickListener {
            performAction()
        }
        binding.barcode.setOnClickListener {
            getItem(uid)
        }
        binding.itemRecycler.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
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
                    //binding.txt1.text=owner_id
                    // getItem(owner_id)
                    //val obj = JSONObject(result.contents)
                    binding.barcode.text = result.contents
                    uid=result.contents
                    getItem(uid)
                    //binding.txt2.text = obj.getString("site_name")
                } catch (e: JSONException) {
                    e.printStackTrace()
                    //owner_id=result.contents
                    //getItem(owner_id)
                    //binding.txt1.text=owner_id
                    Toast.makeText(activity, result.contents, Toast.LENGTH_LONG).show()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    private fun getItem(uid:String){

        var tokenFile: SharedPreferences? =activity?.getSharedPreferences("token", Context.MODE_PRIVATE)
        val default = resources.getString(R.string.app_name)
        val text = tokenFile?.getString("token",default).toString()
        var token = "Bearer_$text"
        val product= product()
        product.uid=uid
        product.owner_id=navigationArgs.ownerId
        val scanNgoApi= RetrofitInstance.buildService(ScanNgoApi::class.java)
        val requestCall=scanNgoApi.getItem(product.uid.toString(),product.owner_id.toString())
        requestCall.enqueue(object : Callback<item> {
            override fun onResponse(call: Call<item>, response: Response<item>) {
                if (response.isSuccessful){
                    val Item: item?=response.body()
                    ItemList.add(Item!!)
                    //viewmodel.add(Item)
                    binding.priceTxt.text=Item!!.price
                    binding.quantityTxt.text= Item!!.quantity.toString()
                    binding.itemRecycler.adapter=itemsadapter(ItemList)
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