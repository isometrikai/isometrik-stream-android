package io.isometrik.gs.ui.pk.response

/**
 * The type PkInvite Received event.
 */
data class PkInviteRelatedEvent (
    val userProfileImageUrl: String,
    val userName: String,
    val userMetaData: UserMetaData,
    val userIdentifier: String,
    val userId: String,
    val sentAt: Long,
    val searchableTags: List<Any?>,
    val metaData: MetaData,
    val messageType: Int,
    val messageId: String,
    val deviceId: String,
    val customType: String,
    val body: String,
    val action: String,
)

data class UserMetaData(
    val userType: Long,
    val userId: String,
    val storeId: String,
    val roleId: String,
    val mobileNumber: String,
    val email: String,
    val countryCode: String,
)

data class MetaData(
    val userName: String,
    val userMetaData: Any?,
    val status: String?,
    val userId: String,
    val streamId: String,
    val profilepic: String,
    val lastName: String,
    val isStar: Boolean,
    val inviteId: String,
    val firstName: String,
    val senderStreamId: String?,
    val reciverStreamId: String?,
    val accseptedUserId: String?,
    val accseptedStreamUserId: String?
)
