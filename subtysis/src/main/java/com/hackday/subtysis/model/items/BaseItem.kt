package com.hackday.subtysis.model.items

import com.google.gson.annotations.SerializedName

open class BaseItem(
    @SerializedName("title")
    open val title: String = "HACK DAY",
    @SerializedName("link")
    open val link: String = "https://github.com/NAVER-CAMPUS-HACKDAY"
)