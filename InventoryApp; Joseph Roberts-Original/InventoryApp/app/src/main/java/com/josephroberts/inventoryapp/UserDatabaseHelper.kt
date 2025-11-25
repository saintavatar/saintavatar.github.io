package com.josephroberts.inventoryapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.util.Log

class UserDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "UserDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE,
                password TEXT
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    fun insertTestUser() {
        if (!userExists("tester")) {
            insertUser("tester", "testcase")
            Log.d("UserDB", "Inserted test user")
        } else {
            Log.d("UserDB", "Test user already exists")
        }
    }

    fun validateUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM users WHERE username=? AND password=?",
            arrayOf(username, password)
        )
        val isValid = cursor.count > 0
        Log.d("UserDB", "Validation result for $username: $isValid")
        cursor.close()
        return isValid
    }

    fun insertUser(username: String, password: String): Boolean {
        if (userExists(username)) return false

        val db = writableDatabase
        val values = ContentValues().apply {
            put("username", username)
            put("password", password)
        }

        val result = db.insert("users", null, values)
        return result != -1L
    }

     fun userExists(username: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE username=?", arrayOf(username))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
}
