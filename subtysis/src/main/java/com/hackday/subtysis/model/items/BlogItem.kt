package com.hackday.subtysis.model.items

import com.google.gson.annotations.SerializedName
import com.mungziapp.testlib.model.items.BaseItem

data class BlogItem (
    @SerializedName("description")
    val description: String,
    @SerializedName("bloggername")
    val bloggername: String,
    @SerializedName("bloggerlink")
    val bloggerlink: String
) : BaseItem()