package com.hackday.player

import android.graphics.Bitmap

/**
 * @author Created by lee.cm on 2019-11-19.
 */
data class PlayerItem(
    val id: Long,
    val title: String,
    val size: Int,
    val bitmap: Bitmap
)