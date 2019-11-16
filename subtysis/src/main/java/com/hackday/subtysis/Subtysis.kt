package com.hackday.subtysis

import java.io.File

/**
 * @author Created by lee.cm on 2019-11-01.
 */
class Subtysis {
    lateinit var file: File
    val contentExtractor: ContentExtractor = ContentExtractorImpl()

    fun init(file: File) {
        this.file = file

    }
}