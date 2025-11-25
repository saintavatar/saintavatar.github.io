package com.josephroberts.inventoryapp

object InventoryUtils {

    // Binary search on a sorted list by name
    fun searchItemByName(sortedInventory: List<InventoryItem>, target: String): InventoryItem? {
        val index = sortedInventory.binarySearchBy(target.lowercase()) { it.name.lowercase() }
        return if (index >= 0) sortedInventory[index] else null
    }

    // General filters (extend as needed)
    fun filterInventory(
        inventory: List<InventoryItem>,
        categoryId: Int? = null,
        minQty: Int? = null
    ): List<InventoryItem> {
        return inventory.filter {
            (categoryId == null || it.categoryId == categoryId) &&
                    (minQty == null || it.quantity >= minQty)
        }
    }


    // Combine search and filter
    fun searchAndFilter(
        inventory: List<InventoryItem>,
        nameQuery: String? = null,
        categoryId: Int? = null,
        minQty: Int? = null
    ): List<InventoryItem> {
        val filtered = filterInventory(inventory, categoryId, minQty)
        if (nameQuery.isNullOrBlank()) return filtered.sortedBy { it.name.lowercase() }

        val sorted = filtered.sortedBy { it.name.lowercase() }
        val match = searchItemByName(sorted, nameQuery)
        return if (match != null) listOf(match) else emptyList()
    }

}
