package com.hackday.subtysis

import com.hackday.subtysis.model.Keyword
import com.hackday.subtysis.model.SearchType
import com.hackday.subtysis.model.Subtitle
import java.io.File
import java.util.ArrayList

/**
 * @author Created by lee.cm on 2019-11-01.
 */
class Subtysis {
    lateinit var file: File
    lateinit var mTypes: ArrayList<SearchType>
    lateinit var mListener: SetResponseListener

    fun init(file: File, types: ArrayList<SearchType>) {
        this.file = file
        this.mTypes = types;
    }

    fun analyze() {
        val subtitleParser: SubtitleParser = SubtitleParserImpl()
        val subtitles: ArrayList<Subtitle> = subtitleParser.createSubtitle(file.path)

        val contentExtractor: ContentExtractor = ContentExtractorImpl()
        contentExtractor.initData(subtitles)
        val keywords: ArrayList<Keyword> = contentExtractor.getAllKeywords()

        val metadataCreator: MetadataCreator = MetadataCreatorImpl()
        metadataCreator.fillMetadata(keywords, mTypes, mListener)
    }

    fun setOnResponseListener(listener: SetResponseListener): Subtysis {
        mListener = listener

        return this
    }
}