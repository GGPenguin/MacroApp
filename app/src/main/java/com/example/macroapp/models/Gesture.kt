package com.example.macroapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "macro_table")
data class MacroEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val gestureData: String // Store gesture data as a JSON string or similar format
)

data class Gesture(
    val x: Int,
    val y: Int,
    val duration: Long
)
