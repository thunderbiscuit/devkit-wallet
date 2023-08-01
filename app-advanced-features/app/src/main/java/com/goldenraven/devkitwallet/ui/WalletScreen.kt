/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.goldenraven.devkitwallet.R
import com.goldenraven.devkitwallet.ui.theme.DevkitWalletColors
import com.goldenraven.devkitwallet.ui.theme.firaMono
import com.goldenraven.devkitwallet.ui.theme.firaMonoMedium
import com.goldenraven.devkitwallet.ui.wallet.WalletNavigation
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
                    .background(color = DevkitWalletColors.frost4)
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
                    color = DevkitWalletColors.snow1
                )
                Spacer(modifier = Modifier.padding(16.dp))
                Row(
                    // verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Version:  ",
                        color = DevkitWalletColors.snow1,
                    )
                    Column() {
                        Text(
                            // text = "\uD83D\uDE0E UIOnly",
                            text = "   UIOnly",
                            color = Color(0x60eceff4),
                            fontFamily = firaMono
                        )
                        Text(
                            text = "   SimpleWallet",
                            color = Color(0x60eceff4),
                            fontStyle = FontStyle.Normal,
                            fontFamily = firaMono
                        )
                        Text(
                            text = "\uD83D\uDE0E AdvancedFeatures",
                            color = DevkitWalletColors.snow1,
                            fontFamily = firaMono
                        )
                    }
                }
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
                    selectedTextColor = DevkitWalletColors.snow1,
                    unselectedTextColor = DevkitWalletColors.snow1
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
                    selectedTextColor = DevkitWalletColors.snow1,
                    unselectedTextColor = DevkitWalletColors.snow1
                )
            )
            NavigationDrawerItem(
                label = { Text("Electrum Server") },
                selected = items[2] == selectedItem.value,
                onClick = { navController.navigate(Screen.ElectrumScreen.route) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = DevkitWalletColors.night4,
                    unselectedContainerColor = DevkitWalletColors.night4,
                    selectedTextColor = DevkitWalletColors.snow1,
                    unselectedTextColor = DevkitWalletColors.snow1
                )
            )
        },
        content = {
            Scaffold(
                topBar = { WalletAppBar(scope, drawerState) },
            ) { padding ->
                WalletNavigation(padding)
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
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = "Open drawer",
                    tint = DevkitWalletColors.snow1
                )
            }
        },
        actions = {  }
    )
}

@Composable
internal fun AppTitle() {
    Text(
        text = "Devkit Wallet",
        color = DevkitWalletColors.snow1,
        // fontFamily = firaMonoMedium,
        // fontSize = 20.sp
    )
}
