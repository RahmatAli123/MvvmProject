package com.example.mvvmauth.AllViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmauth.SignUpDataModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    val userLiveData = MutableLiveData<SignUpDataModel?>()


    fun createSignUp(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val hashMap = HashMap<String, Any>()
                hashMap["name"] = name
                hashMap["email"] = email
                hashMap["password"] = password
                hashMap["image"] = ""
                database.getReference().child("MvvmAuth").child(auth.currentUser!!.uid).setValue(hashMap)
                val user = SignUpDataModel(name, email, password)
                userLiveData.postValue(user)


            }
            .addOnFailureListener {
            }
    }

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                userLiveData.postValue(
                    SignUpDataModel(
                        name = "",
                        email = email,
                        password = password
                    )
                )
            }
            .addOnFailureListener {
                userLiveData.postValue(null)
            }


    }

}




