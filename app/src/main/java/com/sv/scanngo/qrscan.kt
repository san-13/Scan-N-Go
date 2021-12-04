package com.sv.scanngo

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import com.sv.scanngo.databinding.FragmentQrscanBinding
import org.json.JSONException
import org.json.JSONObject


class qrscan : Fragment() {

    private var _binding:FragmentQrscanBinding?=null
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
                    val obj = JSONObject(result.contents)
                    binding.txt1.text = obj.getString("name")
                    binding.txt2.text = obj.getString("site_name")
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(activity, result.contents, Toast.LENGTH_LONG).show()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}