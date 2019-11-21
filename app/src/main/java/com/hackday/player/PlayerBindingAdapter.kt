package com.hackday.player

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.hackday.subtysis.model.Keyword
import com.hackday.subtysis.model.SearchType

@BindingAdapter("bind_metadata")
fun bindMetadata(textView: TextView, keywords: ArrayList<Keyword>) {
    for (keyword in keywords) {
        val response = keyword.responses
        val shoppingData = response!![SearchType.SHOPPING]?.items
        val displayString = StringBuilder()

        // TODO("SearchType 별로 문자열 가공 필요")
        shoppingData?.let {
            displayString.append(it.joinToString("\n"))
        }

        textView.text = displayString.toString()
    }
}