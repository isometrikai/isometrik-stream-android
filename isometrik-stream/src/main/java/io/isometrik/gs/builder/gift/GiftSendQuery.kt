package io.isometrik.gs.builder.gift

data class GiftSendQuery(
    val messageStreamId: String,
    val senderId: String,
    val giftThumbnailUrl: String,
    val amount: Int,
    val giftId: String,
    val deviceId: String,
    val giftUrl: String,
    val receiverUserId: String,
    val reciverUserType: String,
    val receiverName: String,
    val pkId: String,
    val receiverStreamId: String,
    val receiverCurrency: String,
    val isometricToken: String,
    val giftTitle: String,
    val isGiftVideo: Boolean,
    val currency: String,
    val isPk: Boolean
)
