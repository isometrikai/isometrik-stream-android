package io.isometrik.gs.ui.streams.list

import io.isometrik.gs.response.stream.Streams
import io.isometrik.gs.rtcengine.utils.Constants
import io.isometrik.gs.ui.IsometrikStreamSdk
import io.isometrik.gs.ui.streams.event.LiveStreamStartEvent
import io.isometrik.gs.ui.streams.event.LiveStreamsResponse
import io.isometrik.gs.ui.utils.TimeUtil
import java.io.Serializable

/**
 * The type Streams model.
 */
class LiveStreamsModel : Serializable {
    /**
     * Is given user is member boolean.
     *
     * @return the boolean
     */
    /**
     * Sets given user is member.
     *
     * @param givenUserIsMember the given user is member
     */
    var isGivenUserIsMember: Boolean = false

    /**
     * Gets stream id.
     *
     * @return the stream id
     */
    @JvmField
    val streamId: String?

    /**
     * Gets stream image.
     *
     * @return the stream image
     */
    @JvmField
    val streamImage: String?
    var streamImage2: String = ""
    var secondUserStreamUserId: String = ""
    var secondUserWalletId: String = ""
    var secondUserStreamId: String = ""

    /**
     * Gets stream description.
     *
     * @return the stream description
     */
    @JvmField
    val streamDescription: String?
    /**
     * Gets members count.
     *
     * @return the members count
     */
    /**
     * Sets members count.
     *
     * @param membersCount the members count
     */
    @JvmField
    var membersCount: Int = 0
    /**
     * Gets viewers count.
     *
     * @return the viewers count
     */
    /**
     * Sets viewers count.
     *
     * @param viewersCount the viewers count
     */
    @JvmField
    var viewersCount: Int = 0

    /**
     * Gets publishers count.
     *
     * @return the publishers count
     */
    @JvmField
    val publishersCount: Int
    /**
     * Gets member ids.
     *
     * @return the member ids
     */
    /**
     * Sets member ids.
     *
     * @param memberIds the member ids
     */
    var memberIds: ArrayList<String>? = ArrayList()

    /**
     * Gets initiator id.
     *
     * @return the initiator id
     */
    @JvmField
    val initiatorId: String?

    /**
     * Gets start time.
     *
     * @return the start time
     */
    @JvmField
    val startTime: Long

    /**
     * Gets initiator name.
     *
     * @return the initiator name
     */
    @JvmField
    val initiatorName: String?

    /**
     * Gets duration.
     *
     * @return the duration
     */
    @JvmField
    val duration: String

    /**
     * Gets initiator image.
     *
     * @return the image of the user who started the stream group
     */
    @JvmField
    var initiatorImage: String?

    /**
     * Gets initiator identifier.
     *
     * @return the identifier of the user who started the stream group
     */
    @JvmField
    val initiatorIdentifier: String?

    /**
     * Gets whether given stream is public.
     *
     * @return whether given stream is public
     */
    @JvmField
    val isPublic: Boolean

    /**
     * Gets whether given stream is audioOnly.
     *
     * @return whether given stream is audioOnly
     */
    val isAudioOnly: Boolean

    /**
     * For stream preview functionality
     */
    var numberOfTimesAlreadySeen: Int = 0

    /**
     * Gets follow status of the initiator by the requesting user.
     *
     * @return follow status of the stream initiator
     */
    var followStatus: Int = 0

    /**
     * Gets whether the initiator of the stream is star or not.
     *
     * @return whether initiator of the stream is star
     */
    var isStar: Boolean = false
    var isPaid: Boolean = false
    var coinsCount: String = "0"
    var walletUserId: String = ""

    var paymentAmount: Int = 0
        private set
    var isAlreadyPaid: Boolean = false

    val isHdBroadcast: Boolean
    val isRestream: Boolean
    val isProductsLinked: Boolean

    @JvmField
    val productsCount: Int

    @JvmField
    val isMultiLive: Boolean
    var pkId: String = ""

    var isSelfHosted: Boolean
    var isRtmpIngest: Boolean = false

    @JvmField
    var isGivenUserCanPublish: Boolean = false
    var isPkChallenge: Boolean = false

