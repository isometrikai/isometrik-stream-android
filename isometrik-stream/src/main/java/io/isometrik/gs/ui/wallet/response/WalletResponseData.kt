package io.isometrik.gs.ui.wallet.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WalletResponseData(
    @SerializedName("message") @Expose val message: String? = null,
    @SerializedName("data") @Expose val data: WalletData? = null,
    @SerializedName("status") @Expose val status: String? = null
) : Parcelable

@Parcelize
data class WalletData(
    @SerializedName("id") @Expose val id: String? = null,
    @SerializedName("currency") @Expose val currency: String? = null,
    @SerializedName("currencySymbol") @Expose val currencySymbol: String? = null,
    @SerializedName("hardLimit") @Expose val hardLimit: Double? = null,
    @SerializedName("isHardLimitHit") @Expose val isHardLimitHit: Boolean? = null,
    @SerializedName("isSoftLimitHit") @Expose val isSoftLimitHit: Boolean? = null,
    @SerializedName("softLimit") @Expose val softLimit: Double? = null,
    @SerializedName("balance") @Expose val balance: Double? = null,
    @SerializedName("status") @Expose val status: String? = null,
    @SerializedName("userId") @Expose val userId: String? = null,
    @SerializedName("userType") @Expose val userType: String? = null,
    @SerializedName("timestamp") @Expose val timestamp: Long? = null,
    @SerializedName("walletType") @Expose val walletType: String? = null,
    @SerializedName("accountId") @Expose val accountId: String? = null,
    @SerializedName("projectId") @Expose val projectId: String? = null
) : Parcelable