package com.example.macroapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "macros")
@TypeConverters(GestureDataConverter::class)
data class MacroEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val gestures: List<MacroRecorder.GestureData>
)

// TypeConverter for saving List<GestureData> to the database
class GestureDataConverter {
    @TypeConverter
    fun fromGestureDataList(gestures: List<MacroRecorder.GestureData>): String {
        return Gson().toJson(gestures)
    }

    @TypeConverter
    fun toGestureDataList(gesturesString: String): List<MacroRecorder.GestureData> {
        val type = object : TypeToken<List<MacroRecorder.GestureData>>() {}.type
        return Gson().fromJson(gesturesString, type)
    }
}
