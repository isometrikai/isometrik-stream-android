package io.isometrik.gs.ui.wallet.transaction.Model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WalletTransactionResponseData(
    @SerializedName("message") @Expose val message: String? = null,
    @SerializedName("data") @Expose val data: ArrayList<WalletTransactionData> = arrayListOf(),
    @SerializedName("status") @Expose val status: String? = null,
    @SerializedName("totalCount") @Expose val totalCount: Int? = null
) : Parcelable

@Parcelize
data class WalletTransactionData(
    @SerializedName("amount") @Expose val amount: String? = null,
    @SerializedName("closingBalance") @Expose val closingBalance: String? = null,
    @SerializedName("currency") @Expose val currency: String? = null,
    @SerializedName("description") @Expose val description: String? = null,
    @SerializedName("initiatedBy") @Expose val initiatedBy: String? = null,
    @SerializedName("notes") @Expose val notes: String? = null,
    @SerializedName("openingBalance") @Expose val openingBalance: String? = null,
    @SerializedName("orderId") @Expose val orderId: String? = null,
    @SerializedName("txnTimeStamp") @Expose val txnTimeStamp: String? = null,
    @SerializedName("txnType") @Expose val txnType: String? = null,
    @SerializedName("paymentTypeMode") @Expose val paymentTypeMode: String? = null,
    @SerializedName("walletId") @Expose val walletId: String? = null,
    @SerializedName("transactionId") @Expose val transactionId: String? = null,
    @SerializedName("accountId") @Expose val accountId: String? = null,
    @SerializedName("projectId") @Expose val projectId: String? = null
) : Parcelable