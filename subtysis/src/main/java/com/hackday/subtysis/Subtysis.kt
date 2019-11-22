package com.hackday.subtysis

import android.os.AsyncTask
import com.hackday.subtysis.model.Keyword
import com.hackday.subtysis.model.SearchType
import com.hackday.subtysis.model.Subtitle
import java.io.File
import java.util.*
import java.util.concurrent.Executor

/**
 * @author Created by lee.cm on 2019-11-01.
 */
class Subtysis(private val file: File, private val types: ArrayList<SearchType>) {

    private val serialExecutor: Executor = AsyncTask.SERIAL_EXECUTOR

    fun analyze(listener: SetResponseListener) {
        serialExecutor.execute {
            val subtitleParser: SubtitleParser = SubtitleParserImpl()
            val subtitles: ArrayList<Subtitle> = subtitleParser.createSubtitle(file.path)

            val contentExtractor: ContentExtractor = ContentExtractorImpl()
            contentExtractor.initData(subtitles)
            val keywords: ArrayList<Keyword> = contentExtractor.getAllKeywords()

            val metadataCreator: MetadataCreator = MetadataCreatorImpl()
            metadataCreator.fillMetadata(keywords, types, listener)
        }
    }
}