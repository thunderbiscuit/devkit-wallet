/*
 * Copyright 2021 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.bdksampleapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.goldenraven.bdksampleapp.data.Wallet
import com.goldenraven.bdksampleapp.databinding.FragmentWalletChoiceBinding
import com.goldenraven.bdksampleapp.wallet.WalletActivity

class WalletChoiceFragment : Fragment() {

    private lateinit var binding: FragmentWalletChoiceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentWalletChoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(view)

        binding.newWalletButton.setOnClickListener {
            Wallet.createWallet()
            val intent: Intent = Intent(this@WalletChoiceFragment.context, WalletActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        binding.recoverWalletButton.setOnClickListener {
            navController.navigate(R.id.action_walletChoiceFragment_to_recoverWalletFragment)
        }
    }
}
