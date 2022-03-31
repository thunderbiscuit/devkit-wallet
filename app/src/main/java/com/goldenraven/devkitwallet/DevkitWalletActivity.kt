/*
 * Copyright 2021-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import com.goldenraven.devkitwallet.ui.CreateWalletNavigation

class DevkitWalletActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val onBuildWalletButtonClicked: () -> Unit = {
            setContent {
                HomeNavigation()
            }
        }

        setContent {
            CreateWalletNavigation(onBuildWalletButtonClicked)
        }

    }
}
