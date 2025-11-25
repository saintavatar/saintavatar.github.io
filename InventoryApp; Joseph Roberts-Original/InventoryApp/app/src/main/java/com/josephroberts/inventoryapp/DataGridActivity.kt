package com.josephroberts.inventoryapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class DataGridActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_grid)

        // TODO: Set up RecyclerView adapter, load inventory data, handle add/remove buttons
        val addButton = findViewById<Button>(R.id.add_data_button)
        addButton.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }
        val removeButton = findViewById<Button>(R.id.remove_item_button)
        removeButton.setOnClickListener {
            val intent = Intent(this, RemoveItemActivity::class.java)
            startActivity(intent)
        }
        val recyclerView = findViewById<RecyclerView>(R.id.inventory_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dbHelper = InventoryDatabaseHelper(this)
        val rawItems = dbHelper.getAllItems()
        val items = rawItems.map { InventoryItem(it.first, it.second) }

        recyclerView.adapter = InventoryAdapter(items)
    }


}
