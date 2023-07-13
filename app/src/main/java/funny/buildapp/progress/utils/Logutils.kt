package funny.buildapp.progress.utils

import android.util.Log

fun Any?.loge(tag: String = "funny") {
    Log.e(tag, this.toString())
}