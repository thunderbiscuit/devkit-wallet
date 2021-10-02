/*
 * Copyright 2021 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.bdksampleapp

import android.app.Application
import android.content.Context
import com.goldenraven.bdksampleapp.utilities.SharedPreferencesManager
import com.goldenraven.bdksampleapp.data.Repository
import com.goldenraven.bdksampleapp.data.Wallet

class BdkSampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // initialize Wallet object with path variable
        Wallet.setPath(applicationContext.filesDir.toString())

        // initialize shared preferences manager object (singleton)
        val sharedPreferencesManager = SharedPreferencesManager(
                applicationContext.getSharedPreferences(
                    "current_wallet",
                    Context.MODE_PRIVATE
                )
            )

        // initialize Repository object with shared preferences
        Repository.setSharedPreferences(sharedPreferencesManager)
    }
}