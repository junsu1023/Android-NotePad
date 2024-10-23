package com.example.core.toast

import android.content.Context
import android.widget.Toast



object ToastUtil {
    private lateinit var applicationContext: Context

    private object Holder {
        val toastUtil = ToastUtil
    }

    fun getInstance(): ToastUtil = Holder.toastUtil

    fun init(context: Context) {
        applicationContext = context
    }

    fun makeShort(message: String?) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    fun makeLong(message: String?) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }
}