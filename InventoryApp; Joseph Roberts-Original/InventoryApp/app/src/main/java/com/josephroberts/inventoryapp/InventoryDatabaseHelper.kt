package com.josephroberts.inventoryapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class InventoryDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "InventoryDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE inventory (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                quantity INTEGER
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS inventory")
        onCreate(db)
    }

    fun insertOrUpdateItem(name: String, quantity: Int): Boolean {
        val db = writableDatabase
        val cursor = db.rawQuery("SELECT quantity FROM inventory WHERE name = ?", arrayOf(name))

        return if (cursor.moveToFirst()) {
            val existingQuantity = cursor.getInt(0)
            val newQuantity = existingQuantity + quantity
            val values = ContentValues().apply {
                put("quantity", newQuantity)
            }
            val result = db.update("inventory", values, "name = ?", arrayOf(name))
            cursor.close()
            result > 0
        } else {
            cursor.close()
            val values = ContentValues().apply {
                put("name", name)
                put("quantity", quantity)
            }
            db.insert("inventory", null, values) != -1L
        }
    }


    fun removeItem(name: String, quantity: Int): Boolean {
        val db = writableDatabase
        val cursor = db.rawQuery("SELECT quantity FROM inventory WHERE name = ?", arrayOf(name))

        return if (cursor.moveToFirst()) {
            val existingQuantity = cursor.getInt(0)
            if (existingQuantity >= quantity) {
                val newQuantity = existingQuantity - quantity
                if (newQuantity > 0) {
                    val values = ContentValues().apply {
                        put("quantity", newQuantity)
                    }
                    val result = db.update("inventory", values, "name = ?", arrayOf(name))
                    cursor.close()
                    result > 0
                } else {
                    val result = db.delete("inventory", "name = ?", arrayOf(name))
                    cursor.close()
                    result > 0
                }
            } else {
                cursor.close()
                false // Not enough quantity to remove
            }
        } else {
            cursor.close()
            false // Item not found
        }
    }


    fun getAllItems(): List<Pair<String, Int>> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT name, quantity FROM inventory", null)
        val items = mutableListOf<Pair<String, Int>>()

        while (cursor.moveToNext()) {
            val name = cursor.getString(0)
            val quantity = cursor.getInt(1)
            items.add(name to quantity)
        }

        cursor.close()
        return items
    }
}
