package io.isometrik.gs.ui.wallet.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WalletInAppPurchaseDataResponse(
    @SerializedName("status") @Expose val status: String? = null,
    @SerializedName("message") @Expose val message: String? = null,
    @SerializedName("data") @Expose val data: ArrayList<WalletInAppPurchaseData> = arrayListOf(),
    @SerializedName("totalCount") @Expose val totalCount: Int? = null
) : Parcelable

@Parcelize
data class WalletInAppPurchaseData(
    @SerializedName("id") @Expose val id: String? = null,
    @SerializedName("currencyPlanImage") @Expose val currencyPlanImage: String? = null,
    @SerializedName("baseCurrencyValue") @Expose val baseCurrencyValue: Double? = null,
    @SerializedName("numberOfUnits") @Expose val numberOfUnits: Int? = null,
    @SerializedName("appStoreProductIdentifier") @Expose val appStoreProductIdentifier: String? = null,
    @SerializedName("googlePlayProductIdentifier") @Expose val googlePlayProductIdentifier: String? = null,
    @SerializedName("planDescription") @Expose val planDescription: String? = null,
    @SerializedName("planId") @Expose val planId: String? = null,
    @SerializedName("planName") @Expose val planName: String? = null,
    @SerializedName("applicationId") @Expose val applicationId: String? = null,
    @SerializedName("clientName") @Expose val clientName: String? = null,
    @SerializedName("baseCurrency") @Expose val baseCurrency: String? = null,
    @SerializedName("unitSymbol") @Expose val unitSymbol: String? = null,
    @SerializedName("baseCurrencySymbol") @Expose val baseCurrencySymbol: String? = null,
    @SerializedName("status") @Expose val status: String? = null
) : Parcelable