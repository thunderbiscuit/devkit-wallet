/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet.ui

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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.goldenraven.devkitwallet.R
import com.goldenraven.devkitwallet.ui.theme.DevkitWalletColors
import com.goldenraven.devkitwallet.ui.theme.firaMono

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AboutScreen(navController: NavController) {
    Scaffold(
        topBar = { AwayFromHomeAppBar(navController, "About") }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DevkitWalletColors.night4),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.bdk_logo),
                contentDescription = "Bitcoin testnet logo",
                Modifier.size(270.dp)
            )
            Spacer(modifier = Modifier.padding(24.dp))
            Text(
                text = "This wallet is build for developers to learn how to leverage the bitcoindevkit. You are currently running the \"UIOnly\" version of the app, which doesn't have any bitcoin-related functionality.",
                color = DevkitWalletColors.snow3,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                 modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Preview(device = Devices.PIXEL_4, showBackground = true)
@Composable
internal fun PreviewAboutScreen() {
    AboutScreen(rememberNavController())
}