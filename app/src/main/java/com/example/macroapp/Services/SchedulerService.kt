package com.example.macroapp.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.macroapp.database.MacroDatabase
import com.example.macroapp.models.MacroEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class SchedulerService : Service() {

    private val TAG = "SchedulerService"

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "SchedulerService started")

        // Check if an action is provided in the intent
        val action = intent?.action
        if (action == ACTION_SCHEDULE_MACRO) {
            val macroId = intent.getLongExtra(EXTRA_MACRO_ID, -1)
            val scheduleTime = intent.getLongExtra(EXTRA_SCHEDULE_TIME, -1)

            if (macroId != -1L && scheduleTime != -1L) {
                scheduleMacro(macroId, scheduleTime)
            }
        } else if (action == ACTION_RUN_MACRO) {
            val macroId = intent.getLongExtra(EXTRA_MACRO_ID, -1)
            if (macroId != -1L) {
                executeMacro(macroId)
            }
        }

        return START_STICKY
    }

    /**
     * Schedules a macro to run at a specific time.
     */
    private fun scheduleMacro(macroId: Long, scheduleTime: Long) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, SchedulerService::class.java).apply {
            action = ACTION_RUN_MACRO
            putExtra(EXTRA_MACRO_ID, macroId)
        }
        val pendingIntent = PendingIntent.getService(
            this,
            macroId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            scheduleTime,
            pendingIntent
        )

        Log.i(TAG, "Macro with ID $macroId scheduled for ${Date(scheduleTime)}")
    }

    /**
     * Executes a macro by retrieving it from the database and playing back its gestures.
     */
    private fun executeMacro(macroId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            val database = MacroDatabase.getInstance(applicationContext)
            val macro = database.macroDao().getMacroById(macroId)

            if (macro != null) {
                Log.i(TAG, "Executing macro: ${macro.name}")
                playMacroGestures(macro)
            } else {
                Log.e(TAG, "Macro with ID $macroId not found")
            }
        }
    }

    /**
     * Plays back the gestures of a macro using the MacroAccessibilityService.
     */
    private fun playMacroGestures(macro: MacroEntity) {
        val gestures = macro.gestures
        if (gestures.isEmpty()) {
            Log.e(TAG, "No gestures to play for macro: ${macro.name}")
            return
        }

        // Bind to MacroAccessibilityService to trigger playback
        val intent = Intent(this, MacroAccessibilityService::class.java)
        intent.putParcelableArrayListExtra(EXTRA_GESTURES, ArrayList(gestures))
        startService(intent)

        Log.i(TAG, "Playback started for macro: ${macro.name}")
    }

    companion object {
        const val ACTION_SCHEDULE_MACRO = "com.example.macroapp.action.SCHEDULE_MACRO"
        const val ACTION_RUN_MACRO = "com.example.macroapp.action.RUN_MACRO"
        const val EXTRA_MACRO_ID = "com.example.macroapp.extra.MACRO_ID"
        const val EXTRA_SCHEDULE_TIME = "com.example.macroapp.extra.SCHEDULE_TIME"
        const val EXTRA_GESTURES = "com.example.macroapp.extra.GESTURES"
    }
}
