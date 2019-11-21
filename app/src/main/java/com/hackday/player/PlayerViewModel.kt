package com.hackday.player

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @author Created by lee.cm on 2019-11-12.
 */
class PlayerViewModel : ViewModel() {

    private val videoSourceUri = MutableLiveData<Uri>()

    fun getVideoSourceUriLiveData() = videoSourceUri

    fun setVideoSourceUri(videoSourceUri: Uri?) {
        this.videoSourceUri.value = videoSourceUri
    }

    fun hasVideoSourceUri(): Boolean {
        return this.videoSourceUri.value != null
    }

}