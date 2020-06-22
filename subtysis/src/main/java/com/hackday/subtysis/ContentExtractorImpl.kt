package com.hackday.subtysis

import com.hackday.subtysis.model.Keyword
import com.hackday.subtysis.model.Subtitle

class ContentExtractorImpl : ContentExtractor {
    private val analyzer = MorphemeAnalyzer()
    private val subtitles = mutableListOf<Subtitle>()

    override fun initData(data: List<Subtitle>) {
        subtitles.clear()
        subtitles.addAll(data)
    }

    override fun addData(data: List<Subtitle>) {
        subtitles.addAll(data)
    }

    override fun getAllKeywords(): List<Keyword> {
        val result = mutableListOf<Keyword>()

        for (subtitle in subtitles) {
            val keywords = analyzer.analyzeSubtitle(subtitle)
            result.addAll(keywords)
        }

        return result
    }

    override fun getKeywordsByFrequency(min: Int): List<Keyword> {
        TODO("not implemented")
    }
}