/*
 * Copyright 2021-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet.ui

sealed class Screen(val route: String) {
    // create wallet screens
    object WalletChoiceScreen : Screen("wallet_choice_screen")
    object WalletRecoveryScreen : Screen("wallet_recovery_screen")

    // home screens
    object WalletScreen : Screen("wallet_screen")
    object AboutScreen : Screen("about_screen")
    object RecoveryPhraseScreen : Screen("recovery_phrase_screen")
    object ElectrumScreen : Screen("electrum_screen")

    // wallet screens
    object HomeScreen : Screen("home_screen")
    object SendScreen : Screen("send_screen")
    object ReceiveScreen : Screen("receive_screen")
    object RBFScreen : Screen("rbf_screen")
    object TransactionsScreen : Screen("transactions_screen")
    object TransactionScreen : Screen("transaction_screen")
}
