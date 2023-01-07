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
    // private const val regtestEsploraUrl: String = "http://10.0.2.2:3002"
    private const val electrumURL: String = "ssl://electrum.blockstream.info:60002"
    private lateinit var blockchainConfig: BlockchainConfig
    private lateinit var blockchain: Blockchain

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
        blockchainConfig = BlockchainConfig.Electrum(ElectrumConfig(electrumURL, null, 10u, 20u, 10u))
        // blockchainConfig = BlockchainConfig.Esplora(EsploraConfig(esploraUrl, null, 5u, 20u, 10u))
        blockchain = Blockchain(blockchainConfig)
    }

    fun createWallet() {
        val mnemonic: Mnemonic = Mnemonic(WordCount.WORDS12)
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
        Repository.saveMnemonic(Mnemonic.toString())
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
        Log.i(TAG, "Loading existing wallet, descriptor is ${initialWalletData.descriptor}")
        Log.i(TAG, "Loading existing wallet, change descriptor is ${initialWalletData.changeDescriptor}")
        initialize(
            externalDescriptor = initialWalletData.descriptor,
            internalDescriptor = initialWalletData.changeDescriptor,
        )
    }

    fun recoverWallet(mnemonic: String) {
        val bip32RootKey: DescriptorSecretKey = DescriptorSecretKey(
            network = Network.TESTNET,
            mnemonic = Mnemonic.fromString(mnemonic),
            password = ""
        )
        val externalDescriptor: String = createExternalDescriptor(bip32RootKey)
        val internalDescriptor: String = createInternalDescriptor(bip32RootKey)
        initialize(
            externalDescriptor = externalDescriptor,
            internalDescriptor = internalDescriptor,
        )
        Repository.saveWallet(path, externalDescriptor, internalDescriptor)
        Repository.saveMnemonic(mnemonic.toString())
    }

    fun createTransaction(recipient: String, amount: ULong, fee_rate: Float): TxBuilderResult {
        val scriptPubkey: Script = Address(recipient).scriptPubkey()
        return TxBuilder()
            .addRecipient(scriptPubkey, amount)
            .feeRate(satPerVbyte = fee_rate)
            .finish(wallet)
    }

    fun sign(psbt: PartiallySignedTransaction) {
        wallet.sign(psbt)
    }

    fun broadcast(signedPsbt: PartiallySignedTransaction): String {
        blockchain.broadcast(signedPsbt)
        return signedPsbt.txid()
    }

    fun getTransactions(): List<TransactionDetails> = wallet.listTransactions()

    fun sync() {
        Log.i(TAG, "Wallet is syncing")
        wallet.sync(blockchain, LogProgress)
    }

    fun getBalance(): ULong = wallet.getBalance().total

    fun getNewAddress(): AddressInfo {
        val newAddress = wallet.getAddress(AddressIndex.NEW)
        Log.i("Wallet", "New address is $newAddress.address")
        return newAddress
    }

    fun getLastUnusedAddress(): AddressInfo = wallet.getAddress(AddressIndex.LAST_UNUSED)

    fun isBlockChainCreated() = ::blockchain.isInitialized
}
