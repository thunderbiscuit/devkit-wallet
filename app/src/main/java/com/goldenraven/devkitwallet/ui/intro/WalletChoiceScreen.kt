/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet.ui.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.goldenraven.devkitwallet.R
import com.goldenraven.devkitwallet.WalletCreateType
import com.goldenraven.devkitwallet.ui.AwayFromHomeAppBar
import com.goldenraven.devkitwallet.ui.IntroAppBar
import com.goldenraven.devkitwallet.ui.Screen
import com.goldenraven.devkitwallet.ui.theme.DevkitWalletColors
import com.goldenraven.devkitwallet.ui.theme.firaMono

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WalletChoiceScreen(
    navController: NavController,
    onBuildWalletButtonClicked: (WalletCreateType) -> Unit
) {

    Scaffold(
        topBar = { IntroAppBar() }
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(DevkitWalletColors.night4)
        ) {
            val (logo, create, recover) = createRefs()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 90.dp)
                    .constrainAs(logo) {
                        top.linkTo(parent.top)
                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_testnet_logo),
                    contentDescription = "Bitcoin testnet logo",
                    Modifier.size(90.dp)
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = stringResource(R.string.bitcoin_testnet),
                    color = DevkitWalletColors.snow3,
                    fontSize = 28.sp,
                    fontFamily = firaMono,
                )
            }

            Button(
                onClick = { onBuildWalletButtonClicked(WalletCreateType.FROMSCRATCH()) },
                colors = ButtonDefaults.buttonColors(DevkitWalletColors.frost1),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .size(width = 300.dp, height = 170.dp)
                    .padding(vertical = 8.dp, horizontal = 8.dp)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
                    .constrainAs(create) {
                        bottom.linkTo(recover.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                Text(
                    stringResource(R.string.create_new_wallet),
                    fontSize = 18.sp,
                    fontFamily = firaMono,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp,
                )
            }

            Button(
                onClick = { navController.navigate(Screen.WalletRecoveryScreen.route) },
                colors = ButtonDefaults.buttonColors(DevkitWalletColors.frost1),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .size(width = 300.dp, height = 170.dp)
                    .padding(vertical = 8.dp, horizontal = 8.dp)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
                    .constrainAs(recover) {
                        bottom.linkTo(parent.bottom, margin = 100.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                Text(
                    stringResource(R.string.recover_existing_wallet),
                    fontSize = 18.sp,
                    fontFamily = firaMono,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp,
                )
            }
        }
    }
}

// @Preview(device = Devices.PIXEL_4, showBackground = true)
// @Composable
// internal fun PreviewWalletChoiceScreen() {
//     WalletChoiceScreen(rememberNavController())
// }
