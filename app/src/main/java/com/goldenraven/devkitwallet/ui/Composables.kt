/*
 * Copyright 2021-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.goldenraven.devkitwallet.R
import com.goldenraven.devkitwallet.ui.theme.DevkitWalletColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DevkitWalletAppBar() {
    SmallTopAppBar(
        title = { IntroAppTitle() },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = DevkitWalletColors.night1),
        actions = { }
    )
}

@Composable
internal fun IntroAppTitle() {
    Text(
        text = stringResource(R.string.app_name),
        color = DevkitWalletColors.snow3,
        // style = TextStyle(
        //     fontFamily = firaMono,
        //     fontSize = 20.sp
        // )
    )
}
