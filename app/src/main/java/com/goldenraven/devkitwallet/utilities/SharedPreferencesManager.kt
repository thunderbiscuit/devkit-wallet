
package com.goldenraven.devkitwallet.utilities

import android.content.SharedPreferences


private const val PREFS_WALLET_INITIALISED = "initialized"
private const val PREFS_PATH = "path"
private const val PREFS_DESCRIPTOR = "descriptor"
private const val PREFS_CHANGE_DESCRIPTOR = "changeDescriptor"
private const val PREFS_MNEMONIC = "mnemonic"

class SharedPreferencesManager(private val sharedPreferences: SharedPreferences) {

    var path: String
        get() = sharedPreferences.getString(PREFS_PATH, "") ?: ""
        set(value) {
            sharedPreferences.edit()?.putString(PREFS_PATH, value)?.apply()
        }

    var descriptor: String
        get() = sharedPreferences.getString(PREFS_DESCRIPTOR, "") ?: ""
        set(value) {
            sharedPreferences.edit()?.putString(PREFS_DESCRIPTOR, value)?.apply()
        }

    var changeDescriptor: String
        get() = sharedPreferences.getString(PREFS_CHANGE_DESCRIPTOR, "") ?: ""
        set(value) {
            sharedPreferences.edit()?.putString(PREFS_CHANGE_DESCRIPTOR, value)?.apply()
        }

    var mnemonic: String
        get() = sharedPreferences.getString(PREFS_MNEMONIC, "No seed phrase saved")
            ?: "Seed phrase not there"
        set(value) {
            sharedPreferences.edit()?.putString(PREFS_MNEMONIC, value)?.apply()
        }

    var walletInitialised: Boolean
        get() = sharedPreferences.getBoolean(PREFS_WALLET_INITIALISED, false)
        set(value) {
            sharedPreferences.edit()?.putBoolean(PREFS_WALLET_INITIALISED, value)?.apply()
        }
}
