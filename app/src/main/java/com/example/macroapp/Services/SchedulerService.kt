package com.example.macroapp

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

class SchedulerService {

    companion object {
        /**
         * Schedules a macro to play at a specific time.
         * @param context The application context.
         * @param macroId The unique ID of the macro to play.
         * @param timeInMillis The time at which the macro should be played, in milliseconds.
         */
        fun scheduleMacro(context: Context, macroId: String, timeInMillis: Long) {
            // Calculate the delay from the current time
            val delay = timeInMillis - System.currentTimeMillis()
            if (delay <= 0) {
                // If the time has already passed, do nothing
                return
            }

            // Create a WorkRequest to schedule playback
            val workRequest = OneTimeWorkRequestBuilder<MacroPlaybackWorker>()
                .setInputData(workDataOf("MACRO_ID" to macroId))
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build()

            // Enqueue the WorkRequest
            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}

/**
 * Worker class to handle macro playback at the scheduled time.
 */
class MacroPlaybackWorker(context: Context, params: WorkerParameters) :
    Worker(context, params) {

    override fun doWork(): Result {
        // Retrieve the macro ID from the input data
        val macroId = inputData.getString("MACRO_ID") ?: return Result.failure()

        // Play the macro (use MacroRecorder's playback logic)
        playMacro(macroId)

        return Result.success()
    }

    private fun playMacro(macroId: String) {
        // Logic to retrieve and play the macro based on its ID
        // For simplicity, this calls MacroRecorder's playback method
        // (You would load gestures from storage here)
        val intent = Intent(applicationContext, MacroRecorder::class.java)
        intent.action = "PLAYBACK"
        applicationContext.startService(intent)
    }
}
