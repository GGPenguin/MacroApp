package com.example.macroapp.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.macroapp.R
import com.example.macroapp.database.MacroDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class SchedulerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scheduler)

        val macroId = intent.getLongExtra("MACRO_ID", -1)
        if (macroId != -1L) {
            showDateTimePicker(macroId)
        }
    }

    private fun showDateTimePicker(macroId: Long) {
        val calendar = Calendar.getInstance()

        DatePickerDialog(this, { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            TimePickerDialog(this, { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                scheduleMacro(macroId, calendar.timeInMillis)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun scheduleMacro(macroId: Long, scheduleTime: Long) {
        val intent = Intent(this, SchedulerService::class.java).apply {
            action = SchedulerService.ACTION_SCHEDULE_MACRO
            putExtra(SchedulerService.EXTRA_MACRO_ID, macroId)
            putExtra(SchedulerService.EXTRA_SCHEDULE_TIME, scheduleTime)
        }
        startService(intent)
        finish()
    }
}
