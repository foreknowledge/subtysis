package com.hackday.subtysis.model.items

import com.google.gson.annotations.SerializedName

data class BlogItem (
    @SerializedName("description")
    val description: String,
    @SerializedName("bloggername")
    val bloggerName: String,
    @SerializedName("bloggerlink")
    val bloggerLink: String
) : BaseItem()