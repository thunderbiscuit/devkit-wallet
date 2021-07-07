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
import androidx.navigation.Navigation
import com.goldenraven.bdksampleapp.R
import com.goldenraven.bdksampleapp.data.Wallet
import com.goldenraven.bdksampleapp.databinding.FragmentWalletBinding

class WalletFragment : Fragment() {

    private lateinit var binding: FragmentWalletBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(view)

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