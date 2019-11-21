package com.hackday.subtysis.model

import com.hackday.subtysis.model.response.ResponseData

data class Keyword(
    val frame: Int,
    val word: String,
    val langCode: LangCode,
    var responseData: ResponseData? = null
)