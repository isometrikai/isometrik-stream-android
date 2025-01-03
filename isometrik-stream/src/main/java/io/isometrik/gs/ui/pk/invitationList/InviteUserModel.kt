package io.isometrik.gs.ui.pk.invitationList

data class InviteUserModel(
    val streamId: String,
    val streamUserId: String,
    val firstName: String,
    val lastName: String,
    val userName: String,
    val userId: String,
    val viewerCount: Int,
    val streamPic: String,
    val profilePic: String,
    val timestamp: String,
    val inviteId: String,
    var isInvited : Boolean = false
)

data class LiveUsersListResponse(
    val streams : ArrayList<InviteUserModel>,
    val message: String,
    val totalCount : Int
)
