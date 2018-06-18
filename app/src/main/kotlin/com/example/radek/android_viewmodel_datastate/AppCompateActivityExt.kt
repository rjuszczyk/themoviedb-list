package com.example.radek.android_viewmodel_datastate

import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import android.view.View

fun <T : View> AppCompatActivity.bindView(@IdRes id:Int) : Lazy<T> {
    return lazy { findViewById<T>(id) }
}