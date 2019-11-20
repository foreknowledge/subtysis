package com.mungziapp.testlib.model.items

import com.google.gson.annotations.SerializedName

open class BaseItem(
    @SerializedName("title")
    open val title: String = "",
    @SerializedName("link")
    open val link: String = ""
)