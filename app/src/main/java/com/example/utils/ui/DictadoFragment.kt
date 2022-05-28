package com.example.utils.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.utils.R

class DictadoFragment : Fragment() {
    private val SPEECH_CAPTURE = 111
    private lateinit var textView: TextView
    private lateinit var talkButton: ImageButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragment = inflater.inflate(R.layout.fragment_dictado, container, false)
        textView = fragment.findViewById(R.id.textView)
        talkButton = fragment.findViewById(R.id.talkBtn)
        talkButton.setOnClickListener {
            dispatchSpeechIntent()
        }
        return fragment
    }
    private fun dispatchSpeechIntent() {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).also { intent ->
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hable ahora")
            activity?.let {
                intent.resolveActivity(it.packageManager)?.also {
                    startActivityForResult(intent, SPEECH_CAPTURE)
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SPEECH_CAPTURE && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            textView.text = result?.get(0) ?: ""
        }
    }
}