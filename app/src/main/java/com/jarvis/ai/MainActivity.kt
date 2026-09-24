package com.jarvis.ai

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val text = TextView(this)
        text.text = "JARVIS AI"
        text.textSize = 28f

        setContentView(text)
    }
}
