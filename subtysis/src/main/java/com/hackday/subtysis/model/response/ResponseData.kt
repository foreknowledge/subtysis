package com.hackday.subtysis.model.response

import com.google.gson.annotations.SerializedName
import com.hackday.subtysis.model.BaseItem

data class ResponseData(
    @SerializedName("title")
    val title: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("lastBuildDate")
    val lastBuildDate: String,
    @SerializedName("total")
    val total: Int,
    @SerializedName("start")
    val start: Int,
    @SerializedName("display")
    val display: Int,
    @SerializedName("items")
    val items: ArrayList<BaseItem>
)