package com.hackday.player

import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.hackday.subtysis.model.Keyword
import com.hackday.subtysis.model.SearchType

@BindingAdapter("bind_metadata")
fun bindMetadata(textView: TextView, keywords: ArrayList<Keyword>?) {
    keywords?.let {
        for (keyword in keywords) {
            val shoppingData = keyword.responses!![SearchType.SHOPPING]?.items
            val displayString = StringBuilder()

            // TODO("SearchType 별로 문자열 다르게 출력")
            shoppingData?.let {
                displayString.append(it.joinToString("\n"))
            }
            textView.text = displayString.toString()
        }
    }
}

@BindingAdapter("set_visibility")
fun setVisibility(linearLayout: LinearLayout, visibility: Boolean) {
    val bottomSheetBehavior = BottomSheetBehavior.from(linearLayout)
    bottomSheetBehavior.state =
        if (visibility) BottomSheetBehavior.STATE_EXPANDED else BottomSheetBehavior.STATE_COLLAPSED
}