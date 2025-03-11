package com.example.mvvmauth.AllViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmauth.AllDataModel.ProfileDataModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().getReference("MvvmAuth")

    private val _profileUpdateStatus = MutableLiveData<ProfileDataModel>()
    val updateStatus: MutableLiveData<ProfileDataModel> = _profileUpdateStatus

    private val _userList = MutableLiveData<List<ProfileDataModel>>()
    val userList: MutableLiveData<List<ProfileDataModel>> = _userList

    fun updateProfile(name: String, email: String, imageUri: String) {
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            val profileHashMapUpdates = HashMap<String, Any>()
            profileHashMapUpdates["name"] = name
            profileHashMapUpdates["email"] = email
            profileHashMapUpdates["image"] = imageUri
            database.child(userId).updateChildren(profileHashMapUpdates)
                .addOnSuccessListener {
                    _profileUpdateStatus.postValue(ProfileDataModel(name, email, imageUri)) // Update successful

                }
                .addOnFailureListener {
                    _profileUpdateStatus.postValue(ProfileDataModel())
                }
        }
    }

    fun profileFetchUsers() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = mutableListOf<ProfileDataModel>()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(ProfileDataModel::class.java)
                    if (user != null) {
                        userList.add(user)
                    }
                }
                _userList.postValue(userList)
            }

            override fun onCancelled(error: DatabaseError) {
                _userList.postValue(emptyList())
            }
        })
    }
}
