/*
 * Copyright 2021 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.bdksampleapp.data

import android.content.SharedPreferences
import android.util.Log

object Repository {

    // shared preferences are a way to save/retrieve small pieces of data without building a database
    private lateinit var sharedPreferences: SharedPreferences

    fun setSharedPreferences(sharedPref: SharedPreferences) {
        sharedPreferences = sharedPref
    }

    // take a look at shared preferences and see if the user already has a wallet saved on device
    fun doesWalletExist(): Boolean {
        val walletInitialized: Boolean = sharedPreferences.getBoolean("initialized", false)
        Log.i("SobiWallet","Value of walletInitialized at launch: $walletInitialized")
        return walletInitialized
    }

    // save the necessary data for wallet reconstruction in shared preferences
    // upon application launch, the wallet can initialize itself using that data
    fun saveWallet(path: String, descriptor: String, changeDescriptor: String) {
        Log.i("SobiWallet", "Saved wallet:\npath -> $path \ndescriptor -> $descriptor \nchange descriptor -> $changeDescriptor")
        val editor = sharedPreferences.edit()
        editor.putBoolean("initialized", true)
        editor.putString("path", path)
        editor.putString("descriptor", descriptor)
        editor.putString("changeDescriptor", changeDescriptor)
        editor.apply()
    }

    fun saveMnemonic(mnemonic: String) {
        Log.i("SobiWallet", "The recovery phrase is: $mnemonic")
        val editor = sharedPreferences.edit()
        editor.putString("mnemonic", mnemonic)
        editor.apply()
    }

    fun getInitialWalletData(): RequiredInitialWalletData {
        val descriptor: String = sharedPreferences.getString("descriptor", null)!!
        val changeDescriptor: String = sharedPreferences.getString("changeDescriptor", null)!!
        return RequiredInitialWalletData(descriptor, changeDescriptor)
    }
}

data class RequiredInitialWalletData(
    val descriptor: String,
    val changeDescriptor: String
)
