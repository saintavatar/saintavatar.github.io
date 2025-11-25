package com.josephroberts.inventoryapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AddItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        val nameInput = findViewById<EditText>(R.id.input_item_name)
        val quantityInput = findViewById<EditText>(R.id.input_item_quantity)
        val saveButton = findViewById<Button>(R.id.save_item_button)
        val categorySpinner = findViewById<Spinner>(R.id.category_spinner)

        val dbHelper = InventoryDatabaseHelper(this)
        val categories = dbHelper.getAllCategories()
        val categoryNames = categories.map { it.second }

        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categoryNames)
        categorySpinner.adapter = spinnerAdapter

        saveButton.setOnClickListener {
            val rawName = nameInput.text.toString().trim()
            val name = rawName.lowercase() // Normalize for storage and matching
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

            val selectedCategoryName = categorySpinner.selectedItem?.toString()
            val categoryId = selectedCategoryName?.let { dbHelper.getCategoryId(it) }

            val userId = getSharedPreferences("user_session", MODE_PRIVATE).getInt("user_id", 1)
            val threshold = 0

            val success = dbHelper.insertOrUpdateItem(userId, categoryId, name, quantity, threshold)

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
