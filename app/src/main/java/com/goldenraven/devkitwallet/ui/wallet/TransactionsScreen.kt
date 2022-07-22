/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet.ui.wallet

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import com.goldenraven.devkitwallet.utilities.timestampToString
import org.bitcoindevkit.Transaction

@Composable
internal fun TransactionsScreen(navController: NavController, paddingValues: PaddingValues) {
    val allTransactions: List<Transaction> = Wallet.getAllTransactions()

    val unconfirmedTransactions = allTransactions.filterIsInstance<Transaction.Unconfirmed>()
    val confirmedTransactions = allTransactions.filterIsInstance<Transaction.Confirmed>()
    val sortedTransactions = confirmedTransactions.sortedByDescending { it.confirmation.height }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(DevkitWalletColors.night4)
            .padding(paddingValues)
    ) {
        val (screenTitle, transactions, bottomButton) = createRefs()
        Text(
            text = "Transaction History",
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
            modifier = Modifier.constrainAs(transactions) {
                top.linkTo(screenTitle.bottom)
                bottom.linkTo(bottomButton.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.fillToConstraints
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(DevkitWalletColors.night3)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Pending",
                    fontSize = 18.sp,
                    fontFamily = firaMono,
                    color = DevkitWalletColors.snow1
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(120.dp)
                    .background(DevkitWalletColors.night2)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(state = scrollState)
                ) {
                    if (unconfirmedTransactions.isNotEmpty()) {
                        unconfirmedTransactions.forEach {
                            Text(
                                text = pendingTransactionsList(it),
                                fontSize = 12.sp,
                                fontFamily = firaMono,
                                color = DevkitWalletColors.snow1,
                                modifier = Modifier
                                    .padding(all = 4.dp)
                                    .clickable { viewTransaction(navController = navController, txid = it.details.txid) }
                            )
                        }
                    } else {
                        Text(
                            text = "No Pending Transactions",
                            fontSize = 12.sp,
                            fontFamily = firaMono,
                            color = DevkitWalletColors.snow1,
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(DevkitWalletColors.night3)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    "Confirmed",
                    fontSize = 18.sp,
                    fontFamily = firaMono,
                    color = DevkitWalletColors.snow1
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(200.dp)
                    .background(DevkitWalletColors.night2)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(state = scrollState)
                ) {
                    if (sortedTransactions.isNotEmpty()) {
                        sortedTransactions.forEach {
                            Text(
                                text = confirmedTransactionsList(it),
                                fontSize = 12.sp,
                                fontFamily = firaMono,
                                color = DevkitWalletColors.snow1,
                                modifier = Modifier
                                    .padding(all = 4.dp)
                                    .clickable { viewTransaction(navController = navController, txid = it.details.txid) }
                            )
                        }
                    } else {
                        Text(
                            text = "No Confirmed Transactions",
                            fontSize = 12.sp,
                            fontFamily = firaMono,
                            color = DevkitWalletColors.snow1
                        )
                    }
                }
            }
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
                .constrainAs(bottomButton) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
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

private fun viewTransaction(navController: NavController, txid: String) {
    navController.navigate("${Screen.TransactionScreen.route}/txid=$txid")
}

private fun confirmedTransactionsList(transactions: Transaction.Confirmed): String {
    return buildString {
        Log.i(TAG, "Transaction list item: $transactions")
        appendLine("Timestamp: ${transactions.confirmation.timestamp.timestampToString()}")
        appendLine("Received: ${transactions.details.received}")
        appendLine("Sent: ${transactions.details.sent}")
        appendLine("Fees: ${transactions.details.fee}")
        appendLine("Block: ${transactions.confirmation.height}")
        appendLine("Txid: ${transactions.details.txid}")
    }
}

private fun pendingTransactionsList(transactions: Transaction.Unconfirmed): String {
    return buildString {
        Log.i(TAG, "Pending transaction list item: $transactions")
        appendLine("Timestamp: Pending")
        appendLine("Received: ${transactions.details.received}")
        appendLine("Sent: ${transactions.details.sent}")
        appendLine("Fees: ${transactions.details.fee}")
        appendLine("Txid: ${transactions.details.txid}")
    }
}

// @Preview(device = Devices.PIXEL_4, showBackground = true)
// @Composable
// internal fun PreviewTransactionsScreen() {
//     TransactionsScreen(rememberNavController())
// }