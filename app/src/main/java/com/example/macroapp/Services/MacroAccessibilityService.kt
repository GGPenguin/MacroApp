package com.example.macroapp.services

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.example.macroapp.models.Gesture
import com.example.macroapp.database.MacroDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MacroAccessibilityService : AccessibilityService() {

    private val TAG = "MacroAccessibilityService"
    private val recordedGestures = mutableListOf<Gesture>()
    private var isRecording = false

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.i(TAG, "Accessibility Service connected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (!isRecording || event == null) return

        if (event.eventType == AccessibilityEvent.TYPE_TOUCH_INTERACTION_START ||
            event.eventType == AccessibilityEvent.TYPE_TOUCH_INTERACTION_END) {

            val source = event.source
            if (source != null && source.boundsInScreen != null) {
                val bounds = source.boundsInScreen
                val centerX = bounds.centerX()
                val centerY = bounds.centerY()

                // Record gesture details
                val gesture = Gesture(
                    x = centerX,
                    y = centerY,
                    type = if (event.eventType == AccessibilityEvent.TYPE_TOUCH_INTERACTION_START) "TAP" else "RELEASE",
                    duration = event.eventTime - event.eventTime // Duration logic can be extended
                )
                recordedGestures.add(gesture)

                Log.d(TAG, "Gesture recorded: $gesture")
            }
        }
    }

    override fun onInterrupt() {
        Log.w(TAG, "Accessibility Service interrupted")
    }

    /**
     * Start recording gestures
     */
    fun startRecording() {
        recordedGestures.clear()
        isRecording = true
        Log.i(TAG, "Started recording gestures")
    }

    /**
     * Stop recording gestures and save them to the database
     */
    fun stopRecording(macroName: String) {
        isRecording = false
        saveGesturesToDatabase(macroName)
        Log.i(TAG, "Stopped recording gestures. Gestures saved.")
    }

    /**
     * Save recorded gestures to the database
     */
    private fun saveGesturesToDatabase(macroName: String) {
        val macroEntity = com.example.macroapp.models.MacroEntity(
            name = macroName,
            gestures = recordedGestures
        )

        CoroutineScope(Dispatchers.IO).launch {
            MacroDatabase.getInstance(applicationContext).macroDao().insertMacro(macroEntity)
            Log.i(TAG, "Macro saved to database: $macroName")
        }
    }

    /**
     * Playback the gestures
     */
    fun playBackGestures(gestures: List<Gesture>) {
        for (gesture in gestures) {
            val path = Path().apply { moveTo(gesture.x.toFloat(), gesture.y.toFloat()) }
            val gestureDescription = GestureDescription.Builder()
                .addStroke(GestureDescription.StrokeDescription(path, 0, gesture.duration))
                .build()

            dispatchGesture(gestureDescription, null, null)
            Log.i(TAG, "Playing back gesture at (${gesture.x}, ${gesture.y}) with duration ${gesture.duration}")
        }
    }
}
