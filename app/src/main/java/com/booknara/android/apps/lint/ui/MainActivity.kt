package com.booknara.android.apps.lint.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.booknara.android.apps.lint.R
import com.booknara.android.apps.lint.viewmodel.HelloWorldViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private val viewModel: HelloWorldViewModel by lazy {
        ViewModelProviders.of(this).get(HelloWorldViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView.setText(R.string.tap_here)
        textView.setOnClickListener {
            setGreeting(viewModel.greeting)
        }
    }

    private fun setGreeting(word: String) {
        textView.text = word
    }
}
