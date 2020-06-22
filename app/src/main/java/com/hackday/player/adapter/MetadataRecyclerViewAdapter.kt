package com.hackday.player.adapter

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class MetadataRecyclerViewAdapter<B : ViewDataBinding, T : Any>(
    @LayoutRes private val layoutResId: Int,
    private val bindingVariableId: Int
) : RecyclerView.Adapter<MetadataViewHolder<B, T>>() {
    private val items = mutableListOf<T>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        object : MetadataViewHolder<B, T>(layoutResId, parent, bindingVariableId) {}

    override fun getItemCount(): Int = items.size

    fun getItem(position: Int) = items[position]

    fun setItems(data: List<T>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MetadataViewHolder<B, T>, position: Int) {
        holder.bind(getItem(position))
    }
}