/*
 * Copyright 2021-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import com.goldenraven.devkitwallet.data.Repository
import com.goldenraven.devkitwallet.data.Wallet
import com.goldenraven.devkitwallet.ui.HomeNavigation
import com.goldenraven.devkitwallet.ui.intro.CreateWalletNavigation
import com.goldenraven.devkitwallet.utilities.TAG

class DevkitWalletActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val onBuildWalletButtonClicked: (WalletCreateType) -> Unit = { walletCreateType ->
            try {
                // load up a wallet either from scratch or using a BIP39 recovery phrase
                when (walletCreateType) {
                    // if we create a wallet from scratch we don't need a recovery phrase
                    is WalletCreateType.FROMSCRATCH -> Wallet.createWallet()

                    is WalletCreateType.RECOVER -> Wallet.recoverWallet(walletCreateType.recoveryPhrase)
                }
                setContent {
                    HomeNavigation()
                }
            } catch(e: Throwable) {
                Log.i(TAG, "Could not build wallet: $e")
            }
        }

        if (Repository.doesWalletExist()) {
            Wallet.loadExistingWallet()
            setContent {
                HomeNavigation()
            }
        } else {
            setContent {
                CreateWalletNavigation(onBuildWalletButtonClicked)
            }
        }
    }
}

sealed class WalletCreateType {
    object FROMSCRATCH : WalletCreateType()
    class RECOVER(val recoveryPhrase: String) : WalletCreateType()
}
