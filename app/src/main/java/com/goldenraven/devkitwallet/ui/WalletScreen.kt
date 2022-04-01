/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.goldenraven.devkitwallet.R
import com.goldenraven.devkitwallet.ui.theme.DevkitWalletColors
import com.goldenraven.devkitwallet.ui.theme.firaMono
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.animation.ExperimentalAnimationApi::class)
@Composable
internal fun WalletScreen(navController: NavController) {

    val scope = rememberCoroutineScope()

    @OptIn(ExperimentalMaterial3Api::class)
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val items = listOf(Icons.Default.Favorite, Icons.Default.Face, Icons.Default.Email, Icons.Default.Face)
    val selectedItem = remember { mutableStateOf(items[0]) }


    ModalNavigationDrawer (
        drawerState = drawerState,
        drawerContainerColor = DevkitWalletColors.night4,
        drawerContent = {
            Column(
                Modifier
                    .background(color = DevkitWalletColors.frost1)
                    .height(300.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_testnet_logo),
                    contentDescription = "Bitcoin testnet logo",
                    Modifier
                        .size(90.dp)
                        .padding(bottom = 16.dp)
                )
                Text(
                    text = "Devkit Wallet",
                    color = DevkitWalletColors.snow3
                )
                Spacer(modifier = Modifier.padding(16.dp))
                Row(
                    // verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Version:  ",
                        color = DevkitWalletColors.snow3,
                    )
                    Column() {
                        Text(
                            text = "\uD83D\uDE0E UIOnly",
                            color = DevkitWalletColors.snow3,
                            fontFamily = firaMono
                        )
                        Text(
                            text = "\uD83D\uDE0E SimpleWallet",
                            color = Color(0x60eceff4),
                            // color = DevkitWalletColors.snow3,
                            fontStyle = FontStyle.Normal,
                            fontFamily = firaMono
                        )
                        Text(
                            text = "\uD83D\uDE0E AdvancedWallet",
                            color = Color(0x60eceff4),
                            // color = DevkitWalletColors.snow3,
                            fontFamily = firaMono
                        )
                    }
                }
                // Text(
                //     buildAnnotatedString {
                //         withStyle(style = SpanStyle(color = DevkitWalletColors.snow3)) {
                //             append("Version: ")
                //         }
                //         withStyle(style = SpanStyle(
                //             color = Color(0xFF2e3440),
                //             fontStyle = FontStyle.Italic,
                //             fontFamily = firaMonoMedium
                //         )) {
                //             append("UIOnly")
                //         }
                //     }
                // )
            }
            Spacer(modifier = Modifier.height(16.dp))
            NavigationDrawerItem(
                label = { Text("About") },
                selected = items[0] == selectedItem.value,
                onClick = { navController.navigate(Screen.AboutScreen.route) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = DevkitWalletColors.night4,
                    unselectedContainerColor = DevkitWalletColors.night4,
                    selectedTextColor = DevkitWalletColors.snow3,
                    unselectedTextColor = DevkitWalletColors.snow3
                )
            )
            NavigationDrawerItem(
                label = { Text("Recovery Phrase") },
                selected = items[1] == selectedItem.value,
                onClick = { navController.navigate(Screen.RecoveryPhraseScreen.route) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = DevkitWalletColors.night4,
                    unselectedContainerColor = DevkitWalletColors.night4,
                    selectedTextColor = DevkitWalletColors.snow3,
                    unselectedTextColor = DevkitWalletColors.snow3
                )
            )
        },
        content = {
            Scaffold(
                topBar = { WalletAppBar(scope, drawerState) },
            ) {
                WalletNavigation()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WalletAppBar(scope: CoroutineScope, drawerState: DrawerState) {
    SmallTopAppBar(
        title = { AppTitle() },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = DevkitWalletColors.night1),
        navigationIcon = {
            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Open drawer",
                    tint = DevkitWalletColors.snow3
                )
            }
        },
        actions = {  }
    )
}

@Composable
internal fun AppTitle() {
    Text(
        text = stringResource(R.string.app_name),
        color = DevkitWalletColors.snow3
    )
}
