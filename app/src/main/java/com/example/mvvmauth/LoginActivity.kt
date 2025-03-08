package com.example.mvvmauth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmauth.AllViewModel.UserViewModel

class LoginActivity : AppCompatActivity() {
        private lateinit var loginEmailEditText: EditText
        private lateinit var loginPasswordEditText: EditText
        private lateinit var loginButton: Button
        private lateinit var signUpTextView: TextView
        private lateinit var viewModel: UserViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginEmailEditText = findViewById(R.id.loginEmail_EditText)
        loginPasswordEditText = findViewById(R.id.loginPassword_EditText)
        loginButton = findViewById(R.id.login_Button)
        signUpTextView=findViewById(R.id.signUp_TextView)
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]

        loginButton.setOnClickListener {
            val email = loginEmailEditText.text.toString()
            val password = loginPasswordEditText.text.toString()
            if (!validateInput(email, password)) {
                return@setOnClickListener
            }
            viewModel.login(email, password)
            viewModel.userLiveData.observe(this) { user ->
                if (user != null) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    val intent=Intent(this,MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)

                }
            }

        }

        signUpTextView.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))

        }


    }
    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            showToast("Email cannot be empty")
            return false
        }
        if (!isValidEmail(email)) {
            showToast("Please enter a valid email address")
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
        val emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        return email.matches(emailPattern.toRegex())
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}