/*
 * Copyright 2021 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.bdksampleapp.wallet

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.goldenraven.bdksampleapp.data.Wallet

class WalletViewModel(application: Application) : AndroidViewModel(application) {

    public var balance: MutableLiveData<ULong> = MutableLiveData(0u)

    public fun updateBalance() {
        Wallet.sync(100u)
        val newBalance = Wallet.getBalance()
        Log.i("BDK Sample App", "New balance is $newBalance")
        balance.postValue(newBalance)
    }
}
