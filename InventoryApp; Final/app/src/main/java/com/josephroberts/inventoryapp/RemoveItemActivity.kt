package com.josephroberts.inventoryapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RemoveItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_item)

        val nameInput = findViewById<EditText>(R.id.input_item_name)
        val quantityInput = findViewById<EditText>(R.id.input_item_quantity)
        val deleteButton = findViewById<Button>(R.id.delete_button)

        deleteButton.setOnClickListener {
            val rawName = nameInput.text.toString().trim()
            val name = rawName.lowercase() // Normalize for matching

            val quantityText = quantityInput.text.toString().trim()

            if (name.isEmpty() || quantityText.isEmpty()) {
                Toast.makeText(this, "Please enter both name and quantity", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val quantity = quantityText.toIntOrNull()
            if (quantity == null || quantity <= 0) {
                Toast.makeText(this, "Quantity must be a positive number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = getSharedPreferences("user_session", MODE_PRIVATE).getInt("user_id", 1)
            val dbHelper = InventoryDatabaseHelper(this)
            val success = dbHelper.removeItem(userId, name, quantity)

            if (success) {
                Toast.makeText(this, "Item deleted!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, DataGridActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Item not found or failed to delete", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