    constructor(stream: LiveStreamStartEvent, givenUserIsMember: Boolean) {
        streamId = stream.streamId
        streamImage = stream.streamImage
        streamDescription = stream.streamDescription
        viewersCount = stream.viewersCount
        initiatorId = stream.userId
        startTime = stream.startDateTime
        if (stream.members != null) {
            memberIds = stream.members
        }
        initiatorName =
            if (stream.userDetails.firstName == null && stream.userDetails.lastName == null) {
                stream.userDetails.userName
            } else {
                if (stream.userDetails.lastName != null) {
                    stream.userDetails.firstName + " " + stream.userDetails.lastName
                } else {
                    stream.userDetails.firstName
                }
            }
        initiatorImage = if (stream.userDetails.userProfile == null) {
            null
        } else {
            if (stream.userDetails.userProfile == Constants.PROFILE_IMAGE_PLACEHOLDER_URL) {
                null
            } else {
                stream.userDetails.userProfile
            }
        }
        initiatorIdentifier = stream.userDetails.userName
        isPublic = stream.isPublicStream
        isAudioOnly = stream.isAudioOnly
        this.isGivenUserIsMember = givenUserIsMember
        this.duration = TimeUtil.getDurationString(TimeUtil.getDuration(startTime))

        isStar = stream.userDetails.isStar
        isPaid = stream.isPaid

        if (isPaid) {
            paymentAmount = stream.paymentAmount
            isAlreadyPaid = stream.isAlreadyPaid
        }

        walletUserId =
            if (stream.userDetails.userId != null && !stream.userDetails.userId.isBlank()) {
                stream.userDetails.userId
            } else {
                stream.userDetails.userMetaData.userId
            }

        coinsCount = stream.coinsCount.toString()

        //coinsCount = Utilities.formatMoney(stream.getCoinsCount());
        followStatus = stream.userDetails.followStatus

        membersCount = stream.members.size
        publishersCount = 0

        //publishersCount = stream.getPublishersCount();
        isHdBroadcast = stream.isHdBroadcast
        isRestream = stream.isRestream
        isProductsLinked = stream.isProductsLinked
        productsCount = stream.productsCount
        isMultiLive = stream.isGroupStream

        pkId = stream.pkId
        if (stream.secondUserDetails == null) {
            streamImage2 = ""
            secondUserStreamUserId = ""
            secondUserWalletId = ""
            secondUserStreamId = ""
        } else {
            streamImage2 = stream.secondUserDetails.streamImage
            secondUserStreamUserId = stream.secondUserDetails.streamUserId
            secondUserWalletId = stream.secondUserDetails.userId
            secondUserStreamId = stream.secondUserDetails.streamId
        }
        isSelfHosted = stream.selfHosted
        isRtmpIngest = stream.isRtmpIngest
    }

    /**
     * Instantiates a new Streams model.
     *
     * @param stream            the stream
     * @param givenUserIsMember the given user is member
     */
    constructor(stream: LiveStreamsResponse.Stream, givenUserIsMember: Boolean) {
        streamId = stream.streamId
        streamImage = stream.streamImage
        streamDescription = stream.streamDescription
        viewersCount = stream.viewersCount
        initiatorId = stream.userId
        startTime = stream.startDateTime
        if (stream.members != null) {
            memberIds = stream.members
        }
        initiatorName =
            if (stream.userDetails.firstName == null && stream.userDetails.lastName == null) {
                stream.userDetails.userName
            } else {
                if (stream.userDetails.lastName != null) {
                    stream.userDetails.firstName + " " + stream.userDetails.lastName
                } else {
                    stream.userDetails.firstName
                }
            }
        initiatorImage = if (stream.userDetails.userProfile == null) {
            null
        } else {
            if (stream.userDetails.userProfile == Constants.PROFILE_IMAGE_PLACEHOLDER_URL) {
                null
            } else {
                stream.userDetails.userProfile
            }
        }
        initiatorIdentifier = stream.userDetails.userName
        isPublic = stream.isPublicStream
        isAudioOnly = stream.isAudioOnly
        this.isGivenUserIsMember = givenUserIsMember
        this.duration = TimeUtil.getDurationString(TimeUtil.getDuration(startTime))

        isStar = stream.userDetails.isStar
        isPaid = stream.isPaid

        if (isPaid) {
            paymentAmount = stream.paymentAmount
            isAlreadyPaid = stream.isAlreadyPaid
        }
        walletUserId =
            if (stream.userDetails.userId != null && !stream.userDetails.userId.isBlank()) {
                stream.userDetails.userId
            } else {
                stream.userDetails.userMetaData.userId
            }
        //coinsCount = Utilities.formatMoney(stream.getCoinsCount());
        coinsCount = stream.coinsCount.toString()
        followStatus = stream.userDetails.followStatus

        if (stream.members != null) {
            membersCount = stream.members.size
        }
        publishersCount = 0
        //publishersCount = stream.getPublishersCount();
        isHdBroadcast = stream.isHdBroadcast
        isRestream = stream.isRestream
        isProductsLinked = stream.isProductsLinked
        productsCount = stream.productsCount
        isMultiLive = stream.isGroupStream

        pkId = stream.pkId
        if (stream.secondUserDetails == null) {
            streamImage2 = ""
            secondUserStreamUserId = ""
            secondUserWalletId = ""
            secondUserStreamId = ""
        } else {
            streamImage2 = stream.secondUserDetails.streamImage
            secondUserStreamUserId = stream.secondUserDetails.streamUserId
            secondUserWalletId = stream.secondUserDetails.userId
            secondUserStreamId = stream.secondUserDetails.streamId
        }
        isSelfHosted = stream.selfHosted
        isRtmpIngest = stream.isRtmpIngest
    }

