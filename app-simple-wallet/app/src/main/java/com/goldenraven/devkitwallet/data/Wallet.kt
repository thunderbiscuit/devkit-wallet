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
        val blockchainConfig = BlockchainConfig.Electrum(ElectrumConfig(
            url = electrumURL,
            socks5 = null,
            retry = 5u,
            timeout = null,
            stopGap = 10u,
            validateDomain = true
        ))
        blockchain = Blockchain(blockchainConfig)
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

    fun createTransaction(recipient: String, amount: ULong, fee_rate: Float): TxBuilderResult {
        val scriptPubkey: Script = Address(recipient).scriptPubkey()
        return TxBuilder()
            .addRecipient(scriptPubkey, amount)
            .feeRate(satPerVbyte = fee_rate)
            .finish(wallet)
    }

    fun sign(psbt: PartiallySignedTransaction): Boolean {
        return wallet.sign(psbt, null)
    }

    fun broadcast(signedPsbt: PartiallySignedTransaction): String {
        blockchain.broadcast(signedPsbt.extractTx())
        return signedPsbt.txid()
    }

    fun getTransactions(): List<TransactionDetails> = wallet.listTransactions(true)

    fun sync() {
        Log.i(TAG, "Wallet is syncing")
        wallet.sync(blockchain, LogProgress)
    }

    fun getBalance(): ULong = wallet.getBalance().total

//    fun getNewAddress(): AddressInfo {
//        val newAddress = wallet.getAddress(AddressIndex.NEW)
//        Log.i("Wallet", "New address is $newAddress.address")
//        return newAddress
//    }

    fun getLastUnusedAddress(): AddressInfo = wallet.getAddress(AddressIndex.LastUnused)

    fun isBlockChainCreated() = ::blockchain.isInitialized
}
