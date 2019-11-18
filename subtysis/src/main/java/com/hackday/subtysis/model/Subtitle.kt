package com.hackday.subtysis.model

data class Subtitle(
    var frame: Int=0,
    var sentence: String="",
    var langCode: LangCode=LangCode.KO
)

