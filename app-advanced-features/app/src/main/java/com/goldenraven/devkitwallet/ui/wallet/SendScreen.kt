/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet.ui.wallet

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.material.Icon
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.Switch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.goldenraven.devkitwallet.R
import com.goldenraven.devkitwallet.ui.theme.firaMonoMedium
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.bitcoindevkit.PartiallySignedTransaction

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun SendScreen(
    navController: NavController,
    paddingValues: PaddingValues,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val recipientList: MutableList<Recipient> = remember { mutableStateListOf(Recipient(address = "", amount = 0u)) }
    val feeRate: MutableState<String> = rememberSaveable { mutableStateOf("") }
    val (showDialog, setShowDialog) =  rememberSaveable { mutableStateOf(false) }

    val sendAll: MutableState<Boolean> = remember { mutableStateOf(false) }
    val rbfEnabled: MutableState<Boolean> = remember { mutableStateOf(false) }
    val opReturnMsg: MutableState<String?> = remember { mutableStateOf(null) }

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )

    BottomSheetScaffold(
        sheetContent = { AdvancedOptions(sendAll, rbfEnabled, opReturnMsg, recipientList) },
        scaffoldState = bottomSheetScaffoldState,
        sheetBackgroundColor = DevkitWalletColors.night1,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetElevation = 12.dp,
        sheetPeekHeight = 0.dp,
        modifier = Modifier.padding(paddingValues)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
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
                TransactionRecipientInput(recipientList = recipientList)
                TransactionAmountInput(
                    recipientList = recipientList,
                    transactionType = if (sendAll.value) TransactionType.SEND_ALL else TransactionType.DEFAULT
                )
                TransactionFeeInput(feeRate = feeRate)
                MoreOptions(coroutineScope = coroutineScope, bottomSheetScaffoldState = bottomSheetScaffoldState)
                Dialog(
                    recipientList = recipientList,
                    feeRate = feeRate,
                    showDialog = showDialog,
                    setShowDialog = setShowDialog,
                    transactionType = if (sendAll.value) TransactionType.SEND_ALL else TransactionType.DEFAULT,
                    rbfEnabled = rbfEnabled.value,
                    opReturnMsg = opReturnMsg.value,
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
}

@Composable
internal fun AdvancedOptions(
    sendAll: MutableState<Boolean>,
    rbfEnabled: MutableState<Boolean>,
    opReturnMsg: MutableState<String?>,
    recipientList: MutableList<Recipient>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Advanced Options",
                color = DevkitWalletColors.snow3,
                fontSize = 18.sp,
                fontFamily = firaMonoMedium,
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Send All",
                color = DevkitWalletColors.snow3,
                fontSize = 14.sp,
                fontFamily = firaMono,
                textAlign = TextAlign.Center,
                lineHeight = 28.sp,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = sendAll.value,
                onCheckedChange = {
                    sendAll.value = !sendAll.value
                    while (recipientList.size > 1) { recipientList.removeLast() }
                },
                colors = SwitchDefaults.colors(
                    uncheckedBorderColor = DevkitWalletColors.night1,
                    uncheckedThumbColor = DevkitWalletColors.night1,
                    uncheckedTrackColor = DevkitWalletColors.snow1,
                    checkedThumbColor = DevkitWalletColors.snow1,
                    checkedTrackColor = DevkitWalletColors.auroraGreen,
                )
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Enable Replace-by-Fee",
                color = DevkitWalletColors.snow3,
                fontSize = 14.sp,
                fontFamily = firaMono,
                textAlign = TextAlign.Center,
                lineHeight = 28.sp,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = rbfEnabled.value,
                onCheckedChange = {
                    rbfEnabled.value = !rbfEnabled.value
                },
                colors = SwitchDefaults.colors(
                    uncheckedBorderColor = DevkitWalletColors.night1,
                    uncheckedThumbColor = DevkitWalletColors.night1,
                    uncheckedTrackColor = DevkitWalletColors.snow1,
                    checkedThumbColor = DevkitWalletColors.snow1,
                    checkedTrackColor = DevkitWalletColors.auroraGreen,
                )
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .weight(0.5f),
                value = opReturnMsg.value ?: "",
                onValueChange = {
                    opReturnMsg.value = it
                },
                label = {
                    Text(
                        text = "Optional OP_RETURN message",
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
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Number of Recipients",
                color = DevkitWalletColors.snow3,
                fontSize = 14.sp,
                fontFamily = firaMonoMedium,
            )
        }

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { if (recipientList.size > 1) { recipientList.removeLast() } },
                enabled = !sendAll.value,
                colors = ButtonDefaults.buttonColors(DevkitWalletColors.auroraRed),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.width(70.dp)
            ) {
                Text(text = "-")
            }

            Text(
                text = "${recipientList.size}",
                color = DevkitWalletColors.snow1,
                fontSize = 18.sp,
            )

            Button(
                onClick = { recipientList.add(Recipient("", 0u)) },
                enabled = !sendAll.value,
                colors = ButtonDefaults.buttonColors(DevkitWalletColors.auroraGreen),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.width(70.dp)
            ) {
                Text(text = "+")
            }


        }

    }
}

@Composable
private fun TransactionRecipientInput(recipientList: MutableList<Recipient>) {
    LazyColumn (modifier = Modifier
        .fillMaxWidth(0.9f)
        .heightIn(max = 100.dp)) {
        itemsIndexed(recipientList) { index, _ ->
            val recipientAddress: MutableState<String> = rememberSaveable { mutableStateOf("") }

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .weight(0.5f),
                    value = recipientAddress.value,
                    onValueChange = {
                        recipientAddress.value = it
                        recipientList[index].address = it
                    },
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
                )
            }
        }
    }
}

