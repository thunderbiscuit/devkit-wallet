/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet.ui.wallet

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.graphics.createBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.goldenraven.devkitwallet.data.Wallet
import com.goldenraven.devkitwallet.ui.Screen
import com.goldenraven.devkitwallet.ui.theme.DevkitWalletColors
import com.goldenraven.devkitwallet.ui.theme.firaMono
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter

internal class AddressViewModel() : ViewModel() {

    private var _address: MutableLiveData<String> = MutableLiveData("No address yet")
    val address: LiveData<String>
        get() = _address

    fun updateAddress() {
        _address.value = Wallet.getLastUnusedAddress()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReceiveScreen(
    navController: NavController,
    addressViewModel: AddressViewModel = viewModel()
) {

    val address by addressViewModel.address.observeAsState("Generate new address")

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(DevkitWalletColors.night4)
    ) {
        val (screenTitle, QRCode, bottomButtons) = createRefs()
        Text(
            text = "Receive Address",
            color = DevkitWalletColors.snow1,
            fontSize = 28.sp,
            fontFamily = firaMono,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .constrainAs(screenTitle) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(top = 70.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.constrainAs(QRCode) {
                top.linkTo(screenTitle.bottom)
                bottom.linkTo(bottomButtons.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.fillToConstraints
            }
        ) {
            val QR: ImageBitmap? = addressToQR(address)
            Log.i("ReceiveScreen", "New receive address is $address")
            if (address != "No address yet" && QR != null) {
                Image(
                    bitmap = QR,
                    contentDescription = "Bitcoindevkit website QR code",
                    Modifier.size(250.dp)
                )
                Spacer(modifier = Modifier.padding(vertical = 8.dp))
                SelectionContainer {
                    Text(
                        text = address,
                        fontFamily = firaMono,
                        color = DevkitWalletColors.snow1
                    )
                }
            }
        }

        // Log.i("Colors", getColor(requireContext(), R.color.night_1).toString())
        Column(
            Modifier
                .constrainAs(bottomButtons) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(bottom = 24.dp)
        ) {
            Button(
                onClick = { addressViewModel.updateAddress() },
                colors = ButtonDefaults.buttonColors(DevkitWalletColors.auroraGreen),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp, horizontal = 8.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
            ) {
                Text(
                    text = "generate new address",
                    fontSize = 14.sp,
                    fontFamily = firaMono,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp,
                )
            }
            Button(
                onClick = { navController.navigate(Screen.HomeScreen.route) },
                colors = ButtonDefaults.buttonColors(DevkitWalletColors.frost4),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp, horizontal = 8.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
            ) {
                Text(
                    text = "back to wallet",
                    fontSize = 14.sp,
                    fontFamily = firaMono,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp,
                )
            }
        }
    }
}

private fun addressToQR(address: String): ImageBitmap? {
    Log.i("ReceiveScreen", "We are generating the QR code for address $address")
    try {
        val qrCodeWriter: QRCodeWriter = QRCodeWriter()
        val bitMatrix: BitMatrix = qrCodeWriter.encode(address, BarcodeFormat.QR_CODE, 1000, 1000)
        val bitMap = createBitmap(1000, 1000)
        for (x in 0 until 1000) {
            for (y in 0 until 1000) {
                // uses night1 and snow1 for colors
                bitMap.setPixel(x, y, if (bitMatrix[x, y]) 0xFF2e3440.toInt() else 0xFFd8dee9.toInt())
            }
        }
        // Log.i("ReceiveScreen", "QR is ${bitMap.asImageBitmap()}")
        return bitMap.asImageBitmap()
    } catch (e: Throwable) {
        Log.i("ReceiveScreen", "Error with QRCode generation, $e")
    }
    return null
}

@Preview(device = Devices.PIXEL_4, showBackground = true)
@Composable
internal fun PreviewReceiveScreen() {
    ReceiveScreen(rememberNavController())
}