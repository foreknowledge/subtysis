package com.hackday.player.adapter

import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.hackday.databinding.ItemShoppingBinding
import com.hackday.subtysis.model.Keyword
import com.hackday.subtysis.model.SearchType
import com.hackday.subtysis.model.items.ShoppingItem

@SuppressWarnings("unchecked")
@BindingAdapter("bind_metadata")
fun bindMetadata(recyclerView: RecyclerView, keywords: ArrayList<Keyword>?) {
    val adapter =
        recyclerView.adapter as MetadataRecyclerViewAdapter<ItemShoppingBinding, ShoppingItem>
    keywords?.let {
        val displayData = arrayListOf<ShoppingItem>()
        for (keyword in keywords) {
            val shoppingData = keyword.responses!![SearchType.SHOPPING]?.items

            shoppingData?.let {
                for (data in shoppingData) displayData.add(data as ShoppingItem)
            }
        }

        adapter.setItems(displayData)
        adapter.notifyDataSetChanged()
    }
}

@BindingAdapter("set_visibility")
fun setVisibility(linearLayout: LinearLayout, visibility: Boolean) {
    val bottomSheetBehavior = BottomSheetBehavior.from(linearLayout)
    bottomSheetBehavior.state =
        if (visibility) BottomSheetBehavior.STATE_EXPANDED else BottomSheetBehavior.STATE_COLLAPSED
}