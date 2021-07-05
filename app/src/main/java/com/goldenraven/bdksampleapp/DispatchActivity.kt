/*
 * Copyright 2021 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.bdksampleapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class DispatchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // not implemented yet
        // launch into wallet activity if user already has a Bitcoindevkit Sample App wallet on device
        // if (currentWalletExists) {
        //     startActivity(Intent(this, WalletActivity::class.java))
        //     finish()
        // } else {
        //     startActivity(Intent(this, IntroActivity::class.java))
        //     finish()
        // }

        startActivity(Intent(this, WalletChoiceActivity::class.java))
        finish()
    }
}
