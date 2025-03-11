package com.example.mvvmauth.AllViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmauth.AllDataModel.OrderDataModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyOrderViewModel: ViewModel()  {
    val orderList = MutableLiveData<ArrayList<OrderDataModel>>()
    private val db = FirebaseDatabase.getInstance()
    private val userId= FirebaseAuth.getInstance().currentUser?.uid

    fun fetchOrderData() {
        db.getReference("products").child("orders").child(userId!!).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val orderList = ArrayList<OrderDataModel>()
                for (data in snapshot.children){
                    try {
                        val item = data.getValue(OrderDataModel::class.java)
                        if (item != null) {
                            orderList.add(item)
                        }
                        } catch (e: Exception) {
                        Log.e("FirebaseError", "Data conversion error: ${e.message}")
                    }
                    this@MyOrderViewModel.orderList.postValue(orderList)

                }


            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Error fetching order data: ${error.message}")

            }

        })

    }
}