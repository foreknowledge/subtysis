package com.hackday.subtysis

import com.hackday.subtysis.model.Keyword
import com.hackday.subtysis.model.LangCode
import com.hackday.subtysis.model.SearchType
import java.io.File
import java.util.ArrayList

/**
 * @author Created by lee.cm on 2019-11-01.
 */
class Subtysis {
    lateinit var file: File
    lateinit var mTypes: ArrayList<SearchType>
    lateinit var mListener: SetResponseListener

    val contentExtractor: ContentExtractor = ContentExtractorImpl()

    fun init(file: File, types: ArrayList<SearchType>) {
        this.file = file
        this.mTypes = types;
    }

    fun analyze() {
        val keywords = ArrayList<Keyword>()
        keywords.add(Keyword(1234, "모자", LangCode.KO, null))
        keywords.add(Keyword(1234, "아이폰11", LangCode.KO, null))
        keywords.add(Keyword(1234, "삼성 갤럭시 A80", LangCode.KO, null))

        val metadataCreator = MetadataCreatorImpl()
        metadataCreator.fillMetadata(keywords, mTypes, mListener)
    }

    fun setOnResponseListener(listener: SetResponseListener): Subtysis {
        mListener = listener

        return this
    }
}