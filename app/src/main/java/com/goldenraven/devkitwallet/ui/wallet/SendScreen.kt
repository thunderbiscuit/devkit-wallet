/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet.ui.wallet

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.goldenraven.devkitwallet.data.Wallet
import com.goldenraven.devkitwallet.ui.Screen
import com.goldenraven.devkitwallet.ui.theme.DevkitWalletColors
import com.goldenraven.devkitwallet.ui.theme.firaMono
import com.goldenraven.devkitwallet.utilities.TAG
import org.bitcoindevkit.PartiallySignedBitcoinTransaction

@Composable
internal fun SendScreen(navController: NavController) {

    val (showDialog, setShowDialog) =  rememberSaveable { mutableStateOf(false) }

    val recipientAddress: MutableState<String> = rememberSaveable { mutableStateOf("") }
    val amount: MutableState<String> = rememberSaveable { mutableStateOf("") }
    val feeRate: MutableState<String> = rememberSaveable { mutableStateOf("") }

    val recipientList: MutableList<Pair<String, ULong>> = remember { mutableStateListOf() }

    val context = LocalContext.current

    val isChecked: MutableState<Boolean> = remember { mutableStateOf(false) }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(DevkitWalletColors.night4)
    ) {
        val (screenTitle, transactionInputs, bottomButtons, sendFuncSwitch) = createRefs()
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
            TransactionRecipientInput(recipientAddress, recipientList, amount, context)
            TransactionAmountInput(amount, isChecked.value)
            TransactionFeeInput(feeRate)
            RecipientList(recipientList)
            Dialog(
                recipientAddress = recipientAddress,
                amount = amount,
                feeRate = feeRate,
                showDialog = showDialog,
                setShowDialog = setShowDialog,
                isChecked = isChecked.value,
                recipientList = recipientList,
                context = context
            )
        }

        Column(
            Modifier
                .constrainAs(sendFuncSwitch) {
                    end.linkTo(parent.end)
                }
                .padding(end = 16.dp, top = 16.dp)
        ) {
            SendFuncToggle(isChecked = isChecked.value, onCheckedChange = {
                    if (it) {
                        isChecked.value = it
                    } else {
                        isChecked.value = it
                    }
                }
                setShowDialog = setShowDialog,
            )
        }

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
                    // .size(width = 300.dp, height = 70.dp)
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
fun SendFuncToggle(isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "Send All",
            color = DevkitWalletColors.snow1,
            fontSize = 14.sp,
            fontFamily = firaMono,
            textAlign = TextAlign.Center,
        )
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun TransactionRecipientInput(
    recipientAddress: MutableState<String>,
    recipientList: MutableList<Pair<String, ULong>>,
    amount: MutableState<String>,
    context: Context
) {
    Row (verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(0.8f),
            value = recipientAddress.value,
            onValueChange = { recipientAddress.value = it },
            label = {
                Text(
                    text = "Recipient address",
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
        )

        Spacer(modifier = Modifier.size(8.dp))

        IconButton(
            modifier = Modifier.size(24.dp),
            onClick = {
                if (checkRecipientList(recipientAddress = recipientAddress.value, recipientList = recipientList, amount = amount.value, context = context)) {
                    recipientList.add(Pair(recipientAddress.value, amount.value.toULong()))
                    recipientAddress.value = ""
                    amount.value = ""
                }
            }
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = "Add Recipient",
                tint = DevkitWalletColors.snow1
            )
        }
    }
}

fun checkRecipientList(
    recipientAddress: String,
    recipientList: MutableList<Pair<String, ULong>>,
    amount: String,
    context: Context
): Boolean {
    if (recipientList.size > 4) {
        Toast.makeText(context, "Too many recipients", Toast.LENGTH_SHORT).show()
        return false
    }
    if (recipientAddress == "") {
        Toast.makeText(context, "Address is empty", Toast.LENGTH_SHORT).show()
        return false
    }
    if (amount == "") {
        Toast.makeText(context, "Amount is empty", Toast.LENGTH_SHORT).show()
        return false
    }
    if (recipientAddress in recipientList.map { it.first }){
        Toast.makeText(context, "Recipient already in list", Toast.LENGTH_SHORT).show()
        return false
    }
    return true
}

