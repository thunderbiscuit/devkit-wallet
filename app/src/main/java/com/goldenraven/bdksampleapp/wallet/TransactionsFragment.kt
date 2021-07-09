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
import org.bitcoindevkit.bdkjni.Types.TransactionDetails
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

        binding.transactionsView.text = transactionList()

        val navController = Navigation.findNavController(view)
        binding.transactionsToWalletButton.setOnClickListener {
            navController.navigate(R.id.action_transactionsFragment_to_walletFragment)
        }
    }

    private fun transactionList(): String {
        // we sort the list of transactions by their height in the blockchain
        // highest height first, so the most recent transactions show up first
        // note that pending transactions have a confirmation_time of null, in which case we give them a height of 100,000,000
        // ensuring they show up above all other transactions until they get mined, at which point they get a proper height
        val rawList: List<TransactionDetails> = Wallet.listTransactions().sortedByDescending { it.confirmation_time?.height ?: 100_000_000 }
        var finalList: String = ""
        for (item in rawList) {
            Log.i("BDK Sample App", "Transaction list item: $item")
            val confirmationTime: String = item.confirmation_time?.timestampToString() ?: "Pending"
            val transactionInfo: String =
                "Timestamp: ${confirmationTime}\nReceived: ${item.received}\nSent: ${item.sent}\nFees: ${item.fee}\nTxid: ${item.txid}"

            finalList = "$finalList\n$transactionInfo\n"
        }
        return finalList
    }
}
