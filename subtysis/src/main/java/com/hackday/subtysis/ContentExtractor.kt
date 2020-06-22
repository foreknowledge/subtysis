package com.hackday.subtysis

import com.hackday.subtysis.model.Keyword
import com.hackday.subtysis.model.Subtitle

interface ContentExtractor {

    fun initData(data: List<Subtitle>)

    fun addData(data: List<Subtitle>)

    fun getAllKeywords(): List<Keyword>

    fun getKeywordsByFrequency(min: Int = 1): List<Keyword>
}