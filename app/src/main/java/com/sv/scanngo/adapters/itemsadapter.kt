package com.sv.scanngo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sv.scanngo.R
import com.sv.scanngo.model.item

class itemsadapter(private var Items:MutableList<item> ) :
RecyclerView.Adapter<itemsadapter.ViewHolder>(){
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
            val price:TextView=view.findViewById(R.id.price_txt)
            val quantity:TextView=view.findViewById(R.id.quantity_txt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_list,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        val Item = Items[position]
        holder.price.text=Item.price.toString()
        holder.quantity.text= Item.quantity.toString()
    }

    override fun getItemCount(): Int {
        return Items.size
    }
}