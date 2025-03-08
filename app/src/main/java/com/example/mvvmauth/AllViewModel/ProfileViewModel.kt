package com.example.mvvmauth.AllViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmauth.ProfileModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().getReference("MvvmAuth")

    private val _profileUpdateStatus = MutableLiveData<ProfileModel>()
    val updateStatus: MutableLiveData<ProfileModel> = _profileUpdateStatus

    private val _userList = MutableLiveData<List<ProfileModel>>()
    val userList: MutableLiveData<List<ProfileModel>> = _userList

    fun updateProfile(name: String, email: String, imageUri: String) {
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            val profileUpdates = mutableMapOf<String, Any>()
            profileUpdates["name"] = name
            profileUpdates["email"] = email
            profileUpdates["image"] = imageUri
            database.child(userId).updateChildren(profileUpdates)
                .addOnSuccessListener {
                    _profileUpdateStatus.postValue(ProfileModel(name, email, imageUri)) // Update successful

                }
                .addOnFailureListener {
                    _profileUpdateStatus.postValue(ProfileModel())
                }
        }
    }

    fun profileFetchUsers() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = mutableListOf<ProfileModel>()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(ProfileModel::class.java)
                    if (user != null) {
                        userList.add(user)
                    }
                }
                _userList.postValue(userList)
            }

            override fun onCancelled(error: DatabaseError) {
                _userList.postValue(emptyList()) // In case of error, return empty list
            }
        })
    }
}
