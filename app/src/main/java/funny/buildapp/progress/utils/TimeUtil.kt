package funny.buildapp.progress.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

fun compareDate(startTime: String, endTime: String): Boolean {
    if (startTime == endTime) return true
    return startTime.stringToDate().before(endTime.stringToDate())
}

fun String.stringToDate(): Date {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return format.parse(this) ?: Date()
}

fun Long.dateToString(): String {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return format.format(this)
}

fun getCurrentDate(isDayOfStart: Boolean = true): Long {
    return if (isDayOfStart) {
        System.currentTimeMillis().dateToString().stringToDate().time
    } else {
        System.currentTimeMillis().dateToString().stringToDate().time + 24 * 60 * 60 * 1000 - 1000
    }

}

@RequiresApi(Build.VERSION_CODES.O)
fun daysBetweenDates(date1: String, date2: String): Long {
    val firstDate = LocalDate.parse(date1)
    val secondDate = LocalDate.parse(date2)
    return ChronoUnit.DAYS.between(firstDate, secondDate)
}