@Composable
private fun TransactionAmountInput(amount: MutableState<String>, isChecked: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        OutlinedTextField(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(0.9f),
            value = amount.value,
            onValueChange = { value: String ->
                amount.value = value.filter { it.isDigit() }
            },
            singleLine = true,
            textStyle = TextStyle(fontFamily = firaMono, color = DevkitWalletColors.snow1),
            label = {
                Text(
                    text = if (isChecked) "Amount (Send All)" else "Amount",
                    color = if (isChecked) DevkitWalletColors.snow1Disabled else DevkitWalletColors.snow1,
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = DevkitWalletColors.auroraGreen,
                unfocusedBorderColor = DevkitWalletColors.snow1,
                cursorColor = DevkitWalletColors.auroraGreen,
            ),
            enabled = !isChecked
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
                    text = "Fee rate",
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
fun RecipientList(recipientList: MutableList<Pair<String, ULong>>) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
    ) {
        itemsIndexed(recipientList) { index, item ->
            Row (verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .weight(0.5f),
                    value = item.first,
                    onValueChange = { },
                    label = {
                        Text(
                            text = "Recipient address ${index + 1}",
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
                    enabled = false
                )

                Spacer(modifier = Modifier.size(8.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .weight(0.2f),
                    value = item.second.toString(),
                    onValueChange = { },
                    label = {
                        Text(
                            text = "Amount",
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
                    enabled = false
                )

                Spacer(modifier = Modifier.size(8.dp))

                IconButton(
                    modifier = Modifier.size(24.dp),
                    onClick = { recipientList.remove(item) }
                ) {
                    Icon(
                        Icons.Filled.Clear,
                        contentDescription = "Remove Recipient",
                        tint = DevkitWalletColors.snow1
                    )
                }
            }
        }
    }
}


@Composable
fun Dialog(
    recipientAddress: MutableState<String>,
    amount: MutableState<String>,
    feeRate: MutableState<String>,
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
    isChecked: Boolean,
    recipientList: MutableList<Pair<String, ULong>>,
    context: Context,
) {
    if (showDialog) {
        val confirmationText = if (!isChecked) "Send: $amount\nto: $recipientAddress\nFee rate: ${feeRate.toFloat()}" else "Send all to: $recipientAddress\nFee rate: ${feeRate.toFloat()}"
        if (recipientAddress.value != "") {
            if (checkRecipientList(recipientAddress = recipientAddress.value, recipientList = recipientList, amount = amount.value, context = context)) {
                recipientList.add(Pair(recipientAddress.value, amount.value.toULong()))
                recipientAddress.value = ""
                amount.value = ""
            }
        }
        var confirmationText = "Confirm Transaction : \n"
        recipientList.forEach { confirmationText += "${it.first}, ${it.second}\n"}
        confirmationText += "Fee Rate : $feeRate"
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
                        if (recipientAddress.value != "") {
                            if (checkRecipientList(recipientAddress = recipientAddress.value, recipientList = recipientList, amount = amount.value, context = context)) {
                                recipientList.add(Pair(recipientAddress.value, amount.value.toULong()))
                            }
                        }
                        broadcastTransaction(recipientList, feeRate.value.toFloat())
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

private fun broadcastTransaction(
    recipientList: MutableList<Pair<String, ULong>>,
    feeRate: Float = 1F,
    isChecked: Boolean
) {
    Log.i(TAG, "Attempting to broadcast transaction with inputs: recipient, amount: $recipientList, fee rate: $feeRate")
    try {
        // create, sign, and broadcast
        val psbt: PartiallySignedBitcoinTransaction = if (!isChecked) Wallet.createTransaction(recipientList, feeRate) else Wallet.createSendAllTransaction(recipientList, feeRate)
        Wallet.sign(psbt)
        val txid: String = Wallet.broadcast(psbt)
        Log.i(TAG, "Transaction was broadcast! txid: $txid")
    } catch (e: Throwable) {
        Log.i(TAG, "Broadcast error: ${e.message}")
    }
}

@Preview(device = Devices.PIXEL_4, showBackground = true)
@Composable
internal fun PreviewSendScreen() {
    SendScreen(rememberNavController())
}
