package com.example.macroapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MacroEntity::class], version = 1, exportSchema = false)
abstract class MacroDatabase : RoomDatabase() {
    abstract fun macroDao(): MacroDao

    companion object {
        @Volatile
        private var INSTANCE: MacroDatabase? = null

        fun getDatabase(context: Context): MacroDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MacroDatabase::class.java,
                    "macro_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
