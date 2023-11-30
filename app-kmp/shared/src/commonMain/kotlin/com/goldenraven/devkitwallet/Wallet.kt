package com.goldenraven.devkitwallet

interface FFIWallet {
    fun getBalance(): Balance
}

interface Balance {
    val immature: ULong
    val trusted_pending: ULong
    val untrusted_pending: ULong
    val confirmed: ULong
    val spendable: ULong
    val total: ULong
}

class CommonCodeWallet(private val ffiWallet: FFIWallet) {
    fun getBalance(): Balance {
        return ffiWallet.getBalance()
    }
}
