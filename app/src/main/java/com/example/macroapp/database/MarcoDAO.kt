package com.example.macroapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.macroapp.models.MacroEntity

@Dao
interface MacroDao {

    @Insert
    suspend fun insertMacro(macro: MacroEntity)

    @Update
    suspend fun updateMacro(macro: MacroEntity)

    @Query("SELECT * FROM macro_table WHERE id = :id")
    suspend fun getMacroById(id: Long): MacroEntity?

    @Query("SELECT * FROM macro_table")
    suspend fun getAllMacros(): List<MacroEntity>

    @Query("DELETE FROM macro_table WHERE id = :id")
    suspend fun deleteMacroById(id: Long)
}
