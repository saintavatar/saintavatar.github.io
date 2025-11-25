package com.josephroberts.inventoryapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.util.Log

class UserDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "UserDB", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE COLLATE NOCASE,
                password TEXT,
                phone TEXT
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    fun insertTestUser() {
        if (!userExists("tester")) {
            val success = insertUser("tester", "testcase", "2564548834")
            Log.d("UserDB", if (success) "Inserted test user" else "Failed to insert test user")
        } else {
            Log.d("UserDB", "Test user already exists")
        }
    }

    fun validateUser(username: String, password: String): Boolean {
        val normalizedUsername = username.lowercase().trim()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM users WHERE username=? AND password=?",
            arrayOf(normalizedUsername, password)
        )
        val isValid = cursor.count > 0
        cursor.close()
        Log.d("UserDB", "Validation result for $normalizedUsername: $isValid")
        return isValid
    }

    fun getPhoneNumberForUser(username: String): String? {
        val normalizedUsername = username.lowercase().trim()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT phone FROM users WHERE username = ?", arrayOf(normalizedUsername))
        val phone = if (cursor.moveToFirst()) cursor.getString(0) else null
        cursor.close()
        return phone
    }

    fun insertUser(username: String, password: String, phone: String): Boolean {
        val normalizedUsername = username.lowercase().trim()

        val dbWrite = writableDatabase
        val values = ContentValues().apply {
            put("username", normalizedUsername)
            put("password", password)
            put("phone", phone)
        }

        val result = dbWrite.insert("users", null, values)
        return result != -1L
    }

    fun userExists(username: String): Boolean {
        val normalizedUsername = username.lowercase().trim()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE username=?", arrayOf(normalizedUsername))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
}
