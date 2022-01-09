package com.example.clean_mvvm.presentation.activity

import android.widget.TextView
import com.example.clean_mvvm.R
import foundation.views.*

class MainActivity : BaseActivity(R.id.fragmentContainer, R.layout.activity_main) {

    override fun updateToolbar(message: String) {
        val toolbar = findViewById<TextView>(R.id.toolbarCustomTitle)
        toolbar.text = message
    }

}