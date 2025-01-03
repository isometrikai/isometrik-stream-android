package io.isometrik.gs.ui.wallet.coin.pack

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsParams
import io.isometrik.gs.ui.BuildConfig
import io.isometrik.gs.ui.IsometrikStreamSdk
import io.isometrik.gs.ui.R
import io.isometrik.gs.ui.utils.Loader
import io.isometrik.gs.ui.wallet.alltransaction.AllTransactionsActivity
import io.isometrik.gs.ui.wallet.coin.adapter.CoinPackageAdapter
import io.isometrik.gs.ui.wallet.response.WalletInAppPurchaseData

class CoinPackFragment : Fragment(), CoinPackContract.View,
        CoinPackageAdapter.OnClickListener, PurchasesUpdatedListener {

    lateinit var mPresenter: CoinPackPresenter

    private val mAdapter: CoinPackageAdapter by lazy {
        CoinPackageAdapter(requireContext(), arrayListOf())
    }

    private lateinit var ibBack: ImageButton
    private lateinit var tvCoinBalance: TextView
    private lateinit var tvWalletBalance: TextView
    private lateinit var tvTransactions: TextView
    private lateinit var tvWalletTransactions: TextView
    private lateinit var tvMyBalanceText: TextView
    private lateinit var mLayoutManager: GridLayoutManager
    private lateinit var rvPackage: RecyclerView
    private lateinit var mLoader: Loader
    private lateinit var refresh: SwipeRefreshLayout
    private var isBillingAvailable: Boolean = false
    private lateinit var billingClient: BillingClient
    private val skuList = mutableListOf<String>()
    private val skuDetailsList = mutableListOf<SkuDetails>()
    private var packages: ArrayList<WalletInAppPurchaseData> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(getLayoutId(), container, false)

        mPresenter = CoinPackPresenter()
        initView(view)
        return view
    }

     fun getLayoutId(): Int {
        return R.layout.fragment_coin_package
    }

     fun initView(view: View) {
        mLoader = context?.let { Loader(it) }!!
        tvMyBalanceText = view.findViewById(R.id.tvMyBalanceText)
        tvTransactions = view.findViewById(R.id.tvTransactions)
        tvWalletTransactions = view.findViewById(R.id.tvWalletTransactions)
        tvCoinBalance = view.findViewById(R.id.tvCoinBalance)
        tvWalletBalance = view.findViewById(R.id.tvWalletBalance)
        rvPackage = view.findViewById(R.id.rvPackage)
        refresh = view.findViewById(R.id.refresh)
        ibBack = view.findViewById(R.id.ibBack)

        refresh.setOnRefreshListener {
            refreshCoinPackage()
            refresh.isRefreshing = false
        }

        mLayoutManager = GridLayoutManager(context, 3)
        rvPackage.layoutManager = mLayoutManager
        mAdapter.setListener(this)
        rvPackage.adapter = mAdapter

        mPresenter.attachView(this)
        mPresenter.balance()

        setupBillingClient()

        ibBack.setOnClickListener {
            activity?.finish()
        }

        tvTransactions.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    AllTransactionsActivity::class.java
                )
                    .putExtra(AllTransactionsActivity.TITLE,
                        "Coin Transactions")
//                    .putExtra(
//                        Constants.WALLET_ID,
//                        mPresenter.sessionManager.coinWalletId
//                    )
                    .putExtra(
                        AllTransactionsActivity.CURRENCY,
                        "COIN"
                    )
//                    .putExtra(
//                        Constants.WALLET_EARNING_ID,
//                        mPresenter.sessionManager.walletEarningId
//                    )
            )
        }
        tvWalletTransactions.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    AllTransactionsActivity::class.java
                )
                    .putExtra(AllTransactionsActivity.TITLE,
                        "Money Transactions")
//                    .putExtra(
//                        Constants.WALLET_ID,
//                        mPresenter.sessionManager.coinWalletId
//                    )
                    .putExtra(
                        AllTransactionsActivity.CURRENCY,
                        BuildConfig.DEFAUlT_CURRENCY_CODE
//                        BuildConfig.DEFAUlT_CURRENCY_CODE
                    )
