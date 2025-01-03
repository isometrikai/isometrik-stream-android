package io.isometrik.gs.ui.pk.response

data class PkStopEvent(
    val sentAt: Long,
    val payload: Payload,
    val messageId: String,
    val action: String,
)

data class Payload(
    val type: String,
    val pkId: String,
    val message: String,
)