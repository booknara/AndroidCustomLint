package com.booknara.android.apps.lint.ui

import android.annotation.SuppressLint
import android.os.Bundle

@SuppressLint("Registered")
open class MiddleBaseActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callCommonFunction()
    }

    fun callCommonFunction() {

    }
}
