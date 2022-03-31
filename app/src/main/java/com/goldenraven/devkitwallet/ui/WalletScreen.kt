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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.goldenraven.devkitwallet.R
import com.goldenraven.devkitwallet.ui.Screen
import com.goldenraven.devkitwallet.ui.WalletNavigation
import com.goldenraven.devkitwallet.ui.theme.DevkitWalletColors
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
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

    // val navControllerWalletNavigation: NavHostController = rememberAnimatedNavController()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContainerColor = MaterialTheme.colorScheme.background,
        drawerContent = {
            Column(
                Modifier
                    .background(color = DevkitWalletColors.frost1)
                    .height(200.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_testnet_logo),
                    contentDescription = "Bitcoin testnet logo",
                    Modifier.size(90.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            NavigationDrawerItem(
                // icon = { Icon(Icons.Default.Face, contentDescription = null) },
                label = { Text("About") },
                selected = items[0] == selectedItem.value,
                onClick = { navController.navigate(Screen.AboutScreen.route) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = DevkitWalletColors.night1,
                    unselectedContainerColor = DevkitWalletColors.night1,
                )
            )
            NavigationDrawerItem(
                // icon = { painterResource(id = R.drawable.ic_baseline_tune_24) },
                label = { Text("Recovery Phrase") },
                selected = items[1] == selectedItem.value,
                onClick = { navController.navigate(Screen.RecoveryPhraseScreen.route) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = DevkitWalletColors.night1,
                    unselectedContainerColor = DevkitWalletColors.night1,
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
        actions = { }
    )
}

@Composable
internal fun AppTitle() {
    Text(
        text = stringResource(R.string.app_name),
        color = DevkitWalletColors.snow3
    )
}
