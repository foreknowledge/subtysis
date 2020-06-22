package com.hackday.subtysis

import android.os.AsyncTask
import com.hackday.subtysis.model.Keyword
import com.hackday.subtysis.model.LangCode
import com.hackday.subtysis.model.SearchType
import java.io.File
import java.util.concurrent.Executor

/**
 * @author Created by lee.cm on 2019-11-01.
 */
class Subtysis(private val file: File, private val types: List<SearchType>) {

    private val serialExecutor: Executor = AsyncTask.SERIAL_EXECUTOR

    fun analyze(listener: ResponseListener) {
        serialExecutor.execute {
//            val subtitleParser: SubtitleParser = SubtitleParserImpl()
//            val subtitles: List<Subtitle> = subtitleParser.createSubtitle(file.path)
//
//            val contentExtractor: ContentExtractor = ContentExtractorImpl()
//            contentExtractor.initData(subtitles)
//            val keywords: List<Keyword> = contentExtractor.getAllKeywords()

            val keywords = mutableListOf<Keyword>()
            keywords.add(Keyword(111, "맥북", LangCode.KO, null))
            keywords.add(Keyword(111, "거울", LangCode.KO, null))
            keywords.add(Keyword(111, "미니 고데기", LangCode.KO, null))

            val metadataCreator: MetadataCreator = MetadataCreatorImpl()
            metadataCreator.fillMetadata(keywords, types, listener)
        }
    }
}