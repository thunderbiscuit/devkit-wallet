/*
 * Copyright 2021 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet

import android.app.Application
import android.content.Context
import com.goldenraven.devkitwallet.utilities.SharedPreferencesManager
import com.goldenraven.devkitwallet.data.Repository
import com.goldenraven.devkitwallet.data.Wallet

class DevkitWalletApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // initialize Wallet object (singleton) with path variable
        Wallet.setPath(applicationContext.filesDir.toString())

        // initialize shared preferences manager object (singleton)
        val sharedPreferencesManager = SharedPreferencesManager(
            sharedPreferences = applicationContext.getSharedPreferences("current_wallet", Context.MODE_PRIVATE)
        )

        // initialize Repository object with shared preferences
        Repository.setSharedPreferences(sharedPreferencesManager)
    }
}
