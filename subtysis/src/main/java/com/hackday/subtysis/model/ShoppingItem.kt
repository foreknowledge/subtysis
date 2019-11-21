package com.hackday.subtysis.model

import com.google.gson.annotations.SerializedName

data class ShoppingItem (
    @SerializedName("title")
    val title: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("lprice")
    val lprice: Int,
    @SerializedName("hprice")
    val hprice: Int,
    @SerializedName("mallName")
    val mallName: String,
    @SerializedName("productId")
    val productId: Long,
    @SerializedName("productType")
    val productType: Int
)