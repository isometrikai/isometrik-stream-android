package io.isometrik.gs.ui.wallet.coin.pack

import io.isometrik.gs.builder.wallet.CurrencyPlansQuery
import io.isometrik.gs.builder.wallet.WalletBalanceQuery
import io.isometrik.gs.response.CompletionHandler
import io.isometrik.gs.response.error.IsometrikError
import io.isometrik.gs.ui.BuildConfig
import io.isometrik.gs.ui.IsometrikStreamSdk
import io.isometrik.gs.ui.wallet.response.WalletInAppPurchaseDataResponse
import io.isometrik.gs.ui.wallet.response.WalletResponseData

class CoinPackPresenter : CoinPackContract.Presenter {

    private var mView: CoinPackContract.View? = null

    /**
     * to get coin balance
     */
    fun coinBalance() {
        val walletBalanceQuery = WalletBalanceQuery.Builder()
            .setUserToken(IsometrikStreamSdk.getInstance().userSession.userToken)
            .setCurrency("COIN")

        IsometrikStreamSdk.getInstance().isometrik.getRemoteUseCases().walletUseCases
            .fetchWalletBalance(
                walletBalanceQuery.build(),
                (CompletionHandler<WalletResponseData> { var1: WalletResponseData?, var2: IsometrikError? ->
                    if (var1 != null) {
                        (var1?.data?.balance ?: 0.0).let { balance ->
                            (var1?.data?.id ?: "").let { id ->
                                IsometrikStreamSdk.getInstance().userSession.coinBalance = balance.toString()
//                               sessionManager.coinWalletId = id
                                mView?.balance(balance)
                            }
                        }
                    } else {
                        mView?.hideLoader()
                    }
                })
            )
    }

    /**
     * to get wallet balance
     */
    fun walletBalance() {

        val walletBalanceQuery = WalletBalanceQuery.Builder()
            .setUserToken(IsometrikStreamSdk.getInstance().userSession.userToken)
            .setCurrency(BuildConfig.DEFAUlT_CURRENCY_CODE)

        IsometrikStreamSdk.getInstance().isometrik.getRemoteUseCases().walletUseCases
            .fetchWalletBalance(
                walletBalanceQuery.build(),
                (CompletionHandler<WalletResponseData> { var1: WalletResponseData?, var2: IsometrikError? ->
                    if (var1 != null) {
                        (var1?.data?.balance ?: 0.0).let { balance ->
                            (var1?.data?.id ?: "").let { id ->
                                IsometrikStreamSdk.getInstance().userSession.walletBalance = balance.toString()
//                                sessionManager.walletId = id
                                mView?.balance(balance)
                            }
                        }
                    } else {
                        mView?.hideLoader()
                    }
                })
            )

    }

    override fun balance() {
        walletBalance()
        coinBalance()
    }

    override fun coinPackage(skip: Int, limit: Int) {
        mView!!.showLoader()

        val currencyPlansQuery = CurrencyPlansQuery.Builder()
            .setUserToken(IsometrikStreamSdk.getInstance().userSession.userToken)

        IsometrikStreamSdk.getInstance().isometrik.getRemoteUseCases().walletUseCases
            .fetchCurrencyPlans(
                currencyPlansQuery.build(),
                (CompletionHandler<WalletInAppPurchaseDataResponse> { var1: WalletInAppPurchaseDataResponse?, var2: IsometrikError? ->
                    if (var1 != null) {
                        mView?.coinPackage(var1?.data ?: arrayListOf())
                        mView?.hideLoader()
                    } else {
                        mView?.hideLoader()
                    }
                })
            )
    }

    override fun attachView(view: CoinPackContract.View?) {
        mView = view
    }

    override fun detachView() {
        mView = null
    }

}