package io.isometrik.gs.ui.wallet.coin.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.isometrik.gs.ui.R


class CoinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin)
    }
}