package com.booknara.android.apps.lint.ui

import android.os.Bundle
import com.booknara.android.apps.lint.R

class ThirdActivity : MiddleBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        callCommonFunction()
    }
}
