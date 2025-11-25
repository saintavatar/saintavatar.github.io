package com.josephroberts.inventoryapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InventoryAdapter(private val items: List<InventoryItem>) :
    RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder>() {

    class InventoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.item_name)
        val quantityText: TextView = itemView.findViewById(R.id.item_quantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_inventory, parent, false)
        return InventoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        val item = items[position]
        holder.nameText.text = item.name
        holder.quantityText.text = item.quantity.toString()
    }

    override fun getItemCount(): Int = items.size
}
