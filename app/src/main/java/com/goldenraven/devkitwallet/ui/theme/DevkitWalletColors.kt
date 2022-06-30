/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet.ui.theme

import androidx.compose.ui.graphics.Color

// the colors are taken for the Nord theme
// https://www.nordtheme.com/

// snow and night colors go from lightest to darkest
// e.g. snow1 is lighter than snow 2, etc.

object DevkitWalletColors {
    val night1: Color = Color(0xFF4c566a)
    val night2: Color = Color(0xFF434c5e)
    val night3: Color = Color(0xFF3b4252)
    val night4: Color = Color(0xFF2e3440)

    // most text uses snow1
    val snow1: Color = Color(0xFFeceff4)
    val snow1Disabled: Color = Color(0x70eceff4)
    val snow3: Color = Color(0xFFd8dee9)


    val frost4: Color = Color(0xFF5e81ac)
    val frost4Disabled: Color = Color(0x305e81ac)
    val auroraGreen: Color = Color(0xFFa3be8c)
    val auroraRed: Color = Color(0xFFbf616a)
    val auroraRedDisabled: Color = Color(0x30bf616a)
    val auroraYellow: Color = Color(0xFFebcb8b)
}