//                    .putExtra(
//                        Constants.WALLET_EARNING_ID,
//                        mPresenter.sessionManager.walletEarningId
//                    )
            )
        }
    }

    /**
     * calls coin package api
     */
    private fun refreshCoinPackage() {
        mPresenter.coinPackage(0, 20)
    }


    override fun balance(balance: Double) {
        tvCoinBalance.text = IsometrikStreamSdk.getInstance().userSession.coinBalance
        tvWalletBalance.text = IsometrikStreamSdk.getInstance().userSession.walletBalance
    }

    override fun coinPackage(packages: ArrayList<WalletInAppPurchaseData>) {
        this.packages = packages
        for (p in packages) {
            p.googlePlayProductIdentifier?.let { skuList.add(it) }
        }

        if (IsometrikStreamSdk.getInstance().skusFromPlayStore())   // to be used when play console plan is set up
            loadAllSKUs()
        else
            mAdapter.setData(packages)
    }

    override fun message(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun message(message: Int) {
        message(getString(message))
    }

    override fun showLoader() {
        mLoader.show()
    }

    override fun hideLoader() {
        if (mLoader.isShowing) mLoader.dismiss()
    }

    override fun onPackageSelect(coinPackage: WalletInAppPurchaseData) {
        val billingFlowParams = skuDetailsList.find { it.sku == coinPackage.googlePlayProductIdentifier }?.let {
            BillingFlowParams.newBuilder().setSkuDetails(it).build()
        }
        if (billingFlowParams != null) {
            billingClient.launchBillingFlow(context as Activity, billingFlowParams)
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        billingClient = context?.let {
            BillingClient.newBuilder(it).enablePendingPurchases().setListener(this).build()
        }!!
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                    for (purchase in purchases) {
                        acknowledgePurchase(purchase.purchaseToken)
                    }
                } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                    // Handle an error caused by a user cancelling the purchase flow.

                } else {
                    // Handle any other error codes.
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                print("disconnected")
            }
        })
    }

    private fun setupBillingClient() {
        billingClient = context?.let {
            BillingClient.newBuilder(it)
                    .enablePendingPurchases()
                    .setListener(this)
                    .build()
        }!!
        refreshCoinPackage() // to be removed after play console setup
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is setup successfully
                    isBillingAvailable = true
                    refreshCoinPackage()
                    Log.i("in-app", "billing connected")
                }
            }

            override fun onBillingServiceDisconnected() {
                isBillingAvailable = false
                Log.i("in-app", "billing disconnected")
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }

    private fun loadAllSKUs() = if (billingClient.isReady) {
        val params = SkuDetailsParams.newBuilder().setSkusList(skuList).setType(BillingClient.SkuType.INAPP).build()
        billingClient.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
            // Process the result.
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList!!.isNotEmpty()) {
                skuList.clear()
                skuDetailsList.clear()
                skuDetailsList.addAll(skuDetailsList)
                for (skuDetails in skuDetailsList) {
                    //this will return both the SKUs from Google Play Console
                    skuList.add(skuDetails.sku)
                }
                val tempList: ArrayList<WalletInAppPurchaseData> = ArrayList()
                tempList.clear()
                for (pack in packages) {
                    if (skuList.contains(pack.googlePlayProductIdentifier)) {
                        tempList.add(pack)
                    }
                }
                var i = 0
                mAdapter.setData(tempList)
            }
        }

    } else {
        println("Billing Client not ready")
    }

    private fun acknowledgePurchase(purchaseToken: String) {
        val params = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchaseToken)
                .build()
        billingClient.acknowledgePurchase(params) { billingResult ->
            val responseCode = billingResult.responseCode
            val debugMessage = billingResult.debugMessage
        }
    }

    // This callback will only be called when MyFragment is at least Started.
    var callback: OnBackPressedCallback = object : OnBackPressedCallback(true /* enabled by default */) {
        override fun handleOnBackPressed() {
            activity?.finish()
        }
    }
}