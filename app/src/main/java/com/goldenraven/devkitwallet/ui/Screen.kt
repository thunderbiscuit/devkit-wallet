/*
 * Copyright 2021-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet.ui

sealed class Screen(val route: String) {
    // create wallet screens
    object WalletChoiceScreen : Screen("wallet_choice_screen")
    object WalletRecoveryScreen : Screen("wallet_recovery_screen")

    // wallet screens
    // object HomeScreen : Screen("home_screen")
    // object SendScreen : Screen("home_screen")
    // object ReceiveScreen : Screen("home_screen")
    // object TransactionsScreen : Screen("home_screen")

    // drawer screens
    // object AboutScreen : Screen("home_screen")
    // object RecoveryPhraseScreen : Screen("home_screen")
}
