/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet.ui.wallet

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.goldenraven.devkitwallet.data.Wallet
import com.goldenraven.devkitwallet.ui.Screen
import com.goldenraven.devkitwallet.ui.theme.DevkitWalletColors
import com.goldenraven.devkitwallet.ui.theme.firaMono
import com.goldenraven.devkitwallet.utilities.TAG
import org.bitcoindevkit.PartiallySignedTransaction
import org.bitcoindevkit.TransactionDetails

@Composable
internal fun RBFScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    txid: String?,
) {
    if (txid.isNullOrEmpty()) {
        navController.popBackStack()
    }
    var transaction: TransactionDetails? = getTransaction(txid = txid)
    if (transaction == null) {
        navController.popBackStack()
    }
    transaction = transaction as TransactionDetails
    val context = LocalContext.current

    val amount = (transaction.sent - transaction.received - (transaction.fee ?: 0UL)).toString()
    val feeRate: MutableState<String> = rememberSaveable { mutableStateOf("") }
    val (showDialog, setShowDialog) =  rememberSaveable { mutableStateOf(false) }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(DevkitWalletColors.night4)
    ) {
        val (screenTitle, transactionInputs, bottomButtons) = createRefs()

        Text(
            text = "Send Bitcoin",
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
            modifier = Modifier.constrainAs(transactionInputs) {
                top.linkTo(screenTitle.bottom)
                bottom.linkTo(bottomButtons.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.fillToConstraints
            }
        ) {
            ShowTxnDetail(name = "Transaction Id",content = txid!!)
            ShowTxnDetail(name = "Amount", content = amount)
            TransactionFeeInput(feeRate = feeRate)
            BumpFeeDialog(
                txid = txid,
                amount = amount,
                feeRate = feeRate,
                showDialog = showDialog,
                setShowDialog = setShowDialog,
                context = context
            )
        }
        Column(
            Modifier
                .constrainAs(bottomButtons) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(bottom = 32.dp)
        ) {
            Button(
                onClick = { setShowDialog(true) },
                colors = ButtonDefaults.buttonColors(DevkitWalletColors.auroraRed),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp, horizontal = 8.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
            ) {
                Text(
                    text = "broadcast transaction",
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

@Composable
private fun ShowTxnDetail(name: String, content: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(fraction = 0.9f)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .weight(0.5f),
            value = content,
            onValueChange = {  },
            label = {
                Text(
                    text = name,
                    color = DevkitWalletColors.snow1,
                )
            },
            singleLine = true,
            textStyle = TextStyle(fontFamily = firaMono, color = DevkitWalletColors.snow1),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = DevkitWalletColors.auroraGreen,
                unfocusedBorderColor = DevkitWalletColors.snow1,
                cursorColor = DevkitWalletColors.auroraGreen,
            ),
            enabled = false,
        )
    }
}

@Composable
private fun TransactionFeeInput(feeRate: MutableState<String>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(0.9f),
            value = feeRate.value,
            onValueChange = { newValue: String ->
                feeRate.value = newValue.filter { it.isDigit() }
            },
            singleLine = true,
            textStyle = TextStyle(fontFamily = firaMono, color = DevkitWalletColors.snow1),
            label = {
                Text(
                    text = "New fee rate",
                    color = DevkitWalletColors.snow1,
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = DevkitWalletColors.auroraGreen,
                unfocusedBorderColor = DevkitWalletColors.snow1,
                cursorColor = DevkitWalletColors.auroraGreen,
            ),
        )
    }
}

@Composable
fun BumpFeeDialog(
    txid: String,
    amount: String,
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
    context: Context,
    feeRate: MutableState<String>,
) {
    if (showDialog) {
        var confirmationText = "Confirm Transaction : \nTxid : $txid\nAmount : $amount"
        if (feeRate.value.isNotEmpty()) {
            confirmationText += "Fee Rate : ${feeRate.value.toULong()}"
        }
        AlertDialog(
            containerColor = DevkitWalletColors.night4,
            onDismissRequest = {},
            title = {
                Text(
                    text = "Confirm transaction",
                    color = DevkitWalletColors.snow1
                )
            },
            text = {
                Text(
                    text = confirmationText,
                    color = DevkitWalletColors.snow1
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (feeRate.value.isNotEmpty()) {
                            broadcastTransaction(txid = txid, feeRate = feeRate.value.toFloat())
                        } else {
                            Toast.makeText(context, "Fee is empty!", Toast.LENGTH_SHORT).show()
                        }
                        setShowDialog(false)
                    },
                ) {
                    Text(
                        text = "Confirm",
                        color = DevkitWalletColors.snow1
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        setShowDialog(false)
                    },
                ) {
                    Text(
                        text = "Cancel",
                        color = DevkitWalletColors.snow1
                    )
                }
            },
        )
    }
}

private fun broadcastTransaction(txid: String, feeRate: Float = 1F) {
    Log.i(TAG, "Attempting to broadcast transaction with inputs: txid $txid, fee rate: $feeRate")
    try {
        // create, sign, and broadcast
        val psbt: PartiallySignedTransaction = Wallet.createBumpFeeTransaction(txid = txid, feeRate = feeRate)
        Wallet.sign(psbt)
        val newTxid: String = Wallet.broadcast(psbt)
        Log.i(TAG, "Transaction was broadcast! txid: $newTxid")
    } catch (e: Throwable) {
        Log.i(TAG, "Broadcast error: ${e.message}")
    }
}
