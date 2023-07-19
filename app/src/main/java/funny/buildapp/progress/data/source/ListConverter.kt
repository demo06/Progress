package funny.buildapp.progress.data.source

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import funny.buildapp.progress.data.source.todo.Todo
import java.lang.reflect.Type

class ListConverter {
    private val gson: Gson = Gson()

    @TypeConverter
    fun fromList(list: List<Todo>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun stringToObject(json: String): List<Todo> {
        val listType: Type = object : TypeToken<List<Todo>>() {}.type
        return gson.fromJson(json, listType)
    }
}