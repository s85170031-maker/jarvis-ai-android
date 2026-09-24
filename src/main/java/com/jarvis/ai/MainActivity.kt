package com.jarvis.ai

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.TextView
import java.util.Locale

class MainActivity : Activity(), TextToSpeech.OnInitListener {

    private lateinit var speechText: TextView
    private lateinit var speakButton: Button
    private lateinit var tts: TextToSpeech

    private val speechRequest = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        speechText = TextView(this).apply {
            text = "Hello Boss. I'm JARVIS.\n\nTap the button and speak."
            textSize = 22f
            setPadding(40, 80, 40, 40)
        }

        speakButton = Button(this).apply {
            text = "🎤 TALK TO JARVIS"
            textSize = 18f
            setOnClickListener {
                startListening()
            }
        }

        val layout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            addView(
                speechText,
                android.widget.LinearLayout.LayoutParams(
                    -1,
                    0,
                    1f
                )
            )
            addView(
                speakButton,
                android.widget.LinearLayout.LayoutParams(
                    -1,
                    -2
                )
            )
        }

        setContentView(layout)

        tts = TextToSpeech(this, this)
    }

    private fun startListening() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Yes Boss, I'm listening...")
        }

        startActivityForResult(intent, speechRequest)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == speechRequest && resultCode == RESULT_OK) {
            val results =
                data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            val command = results?.firstOrNull()

            if (!command.isNullOrBlank()) {
                speechText.text = "You said:\n$command"
                speak("Yes Boss. You said $command")
            }
        }
    }

    private fun speak(message: String) {
        tts.speak(
            message,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "jarvis_response"
        )
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.US
        }
    }

    override fun onDestroy() {
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }
}
