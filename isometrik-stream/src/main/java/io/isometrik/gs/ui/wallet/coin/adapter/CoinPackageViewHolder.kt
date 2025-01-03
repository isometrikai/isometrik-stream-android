package io.isometrik.gs.ui.wallet.coin.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.isometrik.gs.ui.R

class CoinPackageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var tvTitle: TextView = view.findViewById(R.id.tvTitle)
    var tvPrice: TextView = view.findViewById(R.id.tvPrice)
    var tvOldPrice: TextView = view.findViewById(R.id.tvOldPrice)
    var ivImage: ImageView = view.findViewById(R.id.ivImage)
}