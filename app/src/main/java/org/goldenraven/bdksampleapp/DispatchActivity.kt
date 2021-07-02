package com.goldenraven.bdksampleapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class DispatchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // not implemented yet
        // launch into wallet activity if user already has a Summer of Bitcoin Wallet on device
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
