package com.sarita.speechtest

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.sarita.speechlibrary.OutputListner
import com.sarita.speechlibrary.VoiceRecognisation
import com.sarita.speechtest.databinding.ActivityMainBinding


class MainActivity : ComponentActivity(), OutputListner {


    private lateinit var voiceRecognisation: VoiceRecognisation
    private lateinit var activityMainBinding: ActivityMainBinding
    private val requestSinglePermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {

            } else {

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        voiceRecognisation = VoiceRecognisation(this, this)
        requestSinglePermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
        voiceRecognisation.speech()
        activityMainBinding.ivMic.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
            activityMainBinding.ivMic.setImageResource(R.drawable.ic_mic_on)
            voiceRecognisation.startListing()
            false
        })

    }

    override fun result(data: String) {
        activityMainBinding.ivMic.setImageResource(R.drawable.ic_mic_off)
        activityMainBinding.etText.setText(data)
    }

    override fun listening(data: String) {
        activityMainBinding.etText.setText("")
        activityMainBinding.etText.hint = "Listening..."
    }

    override fun error(data: String) {
        Toast.makeText(this, data, Toast.LENGTH_LONG).show()

    }

    override fun onDestroy() {
        super.onDestroy()
        voiceRecognisation.destroySpeech()
    }
}