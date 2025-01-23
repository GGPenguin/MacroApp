import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GestureDataConverter {
    @TypeConverter
    fun fromGestureList(gestures: List<Gesture>): String {
        return Gson().toJson(gestures)
    }

    @TypeConverter
    fun toGestureList(gestureString: String): List<Gesture> {
        val type = object : TypeToken<List<Gesture>>() {}.type
        return Gson().fromJson(gestureString, type)
    }
}
