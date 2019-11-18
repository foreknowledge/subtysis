package com.hackday.subtysis

import com.hackday.subtysis.model.Keyword
import com.hackday.subtysis.model.Subtitle

interface ContentExtractor {

    fun initData(data: ArrayList<Subtitle>)

    fun addData(data: ArrayList<Subtitle>)

    fun getAllKeywords(): ArrayList<Keyword>

    fun getKeywordsByFrequency(min: Int = 1): ArrayList<Keyword>
}