package com.hackday.subtysis

import com.hackday.subtysis.model.Keyword
import com.hackday.subtysis.model.Subtitle
import com.hackday.subtysis.model.WordType
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL
import kr.co.shineware.nlp.komoran.core.Komoran

class MorphemeAnalyzer(
    private val analysisType: WordType = WordType.NN
) {
    private val komoran = Komoran(DEFAULT_MODEL.LIGHT)

    fun analyzeSubtitle(subtitle: Subtitle): List<Keyword> {
        val tokenList = analyzeSentence(subtitle.sentence).iterator()
        val result = mutableListOf<Keyword>()

        while (tokenList.hasNext()) {
            val token = tokenList.next()

            if (getWordType(token.pos) == analysisType) {
                result.add(
                    Keyword(
                        subtitle.frame,
                        token.morph,
                        subtitle.langCode
                    )
                )
            }
        }

        return result
    }

    private fun analyzeSentence(sentence: String) = komoran.analyze(sentence).tokenList

    private fun getWordType(type: String) = when (type) {
        "NNG", "NNP", "NNB" -> WordType.NN
        "NP", "NR" -> WordType.NP
        "VV" -> WordType.VV
        "VA" -> WordType.VA
        "VX" -> WordType.VX
        "VCP", "VCN" -> WordType.VC
        "MM", "MAG", "MAJ" -> WordType.MM
        "IC" -> WordType.IC
        "JKS", "JKC", "JKG", "JKO", "JKB", "JKV", "JKQ", "JX", "JC" -> WordType.ASS
        "EP", "EF", "EC", "ETN", "ETM", "XPN", "XSN", "XSV", "XSA", "XR" -> WordType.DEP
        "SF", "SP", "SS", "SW", "SE", "SO" -> WordType.SYMBOL
        "SL", "SH" -> WordType.FOREIGN
        "NF" -> WordType.NF
        "NV" -> WordType.NV
        "SN" -> WordType.NUM
        else -> WordType.NONE
    }
}