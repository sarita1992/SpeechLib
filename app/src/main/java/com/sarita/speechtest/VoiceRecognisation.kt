package com.sarita.speechtest

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.View
import java.util.Locale


public class VoiceRecognisation  {

    private var speechRecognizer: SpeechRecognizer? = null
    private lateinit var speechRecognizerIntent: Intent
    private var context: Context
    private var listner: OutputListner


    constructor(context: Context, listener: OutputListner) : super() {
        this.context = context
        this.listner = listener
    }

    fun startListing() {
        if (context.checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
          if(speechRecognizer!=null)
            speechRecognizer!!.startListening(speechRecognizerIntent)
          else {
              listner.error("Call speech() before startListing")
          }
        } else {
            listner.error("Please provide RECORD_AUDIO permission")
        }
    }

     fun speech() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

        speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        speechRecognizer!!.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(bundle: Bundle) {}
            override fun onBeginningOfSpeech() {
                Log.e("onBeginningOfSpeech","onBeginningOfSpeech")
                listner.listening("Listening...")
            }

            override fun onRmsChanged(v: Float) {}
            override fun onBufferReceived(bytes: ByteArray) {}
            override fun onEndOfSpeech() {
                Log.e("onEndOfSpeech","onEndOfSpeech")
                speechRecognizer!!.stopListening()
            }

            override fun onError(i: Int) {
                Log.e("onError","onError")
                listner.error("Error code= $i")
            }
            override fun onResults(bundle: Bundle) {
                Log.e("onResults","onResults")
                val data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                listner.result(data!![0])
            }

            override fun onPartialResults(bundle: Bundle) {}
            override fun onEvent(i: Int, bundle: Bundle) {}
        })
    }

    fun destroySpeech() {
        if (speechRecognizer != null)
            speechRecognizer!!.destroy()
    }
}