package io.isometrik.gs.ui.gifts.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GiftCategoriesResponse(
    val status: String,
    val message: String,
    @SerializedName("data") val giftCategories: List<GiftCategory>,
    val totalCount: Long,
)


data class GiftCategory(
    val id: String,
    val giftTitle: String,
    val giftImage: String,
    val giftCount: Long,
    val applicationId: String,
    val clientName: String,
)  : Serializable
