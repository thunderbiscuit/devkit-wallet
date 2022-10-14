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
    // to use Esplora on regtest locally, use the following address
    // private const val regtestEsploraUrl: String = "http://10.0.2.2:3002"

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
        externalDescriptor: String,
        internalDescriptor: String,
    ) {
        val database = DatabaseConfig.Sqlite(SqliteDbConfiguration("$path/bdk-sqlite"))
        wallet = BdkWallet(
            externalDescriptor,
            internalDescriptor,
            // Network.REGTEST,
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
        val mnemonic: String = generateMnemonic(WordCount.WORDS12)
        val bip32RootKey: DescriptorSecretKey = DescriptorSecretKey(
            network = Network.TESTNET,
            mnemonic = mnemonic,
            password = ""
        )
        val externalDescriptor: String = createExternalDescriptor(bip32RootKey)
        val internalDescriptor: String = createInternalDescriptor(bip32RootKey)
        initialize(
            externalDescriptor = externalDescriptor,
            internalDescriptor = internalDescriptor,
        )
        Repository.saveWallet(path, externalDescriptor, internalDescriptor)
        Repository.saveMnemonic(mnemonic)
    }

    // only create BIP84 compatible wallets
    private fun createExternalDescriptor(rootKey: DescriptorSecretKey): String {
        val externalPath: DerivationPath = DerivationPath("m/84h/1h/0h/0")
        val externalDescriptor = "wpkh(${rootKey.extend(externalPath).asString()})"
        Log.i(TAG, "Descriptor for receive addresses is $externalDescriptor")
        return externalDescriptor
    }

    private fun createInternalDescriptor(rootKey: DescriptorSecretKey): String {
        val internalPath: DerivationPath = DerivationPath("m/84h/1h/0h/1")
        val internalDescriptor = "wpkh(${rootKey.extend(internalPath).asString()})"
        Log.i(TAG, "Descriptor for change addresses is $internalDescriptor")
        return internalDescriptor
    }

    // if the wallet already exists, its descriptors are stored in shared preferences
    fun loadExistingWallet() {
        val initialWalletData: RequiredInitialWalletData = Repository.getInitialWalletData()
        Log.i(TAG, "Loading existing wallet, descriptor is ${initialWalletData.externalDescriptor}")
        Log.i(TAG, "Loading existing wallet, change descriptor is ${initialWalletData.internalDescriptor}")
        initialize(
            externalDescriptor = initialWalletData.externalDescriptor,
            internalDescriptor = initialWalletData.internalDescriptor,
        )
    }

    fun recoverWallet(mnemonic: String) {
        val bip32RootKey: DescriptorSecretKey = DescriptorSecretKey(
            network = Network.TESTNET,
            mnemonic = mnemonic,
            password = ""
        )
        val externalDescriptor: String = createExternalDescriptor(bip32RootKey)
        val internalDescriptor: String = createInternalDescriptor(bip32RootKey)
        initialize(
            externalDescriptor = externalDescriptor,
            internalDescriptor = internalDescriptor,
        )
        Repository.saveWallet(path, externalDescriptor, internalDescriptor)
        Repository.saveMnemonic(mnemonic)
    }

    fun createTransaction(
        recipientList: MutableList<Recipient>,
        feeRate: Float,
        enableRBF: Boolean
    ): PartiallySignedBitcoinTransaction {
        // technique 1 for adding a list of recipients to the TxBuilder
        // var txBuilder = TxBuilder()
        // for (recipient in recipientList) {
        //     txBuilder  = txBuilder.addRecipient(address = recipient.first, amount = recipient.second)
        // }
        // txBuilder = txBuilder.feeRate(satPerVbyte = fee_rate)

        // technique 2 for adding a list of recipients to the TxBuilder
        var txBuilder = recipientList.fold(TxBuilder()) { builder, recipient ->
            builder.addRecipient(recipient.address, recipient.amount)
        }
        if (enableRBF) {
            txBuilder = txBuilder.enableRbf()
        }
        return txBuilder.feeRate(feeRate).finish(wallet)
    }

    fun createSendAllTransaction(
        recipient: String,
        feeRate: Float,
        enableRBF: Boolean
    ): PartiallySignedBitcoinTransaction {
        var txBuilder = TxBuilder()
            .drainWallet()
            .drainTo(address = recipient)
            .feeRate(satPerVbyte = feeRate)

        if (enableRBF) {
            txBuilder = txBuilder.enableRbf()
        }
        return txBuilder.finish(wallet)
    }

    fun createBumpFeeTransaction(txid: String, feeRate: Float): PartiallySignedBitcoinTransaction {
        return BumpFeeTxBuilder(txid = txid, newFeeRate = feeRate)
            .enableRbf()
            .finish(wallet = wallet)
    }

    fun sign(psbt: PartiallySignedBitcoinTransaction) {
        wallet.sign(psbt)
    }

    fun broadcast(signedPsbt: PartiallySignedBitcoinTransaction): String {
        electrumServer.server.broadcast(signedPsbt)
        return signedPsbt.txid()
    }

    fun getAllTransactions(): List<TransactionDetails> = wallet.listTransactions()

    fun getTransaction(txid: String): TransactionDetails? {
        val allTransactions = getAllTransactions()
        allTransactions.forEach {
            if (it.txid == txid) {
                return it
            }
        }
        return null
    }

    fun sync() {
        Log.i(TAG, "Wallet is syncing")
        wallet.sync(electrumServer.server, LogProgress)
    }

    fun getBalance(): ULong = wallet.getBalance().total

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
