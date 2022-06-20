package com.sv.scanngo.viewModels

import android.content.res.Resources
import android.media.Image
import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import com.sv.scanngo.R
import com.sv.scanngo.model.item
import com.sv.scanngo.model.promotions

class cartViewModel:ViewModel() {
     var ItemList:MutableList<item> = mutableListOf<item>()
     var owner_Id:String?=null

}