/*
 * Copyright 2021 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.bdksampleapp.data

import android.util.Log
import org.bitcoindevkit.bdkjni.Lib
import org.bitcoindevkit.bdkjni.Types.*

object Wallet {

    private val lib: Lib
    private lateinit var path: String
    private lateinit var walletPtr: WalletPtr
    private val name: String = "sobi-wallet-0"
    private val electrumURL: String = "ssl://electrum.blockstream.info:60002"

    init {
        // load bitcoindevkit native library
        Lib.load()
        lib = Lib()
    }

    // setting the path requires the application context and is done once by the SobiWalletApplication class
    fun setPath(path: String) {
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
        Log.i("SobiWallet", "Descriptor for receive addresses is wpkh(${keys.xprv}/84'/1'/0'/0/*)")
        return ("wpkh(" + keys.xprv + "/84'/1'/0'/0/*)")
    }

    private fun createChangeDescriptor(keys: ExtendedKey): String {
        Log.i("SobiWallet", "Descriptor for change addresses is wpkh(${keys.xprv}/84'/1'/0'/1/*)")
        return ("wpkh(" + keys.xprv + "/84'/1'/0'/1/*)")
    }

    public fun loadExistingWallet(): Unit {
        val initialWalletData: RequiredInitialWalletData = Repository.getInitialWalletData()
        Log.i("SobiWallet", "Loading existing wallet, descriptor is ${initialWalletData.descriptor}")
        Log.i("SobiWallet", "Loading existing wallet, change descriptor is ${initialWalletData.changeDescriptor}")
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
            )
        )
    }

    fun sync(max_address: Int?=null) {
        lib.sync(walletPtr, max_address)
        Log.i("SobiWallet", "Wallet successfully synced")
    }

    fun getNewAddress(): String {
        return lib.get_new_address(walletPtr)
    }
}