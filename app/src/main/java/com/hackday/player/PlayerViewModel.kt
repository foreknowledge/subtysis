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

    private val displayData = MutableLiveData<ArrayList<Keyword>>().apply {
        this.value = arrayListOf()
    }

    fun getVideoSourceUriLiveData() = videoSourceUri

    fun getDisplayData(): LiveData<ArrayList<Keyword>> = MutableLiveData<ArrayList<Keyword>>()

    fun setVideoSourceUri(videoSourceUri: Uri?) {
        this.videoSourceUri.value = videoSourceUri
    }

    fun hasVideoSourceUri(): Boolean {
        return this.videoSourceUri.value != null
    }

    fun setDisplayData(keywords: ArrayList<Keyword>) {
        this.displayData.value?.let {
            it.clear()
            it.addAll(keywords)
        }
    }
}
