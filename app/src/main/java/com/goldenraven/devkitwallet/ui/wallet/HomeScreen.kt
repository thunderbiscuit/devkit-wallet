/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet.ui.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.goldenraven.devkitwallet.ui.Screen
import com.goldenraven.devkitwallet.ui.theme.DevkitWalletColors
import com.goldenraven.devkitwallet.ui.theme.firaMono
import com.goldenraven.devkitwallet.ui.theme.firaMonoMedium


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DevkitWalletColors.night4),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.padding(24.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .background(color = DevkitWalletColors.night2)
                .height(110.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Balance here",
                fontFamily = firaMonoMedium,
                fontSize = 32.sp,
                color = DevkitWalletColors.snow3
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(DevkitWalletColors.frost1),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .height(80.dp)
                // .size(width = 300.dp, height = 70.dp)
                .fillMaxWidth(0.9f)
                .padding(vertical = 8.dp, horizontal = 8.dp)
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
        ) {
            Text(
                text = "sync",
                fontSize = 16.sp,
                fontFamily = firaMono,
                textAlign = TextAlign.Center,
                lineHeight = 28.sp,
            )
        }

        Button(
            onClick = { navController.navigate(Screen.TransactionsScreen.route) },
            colors = ButtonDefaults.buttonColors(DevkitWalletColors.frost1),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth(0.9f)
                .padding(vertical = 8.dp, horizontal = 8.dp)
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
        ) {
            Text(
                text = "transaction history",
                fontSize = 16.sp,
                fontFamily = firaMono,
                textAlign = TextAlign.Center,
                lineHeight = 28.sp,
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(140.dp).fillMaxWidth(0.9f)
        ) {
            Button(
                onClick = { navController.navigate(Screen.ReceiveScreen.route) },
                colors = ButtonDefaults.buttonColors(DevkitWalletColors.auroraGreen),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .height(160.dp)
                    .padding(vertical = 8.dp, horizontal = 8.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
            ) {
                Text(
                    text = "receive",
                    fontSize = 16.sp,
                    fontFamily = firaMono,
                    textAlign = TextAlign.End,
                    lineHeight = 28.sp,
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .align(Alignment.Bottom)
                )
            }

            Button(
                onClick = { navController.navigate(Screen.SendScreen.route) },
                colors = ButtonDefaults.buttonColors(DevkitWalletColors.auroraRed),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .height(160.dp)
                    .padding(vertical = 8.dp, horizontal = 8.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
            ) {
                Text(
                    text = "send",
                    fontSize = 16.sp,
                    fontFamily = firaMono,
                    textAlign = TextAlign.End,
                    lineHeight = 28.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Bottom)
                )
            }
        }
    }
}
