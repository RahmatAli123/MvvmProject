package com.example.mvvmauth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mvvmauth.AllViewModel.ProfileViewModel

class EditProfileActivity : AppCompatActivity() {
    private lateinit var profileImageView: ImageView
    private lateinit var editNameEditText: EditText
    private lateinit var editEmailEditText: EditText
    private lateinit var editImageView: ImageView
    private lateinit var updateButton: androidx.appcompat.widget.AppCompatButton
    private lateinit var profileViewModel: ProfileViewModel
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        profileImageView = findViewById(R.id.ProfileImage_ImageView)
        editNameEditText = findViewById(R.id.profileName_EditText)
        editEmailEditText = findViewById(R.id.profileEmail_EditText)
        editImageView = findViewById(R.id.editImage_ImageView)
        updateButton = findViewById(R.id.update_Button)
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")
        val image = intent.getStringExtra("image")

        editNameEditText.setText(name)
        editEmailEditText.setText(email)
        if (image != null) {
            Glide.with(this).load(image).into(profileImageView)
        }

        editImageView.setOnClickListener {
            if (checkPermissions()) {
                showImageSelectionOptions()
            }
        }


        updateButton.setOnClickListener {
            val name = editNameEditText.text.toString()
            val email = editEmailEditText.text.toString()
            if (name.isNotEmpty() && email.isNotEmpty()) {
                profileViewModel.updateProfile(name, email, imageUri.toString())
                Toast.makeText(this, "Update Profile", Toast.LENGTH_SHORT).show()
            }


        }
        profileViewModel.updateStatus.observe(this, Observer {
            if (it != null) {
                Toast.makeText(this, "Update Profile", Toast.LENGTH_SHORT).show()
                finish()
            }
        })


        }

    private fun checkPermissions(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        )
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                100
            )
        return true


    }


    @SuppressLint("IntentReset")
    private fun showImageSelectionOptions() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, 100)
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data?.data != null) {
            imageUri = data.data
            profileImageView.setImageURI(imageUri)
        }

    }
}