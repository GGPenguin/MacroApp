package com.example.macroapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class SchedulerActivity : AppCompatActivity() {

    private var selectedMacroId: String? = null
    private var selectedTimeInMillis: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scheduler)

        val chooseMacroButton: Button = findViewById(R.id.chooseMacroButton)
        val pickDateTimeButton: Button = findViewById(R.id.pickDateTimeButton)
        val scheduleMacroButton: Button = findViewById(R.id.scheduleMacroButton)

        // Mock: Choose a macro (for now, just hard-code a macro ID)
        chooseMacroButton.setOnClickListener {
            selectedMacroId = "mock_macro_id_1" // Replace with actual selection logic
            Toast.makeText(this, "Macro Selected: $selectedMacroId", Toast.LENGTH_SHORT).show()
        }

        // Pick date and time
        pickDateTimeButton.setOnClickListener {
            showDateTimePicker()
        }

        // Schedule the macro
        scheduleMacroButton.setOnClickListener {
            if (selectedMacroId == null || selectedTimeInMillis == 0L) {
                Toast.makeText(this, "Please select a macro and time!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            SchedulerService.scheduleMacro(this, selectedMacroId!!, selectedTimeInMillis)
            Toast.makeText(this, "Macro Scheduled!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(this, { _, year, month, dayOfMonth ->
            TimePickerDialog(this, { _, hourOfDay, minute ->
                // Set the selected time in milliseconds
                calendar.set(year, month, dayOfMonth, hourOfDay, minute)
                selectedTimeInMillis = calendar.timeInMillis
                Toast.makeText(this, "Date & Time Selected", Toast.LENGTH_SHORT).show()
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }
    chooseMacroButton.setOnClickListener {
        Thread {
            val database = MacroDatabase.getDatabase(this)
            val macros = database.macroDao().getAllMacros()

            runOnUiThread {
                if (macros.isNotEmpty()) {
                    // Display a dialog or list for the user to pick a macro
                    val macroNames = macros.map { it.name }.toTypedArray()
                    val macroIds = macros.map { it.id }.toTypedArray()

                    AlertDialog.Builder(this)
                        .setTitle("Select a Macro")
                        .setItems(macroNames) { _, which ->
                            selectedMacroId = macroIds[which].toString()
                            Toast.makeText(this, "Selected Macro: ${macroNames[which]}", Toast.LENGTH_SHORT).show()
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                } else {
                    Toast.makeText(this, "No macros available!", Toast.LENGTH_SHORT).show()

                }
            }
        }.start()
    }

    val macroId = intent.getIntExtra("MACRO_ID", -1)
    if (macroId != -1) {
        selectedMacroId = macroId.toString()
        Toast.makeText(this, "Selected Macro: $macroId", Toast.LENGTH_SHORT).show()
    }
}
