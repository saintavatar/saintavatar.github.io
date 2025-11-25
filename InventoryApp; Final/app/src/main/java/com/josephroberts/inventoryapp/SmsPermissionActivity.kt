package com.josephroberts.inventoryapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class SmsPermissionActivity : AppCompatActivity() {

    private lateinit var statusText: TextView

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                updateStatus(R.string.permission_granted)
                checkInventoryAndSendSms()
            } else {
                updateStatus(R.string.permission_denied)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms_permission)

        Log.d("SMS", "SmsPermissionActivity launched")

        statusText = findViewById(R.id.permission_status)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
            == PackageManager.PERMISSION_GRANTED) {
            updateStatus(R.string.permission_already_granted)
            checkInventoryAndSendSms()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.SEND_SMS)
        }
    }

    private fun checkInventoryAndSendSms() {
        val userId = getSharedPreferences("user_session", MODE_PRIVATE).getInt("user_id", 1)
        val username = getSharedPreferences("user_session", MODE_PRIVATE).getString("username", null)
        val dbHelper = InventoryDatabaseHelper(this)
        val userDb = UserDatabaseHelper(this)

        val lowStockItems = dbHelper.getAllItems(userId).filter { it.second < 5 }

        Log.d("SMS", "Checking inventory...")
        Log.d("SMS", "Low stock items count: ${lowStockItems.size}")

        if (lowStockItems.isNotEmpty()) {
            val itemDetails = lowStockItems.joinToString("\n") { "${it.first}: ${it.second}" }
            val message = "Low inventory alert:\n$itemDetails"
            Log.d("SMS", "Formatted message:\n$message")

            val phoneNumber = username?.let { userDb.getPhoneNumberForUser(it) }
            sendSmsNotification(phoneNumber, message)
        } else {
            updateStatus(R.string.inventory_ok)
        }
    }

    private fun sendSmsNotification(phoneNumber: String?, message: String) {
        if (phoneNumber.isNullOrEmpty()) {
            updateStatus(R.string.sms_send_failed, "No phone number found")
            return
        }

        try {
            val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                getSystemService(SmsManager::class.java)
            } else {
                SmsManager.getDefault()
            }

            Log.d("SMS", "Sending SMS to $phoneNumber")
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            updateStatus(R.string.sms_sent_success)
        } catch (e: Exception) {
            Log.e("SMS", "SMS failed", e)
            updateStatus(R.string.sms_send_failed, e.message)
        }
    }

    private fun updateStatus(resId: Int, vararg formatArgs: Any?) {
        statusText.text = getString(resId, *formatArgs)
    }
}
