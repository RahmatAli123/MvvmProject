package com.example.mvvmauth

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class UserAdapter(private val userList:ArrayList<ProductDataModel>, private val myContext: Context): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    inner class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nameTextView: TextView = itemView.findViewById(R.id.name_TextView)
        val priceTextView: TextView = itemView.findViewById(R.id.price_TextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.description_TextView)
        val productImageView: ImageView =itemView.findViewById(R.id.image_ImageView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
       val view = LayoutInflater.from(myContext).inflate(R.layout.user_item_layout,parent,false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.nameTextView.text = currentUser.name
        holder.priceTextView.text = currentUser.price
        holder.descriptionTextView.text = currentUser.description
        Glide.with(myContext).load(currentUser.image).into(holder.productImageView)

    }
}