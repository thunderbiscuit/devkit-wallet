/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.goldenraven.devkitwallet.data.ElectrumSettings
import com.goldenraven.devkitwallet.data.Wallet
import com.goldenraven.devkitwallet.ui.theme.DevkitWalletColors
import com.goldenraven.devkitwallet.ui.theme.firaMono

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ElectrumScreen(navController: NavController) {
    val focusManager = LocalFocusManager.current
    val isBlockChainCreated = Wallet.isBlockChainCreated()
    val electrumServer: MutableState<String> = remember { mutableStateOf("") }
    val isChecked: MutableState<Boolean> = remember { mutableStateOf(false) }
    if (isBlockChainCreated) {
        electrumServer.value = Wallet.getElectrumURL()
        isChecked.value = Wallet.isElectrumServerDefault()
    }

    Scaffold(
        topBar = { AwayFromHomeAppBar(navController, "Electrum Server") },
        containerColor = DevkitWalletColors.night4
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Use default electrum URL",
                    color = DevkitWalletColors.snow1,
                    fontSize = 14.sp,
                    fontFamily = firaMono,
                    textAlign = TextAlign.Center,
                )
                Switch(
                    checked = isChecked.value,
                    onCheckedChange = {
                        isChecked.value = it
                        if (it) {
                            Wallet.setElectrumSettings(ElectrumSettings.DEFAULT)
                        } else {
                            Wallet.setElectrumSettings(ElectrumSettings.CUSTOM)
                        }
                    },
                    enabled = isBlockChainCreated
                )
            }

            OutlinedTextField(
                    value = electrumServer.value,
                    onValueChange = { electrumServer.value = it },
                    label = {
                        Text(
                            text = "Electrum Server",
                            color = DevkitWalletColors.snow1,
                        )
                    },
                    singleLine = true,
                    textStyle = TextStyle(fontFamily = firaMono, color = DevkitWalletColors.snow1),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = DevkitWalletColors.auroraGreen,
                        unfocusedBorderColor = DevkitWalletColors.snow1,
                        cursorColor = DevkitWalletColors.auroraGreen,
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                    }),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isBlockChainCreated && !isChecked.value
                )

            Button(
                onClick = {
                    Wallet.changeElectrumServer(electrumServer.value)
                    focusManager.clearFocus()
                },
                modifier = Modifier
                    .align(alignment = Alignment.End)
                    .padding(all = 8.dp),
                colors = ButtonDefaults.buttonColors(DevkitWalletColors.frost4),
                enabled = isBlockChainCreated && !isChecked.value
            ) {
                Text(
                    text = "Save",
                    color = DevkitWalletColors.snow1,
                    fontSize = 12.sp,
                    fontFamily = firaMono,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
