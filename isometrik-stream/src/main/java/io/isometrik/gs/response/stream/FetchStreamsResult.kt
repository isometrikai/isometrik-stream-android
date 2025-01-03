package io.isometrik.gs.response.stream

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.RawValue
import kotlinx.parcelize.Parcelize
import java.util.Objects

@Parcelize
data class FetchStreamsResult(
    @SerializedName("message") @Expose val message: String? = "",
    @SerializedName("streams") @Expose val streams: ArrayList<Streams> = arrayListOf(),
    @SerializedName("totalCount") @Expose val totalCount: Int? = 0
) : Parcelable

@Parcelize
data class Streams(
    @SerializedName("isRsvp") @Expose var isRsvp: Boolean? = false,
    @SerializedName("creationTime") @Expose val creationTime: Long? = 0,
    @SerializedName("recordedUrl") @Expose private val recordedUrl: @RawValue Any? = null,
    @SerializedName("scheduleStartTime") @Expose val scheduleStartTime: Long? = 0,
    @SerializedName("duration") @Expose val duration: Int? = 0,
    @SerializedName("totalViewersCount") @Expose val totalViewersCount: Int? = 0,
    @SerializedName("audioOnly") @Expose val audioOnly: Boolean? = false,
    @SerializedName("streamDescription") @Expose val streamDescription: String? = "",
    @SerializedName("streamTitle") @Expose val streamTitle: String? = "",
    @SerializedName("isStreamActive") @Expose val isStreamActive: Boolean? = false,
    @SerializedName("isGroupStream") @Expose val isGroupStream: Boolean? = false,
    @SerializedName("isPublicStream") @Expose val isPublicStream: Boolean? = false,
    @SerializedName("endDateTime") @Expose val endDateTime: Long? = 0,
    @SerializedName("isRecorded") @Expose val isRecorded: Boolean? = false,
    @SerializedName("isScheduledStream") @Expose val isScheduledStream: Boolean? = false,
    @SerializedName("streamImage") @Expose val streamImage: String? = "",
    @SerializedName("startDateTime") @Expose val startDateTime: Long? = 0,
    @SerializedName("scheduleDuration") @Expose val scheduleDuration: Int? = 0,
    @SerializedName("status") @Expose val status: String? = "",
    @SerializedName("selfHosted") @Expose val selfHosted: Boolean? = false,
    @SerializedName("isPkChallenge") @Expose val isPkChallenge: Boolean? = false,
    @SerializedName("restream") @Expose val restream: Boolean? = false,
    @SerializedName("productsLinked") @Expose val productsLinked: Boolean? = false,
    @SerializedName("metaData") @Expose val metaData: @RawValue Any? = "",
    @SerializedName("lowLatencyMode") @Expose val lowLatencyMode: Boolean? = false,
    @SerializedName("isSelfHosted") @Expose val isSelfHosted: Boolean? = false,
    @SerializedName("hdBroadcast") @Expose val hdBroadcast: Boolean? = false,
    @SerializedName("statusLog") @Expose val statusLog: String? = "",
    @SerializedName("address") @Expose val address: UserLiveStreamAddressResponceData? = UserLiveStreamAddressResponceData(),
    @SerializedName("enableRecording") @Expose val enableRecording: Boolean? = false,
    @SerializedName("streamIds") @Expose val streamIds: String? = "",
    @SerializedName("pinProductDetails") @Expose val pinProductDetails: UserLiveStreamPinProductDetailsResponceData? = UserLiveStreamPinProductDetailsResponceData(),
    @SerializedName("userDetails") @Expose val userDetails: LiveUserDetailsResponceData? = LiveUserDetailsResponceData(),
    @SerializedName("storeId") @Expose val storeId: String? = "",
    @SerializedName("storeCategoryId") @Expose val storeCategoryId: String? = "",
    @SerializedName("products") @Expose val products: ArrayList<UserLiveStreamProductsResponceData>? = arrayListOf(),
    @SerializedName("otherProducts") @Expose val otherProducts: ArrayList<UserLiveStreamProductsResponceData>? = arrayListOf(),
    @SerializedName("_id", alternate = ["id"]) @Expose val Id: String? = "",
    @SerializedName("pkId") @Expose val pkId: String? = "",
    @SerializedName("streamId") @Expose val streamId: String? = "",
    @SerializedName("members") @Expose val members: ArrayList<String>? = arrayListOf(),
    @SerializedName("eventId") @Expose val eventId: String? = "",
    @SerializedName("isometrikUserID") @Expose val isometrikUserID: String? = "",
    @SerializedName("appUserId") @Expose val appUserId: String? = "",
    @SerializedName("streamPreviewUrl") @Expose val streamPreviewUrl: String? = "",
    @SerializedName("taggedProductIds") @Expose val taggedProductIds: ArrayList<String>? = arrayListOf(),
    @SerializedName("isPaid") @Expose val isPaid: Boolean? = false,
    @SerializedName("alreadyPaid") @Expose var alreadyPaid: Boolean = false,
    @SerializedName("amount") @Expose val amount: String? = null,
    @SerializedName("firstUserDetails") @Expose val firstUserDetails: LiveUserDetailsResponceData? = null,
    @SerializedName("secondUserDetails") @Expose val secondUserDetails: LiveUserDetailsResponceData? = null,
) : Parcelable {

    fun recordedUrl(): ArrayList<String> {
        return when (recordedUrl) {
            is ArrayList<*> -> {
                recordedUrl as? ArrayList<String> ?: arrayListOf()
            }

            is String -> {
                val list = arrayListOf<String>()
                if (recordedUrl.trim().isNotEmpty())
                    list.add(recordedUrl.trim())
                list
            }

            else -> arrayListOf()
        }
    }

}

