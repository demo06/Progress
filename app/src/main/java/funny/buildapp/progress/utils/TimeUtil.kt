package funny.buildapp.progress.utils

import java.text.SimpleDateFormat
import java.util.Date

fun compareDate(startTime: String, endTime: String): Boolean {
    if (startTime==endTime) return true
    return startTime.stringToDate().before(endTime.stringToDate())
}

fun String.stringToDate(): Date {
    val format = SimpleDateFormat("yyyy-MM-dd")
    return format.parse(this)
}

fun getCurrentDate(): String {
    val format = SimpleDateFormat("yyyy-MM-dd")
    return format.format(Date())
}