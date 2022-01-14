/*
 * Copyright 2021 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet.utilities

import android.graphics.Color
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.goldenraven.devkitwallet.R

fun showSnackbar(view: View, level: SnackbarLevel, message: String): Unit {

    // set background
    val background = when (level) {
        SnackbarLevel.SUCCESS -> ResourcesCompat.getDrawable(view.resources, R.drawable.background_snackbar_success, null)
        SnackbarLevel.ERROR -> ResourcesCompat.getDrawable(view.resources, R.drawable.background_snackbar_error, null)
    }

    val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
    snackBar.setTextColor(Color.argb(255, 236, 239, 244))
    snackBar.view.background = background
    snackBar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
    snackBar.show()
}

enum class SnackbarLevel {
    SUCCESS,
    ERROR,
}