fun checkRecipientList(
    recipientList: MutableList<Recipient>,
    feeRate: MutableState<String>,
    context: Context
): Boolean {
    if (recipientList.size > 4) {
        Toast.makeText(context, "Too many recipients", Toast.LENGTH_SHORT).show()
        return false
    }
    for (recipient in recipientList) {
        if (recipient.address == "") {
            Toast.makeText(context, "Address is empty", Toast.LENGTH_SHORT).show()
            return false
        }
    }
    if (feeRate.value.isBlank()) {
        Toast.makeText(context, "Fee rate is empty", Toast.LENGTH_SHORT).show()
        return false
    }
    return true
}

@Composable
private fun TransactionAmountInput(recipientList: MutableList<Recipient>, transactionType: TransactionType) {
    LazyColumn (modifier = Modifier
        .fillMaxWidth(0.9f)
        .heightIn(max = 100.dp)) {
        itemsIndexed(recipientList) { index, _ ->
            val amount: MutableState<String> = rememberSaveable { mutableStateOf("") }

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .weight(0.5f),
                    value = amount.value,
                    onValueChange = {
                        amount.value = it
                        recipientList[index].amount = it.toULong()
                    },
                    label = {
                        when (transactionType) {
                            TransactionType.SEND_ALL -> {
                                Text(
                                    text = "Amount (Send All)",
                                    color = DevkitWalletColors.snow1Disabled,
                                )
                            }
                            else -> {
                                Text(
                                    text = "Amount ${index + 1}",
                                    color = DevkitWalletColors.snow1,
                                )
                            }
                        }
                    },
                    singleLine = true,
                    textStyle = TextStyle(fontFamily = firaMono, color = DevkitWalletColors.snow1),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = DevkitWalletColors.auroraGreen,
                        unfocusedBorderColor = DevkitWalletColors.snow1,
                        cursorColor = DevkitWalletColors.auroraGreen,
                    ),
                    enabled = (
                            when (transactionType) {
                                TransactionType.SEND_ALL -> false
                                else                     -> true
                            }
                    )
                )
            }
        }
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MoreOptions(coroutineScope: CoroutineScope, bottomSheetScaffoldState: BottomSheetScaffoldState) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .background(DevkitWalletColors.auroraGreen)
    ) {
        Button(
            onClick = {
                coroutineScope.launch {
                    if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                        bottomSheetScaffoldState.bottomSheetState.expand()
                    } else {
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                }
            },
            // colors = ButtonDefaults.buttonColors(DevkitWalletColors.auroraGreen),
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(fraction = 0.9f)
                .padding(vertical = 8.dp)
                .border(
                    BorderStroke(width = 1.dp, color = DevkitWalletColors.snow1),
                    shape = RoundedCornerShape(5.dp)
                )
        ) {
            Text(
                text = "more options",
                fontSize = 14.sp,
                fontFamily = firaMono,
                textAlign = TextAlign.Center,
                lineHeight = 28.sp,
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_round_expand_more_24),
                tint = DevkitWalletColors.snow1,
                contentDescription = "More options"
            )
        }
    }
}

@Composable
fun Dialog(
    recipientList: MutableList<Recipient>,
    feeRate: MutableState<String>,
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
    transactionType: TransactionType,
    rbfEnabled: Boolean,
    opReturnMsg: String?,
    context: Context,
) {
    if (showDialog) {
        var confirmationText = "Confirm Transaction : \n"
        recipientList.forEach { confirmationText += "${it.address}, ${it.amount}\n"}
        if (feeRate.value.isNotEmpty()) {
            confirmationText += "Fee Rate : ${feeRate.value.toULong()}"
        }
        if (!opReturnMsg.isNullOrEmpty()) {
            confirmationText += "OP_RETURN Message : $opReturnMsg"
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
                        if (checkRecipientList(recipientList = recipientList, feeRate = feeRate, context = context)) {
                            broadcastTransaction(
                                recipientList = recipientList,
                                feeRate = feeRate.value.toFloat(),
                                transactionType = transactionType,
                                rbfEnabled = rbfEnabled,
                                opReturnMsg = opReturnMsg
                            )
                            setShowDialog(false)
                        }
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
    recipientList: MutableList<Recipient>,
    feeRate: Float = 1F,
    transactionType: TransactionType,
    rbfEnabled: Boolean,
    opReturnMsg: String?
) {
    Log.i(TAG, "Attempting to broadcast transaction with inputs: recipient, amount: $recipientList, fee rate: $feeRate")
    try {
        // create, sign, and broadcast
        val psbt: PartiallySignedTransaction = when (transactionType) {
            TransactionType.DEFAULT -> Wallet.createTransaction(recipientList, feeRate, rbfEnabled, opReturnMsg)
            TransactionType.SEND_ALL -> Wallet.createSendAllTransaction(recipientList[0].address, feeRate, rbfEnabled, opReturnMsg)
        }
        var isSigned = Wallet.sign(psbt)
        if (isSigned) {
            val txid: String = Wallet.broadcast(psbt)
            Log.i(TAG, "Transaction was broadcast! txid: $txid")
        } else {
            Log.i(TAG, "Transaction not signed.")
        }
    } catch (e: Throwable) {
        Log.i(TAG, "Broadcast error: ${e.message}")
    }
}

data class Recipient(var address: String, var amount: ULong)

enum class TransactionType {
    DEFAULT,
    SEND_ALL,
}

// @Preview(device = Devices.PIXEL_4, showBackground = true)
// @Composable
// internal fun PreviewSendScreen() {
//     SendScreen(rememberNavController())
// }
