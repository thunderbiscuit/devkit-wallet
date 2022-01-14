/*
 * Copyright 2021 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet.utilities

import android.text.format.DateFormat
import java.util.Calendar
import java.util.Locale

// extension function on the ULong timestamp provided in the Transaction.Confirmed type
fun ULong.timestampToString(): String {
    val calendar = Calendar.getInstance(Locale.ENGLISH)
    calendar.timeInMillis = (this * 1000u).toLong()
    return DateFormat.format("MMMM d yyyy HH:mm", calendar).toString()
}
