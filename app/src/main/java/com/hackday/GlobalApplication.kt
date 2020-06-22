package com.hackday

import android.app.Application
import android.content.Context
import com.hackday.subtysis.RequestQueueProvider

/**
 * @author Created by lee.cm on 2019-11-19.
 */

class GlobalApplication : Application() {

    companion object {

        private var APPLICATION_CONTENXT: Context? = null

        @JvmStatic
        fun getContext(): Context {
            return APPLICATION_CONTENXT!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        APPLICATION_CONTENXT = applicationContext

        RequestQueueProvider.createRequestQueue(applicationContext)
    }
}