/*
 * Copyright 2021 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.bdksampleapp.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.goldenraven.bdksampleapp.R
import com.goldenraven.bdksampleapp.data.Wallet
import com.goldenraven.bdksampleapp.databinding.FragmentWalletBinding
import java.text.DecimalFormat

class WalletFragment : Fragment() {

    private lateinit var binding: FragmentWalletBinding
    private lateinit var viewModel: WalletViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentWalletBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(WalletViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(view)

        viewModel.balance.observe(viewLifecycleOwner, {
            val balanceInBitcoin: Float
            if (it == 0L) {
                balanceInBitcoin = 0F
            } else {
                balanceInBitcoin = it.toFloat().div(100_000_000)
            }
            val humanReadableBalance = DecimalFormat("0.00000000").format(balanceInBitcoin)
            binding.balance.text = humanReadableBalance
        })

        binding.syncButton.setOnClickListener {
            Wallet.sync()
        }
        binding.toTransactionsButton.setOnClickListener {
            navController.navigate(R.id.action_walletFragment_to_transactionsFragment)
        }
        binding.toReceiveButton.setOnClickListener {
            navController.navigate(R.id.action_walletFragment_to_receiveFragment)
        }
        binding.toSendButton.setOnClickListener {
            navController.navigate(R.id.action_walletFragment_to_sendFragment)
        }
    }
}
