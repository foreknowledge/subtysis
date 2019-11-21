package com.hackday.subtysis

import com.hackday.subtysis.model.Keyword
import com.hackday.subtysis.model.Subtitle

class ContentExtractorImpl : ContentExtractor {
    private val analyzer = MorphemeAnalyzer()
    private val subtitles = arrayListOf<Subtitle>()

    override fun initData(data: ArrayList<Subtitle>) {
        subtitles.clear()
        subtitles.addAll(data)
    }

    override fun addData(data: ArrayList<Subtitle>) {
        subtitles.addAll(data)
    }

    override fun getAllKeywords(): ArrayList<Keyword> {
        val result = arrayListOf<Keyword>()

        for (subtitle in subtitles) {
            val keywords = analyzer.analyzeSubtitle(subtitle)
            result.addAll(keywords)
        }

        return result
    }

    override fun getKeywordsByFrequency(min: Int): ArrayList<Keyword> {
        TODO("not implemented")
    }
}