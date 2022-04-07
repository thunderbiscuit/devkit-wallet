/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet.ui.wallet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TransactionsScreen(navController: NavController) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(DevkitWalletColors.night4)
            .padding(bottom = 24.dp)
    ) {
        val (screenTitle, transactions, bottomButton) = createRefs()
        Text(
            text = "Transaction History",
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
            modifier = Modifier.constrainAs(transactions) {
                top.linkTo(screenTitle.bottom)
                bottom.linkTo(bottomButton.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.fillToConstraints
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(DevkitWalletColors.night1)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    "Pending",
                    fontSize = 18.sp,
                    fontFamily = firaMono,
                    color = DevkitWalletColors.snow1
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(70.dp)
                    .background(DevkitWalletColors.night2)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    "List of transactions",
                    fontSize = 12.sp,
                    fontFamily = firaMono,
                    color = DevkitWalletColors.snow1
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(DevkitWalletColors.night1)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    "Confirmed",
                    fontSize = 18.sp,
                    fontFamily = firaMono,
                    color = DevkitWalletColors.snow1
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(70.dp)
                    .background(DevkitWalletColors.night2)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    "List of transactions",
                    fontSize = 12.sp,
                    fontFamily = firaMono,
                    color = DevkitWalletColors.snow1
                )
            }
        }

        Button(
            onClick = { navController.navigate(Screen.HomeScreen.route) },
            colors = ButtonDefaults.buttonColors(DevkitWalletColors.frost1),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth(0.9f)
                .padding(vertical = 8.dp, horizontal = 8.dp)
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                .constrainAs(bottomButton) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
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

@Preview(device = Devices.PIXEL_4, showBackground = true)
@Composable
internal fun PreviewTransactionsScreen() {
    TransactionsScreen(rememberNavController())
}