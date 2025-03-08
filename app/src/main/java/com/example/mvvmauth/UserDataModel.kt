package com.example.mvvmauth

class ProductDataModel (
    val image: String?=null,
    val name: String?=null,
    var price: String?=null,
    val description: String?=null,
)

data class SignUpDataModel(
    val name: String,
    val email:String,
   val password:String
)
data class ProfileModel(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val image: String? = null

)
