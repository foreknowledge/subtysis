package com.hackday.player

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hackday.subtysis.model.Keyword

/**
 * @author Created by lee.cm on 2019-11-12.
 */
class PlayerViewModel : ViewModel() {

    private val videoSourceUri = MutableLiveData<Uri>()

    private val _displayData = MutableLiveData<List<Keyword>>()

    private val _sheetVisibility = MutableLiveData<Boolean>(false)

    fun getVideoSourceUriLiveData() = videoSourceUri

    val displayData: LiveData<List<Keyword>>
        get() = _displayData

    val sheetVisibility: LiveData<Boolean>
        get() = _sheetVisibility

    fun setVideoSourceUri(videoSourceUri: Uri?) {
        this.videoSourceUri.value = videoSourceUri
    }

    fun hasVideoSourceUri(): Boolean {
        return this.videoSourceUri.value != null
    }

    fun setDisplayData(keywords: List<Keyword>) {
        _displayData.value = keywords
        setSheetVisibility(true)
    }

    fun setSheetVisibility(visibility: Boolean) {
        _sheetVisibility.value = visibility
    }
}
