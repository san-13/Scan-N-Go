package com.sv.scanngo

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sv.scanngo.databinding.FragmentHomeBinding


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

    }

}