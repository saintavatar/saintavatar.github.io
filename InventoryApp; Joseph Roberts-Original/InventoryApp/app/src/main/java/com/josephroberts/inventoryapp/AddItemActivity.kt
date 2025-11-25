package com.josephroberts.inventoryapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class AddItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        val nameInput = findViewById<EditText>(R.id.input_item_name)
        val quantityInput = findViewById<EditText>(R.id.input_item_quantity)
        val saveButton = findViewById<Button>(R.id.save_item_button)

        saveButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
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

            // ✅ Use InventoryDatabaseHelper to insert item
            val dbHelper = InventoryDatabaseHelper(this)
            val success = dbHelper.insertOrUpdateItem(name, quantity)


            if (success) {
                Toast.makeText(this, "Item saved!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, DataGridActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Failed to save item", Toast.LENGTH_SHORT).show()
            }
        }


        }
    }

