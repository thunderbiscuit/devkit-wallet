/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet.ui.wallet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.goldenraven.devkitwallet.R
import com.goldenraven.devkitwallet.ui.DevkitWalletAppBar
import com.goldenraven.devkitwallet.ui.Screen
import com.goldenraven.devkitwallet.ui.theme.DevkitWalletColors
import com.goldenraven.devkitwallet.ui.theme.firaMono

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReceiveScreen() {
    Scaffold(
        topBar = { DevkitWalletAppBar() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DevkitWalletColors.night4),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_testnet_logo),
                contentDescription = "Bitcoin testnet logo",
                Modifier.size(90.dp)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = "Receive Screen",
                color = DevkitWalletColors.snow3,
                fontSize = 28.sp,
                fontFamily = firaMono,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(device = Devices.PIXEL_4, showBackground = true)
@Composable
internal fun PreviewReceiveScreen() {
    ReceiveScreen()
}