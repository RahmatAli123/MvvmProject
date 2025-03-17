package com.example.mvvmauth.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mvvmauth.EditProfileActivity
import com.example.mvvmauth.AllViewModel.ProfileViewModel
import com.example.mvvmauth.LoginActivity
import com.example.mvvmauth.R
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {
   private lateinit var tvUserName: TextView
   private lateinit var addTextView: TextView
   private lateinit var orderTextView: TextView
   private lateinit var logoutTextView: TextView
    private lateinit var  profileViewModel: ProfileViewModel
    private lateinit var profileName: TextView
   private lateinit var profileEmail: TextView
    private lateinit var profileImage: ImageView
    private var imageUrl: String? = null

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
        profileViewModel= ViewModelProvider(this)[ProfileViewModel::class.java]

        // Fetch users
        profileViewModel.profileFetchUsers()

        // Observe LiveData
        profileViewModel.userList.observe(viewLifecycleOwner) { userList ->
            if (userList!=null) {
                val user = userList[0]
                profileName.text = user.name
                profileEmail.text = user.email
                imageUrl = user.image
                if (imageUrl!=null) {
                    Glide.with(requireContext()).load(imageUrl).into(profileImage)
                }
                }

        }

        tvUserName.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            intent.putExtra("name", profileName.text.toString())
            intent.putExtra("email", profileEmail.text.toString())
            intent.putExtra("image",imageUrl)

            startActivity(intent)
        }

        logoutTextView.setOnClickListener {
        logOut()

        }

        return view
    }

    private fun logOut(){
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle("Logout")
        dialog.setMessage("Are you sure you want to logout?")

        dialog.setPositiveButton("Yes") { dialog, _ ->
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialog.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        dialog.create().show()
    }
}