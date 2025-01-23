package com.example.macroapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class MacroListAdapter(
    private val onActionClick: (MacroEntity, String) -> Unit
) : ListAdapter<MacroEntity, MacroListAdapter.MacroViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MacroViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_macro, parent, false)
        return MacroViewHolder(view, onActionClick)
    }

    override fun onBindViewHolder(holder: MacroViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MacroViewHolder(
        itemView: View,
        private val onActionClick: (MacroEntity, String) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.macroName)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
        private val scheduleButton: ImageButton = itemView.findViewById(R.id.scheduleButton)

        fun bind(macro: MacroEntity) {
            nameTextView.text = macro.name
            deleteButton.setOnClickListener { onActionClick(macro, "delete") }
            scheduleButton.setOnClickListener { onActionClick(macro, "schedule") }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MacroEntity>() {
        override fun areItemsTheSame(oldItem: MacroEntity, newItem: MacroEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MacroEntity, newItem: MacroEntity): Boolean {
            return oldItem == newItem
        }
    }
}
