/*
 * Copyright 2021-2023 thunderbiscuit and contributors.
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
        descriptor: Descriptor,
        changeDescriptor: Descriptor,
    ) {
        val database = DatabaseConfig.Sqlite(SqliteDbConfiguration("$path/bdk-sqlite"))
        wallet = BdkWallet(
            descriptor,
            changeDescriptor,
            Network.TESTNET,
            database
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
        val mnemonic = Mnemonic(WordCount.WORDS12)
        val bip32ExtendedRootKey = DescriptorSecretKey(Network.TESTNET, mnemonic, null)
        val descriptor: Descriptor = Descriptor.newBip84(bip32ExtendedRootKey, KeychainKind.EXTERNAL, Network.TESTNET)
        val changeDescriptor: Descriptor = Descriptor.newBip84(bip32ExtendedRootKey, KeychainKind.INTERNAL, Network.TESTNET)
        initialize(
            descriptor = descriptor,
            changeDescriptor = changeDescriptor,
        )
        Repository.saveWallet(path, descriptor.asStringPrivate(), changeDescriptor.asStringPrivate())
        Repository.saveMnemonic(mnemonic.asString())
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
        Log.i(TAG, "Loading existing wallet with descriptor: ${initialWalletData.descriptor}")
        Log.i(TAG, "Loading existing wallet with change descriptor: ${initialWalletData.changeDescriptor}")
        initialize(
            descriptor = Descriptor(initialWalletData.descriptor, Network.TESTNET),
            changeDescriptor = Descriptor(initialWalletData.changeDescriptor, Network.TESTNET),
        )
    }

    fun recoverWallet(recoveryPhrase: String) {
        val mnemonic = Mnemonic.fromString(recoveryPhrase)
        val bip32ExtendedRootKey = DescriptorSecretKey(Network.TESTNET, mnemonic, null)
        val descriptor: Descriptor = Descriptor.newBip84(bip32ExtendedRootKey, KeychainKind.EXTERNAL, Network.TESTNET)
        val changeDescriptor: Descriptor = Descriptor.newBip84(bip32ExtendedRootKey, KeychainKind.INTERNAL, Network.TESTNET)
        initialize(
            descriptor = descriptor,
            changeDescriptor = changeDescriptor,
        )
        Repository.saveWallet(path, descriptor.asStringPrivate(), changeDescriptor.asStringPrivate())
        Repository.saveMnemonic(mnemonic.asString())
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    fun createTransaction(
        recipientList: MutableList<Recipient>,
        feeRate: Float,
        enableRBF: Boolean,
        opReturnMsg: String?
    ): PartiallySignedTransaction {
        // technique 1 for adding a list of recipients to the TxBuilder
        // var txBuilder = TxBuilder()
        // for (recipient in recipientList) {
        //     txBuilder  = txBuilder.addRecipient(address = recipient.first, amount = recipient.second)
        // }
        // txBuilder = txBuilder.feeRate(satPerVbyte = fee_rate)

        // technique 2 for adding a list of recipients to the TxBuilder
        var txBuilder = recipientList.fold(TxBuilder()) { builder, recipient ->
            val scriptPubkey: Script = Address(recipient.address).scriptPubkey()
            builder.addRecipient(scriptPubkey, recipient.amount)
        }
        if (enableRBF) {
            txBuilder = txBuilder.enableRbf()
        }
        if (!opReturnMsg.isNullOrEmpty()) {
            txBuilder = txBuilder.addData(opReturnMsg.toByteArray(charset = Charsets.UTF_8).asUByteArray().toList())
        }
        return txBuilder.feeRate(feeRate).finish(wallet).psbt
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    fun createSendAllTransaction(
        recipient: String,
        feeRate: Float,
        enableRBF: Boolean,
        opReturnMsg: String?
    ): PartiallySignedTransaction {
        val scriptPubkey: Script = Address(recipient).scriptPubkey()
        var txBuilder = TxBuilder()
            .drainWallet()
            .drainTo(scriptPubkey)
            .feeRate(satPerVbyte = feeRate)

        if (enableRBF) {
            txBuilder = txBuilder.enableRbf()
        }
        if (!opReturnMsg.isNullOrEmpty()) {
            txBuilder = txBuilder.addData(opReturnMsg.toByteArray(charset = Charsets.UTF_8).asUByteArray().toList())
        }
        return txBuilder.finish(wallet).psbt
    }

    fun createBumpFeeTransaction(txid: String, feeRate: Float): PartiallySignedTransaction {
        return BumpFeeTxBuilder(txid = txid, newFeeRate = feeRate)
            .enableRbf()
            .finish(wallet = wallet)
    }

    fun sign(psbt: PartiallySignedTransaction): Boolean {
        return wallet.sign(psbt, null)
    }

    fun broadcast(signedPsbt: PartiallySignedTransaction): String {
        electrumServer.server.broadcast(signedPsbt.extractTx())
        return signedPsbt.txid()
    }

    fun getAllTransactions(): List<TransactionDetails> = wallet.listTransactions(true)

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

    fun getNewAddress(): AddressInfo = wallet.getAddress(AddressIndex.New)

    fun getLastUnusedAddress(): AddressInfo = wallet.getAddress(AddressIndex.LastUnused)

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
