/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet.ui.wallet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.goldenraven.devkitwallet.R
import com.goldenraven.devkitwallet.ui.Screen
import com.goldenraven.devkitwallet.ui.theme.DevkitWalletColors
import com.goldenraven.devkitwallet.ui.theme.firaMono
import com.google.android.material.textfield.TextInputEditText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SendScreen(navController: NavController) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(DevkitWalletColors.night4)
    ) {
        val (screenTitle, transactionInputs, bottomButtons) = createRefs()
        Text(
            text = "Send Bitcoin",
            color = DevkitWalletColors.snow3,
            fontSize = 28.sp,
            fontFamily = firaMono,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .constrainAs(screenTitle) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(top = 70.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.constrainAs(transactionInputs) {
                top.linkTo(screenTitle.bottom)
                bottom.linkTo(bottomButtons.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.fillToConstraints
            }
        ) {
            TransactionRecipientInput()
            TransactionAmountInput()
            TransactionFeeInput()
        }

        Column(
            Modifier
                .constrainAs(bottomButtons) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(bottom = 24.dp)
        ) {
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(DevkitWalletColors.auroraRed),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .size(width = 300.dp, height = 70.dp)
                    .padding(vertical = 8.dp, horizontal = 8.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
            ) {
                Text(
                    text = "broadcast transaction",
                    fontSize = 14.sp,
                    fontFamily = firaMono,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp,
                )
            }
            Button(
                onClick = { navController.navigate(Screen.HomeScreen.route) },
                colors = ButtonDefaults.buttonColors(DevkitWalletColors.frost1),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .size(width = 300.dp, height = 70.dp)
                    // .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 8.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
            ) {
                Text(
                    text = "back to wallet",
                    fontSize = 14.sp,
                    fontFamily = firaMono,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp,
                )
            }
        }
    }
}

@Composable
private fun TransactionRecipientInput() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        var text by remember { mutableStateOf("") }

        OutlinedTextField(
            modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(0.8f),
            value = text,
            onValueChange = { text = it },
            label = {
                Text(
                    text = "Recipient address",
                    color = DevkitWalletColors.night1,
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = DevkitWalletColors.auroraGreen,
                unfocusedBorderColor = DevkitWalletColors.snow3,
            ),
        )
    }
}

@Composable
private fun TransactionAmountInput() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        var text by remember { mutableStateOf("") }

        OutlinedTextField(
            modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(0.8f),
            value = text,
            onValueChange = { text = it },
            label = {
                Text(
                    text = "Amount",
                    color = DevkitWalletColors.night1,
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = DevkitWalletColors.auroraGreen,
                unfocusedBorderColor = DevkitWalletColors.snow3,
            ),
        )
    }
}

@Composable
private fun TransactionFeeInput() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        var text by remember { mutableStateOf("") }

        OutlinedTextField(
            modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(0.8f),
            value = text,
            onValueChange = { text = it },
            label = {
                Text(
                    text = "Fee rate",
                    color = DevkitWalletColors.night1,
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = DevkitWalletColors.auroraGreen,
                unfocusedBorderColor = DevkitWalletColors.snow3,
            ),
        )
    }
}

@Preview(device = Devices.PIXEL_4, showBackground = true)
@Composable
internal fun PreviewSendScreen() {
    SendScreen(rememberNavController())
}
