/*
 * Copyright 2021 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.bdksampleapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.goldenraven.bdksampleapp.data.Wallet
import com.goldenraven.bdksampleapp.databinding.FragmentRecoverBinding
import com.goldenraven.bdksampleapp.utilities.SnackbarLevel
import com.goldenraven.bdksampleapp.utilities.showSnackbar
import com.goldenraven.bdksampleapp.wallet.WalletActivity
import java.util.*

class RecoverWalletFragment : Fragment() {

    private lateinit var binding: FragmentRecoverBinding
    private lateinit var wordList: List<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentRecoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filename = "bip39-english.txt"
        val inputString: String = requireActivity().applicationContext.assets.open(filename).bufferedReader().use {
            it.readText()
        }

        this.wordList = inputString.split("\n").map { it.trim() }

        binding.recoverWalletButton.setOnClickListener {
            if (checkWords()) {
                val recoveryPhraseString = buildRecoveryPhrase()
                Wallet.recoverWallet(recoveryPhraseString)

                // launch home activity
                val intent: Intent = Intent(this@RecoverWalletFragment.context, WalletActivity::class.java)
                startActivity(intent)
            } else {
                Log.i("BDK Sample App", "Recovery phrase was invalid")
            }
        }
    }

    private fun checkWords(): Boolean {
        val mnemonicWordsTextViews: List<Int> = listOfNotNull<Int>(
            R.id.word1, R.id.word2, R.id.word3, R.id.word4, R.id.word5, R.id.word6,
            R.id.word7, R.id.word8, R.id.word9, R.id.word10, R.id.word11, R.id.word12,
        )

        for (word in 0..11) {
            val mnemonicWord: String = requireView().findViewById<TextView>(mnemonicWordsTextViews[word]).text.toString()
                .trim().lowercase(Locale.getDefault())
            Log.i("BDK Sample App", "Verifying word $word: $mnemonicWord")

            when {
                mnemonicWord.isEmpty() -> {
                    Log.i("BDK Sample App", "Word #$word is empty!")
                    showSnackbar(
                        requireView(),
                        SnackbarLevel.ERROR,
                        "Word #${word + 1} is empty!"
                    )
                    return false
                }
                mnemonicWord !in this.wordList -> {
                    Log.i("BDK Sample App", "Word #$word, $mnemonicWord, is not valid!")
                    showSnackbar(
                        requireView(),
                        SnackbarLevel.ERROR,
                        "Word #${word + 1} is invalid!"
                    )
                    return false
                }
                else -> {
                    Log.i("BDK Sample App", "Word #$word, $mnemonicWord, is valid")
                }
            }
        }
        return true
    }

    private fun buildRecoveryPhrase(): String {
        val mnemonicWordList: MutableList<String> = mutableListOf()
        val mnemonicWordsTextViews: List<Int> = listOfNotNull<Int>(
            R.id.word1, R.id.word2, R.id.word3, R.id.word4, R.id.word5, R.id.word6,
            R.id.word7, R.id.word8, R.id.word9, R.id.word10, R.id.word11, R.id.word12,
        )

        for (word in 0..11) {
            val mnemonicWord: String = requireView().findViewById<TextView>(mnemonicWordsTextViews[word]).text.toString()
                .trim().lowercase(Locale.getDefault())
            mnemonicWordList.add(mnemonicWord)
        }

        val recoveryPhraseString = mnemonicWordList.joinToString(" ")
        Log.i("BDK Sample App", "The recovery phrase is $recoveryPhraseString")
        return recoveryPhraseString
    }
}
