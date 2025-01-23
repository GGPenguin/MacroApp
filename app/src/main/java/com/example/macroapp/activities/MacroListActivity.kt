package com.example.macroapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.macroapp.R
import com.example.macroapp.adapters.MacroListAdapter
import com.example.macroapp.database.MacroDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MacroListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MacroListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_macro_list)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MacroListAdapter { macro ->
            // Handle macro item click (e.g., playback or delete)
            playMacro(macro.id)
        }
        recyclerView.adapter = adapter

        loadMacros()
    }

    private fun loadMacros() {
        CoroutineScope(Dispatchers.IO).launch {
            val macros = MacroDatabase.getInstance(this@MacroListActivity).macroDao().getAllMacros()
            runOnUiThread { adapter.submitList(macros) }
        }
    }

    private fun playMacro(macroId: Long) {
        val intent = Intent(this, SchedulerService::class.java).apply {
            action = SchedulerService.ACTION_RUN_MACRO
            putExtra(SchedulerService.EXTRA_MACRO_ID, macroId)
        }
        startService(intent)
    }
}
