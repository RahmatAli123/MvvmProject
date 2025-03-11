package com.example.mvvmauth.AllViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmauth.AllDataModel.AddToCartDataModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FetchCartDataViewModel: ViewModel() {
    val productList = MutableLiveData<ArrayList<AddToCartDataModel>>()
    private val db = FirebaseDatabase.getInstance()

    fun fetchCartData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        db.getReference("products").child("cart").child(userId!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val cartList = ArrayList<AddToCartDataModel>()
                    for (data in snapshot.children) {
                        try {
                            val item = data.getValue(AddToCartDataModel::class.java)
                            if (item != null) {
                                cartList.add(item)
                            }
                        } catch (e: Exception) {
                            Log.e("FirebaseError", "Data conversion error: ${e.message}")
                        }
                    }
                    productList.postValue(cartList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseError", "Error fetching cart data: ${error.message}")
                }
            })
    }}