/*
 * Copyright 2021 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet.wallet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.goldenraven.bdksampleapp.utilities.QRCodeGenerator
import com.goldenraven.devkitwallet.R
import com.goldenraven.devkitwallet.databinding.FragmentReceiveBinding
import com.goldenraven.devkitwallet.data.Wallet
import com.goldenraven.devkitwallet.utilities.TAG

class ReceiveFragment : Fragment() {

    private lateinit var binding: FragmentReceiveBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentReceiveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(view)

        binding.receiveToWalletButton.setOnClickListener {
            navController.navigate(R.id.action_receiveFragment_to_walletFragment)
        }

        binding.generateNewAddressButton.setOnClickListener {
            Log.i(TAG, Wallet.getLastUnusedAddress())
            displayNewAddress()
        }
    }

    private fun displayNewAddress() {
        val newGeneratedAddress: String = Wallet.getLastUnusedAddress()
        Log.i(TAG, "New deposit address is $newGeneratedAddress")

        val colorBlack = ContextCompat.getColor(requireContext(), R.color.night_1)
        val colorWhite = ContextCompat.getColor(requireContext(), R.color.snow_1)
        try {
            val bitmap = QRCodeGenerator(colorBlack, colorWhite)
                .generate(newGeneratedAddress, 1000)
            binding.qrCode.setImageBitmap(bitmap)
        } catch (e: Throwable) {
            Log.i(TAG, "Error with QRCode generator", e)
        }
        binding.receiveAddress.text = newGeneratedAddress
    }
}
