package com.josephroberts.inventoryapp

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import android.widget.Toast
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit

class LoginActivity : AppCompatActivity() {

    private lateinit var loginButton: Button
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText

    private lateinit var dbHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Bind views
        loginButton = findViewById(R.id.loginButton)
        usernameField = findViewById(R.id.usernameField)
        passwordField = findViewById(R.id.passwordField)
        val registerButton = findViewById<Button>(R.id.registerButton)

        // Initialize DB
        dbHelper = UserDatabaseHelper(this)
        dbHelper.insertTestUser() // Optional: insert a known test user

        // Set listener
        loginButton.setOnClickListener {
            val username = usernameField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            } else if (dbHelper.validateUser(username, password)) {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()

                getSharedPreferences("user_session", MODE_PRIVATE).edit {
                    putString("username", username)
                }


                Log.d("Login", "Stored username: $username")

                val intent = Intent(this, DataGridActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}

