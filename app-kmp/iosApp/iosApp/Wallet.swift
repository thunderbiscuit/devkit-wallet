//
//  Wallet.swift
//  iosApp
//
//  Created by thunderbiscuit on 2023-09-15.
//  Copyright © 2023 orgName. All rights reserved.
//

// import Foundation
// import BitcoinDevKit
// import shared
//
// let db = DatabaseConfig.memory
//
// let descriptor = try! Descriptor.init(descriptor: "wpkh(tprv8ZgxMBicQKsPf2qfrEygW6fdYseJDDrVnDv26PH5BHdvSuG6ecCbHqLVof9yZcMoM31z9ur3tTYbSnr1WBqbGX97CbXcmp5H6qeMpyvx35B/84h/1h/0h/0/*)", network: Network.testnet)
// let electrum = ElectrumConfig(url: "ssl://electrum.blockstream.info:60002", socks5: nil, retry: 5, timeout: nil, stopGap: 10, validateDomain: true)
// let blockchainConfig = BlockchainConfig.electrum(config: electrum)
// let blockchain = try! Blockchain(config: blockchainConfig)
// let bdkWallet = try! Wallet(descriptor: descriptor, changeDescriptor: nil, network: Network.testnet, databaseConfig: db)
//
// let testingWallet = bdkWallet as! FFIWallet
//
// let wallet = CommonCodeWallet(bdkWallet: testingWallet)
// wallet.getBalance()
