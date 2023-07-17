package funny.buildapp.progress.data.source

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Locale

class DateConverter {
    //parse string date time  to long
    @TypeConverter
    fun fromDateString(value: String?): Long? {
        return SimpleDateFormat("yyyy-dd-MM", Locale.CHINA).parse(value)?.time
    }

    //parse long to string date time
    @TypeConverter
    fun toDateString(value: Long?): String? {
        return SimpleDateFormat("yyyy-dd-MM", Locale.CHINA).format(value)
    }
}