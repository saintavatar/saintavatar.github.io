package com.josephroberts.inventoryapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class InventoryAdapter(
    private var items: MutableList<Triple<String, Int, String>>
) : RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder>() {

    class InventoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.item_name)
        val quantityText: TextView = itemView.findViewById(R.id.item_quantity)
        val categoryText: TextView = itemView.findViewById(R.id.item_category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_inventory, parent, false)
        return InventoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        val (name, quantity, categoryName) = items[position]
        val context = holder.itemView.context

        val displayName = name.replaceFirstChar { it.uppercaseChar() }
        val displayCategory = categoryName.replaceFirstChar { it.uppercaseChar() }

        holder.nameText.text = displayName
        holder.quantityText.text = context.getString(R.string.quantity_label, quantity)
        holder.categoryText.text = context.getString(R.string.category_label, displayCategory)
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newItems: List<Triple<String, Int, String>>) {
        val diffCallback = InventoryDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items = newItems.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }

    fun getCurrentItems(): List<Triple<String, Int, String>> = items.toList()
}
