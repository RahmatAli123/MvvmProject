package com.example.mvvmauth.Adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvvmauth.AllDataModel.OrderDataModel
import com.example.mvvmauth.R

class OrdersAdapter(val orderList:ArrayList<OrderDataModel>):RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {
    inner class ViewHolder(item:View):RecyclerView.ViewHolder(item){
        val orderImageView=item.findViewById<ImageView>(R.id.orderImageView)
        val orderTittle=item.findViewById<TextView>(R.id.orderTittle)
        val orderDescription=item.findViewById<TextView>(R.id.orderDescription)
        val orderPriceTV=item.findViewById<TextView>(R.id.orderPriceTV)

    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val view=View.inflate(parent.context,R.layout.order_item_layout,null)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orderList.size


    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val orderItem=orderList[position]
        holder.orderTittle.text=orderItem.orderTittle
        holder.orderDescription.text=orderItem.orderDescription
        holder.orderPriceTV.text=orderItem.orderPrice
        Glide.with(holder.itemView.context).load(orderItem.orderImage).into(holder.orderImageView)


    }
}