    constructor(stream: Streams) {
        streamId = stream.streamId
        streamImage = stream.streamImage
        streamDescription = stream.streamDescription
        //        viewersCount = stream.getViewersCount();
        initiatorId = stream.userDetails!!.Id
        startTime = stream.startDateTime!!
        if (stream.members != null) {
            memberIds = stream.members
        }

        val fName = if (stream.userDetails.firstName.isNullOrEmpty()) {
            stream.userDetails.metaData?.firstName ?: ""
        } else {
            stream.userDetails.firstName
        }

        val lName = if (stream.userDetails.lastName.isNullOrEmpty()) {
            stream.userDetails.metaData?.lastName ?: ""
        } else {
            stream.userDetails.lastName
        }

        initiatorName = "$fName $lName"
        initiatorImage = stream.userDetails.profilePic ?: stream.userDetails.metaData?.profilePic
        if (initiatorImage == Constants.PROFILE_IMAGE_PLACEHOLDER_URL) {
            initiatorImage = null
        }
        initiatorIdentifier = stream.userDetails.userName ?: stream.userDetails.metaData?.userName

        isPublic = stream.isPublicStream!!
        isAudioOnly = stream.audioOnly!!
        this.isGivenUserIsMember =
            stream.userDetails.Id == IsometrikStreamSdk.getInstance().userSession.userId
        this.duration = TimeUtil.getDurationString(TimeUtil.getDuration(startTime))

        //        isStar = stream.getUserDetails().isStar();
//        isPaid = stream.isPaid();
        isStar = false
        isPaid = false
        if (isPaid) {
            paymentAmount = 0
            try {
                paymentAmount = stream.amount!!.toInt()
            } catch (ignore: Exception) {
            }
            isAlreadyPaid = stream.alreadyPaid
        }
        //        walletUserId = stream.getUserDetails().getWalletUserId();
//        //coinsCount = Utilities.formatMoney(stream.getCoinsCount());
//        coinsCount = String.valueOf(stream.getCoinsCount());
        walletUserId = ""
        coinsCount = "0"
        followStatus = stream.userDetails.followStatus!!

        if (stream.members != null) {
            membersCount = stream.members.size
        }
        publishersCount = 0
        //publishersCount = stream.getPublishersCount();
        isHdBroadcast = stream.hdBroadcast!!
        isRestream = stream.restream!!
        isProductsLinked = stream.productsLinked!!
        productsCount = stream.taggedProductIds!!.size
        isMultiLive = stream.isGroupStream!!
        isSelfHosted = stream.selfHosted!!

        isPkChallenge = stream.isPkChallenge ?: false

        streamImage2 = stream.secondUserDetails?.streamImage.orEmpty()
        secondUserStreamUserId = stream.secondUserDetails?.Id.orEmpty()
        secondUserStreamId = stream.secondUserDetails?.streamId.orEmpty()
        pkId = stream.pkId.orEmpty()
        isRtmpIngest = false
    }

    constructor(
        streamId: String?,
        streamImage: String?,
        streamDescription: String?,
        membersCount: Int,
        viewersCount: Int,
        publishersCount: Int,
        initiatorId: String?,
        initiatorName: String?,
        initiatorIdentifier: String?,
        initiatorImage: String?,
        startTime: Long,
        memberIds: ArrayList<String?>?,
        givenUserCanPublish: Boolean,
        isPublic: Boolean,
        audioOnly: Boolean,
        multiLive: Boolean,
        hdBroadcast: Boolean,
        restream: Boolean,
        productsLinked: Boolean,
        productsCount: Int,
        selfHosted: Boolean
    ) {
        this.streamId = streamId
        this.streamImage = streamImage
        this.streamDescription = streamDescription
        this.membersCount = membersCount
        this.viewersCount = viewersCount
        this.publishersCount = publishersCount
        this.initiatorId = initiatorId
        this.startTime = startTime
        this.initiatorName = initiatorName
        this.initiatorIdentifier = initiatorIdentifier
        this.initiatorImage = initiatorImage
        this.isGivenUserCanPublish = givenUserCanPublish
        this.duration = TimeUtil.getDurationString(TimeUtil.getDuration(startTime))
        this.isPublic = isPublic
        this.isAudioOnly = audioOnly
        this.isMultiLive = multiLive
        this.isHdBroadcast = hdBroadcast
        this.isRestream = restream
        this.isProductsLinked = productsLinked
        this.productsCount = productsCount
        this.isSelfHosted = selfHosted
    }
}
