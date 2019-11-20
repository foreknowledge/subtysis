package com.mungziapp.testlib.model.items

import com.google.gson.annotations.SerializedName

data class EncyclopediaItem (
    @SerializedName("description")
    val description: String,
    @SerializedName("thumbnail")
    val thumbnail: String
) : BaseItem()