/*
 * Copyright 2021 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet.wallet

import android.R.attr.height
import android.R.attr.width
import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.graphics.createBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.goldenraven.devkitwallet.R
import com.goldenraven.devkitwallet.data.Wallet
import com.goldenraven.devkitwallet.databinding.FragmentReceiveBinding
import com.goldenraven.devkitwallet.utilities.TAG
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter


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

        try {
            val qrCodeWriter: QRCodeWriter = QRCodeWriter()
            val bitMatrix: BitMatrix = qrCodeWriter.encode(newGeneratedAddress, BarcodeFormat.QR_CODE, 1000, 1000)
            val bitMap = createBitmap(1000, 1000)
            for (x in 0 until 1000) {
                for (y in 0 until 1000) {
                    bitMap.setPixel(x, y, if (bitMatrix[x, y]) getColor(requireContext(), R.color.night_1) else getColor(requireContext(), R.color.snow_1))
                }
            }
            binding.qrCode.setImageBitmap(bitMap)
        } catch (e: Throwable) {
            Log.i(TAG, "Error with QRCode generation, $e")
        }
        binding.receiveAddress.text = newGeneratedAddress
    }
}
