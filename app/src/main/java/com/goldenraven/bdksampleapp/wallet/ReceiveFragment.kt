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
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.goldenraven.bdksampleapp.R
import com.goldenraven.bdksampleapp.databinding.FragmentReceiveBinding
import com.goldenraven.bdksampleapp.data.Wallet

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
            Log.i("BDK Sample App", Wallet.getNewAddress())
            displayNewAddress()
        }
    }

    private fun displayNewAddress() {
        val newGeneratedAddress: String = Wallet.getNewAddress()
        Log.i("SobiWallet", "New deposit address is $newGeneratedAddress")

        val qrgEncoder: QRGEncoder = QRGEncoder(newGeneratedAddress, null, QRGContents.Type.TEXT, 1000)
        qrgEncoder.colorBlack = ContextCompat.getColor(requireContext(), R.color.night_1)
        qrgEncoder.colorWhite = ContextCompat.getColor(requireContext(), R.color.snow_1)
        try {
            val bitmap = qrgEncoder.bitmap
            binding.qrCode.setImageBitmap(bitmap)
        } catch (e: Throwable) {
            Log.i("SobiWallet", "Error with QRCode generator, ${e.toString()}")
        }
        binding.receiveAddress.text = newGeneratedAddress
    }
}
