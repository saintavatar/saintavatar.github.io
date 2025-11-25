package com.josephroberts.inventoryapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val smsPermissionCode = 101
    private var smsPermissionGranted = false

    private val inventory = mutableMapOf(
        "Apples" to 10,
        "Bananas" to 5,
        "Oranges" to 0
    )

    private val addItemLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val itemName = result.data?.getStringExtra("itemName")?.trim() ?: return@registerForActivityResult
            handleAddItem(itemName)
        }
    }

    private val removeItemLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val itemName = result.data?.getStringExtra("itemName")?.trim() ?: return@registerForActivityResult
            handleRemoveItem(itemName)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_grid)

        requestSmsPermission()

        val addButton = findViewById<Button>(R.id.add_data_button)
        val removeButton = findViewById<Button>(R.id.remove_item_button)

        addButton.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            addItemLauncher.launch(intent)
        }

        removeButton.setOnClickListener {
            val intent = Intent(this, RemoveItemActivity::class.java)
            removeItemLauncher.launch(intent)
        }
    }

    private fun handleAddItem(itemName: String) {
        val currentQty = inventory[itemName] ?: 0
        inventory[itemName] = currentQty + 1
        Toast.makeText(this, "$itemName added. Qty: ${inventory[itemName]}", Toast.LENGTH_SHORT).show()

        if (smsPermissionGranted) {
            sendSmsNotification("$itemName added. Qty: ${inventory[itemName]}")
        }
    }

    private fun handleRemoveItem(itemName: String) {
        if (inventory.containsKey(itemName)) {
            val currentQty = inventory[itemName] ?: 0
            if (currentQty > 1) {
                inventory[itemName] = currentQty - 1
                Toast.makeText(this, "$itemName removed. Qty: ${inventory[itemName]}", Toast.LENGTH_SHORT).show()
            } else {
                inventory.remove(itemName)
                Toast.makeText(this, "$itemName removed from inventory.", Toast.LENGTH_SHORT).show()
            }

            if (smsPermissionGranted) {
                sendSmsNotification("$itemName removed. Remaining: ${inventory[itemName] ?: 0}")
            }
        } else {
            Toast.makeText(this, "Item not found.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestSmsPermission() {
        val permissionCheck = ContextCompat.checkSelfPermission(
            this, Manifest.permission.SEND_SMS
        )

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.SEND_SMS),
                smsPermissionCode
            )
        } else {
            smsPermissionGranted = true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == smsPermissionCode) {
            smsPermissionGranted = grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED

            val message = if (smsPermissionGranted) {
                "SMS permission granted"
            } else {
                "SMS permission denied — alerts will be disabled"
            }

            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun sendSmsNotification(message: String) {
        try {
            val smsManager = if (Build.VERSION.SDK_INT >= 24) {
                val subId = android.telephony.SubscriptionManager.getDefaultSmsSubscriptionId()
                SmsManager.getSmsManagerForSubscriptionId(subId)
            } else {
                SmsManager.getDefault()
            }
            smsManager.sendTextMessage("1234567890", null, message, null, null)
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to send SMS: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
