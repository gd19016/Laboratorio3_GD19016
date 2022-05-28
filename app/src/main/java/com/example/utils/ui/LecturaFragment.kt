package com.example.utils.ui

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.utils.R
import java.util.*
class LecturaFragment : Fragment(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private lateinit var textInput: EditText
    private lateinit var readButton: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_lectura, container, false)
        textInput = fragment.findViewById(R.id.tts_editText)
        readButton = fragment.findViewById(R.id.tts_button)
        readButton.isEnabled = false
        readButton.setOnClickListener { speakOut() }
        tts = TextToSpeech(context, this)
        return fragment
    }
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale("spa", "ESP"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result ==
                TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(context, "Lenguaje no soportado", Toast.LENGTH_SHORT).show()
            } else {
                readButton.isEnabled = true
            }
        } else {
            Toast.makeText(context, "TTS no disponible", Toast.LENGTH_SHORT).show()
        }
    }
    private fun speakOut() {
        val text = textInput.text.toString()
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
    }
    override fun onDestroy() {
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }
}