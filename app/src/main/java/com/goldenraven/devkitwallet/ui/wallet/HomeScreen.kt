package com.goldenraven.devkitwallet.ui.wallet

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.goldenraven.devkitwallet.R
import com.goldenraven.devkitwallet.ui.DevkitWalletAppBar
import com.goldenraven.devkitwallet.ui.theme.DevkitWalletColors
import com.goldenraven.devkitwallet.ui.theme.firaMono


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DevkitWalletColors.night4),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_testnet_logo),
            contentDescription = "Bitcoin testnet logo",
            Modifier.size(90.dp)
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = "Home Screen",
            color = DevkitWalletColors.snow3,
            fontSize = 28.sp,
            fontFamily = firaMono,
            textAlign = TextAlign.Center
        )
    }
}
