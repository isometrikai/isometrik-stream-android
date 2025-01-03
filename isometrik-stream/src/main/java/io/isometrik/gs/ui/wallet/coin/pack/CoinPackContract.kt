package io.isometrik.gs.ui.wallet.coin.pack

import io.isometrik.gs.ui.utils.BasePresenter
import io.isometrik.gs.ui.wallet.coin.base.BaseView
import io.isometrik.gs.ui.wallet.response.WalletInAppPurchaseData

interface CoinPackContract {
    interface View : BaseView {
        /**
         * To display current coin wallet balance
         *
         * @param balance : coin balance
         */
        fun balance(balance: Double)

        /**
         * To set coin package to the adapter
         *
         * @param packages : list of data
         */
        fun coinPackage(packages: ArrayList<WalletInAppPurchaseData>)
    }

    interface Presenter : BasePresenter<View> {
        /**
         * Gets coin wallet balance
         */
        fun balance()

        /**
         * Gets coin packages
         */
        fun coinPackage(skip: Int, limit: Int)
    }
}