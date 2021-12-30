/*
 * Copyright 2021 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.bdksampleapp.wallet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.goldenraven.bdksampleapp.R
import com.goldenraven.bdksampleapp.databinding.FragmentTransactionsBinding
import org.bitcoindevkit.Transaction
import com.goldenraven.bdksampleapp.data.Wallet
import com.goldenraven.bdksampleapp.utilities.timestampToString

private const val TAG = "Devkit Wallet"

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

        val allTransactions: List<Transaction> = Wallet.getTransactions()

        binding.pendingTransactionsContent.text = pendingTransactionsList(
            allTransactions.filterIsInstance<Transaction.Unconfirmed>()
        )
        binding.confirmedTransactionsContent.text = confirmedTransactionsList(
            allTransactions.filterIsInstance<Transaction.Confirmed>()
        )

        val navController = Navigation.findNavController(view)
        binding.transactionsToWalletButton.setOnClickListener {
            navController.navigate(R.id.action_transactionsFragment_to_walletFragment)
        }
    }

    private fun confirmedTransactionsList(transactions: List<Transaction.Confirmed>): String {
        if (transactions.isEmpty()) {
            Log.i(TAG, "Confirmed transaction list is empty")
            return "No confirmed transactions"
        } else {
            val sortedTransactions = transactions.sortedByDescending { it.confirmation.height }
            return buildString {
                for (item in sortedTransactions) {
                    Log.i(TAG, "Transaction list item: $item")
                    appendLine("Timestamp: ${item.confirmation.timestamp.timestampToString()}")
                    appendLine("Received: ${item.details.received}")
                    appendLine("Sent: ${item.details.sent}")
                    appendLine("Fees: ${item.details.fees}")
                    appendLine("Block: ${item.confirmation.height}")
                    appendLine("Txid: ${item.details.txid}")
                    appendLine()
                }
            }
        }
    }

    private fun pendingTransactionsList(transactions: List<Transaction.Unconfirmed>): String {
        if (transactions.isEmpty()) {
            Log.i(TAG, "Pending transaction list is empty")
            return "No pending transactions"
        } else {
            return buildString {
                for (item in transactions) {
                    Log.i(TAG, "Pending transaction list item: $item")
                    appendLine("Timestamp: Pending")
                    appendLine("Received: ${item.details.received}")
                    appendLine("Sent: ${item.details.sent}")
                    appendLine("Fees: ${item.details.fees}")
                    appendLine("Txid: ${item.details.txid}")
                    appendLine()
                }
            }
        }
    }
}
