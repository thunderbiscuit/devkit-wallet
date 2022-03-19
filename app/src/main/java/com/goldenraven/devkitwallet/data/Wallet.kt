/*
 * Copyright 2021 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet.data

import android.util.Log
import com.goldenraven.devkitwallet.utilities.TAG
import org.bitcoindevkit.*
import org.bitcoindevkit.Wallet as BdkWallet


object Wallet {

    private lateinit var wallet: BdkWallet
    private lateinit var path: String
    private const val electrumURL: String = "ssl://electrum.blockstream.info:60002"

    object LogProgress: BdkProgress {
        override fun update(progress: Float, message: String?) {
            Log.i(TAG, "Sync wallet")
        }
    }

    // setting the path requires the application context and is done once by the BdkSampleApplication class
    fun setPath(path: String) {
        Wallet.path = path
    }

    private fun initialize(
        descriptor: String,
        changeDescriptor: String,
    ): Unit {
        val database = DatabaseConfig.Sqlite(SqliteDbConfiguration("$path/bdk-sqlite"))
        val blockchain = BlockchainConfig.Electrum(ElectrumConfig(electrumURL, null, 5u, null, 10u))
        wallet = BdkWallet(
            descriptor,
            changeDescriptor,
            Network.TESTNET,
            database,
            blockchain
        )
    }

    fun createWallet(): Unit {
        val keys: ExtendedKeyInfo = generateExtendedKey(Network.TESTNET, WordCount.WORDS12, null)
        val descriptor: String = createDescriptor(keys)
        val changeDescriptor: String = createChangeDescriptor(keys)
        initialize(
            descriptor = descriptor,
            changeDescriptor = changeDescriptor,
        )
        Repository.saveWallet(path, descriptor, changeDescriptor)
        Repository.saveMnemonic(keys.mnemonic)
    }

    // only create BIP84 compatible wallets
    private fun createDescriptor(keys: ExtendedKeyInfo): String {
        Log.i(TAG, "Descriptor for receive addresses is wpkh(${keys.xprv}/84'/1'/0'/0/*)")
        return ("wpkh(" + keys.xprv + "/84'/1'/0'/0/*)")
    }

    private fun createChangeDescriptor(keys: ExtendedKeyInfo): String {
        Log.i(TAG, "Descriptor for change addresses is wpkh(${keys.xprv}/84'/1'/0'/1/*)")
        return ("wpkh(" + keys.xprv + "/84'/1'/0'/1/*)")
    }

    // if the wallet already exists, its descriptors are stored in shared preferences
    fun loadExistingWallet(): Unit {
        val initialWalletData: RequiredInitialWalletData = Repository.getInitialWalletData()
        Log.i(TAG, "Loading existing wallet, descriptor is ${initialWalletData.descriptor}")
        Log.i(TAG, "Loading existing wallet, change descriptor is ${initialWalletData.changeDescriptor}")
        initialize(
            descriptor = initialWalletData.descriptor,
            changeDescriptor = initialWalletData.changeDescriptor,
        )
    }

    fun recoverWallet(mnemonic: String) {
        val keys: ExtendedKeyInfo = restoreExtendedKey(Network.TESTNET, mnemonic, null)
        val descriptor: String = createDescriptor(keys)
        val changeDescriptor: String = createChangeDescriptor(keys)
        initialize(
            descriptor = descriptor,
            changeDescriptor = changeDescriptor,
        )
        Repository.saveWallet(path, descriptor, changeDescriptor)
        Repository.saveMnemonic(keys.mnemonic)
    }

    fun createTransaction(recipient: String, amount: ULong, fee_rate: Float?): PartiallySignedBitcoinTransaction {
        return PartiallySignedBitcoinTransaction(wallet, recipient, amount, fee_rate)
    }

    fun sign(psbt: PartiallySignedBitcoinTransaction) {
        wallet.sign(psbt)
    }

    fun broadcast(signedPsbt: PartiallySignedBitcoinTransaction): String {
        return wallet.broadcast(signedPsbt)
    }

    fun getTransactions(): List<Transaction> = wallet.getTransactions()

    fun sync(maxAddress: UInt?=null): Unit {
        wallet.sync(LogProgress, maxAddress)
    }

    fun getBalance(): ULong = wallet.getBalance()

    fun getNewAddress(): String = wallet.getNewAddress()

    fun getLastUnusedAddress(): String = wallet.getLastUnusedAddress()
}