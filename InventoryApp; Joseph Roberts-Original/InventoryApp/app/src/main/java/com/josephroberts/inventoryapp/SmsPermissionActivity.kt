package com.josephroberts.inventoryapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.telephony.SmsManager

class SmsPermissionActivity : AppCompatActivity() {

    private lateinit var statusText: TextView

    // Modern permission launcher
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                statusText.text = getString(R.string.permission_granted)
                // Trigger SMS logic here
            } else {
                statusText.text = getString(R.string.permission_denied)
                // App continues without SMS
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms_permission)

        val requestButton = findViewById<Button>(R.id.request_sms_permission)
        statusText = findViewById(R.id.permission_status)

        requestButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
                statusText.text = getString(R.string.permission_already_granted)
                sendSmsNotification()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.SEND_SMS)
            }
        }
    }

    private fun sendSmsNotification() {
        val smsManager = SmsManager.getDefault()

        val phoneNumber = "1234567890"
        val message = "Low inventory alert: Restock needed soon!"

        try {
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            statusText.text = getString(R.string.sms_sent_success)
        } catch (e: Exception) {
            statusText.text = getString(R.string.sms_send_failed, e.message)
        }
    }
}