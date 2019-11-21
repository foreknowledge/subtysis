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

    private val _displayData = MutableLiveData<ArrayList<Keyword>>()

    private val _sheetVisibility = MutableLiveData<Boolean>(true)

    fun getVideoSourceUriLiveData() = videoSourceUri

    val displayData: LiveData<ArrayList<Keyword>>
        get() = _displayData

    val sheetVisibility: LiveData<Boolean>
        get() = _sheetVisibility

    fun setVideoSourceUri(videoSourceUri: Uri?) {
        this.videoSourceUri.value = videoSourceUri
    }

    fun hasVideoSourceUri(): Boolean {
        return this.videoSourceUri.value != null
    }

    fun setDisplayData(keywords: ArrayList<Keyword>) {
        _displayData.value = keywords
        setSheetVisibility(true)
    }

    fun setSheetVisibility(visibility: Boolean) {
        _sheetVisibility.value = visibility
    }
}
