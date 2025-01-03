package io.isometrik.gs.ui.pk.response;

import com.google.gson.annotations.SerializedName


data class PkStatsResponse(

    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: PkStats? = PkStats()

)

data class FirstUserDetails(

    @SerializedName("userId") var userId: String? = null,
    @SerializedName("userName") var userName: String? = null,
    @SerializedName("firstName") var firstName: String? = null,
    @SerializedName("lastName") var lastName: String? = null,
    @SerializedName("profilePic") var profilePic: String? = null,
    @SerializedName("streamImage") var streamImage: String? = null,
    @SerializedName("isometrikUserId") var isometrikUserId: String? = null


)

data class SecondUserDetails(

    @SerializedName("userId") var userId: String? = null,
    @SerializedName("userName") var userName: String? = null,
    @SerializedName("firstName") var firstName: String? = null,
    @SerializedName("lastName") var lastName: String? = null,
    @SerializedName("profilePic") var profilePic: String? = null,
    @SerializedName("streamImage") var streamImage: String? = null,
    @SerializedName("isometrikUserId") var isometrikUserId: String? = null

)

data class PkStats(

    @SerializedName("streamId") var streamId: String? = null,
    @SerializedName("pkId") var pkId: String? = null,
    @SerializedName("firstUserId") var firstUserId: String? = null,
    @SerializedName("secoundUserId") var secoundUserId: String? = null,
    @SerializedName("firstUserDetails") var firstUserDetails: FirstUserDetails? = FirstUserDetails(),
    @SerializedName("secondUserDetails") var secondUserDetails: SecondUserDetails? = SecondUserDetails(),
    @SerializedName("firstUserCoins") var firstUserCoins: Int? = null,
    @SerializedName("secondUserCoins") var secondUserCoins: Int? = null,
    @SerializedName("timeRemain") var timeRemain: Int? = null

)