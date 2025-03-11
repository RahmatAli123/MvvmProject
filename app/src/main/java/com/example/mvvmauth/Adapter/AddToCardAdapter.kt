package com.example.mvvmauth.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvvmauth.AllDataModel.AddToCartDataModel
import com.example.mvvmauth.DeleteCartItem
import com.example.mvvmauth.R


class AddToCartAdapter(private val addToCardList:ArrayList<AddToCartDataModel>,val deleteCartItem: DeleteCartItem):RecyclerView.Adapter<AddToCartAdapter.AddToCardViewHolder>() {
    inner class AddToCardViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val cartTitleTextView:TextView=itemView.findViewById(R.id.cartTittle_TextView)
        val cartDescriptionTextView:TextView=itemView.findViewById(R.id.cartDescription_TextView)
        val cartPriceTextView:TextView=itemView.findViewById(R.id.cartPrice_TextView)
        val deleteCartItem:TextView=itemView.findViewById(R.id.delete_cart_items)
        val cartImageView:ImageView=itemView.findViewById(R.id.cartImageView)




    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddToCardViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.cart_item_layout,parent,false)
        return AddToCardViewHolder(view)

    }


    override fun getItemCount(): Int {
        return addToCardList.size


    }


    override fun onBindViewHolder(holder: AddToCardViewHolder, position: Int) {
        val currentItem=addToCardList[position]
        holder.cartTitleTextView.text=currentItem.cartTittle
        holder.cartPriceTextView.text=currentItem.cartPrice
        holder.cartDescriptionTextView.text=currentItem.cartDescription
        Glide.with(holder.itemView.context).load(currentItem.cartImage).into(holder.cartImageView)
        holder.deleteCartItem.setOnClickListener {
            deleteCartItem.deleteItem(position,currentItem)

        }


    }
}