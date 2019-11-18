package com.hackday.subtysis

import com.hackday.subtysis.model.Subtitle

interface SubtitleParser{
    fun createSubtitle(filename: String):ArrayList<Subtitle>
}
