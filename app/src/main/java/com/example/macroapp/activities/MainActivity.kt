package com.example.macroapp

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Buttons
        val startOverlayButton: Button = findViewById(R.id.startOverlayButton)
        val startRecordingButton: Button = findViewById(R.id.startRecordingButton)
        val stopRecordingButton: Button = findViewById(R.id.stopRecordingButton)
        val scheduleButton: Button = findViewById(R.id.scheduleButton)

        // Start overlay service
        startOverlayButton.setOnClickListener {
            if (Settings.canDrawOverlays(this)) {
                val intent = Intent(this, OverlayService::class.java)
                startService(intent)
            } else {
                // Request permission to draw overlays
                val overlayIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                startActivity(overlayIntent)
            }
        }

        // Start recording macros
        startRecordingButton.setOnClickListener {
            val intent = Intent(this, MacroRecorder::class.java)
            intent.action = "START_RECORDING"
            startService(intent)
        }

        // Stop recording and save macros
        stopRecordingButton.setOnClickListener {
            val intent = Intent(this, MacroRecorder::class.java)
            intent.action = "STOP_RECORDING"
            startService(intent)
        }

        // Open scheduler
        scheduleButton.setOnClickListener {
            val intent = Intent(this, SchedulerActivity::class.java)
            startActivity(intent)
        }

        scheduleButton.setOnClickListener {
            val intent = Intent(this, SchedulerActivity::class.java)
            startActivity(intent)
        }
    }
}


