/*
 * Copyright 2021 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet.data

import android.util.Log
import com.goldenraven.devkitwallet.ui.wallet.Recipient
import com.goldenraven.devkitwallet.utilities.TAG
import org.bitcoindevkit.*
import org.bitcoindevkit.Wallet as BdkWallet

object Wallet {

    private lateinit var wallet: BdkWallet
    private lateinit var path: String
    private lateinit var electrumServer: ElectrumServer

    object LogProgress: Progress {
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
    ) {
        val database = DatabaseConfig.Sqlite(SqliteDbConfiguration("$path/bdk-sqlite"))
        wallet = BdkWallet(
            descriptor,
            changeDescriptor,
            Network.TESTNET,
            database,
        )
    }

    fun createBlockchain() {
        electrumServer = ElectrumServer()
        Log.i(TAG, "Current electrum URL : ${electrumServer.getElectrumURL()}")
    }

    fun changeElectrumServer(electrumURL: String) {
        electrumServer.createCustomElectrum(electrumURL = electrumURL)
        wallet.sync(electrumServer.server, LogProgress)
    }

    fun createWallet() {
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
        return ("wpkh(${keys.xprv}/84'/1'/0'/0/*)")
    }

    private fun createChangeDescriptor(keys: ExtendedKeyInfo): String {
        Log.i(TAG, "Descriptor for change addresses is wpkh(${keys.xprv}/84'/1'/0'/1/*)")
        return ("wpkh(${keys.xprv}/84'/1'/0'/1/*)")
    }

    // if the wallet already exists, its descriptors are stored in shared preferences
    fun loadExistingWallet() {
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

    fun createTransaction(recipientList: MutableList<Recipient>, feeRate: Float): PartiallySignedBitcoinTransaction {
        /*
        var txBuilder = TxBuilder()
        for (recipient in recipientList) {
            txBuilder  = txBuilder.addRecipient(address = recipient.first, amount = recipient.second)
        }
        txBuilder = txBuilder.feeRate(satPerVbyte = fee_rate)
         */
        val txBuilder = recipientList.fold(TxBuilder()) { builder, recipient ->
            builder.addRecipient(recipient.address, recipient.amount)
        }
        return txBuilder.feeRate(satPerVbyte = feeRate).finish(wallet)
    }

    fun createSendAllTransaction(recipient: String, feeRate: Float): PartiallySignedBitcoinTransaction {
        return TxBuilder()
            .drainWallet()
            .drainTo(address = recipient)
            .feeRate(satPerVbyte = feeRate)
            .finish(wallet)
    }

    fun sign(psbt: PartiallySignedBitcoinTransaction) {
        wallet.sign(psbt)
    }

    fun broadcast(signedPsbt: PartiallySignedBitcoinTransaction): String {
        electrumServer.server.broadcast(signedPsbt)
        return signedPsbt.txid()
    }

    fun getTransactions(): List<Transaction> = wallet.getTransactions()

    fun sync() {
        Log.i(TAG, "Wallet is syncing")
        wallet.sync(electrumServer.server, LogProgress)
    }

    fun getBalance(): ULong = wallet.getBalance()

    fun getNewAddress(): AddressInfo = wallet.getAddress(AddressIndex.NEW)

    fun getLastUnusedAddress(): AddressInfo = wallet.getAddress(AddressIndex.LAST_UNUSED)

    fun isBlockChainCreated() = ::electrumServer.isInitialized

    fun getElectrumURL(): String = electrumServer.getElectrumURL()

    fun isElectrumServerDefault(): Boolean = electrumServer.isElectrumServerDefault()

    fun setElectrumSettings(electrumSettings: ElectrumSettings) {
        when (electrumSettings) {
            ElectrumSettings.DEFAULT -> electrumServer.useDefaultElectrum()
            ElectrumSettings.CUSTOM ->  electrumServer.useCustomElectrum()
        }
    }
}

enum class ElectrumSettings {
    DEFAULT,
    CUSTOM
}
