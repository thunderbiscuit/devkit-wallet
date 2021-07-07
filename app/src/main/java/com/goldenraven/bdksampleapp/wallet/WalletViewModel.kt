package com.goldenraven.bdksampleapp.wallet

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.goldenraven.bdksampleapp.data.Wallet

class WalletViewModel(application: Application) : AndroidViewModel(application) {

    public var balance: MutableLiveData<Long> = MutableLiveData(0)

    public fun updateBalance() {
        Wallet.sync(100)
        val newBalance = Wallet.getBalance()
        Log.i("BDK Sample App", "New balance is $newBalance")
        balance.postValue(newBalance)
    }
}
