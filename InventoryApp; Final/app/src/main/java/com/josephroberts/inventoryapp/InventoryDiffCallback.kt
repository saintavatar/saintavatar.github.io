package com.josephroberts.inventoryapp

import androidx.recyclerview.widget.DiffUtil

class InventoryDiffCallback(
    private val oldList: List<Triple<String, Int, String>>,
    private val newList: List<Triple<String, Int, String>>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].first == newList[newItemPosition].first // Compare by name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}


