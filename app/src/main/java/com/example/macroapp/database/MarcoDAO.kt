package com.example.macroapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MacroDao {
    @Insert
    suspend fun insertMacro(macro: MacroEntity): Long

    @Query("SELECT * FROM macros")
    suspend fun getAllMacros(): List<MacroEntity>

    @Query("SELECT * FROM macros WHERE id = :macroId")
    suspend fun getMacroById(macroId: Int): MacroEntity?

    @Query("DELETE FROM macros WHERE id = :macroId")
    suspend fun deleteMacro(macroId: Int)
}
