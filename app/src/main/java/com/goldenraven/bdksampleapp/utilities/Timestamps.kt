
package com.goldenraven.bdksampleapp.utilities

import android.text.format.DateFormat
import java.util.Calendar
import java.util.Locale

fun timestampToString(unixTimestamp: Long): String {
    val calendar = Calendar.getInstance(Locale.ENGLISH)
    calendar.timeInMillis = unixTimestamp * 1000
    return DateFormat.format("MMMM d yyyy HH:mm", calendar).toString()
}
