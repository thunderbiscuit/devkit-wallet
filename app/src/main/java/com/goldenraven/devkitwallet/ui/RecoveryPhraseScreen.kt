/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.goldenraven.devkitwallet.data.Repository
import com.goldenraven.devkitwallet.ui.theme.DevkitWalletColors
import com.goldenraven.devkitwallet.ui.theme.firaMono

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecoveryPhraseScreen(navController: NavController) {

    val seedPhrase: String = Repository.getMnemonic()
    val wordList: List<String> = seedPhrase.split(" ")

    Scaffold(
        topBar = { AwayFromHomeAppBar(navController, "Recovery Phrase") },
        containerColor = DevkitWalletColors.night4
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 32.dp)
        ) {
            wordList.forEachIndexed { index, item ->
                Text(
                    text = "${index + 1}. $item",
                    modifier = Modifier.weight(weight = 1F),
                    color = DevkitWalletColors.snow1,
                    fontFamily = firaMono
                )
            }
        }
    }
}

@Preview(device = Devices.PIXEL_4, showBackground = true)
@Composable
internal fun PreviewRecoveryPhraseScreen() {
    RecoveryPhraseScreen(rememberNavController())
}