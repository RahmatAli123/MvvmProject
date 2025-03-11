package com.example.mvvmauth

import com.example.mvvmauth.AllDataModel.AddToCartDataModel

interface DeleteCartItem {
    fun deleteItem(position: Int,data: AddToCartDataModel)
}