package com.hackday

import android.os.Handler
import android.os.Message

class l {
    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == 0) {

            }
        }
    }

    internal inner class NewThread : Thread() {

        var handler = mHandler

        override fun run() {

            while (true) {
                Thread.sleep(100)
                handler.sendEmptyMessage(0)
            }
        }
    }
}
