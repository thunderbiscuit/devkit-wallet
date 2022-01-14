/*
 * Copyright 2021 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.goldenraven.devkitwallet.data.Repository
import com.goldenraven.devkitwallet.data.Wallet
import com.goldenraven.devkitwallet.wallet.WalletActivity

class DispatchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ask Repository if a wallet already exists
        val walletInitialized: Boolean = Repository.doesWalletExist()

        // launch into wallet activity if user already has a Summer of Bitcoin Wallet saved on device
        if (walletInitialized) {
            Wallet.loadExistingWallet()
            startActivity(Intent(this, WalletActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, WalletChoiceActivity::class.java))
            finish()
        }
    }
}
