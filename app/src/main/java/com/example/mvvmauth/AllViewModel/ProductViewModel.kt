package com.example.mvvmauth.AllViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmauth.AllDataModel.ProductDataModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProductViewModel: ViewModel() {
    val productListLiveData = MutableLiveData<List<ProductDataModel>?>()
    private val database = FirebaseDatabase.getInstance()

    fun getProductListAll() {
        database.getReference("products").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = ArrayList<ProductDataModel>()
                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(ProductDataModel::class.java)
                    if (product!=null){
                        productList.add(product)
                        productListLiveData.postValue(productList)

                    }


                }
            }

            override fun onCancelled(error: DatabaseError) {
                productListLiveData.postValue(null)


            }

        })
    }
}