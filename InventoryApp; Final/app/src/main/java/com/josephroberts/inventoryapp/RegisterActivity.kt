package com.josephroberts.inventoryapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var confirmField: EditText
    private lateinit var phoneField: EditText
    private lateinit var registerButton: Button
    private lateinit var dbHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Bind views using correct XML IDs
        usernameField = findViewById(R.id.newUsername)
        passwordField = findViewById(R.id.newPassword)
        confirmField = findViewById(R.id.confirmPassword)
        phoneField = findViewById(R.id.phoneNumber)
        registerButton = findViewById(R.id.registerButton)

        dbHelper = UserDatabaseHelper(this)

        registerButton.setOnClickListener {
            val username = usernameField.text.toString().trim()
            val password = passwordField.text.toString().trim()
            val confirm = confirmField.text.toString().trim()
            val phone = phoneField.text.toString().trim()

            if (username.isEmpty() || password.isEmpty() || confirm.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else if (password != confirm) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else if (dbHelper.insertUser(username, password, phone)) {
                Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show()
                finish() // Return to login screen
            } else {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
