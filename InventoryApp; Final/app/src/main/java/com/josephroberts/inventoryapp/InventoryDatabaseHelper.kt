package com.josephroberts.inventoryapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class InventoryDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "InventoryDB", null, 6) {

    override fun onCreate(db: SQLiteDatabase) {
        // Create categories table first
        db.execSQL("""
            CREATE TABLE categories (
                category_id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL UNIQUE
            )
        """.trimIndent())

        val defaultCategories = listOf("Food", "Fruits", "Item", "Materials", "Misc")
        for (category in defaultCategories) {
            db.execSQL("INSERT INTO categories (name) VALUES (?)", arrayOf(category))
        }

        // Create inventory_items table (no foreign key to users)
        db.execSQL("""
            CREATE TABLE inventory_items (
                item_id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                category_id INTEGER,
                name TEXT NOT NULL,
                quantity INTEGER NOT NULL,
                threshold INTEGER DEFAULT 0,
                FOREIGN KEY (category_id) REFERENCES categories(category_id)
            )
        """.trimIndent())
    }

    override fun onConfigure(db: SQLiteDatabase) {
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS inventory_items")
        db.execSQL("DROP TABLE IF EXISTS categories")
        onCreate(db)
    }

    // ---------------- Inventory Operations ----------------

    fun insertOrUpdateItem(
        userId: Int,
        categoryId: Int?,
        name: String,
        quantity: Int,
        threshold: Int = 0
    ): Boolean {
        val db = writableDatabase
        val normalizedName = name.lowercase()

        val cursor = db.rawQuery(
            "SELECT quantity FROM inventory_items WHERE LOWER(name) = ? AND user_id = ?",
            arrayOf(normalizedName, userId.toString())
        )

        val success = if (cursor.moveToFirst()) {
            val existingQuantity = cursor.getInt(0)
            val newQuantity = existingQuantity + quantity
            val values = ContentValues().apply {
                put("quantity", newQuantity)
                put("threshold", threshold)
                put("category_id", categoryId)
            }
            db.update(
                "inventory_items",
                values,
                "LOWER(name) = ? AND user_id = ?",
                arrayOf(normalizedName, userId.toString())
            ) > 0
        } else {
            val values = ContentValues().apply {
                put("name", normalizedName) // store lowercase
                put("quantity", quantity)
                put("threshold", threshold)
                put("user_id", userId)
                put("category_id", categoryId)
            }
            db.insert("inventory_items", null, values) != -1L
        }

        cursor.close()
        return success
    }

    fun removeItem(userId: Int, name: String, quantity: Int): Boolean {
        val db = writableDatabase
        val normalizedName = name.lowercase()

        val cursor = db.rawQuery(
            "SELECT quantity FROM inventory_items WHERE LOWER(name) = ? AND user_id = ?",
            arrayOf(normalizedName, userId.toString())
        )

        val success = if (cursor.moveToFirst()) {
            val existingQuantity = cursor.getInt(0)
            if (existingQuantity >= quantity) {
                val newQuantity = existingQuantity - quantity
                if (newQuantity > 0) {
                    val values = ContentValues().apply { put("quantity", newQuantity) }
                    db.update(
                        "inventory_items",
                        values,
                        "LOWER(name) = ? AND user_id = ?",
                        arrayOf(normalizedName, userId.toString())
                    ) > 0
                } else {
                    db.delete(
                        "inventory_items",
                        "LOWER(name) = ? AND user_id = ?",
                        arrayOf(normalizedName, userId.toString())
                    ) > 0
                }
            } else {
                false
            }
        } else {
            false
        }

        cursor.close()
        return success
    }

    fun getAllItems(userId: Int): List<Triple<String, Int, Int?>> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT name, quantity, category_id FROM inventory_items WHERE user_id = ?",
            arrayOf(userId.toString())
        )

        val items = mutableListOf<Triple<String, Int, Int?>>()
        while (cursor.moveToNext()) {
            val name = cursor.getString(0)
            val quantity = cursor.getInt(1)
            val categoryId = if (!cursor.isNull(2)) cursor.getInt(2) else null
            items.add(Triple(name, quantity, categoryId))
        }

        cursor.close()
        return items
    }

    fun getItemsWithCategoryNames(userId: Int): List<Triple<String, Int, String>> {
        val db = readableDatabase
        val cursor = db.rawQuery("""
            SELECT i.name, i.quantity, c.name
            FROM inventory_items i
            LEFT JOIN categories c ON i.category_id = c.category_id
            WHERE i.user_id = ?
        """.trimIndent(), arrayOf(userId.toString()))

        val items = mutableListOf<Triple<String, Int, String>>()
        while (cursor.moveToNext()) {
            val name = cursor.getString(0)
            val quantity = cursor.getInt(1)
            val categoryName = cursor.getString(2) ?: "Uncategorized"
            items.add(Triple(name, quantity, categoryName))
        }

        cursor.close()
        return items
    }

    // ---------------- Category Operations ----------------

    fun insertCategory(name: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", name.trim().lowercase()) // normalize category names too
        }
        return db.insert("categories", null, values)
    }

    fun getCategoryId(name: String): Int? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT category_id FROM categories WHERE LOWER(name) = ?",
            arrayOf(name.trim().lowercase())
        )
        val id = if (cursor.moveToFirst()) cursor.getInt(0) else null
        cursor.close()
        return id
    }

    fun getCategoryName(id: Int): String? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT name FROM categories WHERE category_id = ?", arrayOf(id.toString()))
        val name = if (cursor.moveToFirst()) cursor.getString(0) else null
        cursor.close()
        return name
    }

    fun getAllCategories(): List<Pair<Int, String>> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT category_id, name FROM categories", null)
        val categories = mutableListOf<Pair<Int, String>>()
        while (cursor.moveToNext()) {
            categories.add(Pair(cursor.getInt(0), cursor.getString(1)))
        }
        cursor.close()
        return categories
    }
}
