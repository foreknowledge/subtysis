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
    val displayData = arrayListOf<ShoppingItem>()
    for (data in 0..10) displayData.add(
        ShoppingItem(
            "", 1000, 1000, "안녕", 10000, 10000
        )
    )

    keywords?.let {
        for (keyword in keywords) {
            val shoppingData = keyword.responses!![SearchType.SHOPPING]?.items

            shoppingData?.let {
                for (data in shoppingData) displayData.add(data as ShoppingItem)
            }
        }
    }

    adapter.setItems(displayData)
    adapter.notifyDataSetChanged()
}

@BindingAdapter("set_visibility")
fun setVisibility(linearLayout: LinearLayout, visibility: Boolean) {
    val bottomSheetBehavior = BottomSheetBehavior.from(linearLayout)
    bottomSheetBehavior.state =
        if (visibility) BottomSheetBehavior.STATE_COLLAPSED else BottomSheetBehavior.STATE_HIDDEN
}