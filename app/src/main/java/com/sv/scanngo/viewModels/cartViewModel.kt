package com.sv.scanngo.viewModels

import androidx.lifecycle.ViewModel
import com.sv.scanngo.model.item

class cartViewModel:ViewModel() {
     var ItemList:MutableList<item> = mutableListOf<item>()
     var owner_Id:String?=null

}