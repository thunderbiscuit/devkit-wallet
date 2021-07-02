package com.goldenraven.bdksampleapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.goldenraven.bdksampleapp.databinding.ActivityWalletChoiceBinding

class WalletChoiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val binding = ActivityWalletChoiceBinding.inflate(layoutInflater)
        val binding = ActivityWalletChoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.thankYouButton.setOnClickListener {
            val intent: Intent = Intent(this, WalletActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
