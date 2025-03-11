package com.example.mvvmauth.AllViewModel

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmauth.AllDataModel.AddToCartDataModel
import com.example.mvvmauth.Fragment.CardFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.UUID

class ProductDetailsViewModel:ViewModel() {
    private val product = MutableLiveData<AddToCartDataModel>()
    private val db = FirebaseDatabase.getInstance()
    private val random = UUID.randomUUID().toString()

    fun addToCartInsertData(myContext: Context, productTittle: String, productPrice: String, productDescription: String, productImage: String) {
        val hashMap = HashMap<String, String>()
        hashMap["cartId"] = random
        hashMap["cartTittle"] = productTittle
        hashMap["cartPrice"] = productPrice
        hashMap["cartDescription"] = productDescription
        hashMap["cartImage"] = productImage

        db.getReference("products").child("cart")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child(random).setValue(hashMap)
            .addOnSuccessListener {
                product.value = AddToCartDataModel(
                    cartId = random,
                    productTittle,
                    cartPrice = productPrice,
                    cartDescription = productDescription,
                    cartImage = productImage
                )
                Toast.makeText(myContext, "AddToCart Success", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener {
                Toast.makeText(myContext, "AddToCart Failed", Toast.LENGTH_SHORT).show()

            }

    }
}