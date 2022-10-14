/*
 * Copyright 2021 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet.data

import android.util.Log
import com.goldenraven.devkitwallet.utilities.SharedPreferencesManager
import com.goldenraven.devkitwallet.utilities.TAG
import java.io.Externalizable

object Repository {

    // shared preferences are a way to save/retrieve small pieces of data without building a database
    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    fun setSharedPreferences(sharedPrefManager: SharedPreferencesManager) {
        sharedPreferencesManager = sharedPrefManager
    }

    // take a look at shared preferences and see if the user already has a wallet saved on device
    fun doesWalletExist(): Boolean {
        val walletInitialized: Boolean = sharedPreferencesManager.walletInitialised
        Log.i(TAG, "Value of walletInitialized at launch: $walletInitialized")
        return walletInitialized
    }

    // save the necessary data for wallet reconstruction in shared preferences
    // upon application launch, the wallet can initialize itself using that data
    fun saveWallet(path: String, externalDescriptor: String, internalDescriptor: String) {
        Log.i(
            TAG,
            "Saved wallet:\npath -> $path \nexternal descriptor -> $externalDescriptor \ninternal descriptor -> $internalDescriptor"
        )
        sharedPreferencesManager.walletInitialised = true
        sharedPreferencesManager.path = path
        sharedPreferencesManager.externalDescriptor = externalDescriptor
        sharedPreferencesManager.internalDescriptor = internalDescriptor
    }

    fun saveMnemonic(mnemonic: String) {
        Log.i(TAG, "The recovery phrase is: $mnemonic")
        sharedPreferencesManager.mnemonic = mnemonic
    }

    fun getMnemonic(): String {
        return sharedPreferencesManager.mnemonic
    }

    fun getInitialWalletData(): RequiredInitialWalletData {
        val externalDescriptor: String = sharedPreferencesManager.externalDescriptor
        val internalDescriptor: String = sharedPreferencesManager.internalDescriptor
        return RequiredInitialWalletData(externalDescriptor, internalDescriptor)
    }
}

data class RequiredInitialWalletData(
    val externalDescriptor: String,
    val internalDescriptor: String
)
