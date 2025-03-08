package com.example.mvvmauth.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mvvmauth.EditProfileActivity
import com.example.mvvmauth.AllViewModel.ProfileViewModel
import com.example.mvvmauth.R


class ProfileFragment : Fragment() {
   private lateinit var tvUserName: TextView
   private lateinit var addTextView: TextView
   private lateinit var orderTextView: TextView
   private lateinit var logoutTextView: TextView
    private lateinit var  profileViewModel: ProfileViewModel
    private lateinit var profileName: TextView
   private lateinit var profileEmail: TextView
    private lateinit var profileImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_profile, container, false)
        tvUserName= view.findViewById(R.id.tvUserName)
        addTextView= view.findViewById(R.id.add_TextView)
        orderTextView= view.findViewById(R.id.order_TextView)
        logoutTextView= view.findViewById(R.id.logout_TextView)
        profileName= view.findViewById(R.id.userName_TextView)
        profileEmail= view.findViewById(R.id.userEmail_TextView)
        profileImage= view.findViewById(R.id.userImage_ImageView)
        profileViewModel= ViewModelProvider(this).get(ProfileViewModel::class.java)

        // Fetch users
        profileViewModel.profileFetchUsers()

        // Observe LiveData
        profileViewModel.userList.observe(viewLifecycleOwner) { userList ->
            if (userList.isNotEmpty()) {
                val user = userList[0]
                profileName.text = user.name
                profileEmail.text = user.email
                Glide.with(requireContext()).load(user.image).into(profileImage)
            }
        }

        tvUserName.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            intent.putExtra("name", profileName.text.toString())
            intent.putExtra("email", profileEmail.text.toString())
            intent.putExtra("image", profileImage.tag?.toString()) // सही तरीका
            startActivity(intent)
        }



        return view
    }


}