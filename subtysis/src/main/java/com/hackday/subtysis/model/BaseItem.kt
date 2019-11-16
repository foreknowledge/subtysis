package com.hackday.subtysis.model

import com.google.gson.annotations.SerializedName

data class BaseItem(
    @SerializedName("title")
    val title: String,
    @SerializedName("link")
    val link: String
)