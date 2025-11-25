package com.josephroberts.inventoryapp

data class InventoryItem(
    val name: String,
    val quantity: Int,
    val categoryId: Int?, // nullable if uncategorized
    val userId: Int,
    val threshold: Int = 0
)

