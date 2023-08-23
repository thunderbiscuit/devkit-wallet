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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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

@Composable
internal fun WalletScreen(navController: NavController) {

    val scope = rememberCoroutineScope()

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val items = listOf(Icons.Default.Favorite, Icons.Default.Face, Icons.Default.Email, Icons.Default.Face)
    val selectedItem = remember { mutableStateOf(items[0]) }


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = DevkitWalletColors.primary,
            ) {
                Column(
                    Modifier
                        .background(color = DevkitWalletColors.secondary)
                        .height(300.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_bitcoin_logo),
                        contentDescription = "Bitcoin testnet logo",
                        Modifier
                            .size(90.dp)
                            .padding(bottom = 16.dp)
                    )
                    Text(
                        text = "Devkit Wallet",
                        color = DevkitWalletColors.white,
                        fontFamily = firaMonoMedium,
                    )
                    Spacer(modifier = Modifier.padding(16.dp))
                    Row {
                        Text(
                            "Version:  ",
                            color = DevkitWalletColors.white,
                            fontFamily = firaMono
                        )
                        Column {
                            Text(
                                text = "   UIOnly",
                                color = Color(0x80ffffff),
                                fontFamily = firaMono
                            )
                            Text(
                                text = "\uD83D\uDE0E SimpleWallet",
                                color = DevkitWalletColors.white,
                                fontStyle = FontStyle.Normal,
                                fontFamily = firaMono
                            )
                            Text(
                                text = "   AdvancedFeatures",
                                color = Color(0x80ffffff),
                                fontFamily = firaMono
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                NavigationDrawerItem(
                    label = { Text(text = "About", fontFamily = firaMono) },
                    selected = items[0] == selectedItem.value,
                    onClick = { navController.navigate(Screen.AboutScreen.route) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = DevkitWalletColors.primary,
                        unselectedContainerColor = DevkitWalletColors.primary,
                        selectedTextColor = DevkitWalletColors.white,
                        unselectedTextColor = DevkitWalletColors.white
                    )
                )
                NavigationDrawerItem(
                    label = { Text(text = "Recovery Phrase", fontFamily = firaMono) },
                    selected = items[1] == selectedItem.value,
                    onClick = { navController.navigate(Screen.RecoveryPhraseScreen.route) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = DevkitWalletColors.primary,
                        unselectedContainerColor = DevkitWalletColors.primary,
                        selectedTextColor = DevkitWalletColors.white,
                        unselectedTextColor = DevkitWalletColors.white
                    )
                )
            }
        },
        content = {
            Scaffold(
                topBar = { WalletAppBar(scope, drawerState) },
            ) { innerPadding ->
                WalletNavigation(innerPadding)
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
internal fun WalletAppBar(scope: CoroutineScope, drawerState: DrawerState) {
    TopAppBar(
        title = { AppTitle() },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = DevkitWalletColors.primaryDark),
        navigationIcon = {
            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = "Open drawer",
                    tint = DevkitWalletColors.white
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
        color = DevkitWalletColors.white,
        fontFamily = firaMonoMedium,
        fontSize = 20.sp
    )
}
