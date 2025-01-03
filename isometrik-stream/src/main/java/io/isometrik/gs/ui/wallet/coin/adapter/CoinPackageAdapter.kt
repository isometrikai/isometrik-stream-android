package io.isometrik.gs.ui.wallet.coin.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.isometrik.gs.ui.IsometrikStreamSdk
import io.isometrik.gs.ui.R
import io.isometrik.gs.ui.utils.Utilities
import io.isometrik.gs.ui.wallet.response.WalletInAppPurchaseData

class CoinPackageAdapter(private val mContext: Context, private var mPackages: ArrayList<WalletInAppPurchaseData>) : RecyclerView.Adapter<CoinPackageViewHolder>() {

    private var userSession = IsometrikStreamSdk.getInstance().userSession
    private lateinit var clickListener: OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinPackageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_coin_package, parent, false)
        return CoinPackageViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CoinPackageViewHolder, position: Int) {
        val data = mPackages[position]
        holder.tvTitle.text = data.planName
        val currencySymbol = if (data.baseCurrencySymbol != null) data.baseCurrencySymbol else userSession.currencySymbol
        holder.tvPrice.text = currencySymbol + "" + data.baseCurrencyValue
        val image = Utilities.getOptimizedGumletImage(holder.ivImage, data.currencyPlanImage)
        Glide.with(mContext).asBitmap().load(image)
                .placeholder(R.drawable.ic_coins)
                .into(holder.ivImage)

        holder.itemView.setOnClickListener { clickListener.onPackageSelect(data) }
    }

    override fun getItemCount(): Int {
        return mPackages.size
    }

    fun setData(packages: ArrayList<WalletInAppPurchaseData>) {
        mPackages = packages
        notifyDataSetChanged()
    }

    fun setListener(onClickListener: OnClickListener) {
        this.clickListener = onClickListener
    }

    interface OnClickListener {
        fun onPackageSelect(coinPackage: WalletInAppPurchaseData)
    }
}