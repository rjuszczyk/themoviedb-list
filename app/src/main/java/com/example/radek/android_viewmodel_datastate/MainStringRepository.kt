package com.example.radek.android_viewmodel_datastate

import android.os.Handler

class MainStringRepository {
    fun loadString(stringCallback: StringCallback) {
        val handler = Handler()
        handler.postDelayed({ stringCallback.failed("LOADED STRING") }, 5000)
    }

    interface StringCallback {
        fun loaded(loadedString: String)
        fun failed(errorMessage: String)
    }
}
