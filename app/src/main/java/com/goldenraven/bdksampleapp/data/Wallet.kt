/*
 * Copyright 2021 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.bdksampleapp.data

import android.util.Log
import org.bitcoindevkit.bdkjni.*

object Wallet {

    private val lib: Lib
    private lateinit var path: String
    private lateinit var walletPtr: WalletPtr
    private val name: String = "bdk-wallet-0"
    private val electrumURL: String = "ssl://electrum.blockstream.info:60002"

    init {
        // load bitcoindevkit native library
        Lib.load()
        lib = Lib()
    }

    // setting the path requires the application context and is done once by the BdkSampleApplication class
    fun setPath(path: String): Unit {
        Wallet.path = path
    }

    fun createWallet(): Unit {
        val keys: ExtendedKey = generateExtendedKey()
        val descriptor: String = createDescriptor(keys)
        val changeDescriptor: String = createChangeDescriptor(keys)
        initialize(
            descriptor = descriptor,
            changeDescriptor = changeDescriptor,
        )
        Repository.saveWallet(path, descriptor, changeDescriptor)
        Repository.saveMnemonic(keys.mnemonic)
    }

    private fun generateExtendedKey(): ExtendedKey {
        return lib.generate_extended_key(Network.testnet, 12, "")
    }

    private fun createDescriptor(keys: ExtendedKey): String {
        Log.i("BDK Sample App", "Descriptor for receive addresses is wpkh(${keys.xprv}/84'/1'/0'/0/*)")
        return ("wpkh(" + keys.xprv + "/84'/1'/0'/0/*)")
    }

    private fun createChangeDescriptor(keys: ExtendedKey): String {
        Log.i("BDK Sample App", "Descriptor for change addresses is wpkh(${keys.xprv}/84'/1'/0'/1/*)")
        return ("wpkh(" + keys.xprv + "/84'/1'/0'/1/*)")
    }

    public fun loadExistingWallet(): Unit {
        val initialWalletData: RequiredInitialWalletData = Repository.getInitialWalletData()
        Log.i("BDK Sample App", "Loading existing wallet, descriptor is ${initialWalletData.descriptor}")
        Log.i("BDK Sample App", "Loading existing wallet, change descriptor is ${initialWalletData.changeDescriptor}")
        initialize(
            descriptor = initialWalletData.descriptor,
            changeDescriptor = initialWalletData.changeDescriptor,
        )
    }

    private fun initialize(
        descriptor: String,
        changeDescriptor: String,
    ): Unit {
        walletPtr = lib.constructor(
            WalletConstructor(
                name = name,
                network = Network.testnet,
                path = path,
                descriptor = descriptor,
                change_descriptor = changeDescriptor,
                electrum_url = electrumURL,
                electrum_proxy = null,
                electrum_retry = 10,
                electrum_timeout = null,
                electrum_stop_gap = 100,
            )
        )
    }

    fun recoverWallet(mnemonic: String) {
        val keys: ExtendedKey = restoreExtendedKeyFromMnemonic(mnemonic)
        val descriptor: String = createDescriptor(keys)
        val changeDescriptor: String = createChangeDescriptor(keys)
        initialize(
            descriptor = descriptor,
            changeDescriptor = changeDescriptor,
        )
        Repository.saveWallet(path, descriptor, changeDescriptor)
        Repository.saveMnemonic(keys.mnemonic)
    }

    private fun restoreExtendedKeyFromMnemonic(mnemonic: String): ExtendedKey {
        return lib.restore_extended_key(Network.testnet, mnemonic, "")
    }

    fun sync(max_address: Int?=null): Unit {
        lib.sync(walletPtr, max_address)
        Log.i("BDK Sample App", "Wallet successfully synced")
    }

    fun getNewAddress(): String {
        return lib.get_new_address(walletPtr)
    }

    fun getLastUnusedAddress(): String {
        return lib.get_last_unused_address(walletPtr)
    }

    fun getBalance(): Long {
        return lib.get_balance(walletPtr)
    }

    fun createTransaction(
        fee_rate: Float,
        addressees: List<Pair<String, String>>,
        send_all: Boolean? = false,
        utxos: List<String>? = null,
        unspendable: List<String>? = null,
        policy: Map<String, List<String>>? = null,
    ): CreateTxResponse {
        return lib.create_tx(walletPtr, fee_rate, addressees, send_all, utxos, unspendable, policy)
    }

    fun sign(psbt: String, assume_height: Int? = null): SignResponse {
        return lib.sign(walletPtr, psbt, assume_height)
    }

    fun extractPsbt(psbt: String): RawTransaction {
        return lib.extract_psbt(walletPtr, psbt)
    }

    fun broadcast(raw_tx: String): Txid {
        return lib.broadcast(walletPtr, raw_tx)
    }

    fun listTransactions(): List<TransactionDetails> {
        return lib.list_transactions(walletPtr, false)
    }
}