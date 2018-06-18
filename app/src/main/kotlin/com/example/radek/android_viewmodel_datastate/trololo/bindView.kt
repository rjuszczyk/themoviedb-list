package com.example.radek.android_viewmodel_datastate.trololo

import android.support.v7.app.AppCompatActivity
import android.view.View

fun <T : View> AppCompatActivity.bindView(id:Int) : Lazy<T> {
    return lazy { findViewById<T>(id) }
}