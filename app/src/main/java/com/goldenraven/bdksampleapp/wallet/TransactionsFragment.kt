/*
 * Copyright 2021 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.bdksampleapp.wallet

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.goldenraven.bdksampleapp.R
import com.goldenraven.bdksampleapp.databinding.FragmentTransactionsBinding
import org.bitcoindevkit.bdkjni.TransactionDetails
import com.goldenraven.bdksampleapp.data.Wallet
import com.goldenraven.bdksampleapp.utilities.timestampToString


class TransactionsFragment : Fragment() {

    private lateinit var binding: FragmentTransactionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTransactionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // we sort the list of transactions by their height in the blockchain
        // highest height first, so the most recent transactions show up first
        // note that pending transactions have a confirmation_time of null, in which case we give them a height of 100,000,000
        // ensuring they show up above all other transactions until they get mined, at which point they get a proper height
        val rawTransactionList: List<TransactionDetails> = Wallet.listTransactions().sortedByDescending {
            it.confirmation_time?.height ?: 100_000_000
        }

        binding.pendingTransactionsContent.text = pendingTransactionsList(
            rawTransactionList.filter { it.confirmation_time == null }
        )
        binding.confirmedTransactionsContent.text = confirmedTransactionsList(
            rawTransactionList.filter { it.confirmation_time != null }
        )

        val navController = Navigation.findNavController(view)
        binding.transactionsToWalletButton.setOnClickListener {
            navController.navigate(R.id.action_transactionsFragment_to_walletFragment)
        }
    }

    private fun confirmedTransactionsList(transactions: List<TransactionDetails>): String {
        if (transactions.isEmpty()) {
            Log.i("SobiWallet", "Confirmed transaction list is empty")
            return "No confirmed transactions"
        } else {
            return buildString {
                for (item in transactions) {
                    Log.i("SobiWallet", "Transaction list item: $item")
                    appendLine("Timestamp: ${item.confirmation_time!!.timestampToString()}")
                    appendLine("Received: ${item.received}")
                    appendLine("Sent: ${item.sent}")
                    appendLine("Fees: ${item.fee}")
                    appendLine("Block: ${item.confirmation_time!!.height}")
                    appendLine("Txid: ${item.txid}")
                    appendLine()
                }
            }
        }
    }

    private fun pendingTransactionsList(transactions: List<TransactionDetails>): String {
        if (transactions.isEmpty()) {
            Log.i("SobiWallet", "Pending transaction list is empty")
            return "No pending transactions"
        } else {
            return buildString {
                for (item in transactions) {
                    Log.i("SobiWallet", "Pending transaction list item: $item")
                    appendLine("Timestamp: Pending")
                    appendLine("Received: ${item.received}")
                    appendLine("Sent: ${item.sent}")
                    appendLine("Fees: ${item.fee}")
                    appendLine("Txid: ${item.txid}")
                    appendLine()
                }
            }
        }
    }
}
