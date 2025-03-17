package com.example.mvvmauth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class EditProfileActivity : AppCompatActivity() {
    private lateinit var profileImageView: ImageView
    private lateinit var editNameEditText: EditText
    private lateinit var editEmailEditText: EditText
    private lateinit var editImageView: ImageView
    private lateinit var updateButton: androidx.appcompat.widget.AppCompatButton
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var mProgressDialog: ProgressDialog
    private var imageUri: Uri? = null
    private var imageUpdated: Boolean = false

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
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
                    112
                )
            } else {
                showImageSelectionOptions()
            }
        }

        updateButton.setOnClickListener {
            val updatedName = editNameEditText.text.toString()
            val updatedEmail = editEmailEditText.text.toString()

            if (updatedName.isEmpty() || updatedEmail.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            mProgressDialog = ProgressDialog(this)
            mProgressDialog.setMessage("Updating profile...")
            mProgressDialog.show()

            if (imageUpdated) {
                uploadImage(updatedName, updatedEmail)
            } else {
                profileViewModel.updateProfile(updatedName, updatedEmail, image ?: "")
            }
        }

        // Observe update status
        profileViewModel.updateStatus.observe(this, Observer { success ->
            mProgressDialog.dismiss()
            if (success != null) {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Profile update failed", Toast.LENGTH_SHORT).show()
            }

        })
    }

    @SuppressLint("IntentReset")
    private fun showImageSelectionOptions() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "image/*"
        val chooserIntent = Intent.createChooser(galleryIntent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
        startActivityForResult(chooserIntent, 100)
    }

    @Deprecated("This method is deprecated, but using for demonstration")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            if (data?.data != null) {
                imageUri = data.data
                profileImageView.setImageURI(imageUri)
                imageUpdated = true
            } else if (data?.extras?.get("data") != null) {
                val bitmap = data.extras?.get("data") as Bitmap
                profileImageView.setImageBitmap(bitmap)
                imageUri = getImageUri(bitmap)
                imageUpdated = true
            }
        }
    }

    private fun getImageUri(inImage: Bitmap): Uri? {
        return try {
            val fileName = "IMG_${System.currentTimeMillis()}.jpg"
            val file = File(getExternalFilesDir(null), fileName)

            val outStream = FileOutputStream(file)
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            outStream.flush()
            outStream.close()
            Uri.fromFile(file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 112 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showImageSelectionOptions()
        } else {
            Toast.makeText(this, "Permissions required for this action", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImage(name: String, email: String) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")
        if (imageUri != null) {
            imageRef.putFile(imageUri!!).addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener {
                    val image = it.toString()
                    profileViewModel.updateProfile(name, email, image)
                    Toast.makeText(this, "Image Upload successfully", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
