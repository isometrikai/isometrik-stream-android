package io.isometrik.gs.ui.gifts.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GiftsByCategoryResponse(
    val status: String,
    val message: String,
    @SerializedName("data") val gits: List<Gift>,
    val totalCount: Long,
)

data class Gift(
    val id: String,
    val giftTitle: String,
    val giftAnimationImage: String,
    val giftImage: String,
    val giftTag: String,
    val virtualCurrency: Long,
    val giftGroupId: String,
    val applicationId: String,
    val clientName: String,
    var reciverId: String? = null,
    var reciverStreamUserId: String? = null,
    var receiverName: String? = null
)