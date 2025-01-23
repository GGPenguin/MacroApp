package com.example.macroapp.utils

import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.view.GestureDetector
import com.example.macroapp.models.Gesture

object GestureUtils {

    // Convert gesture to GestureDescription for playback
    fun createGestureDescription(gestures: List<Gesture>): GestureDescription {
        val path = Path()
        gestures.forEach { gesture ->
            path.moveTo(gesture.x.toFloat(), gesture.y.toFloat())
        }
        val strokeDescription = GestureDescription.StrokeDescription(path, 0, gestures.lastOrNull()?.duration ?: 0L)
        return GestureDescription.Builder().addStroke(strokeDescription).build()
    }

    // Create list of Gesture objects from gesture data
    fun createGestureList(gestureData: List<Pair<Int, Int>>): List<Gesture> {
        return gestureData.mapIndexed { index, (x, y) ->
            Gesture(x, y, 100L)  // Assuming a fixed duration for simplicity
        }
    }
}
