package io.isometrik.gs.ui.gifts.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GiftSentResponse implements Serializable {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("giftTitle")
    @Expose
    private String giftTitle;

    @SerializedName("txnId")
    @Expose
    private String txnId;
    @SerializedName("transferedCoins")
    @Expose
    private int transferedCoins;
    @SerializedName("remainingBalance")
    @Expose
    private double remainingBalance;
    @SerializedName("createdAt")
    @Expose
    private long createdAt;

    @SerializedName("errorCode")
    private int errorCode;


    public String getGiftTitle() {
        return giftTitle;
    }

    public String getMessage() {
        return message;
    }

    public String getTxnId() {
        return txnId;
    }

    public int getTransferedCoins() {
        return transferedCoins;
    }

    public double getRemainingBalance() {
        return remainingBalance;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
