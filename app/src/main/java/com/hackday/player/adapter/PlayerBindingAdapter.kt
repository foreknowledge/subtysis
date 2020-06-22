package com.hackday.player.adapter

import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.hackday.databinding.ItemShoppingBinding
import com.hackday.subtysis.model.Keyword
import com.hackday.subtysis.model.SearchType
import com.hackday.subtysis.model.items.ShoppingItem


@SuppressWarnings("unchecked")
@BindingAdapter("bind_metadata")
fun bindMetadata(recyclerView: RecyclerView, keywords: List<Keyword>?) {
    val adapter =
        recyclerView.adapter as MetadataRecyclerViewAdapter<ItemShoppingBinding, ShoppingItem>
    val displayData = mutableListOf<ShoppingItem>()

    keywords?.let {
        for (keyword in keywords) {
            if (keyword.metadata != null) {
                val shoppingData = keyword.metadata!![SearchType.SHOPPING]?.items

                shoppingData?.let {
                    for (data in shoppingData) displayData.add(data as ShoppingItem)
                }
            }
        }
    }

    adapter.setItems(displayData)
}

@BindingAdapter("set_visibility")
fun setVisibility(linearLayout: LinearLayout, visibility: Boolean) {
    val bottomSheetBehavior = BottomSheetBehavior.from(linearLayout)
    bottomSheetBehavior.state =
        if (visibility) BottomSheetBehavior.STATE_HALF_EXPANDED else BottomSheetBehavior.STATE_HIDDEN
}

@BindingAdapter("open_url")
fun openUrl(constraintLayout: ConstraintLayout, url: String) {
    constraintLayout.setOnClickListener {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        constraintLayout.context.startActivity(intent)
    }
}

@BindingAdapter("bind_img")
fun bindImg(imageView: ImageView, url: String) {
    Glide.with(imageView.context)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .into(imageView)
}

@BindingAdapter("set_html_text")
fun setHtmlText(textView: TextView, text: String) {
    textView.text = text.replace("</b>", "")
        .replace("<b>", "")
}