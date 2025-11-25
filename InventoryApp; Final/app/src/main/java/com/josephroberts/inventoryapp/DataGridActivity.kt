package com.josephroberts.inventoryapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class DataGridActivity : AppCompatActivity() {

    private lateinit var adapter: InventoryAdapter
    private lateinit var inventoryDb: InventoryDatabaseHelper

    private var fullInventory: List<Triple<String, Int, String>> = emptyList()
    private var currentQuery: String? = null
    private var currentCategory: String? = null
    private var currentMinQty: Int? = null
    private var userId: Int = 1 // Replace with actual session logic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_grid)

        val addButton = findViewById<Button>(R.id.add_data_button)
        val removeButton = findViewById<Button>(R.id.remove_item_button)
        val logoutButton = findViewById<Button>(R.id.logout_button)
        val exportButton = findViewById<Button>(R.id.export_button)
        val recyclerView = findViewById<RecyclerView>(R.id.inventory_recycler)
        val searchEditText = findViewById<EditText>(R.id.search_edit_text)
        val categorySpinner = findViewById<Spinner>(R.id.category_spinner)
        val qtySeekBar = findViewById<SeekBar>(R.id.qty_seekbar)

        inventoryDb = InventoryDatabaseHelper(this)

        addButton.setOnClickListener {
            startActivity(Intent(this, AddItemActivity::class.java))
        }

        removeButton.setOnClickListener {
            startActivity(Intent(this, RemoveItemActivity::class.java))
        }

        logoutButton.setOnClickListener {
            getSharedPreferences("user_session", MODE_PRIVATE).edit { clear() }
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }

        exportButton.setOnClickListener {
            exportInventoryToCSV()
        }

        // Load inventory with category names
        fullInventory = inventoryDb.getItemsWithCategoryNames(userId)
            .sortedBy { it.first.lowercase() }

        adapter = InventoryAdapter(fullInventory.toMutableList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Load categories from InventoryDatabaseHelper
        val categories = inventoryDb.getAllCategories().map { it.second }.sorted()
        val spinnerItems = listOf("All") + categories
        categorySpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerItems)

        fun applyFilters() {
            val filtered = fullInventory.filter { (name, quantity, categoryName) ->
                (currentQuery.isNullOrBlank() || name.contains(currentQuery!!, ignoreCase = true)) &&
                        (currentCategory == null || categoryName == currentCategory) &&
                        (currentMinQty == null || quantity >= currentMinQty!!)
            }
            adapter.updateList(filtered)
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currentQuery = s?.toString()
                applyFilters()
            }
        })

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                currentCategory = parent.getItemAtPosition(position)?.toString()?.takeIf { it != "All" }
                applyFilters()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                currentCategory = null
                applyFilters()
            }
        }

        qtySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                currentMinQty = if (progress > 0) progress else null
                applyFilters()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun exportInventoryToCSV() {
        val timestamp = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(Date())
        val filename = "inventory_$timestamp.csv"
        val file = File(getExternalFilesDir("exports"), filename)

        try {
            FileWriter(file).use { writer ->
                writer.append("Item Name,Quantity,Category\n")

                val items = adapter.getCurrentItems()
                for ((name, quantity, category) in items) {
                    val displayName = name.replaceFirstChar { it.uppercaseChar() }
                    val displayCategory = category.replaceFirstChar { it.uppercaseChar() }
                    writer.append("$displayName,$quantity,$displayCategory\n")
                }
            }

            val uri: Uri = FileProvider.getUriForFile(
                this,
                "${packageName}.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            startActivity(Intent.createChooser(shareIntent, "Share Inventory CSV"))

            val viewIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "text/csv")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            try {
                startActivity(viewIntent)
            } catch (_: ActivityNotFoundException) {
                Toast.makeText(this, "No app found to preview CSV", Toast.LENGTH_SHORT).show()
            }

        } catch (e: IOException) {
            Toast.makeText(this, "Export failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
