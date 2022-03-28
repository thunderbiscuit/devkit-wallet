/*
 * Copyright 2021 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet.old

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.Navigation
import com.goldenraven.devkitwallet.R

class WalletActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    // override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    //     menuInflater.inflate(R.menu.overflow_menu, menu)
    //     return true
    // }

    // override fun onOptionsItemSelected(item: MenuItem): Boolean {
    //     val navController = Navigation.findNavController(this, R.id.navHostWallet)
    //     when (item.itemId) {
    //         R.id.menu_recovery_phrase -> {
    //             navController.navigate(R.id.action_walletFragment_to_settingsFragment)
    //             return true
    //         }
    //         R.id.menu_about -> {
    //             navController.navigate(R.id.action_walletFragment_to_aboutFragment)
    //             return true
    //         }
    //         else -> {
    //             Log.i("WalletActivity", "Overflow menu choice didn't match available options")
    //             return true
    //         }
    //     }
    // }
}