@Parcelize
data class UserLiveStreamAddressResponceData(
    @SerializedName("cityId") @Expose val cityId: String? = "",
    @SerializedName("cityName") @Expose val cityName: String? = "",
    @SerializedName("countryId") @Expose val countryId: String? = "",
    @SerializedName("countryName") @Expose val countryName: String? = ""
) : Parcelable


@Parcelize
data class UserLiveStreamPinProductDetailsResponceData(
    @SerializedName("pinProductId") @Expose val pinProductId: String? = "",
    @SerializedName("duration") @Expose val duration: Int? = 0,
    @SerializedName("timeStamp") @Expose val timeStamp: Long? = 0,
    @SerializedName("expireTime") @Expose val expireTime: Long? = 0,
    @SerializedName("discountPercentage") @Expose val discountPercentage: Double? = 0.0,
    @SerializedName("startBidPrice") @Expose val startBidPrice: Double? = 0.0
) : Parcelable


@Parcelize
data class LiveUserDetailsResponceData(
    @SerializedName("userName") @Expose val userName: String? = "",
    @SerializedName("firstName") @Expose val firstName: String? = "",
    @SerializedName("lastName") @Expose val lastName: String? = "",
    @SerializedName("email") @Expose val email: String? = "",
    @SerializedName("mobile") @Expose val mobile: String? = "",
    @SerializedName("countryCode") @Expose val countryCode: String? = "",
    @SerializedName("profilePic", alternate = ["userProfile"]) @Expose val profilePic: String? = "",
    @SerializedName("profileVideoThumbnail") @Expose val profileVideoThumbnail: String? = "",
    @SerializedName("isometrikUserId") @Expose val isomatricChatUserId: String? = "",
    @SerializedName("_id", alternate = ["id", "userId"]) @Expose val Id: String? = "",
    @SerializedName("privacy") @Expose val privacy: Int? = 0,
    @SerializedName("followStatus") @Expose var followStatus: Int? = 0,
    @SerializedName("metaData") @Expose val metaData: MetaData? = null,
    @SerializedName("streamImage") @Expose val streamImage: String? = "",
    @SerializedName("streamId") @Expose val streamId: String? = "",
) : Parcelable

@Parcelize
data class UserLiveStreamProductsResponceData(
    @SerializedName("id") @Expose val id: String? = "",
    @SerializedName("discount") @Expose val discount: Double? = 0.0,
    @SerializedName("parentCategoryId") @Expose val parentCategoryId: String? = ""
) : Parcelable

@Parcelize
data class MetaData(
    val firstName: String?,
    val lastName: String?,
    val profilePic: String?,
    val userName: String?
) : Parcelable

