package com.example.mvvmauth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmauth.AllViewModel.UserViewModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var signUpNameEditText: EditText
    private lateinit var signUpEmailEditText: EditText
    private lateinit var signUpPasswordEditText: EditText
    private lateinit var signUpButton: Button
    private lateinit var loginTextView: TextView
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signUpNameEditText = findViewById(R.id.nameSignUp_EditText)
        signUpEmailEditText = findViewById(R.id.emailSignUp_EditText)
        signUpPasswordEditText = findViewById(R.id.passwordSignUp_EditText)
        signUpButton = findViewById(R.id.register_Button)
        loginTextView = findViewById(R.id.login_TextView)

        viewModel = ViewModelProvider(this)[UserViewModel::class.java]

        signUpButton.setOnClickListener {
            val name = signUpNameEditText.text.toString()
            val email = signUpEmailEditText.text.toString()
            val password = signUpPasswordEditText.text.toString()

            if (validateInput(name,email, password)) {
                viewModel.createSignUp(name, email, password, image = "")
                Toast.makeText(this, "register successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }

        // Observe the LiveData for success
        viewModel.userLiveData.observe(this, Observer { user ->
            if (user!=null){
                Toast.makeText(this, user.name, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }
        })


        loginTextView.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
    private fun validateInput(name: String, email: String, password: String): Boolean {
        if (name.isEmpty()) {
            showToast("Name cannot be empty")
            return false
        }
        if (email.isEmpty()) {
            showToast("Email cannot be empty")
            return false
        }
        if (!isValidEmail(email)) {
            showToast("Please enter a valid email")
            return false
        }
        if (password.isEmpty()) {
            showToast("Password cannot be empty")
            return false
        }
        if (password.length < 8) {
            showToast("Password must be at least 8 characters long")
            return false
        }
        return true
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
