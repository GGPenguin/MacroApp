package com.example.macroapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MacroListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MacroListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_macro_list)

        recyclerView = findViewById(R.id.recyclerView)
        val recordButton: FloatingActionButton = findViewById(R.id.recordButton)
        val scheduleButton: FloatingActionButton = findViewById(R.id.scheduleButton)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MacroListAdapter { macro, action ->
            when (action) {
                "delete" -> deleteMacro(macro)
                "schedule" -> scheduleMacro(macro)
            }
        }
        recyclerView.adapter = adapter

        // Fetch and display macros
        fetchMacros()

        // Record Button: Navigate to MacroRecorderActivity
        recordButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        // Schedule Button: Navigate to SchedulerActivity
        scheduleButton.setOnClickListener {
            startActivity(Intent(this, SchedulerActivity::class.java))
        }
    }

    private fun fetchMacros() {
        Thread {
            val database = MacroDatabase.getDatabase(this)
            val macros = database.macroDao().getAllMacros()
            runOnUiThread {
                adapter.submitList(macros)
            }
        }.start()
    }

    private fun deleteMacro(macro: MacroEntity) {
        Thread {
            val database = MacroDatabase.getDatabase(this)
            database.macroDao().deleteMacro(macro.id)
            runOnUiThread {
                Toast.makeText(this, "Macro deleted", Toast.LENGTH_SHORT).show()
                fetchMacros() // Refresh the list
            }
        }.start()
    }

    private fun scheduleMacro(macro: MacroEntity) {
        val intent = Intent(this, SchedulerActivity::class.java)
        intent.putExtra("MACRO_ID", macro.id)
        startActivity(intent)
    }
}
