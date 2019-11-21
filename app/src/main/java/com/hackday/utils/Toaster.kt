package com.hackday.utils

import android.widget.Toast
import com.hackday.GlobalApplication

/**
 * @author Created by lee.cm on 2019-11-19.
 */
object Toaster {
    @JvmStatic
    fun showShort(text: String = "") {
        Toast.makeText(GlobalApplication.getContext(), text, Toast.LENGTH_SHORT).show()
    }

}