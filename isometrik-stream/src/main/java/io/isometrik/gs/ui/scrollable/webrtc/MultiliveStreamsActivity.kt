package io.isometrik.gs.ui.scrollable.webrtc

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.snackbar.Snackbar
import io.isometrik.gs.events.member.MemberLeaveEvent
import io.isometrik.gs.events.member.MemberRemoveEvent
import io.isometrik.gs.response.member.FetchMembersResult
import io.isometrik.gs.rtcengine.rtc.webrtc.ConnectionQuality
import io.isometrik.gs.rtcengine.rtc.webrtc.MultiLiveBroadcastOperations
import io.isometrik.gs.rtcengine.rtc.webrtc.MultilLiveSessionEventsHandler
import io.isometrik.gs.rtcengine.rtc.webrtc.RemoteUsersAudioLevelInfo
import io.isometrik.gs.rtcengine.utils.UserFeedClickedCallback
import io.isometrik.gs.rtcengine.utils.UserIdGenerator
import io.isometrik.gs.ui.IsometrikStreamSdk
import io.isometrik.gs.ui.R
import io.isometrik.gs.ui.action.CopublishActionCallbacks
import io.isometrik.gs.ui.action.accept.AcceptedCopublishRequestFragment
import io.isometrik.gs.ui.action.reject.RejectedCopublishRequestFragment
import io.isometrik.gs.ui.action.request.CopublishRequestStatusFragment
import io.isometrik.gs.ui.action.request.RequestCopublishFragment
import io.isometrik.gs.ui.databinding.IsmActivityScrollableMultiliveStreamsBinding
import io.isometrik.gs.ui.databinding.IsmBottomsheetLiveNotificationBinding
import io.isometrik.gs.ui.databinding.ItemVideoStreamInitialBinding
import io.isometrik.gs.ui.ecommerce.EcommerceOperationsCallback
import io.isometrik.gs.ui.ecommerce.cart.alert.ProductAddedToCartAlertFragment
import io.isometrik.gs.ui.ecommerce.cart.items.CartItemsFragment
import io.isometrik.gs.ui.ecommerce.checkout.CheckoutFragment
import io.isometrik.gs.ui.ecommerce.productdetails.ProductDetailModel
import io.isometrik.gs.ui.ecommerce.productdetails.ProductDetailsFragment
import io.isometrik.gs.ui.ecommerce.products.featured.FeaturedProductsFragment
import io.isometrik.gs.ui.ecommerce.products.featuring.FeaturingProductFragment
import io.isometrik.gs.ui.effects.EffectsFragment
import io.isometrik.gs.ui.endleave.EndLeaveFragment
import io.isometrik.gs.ui.endleave.EndPkFragment
import io.isometrik.gs.ui.gifts.GiftsActionCallback
import io.isometrik.gs.ui.gifts.GiftsModel
import io.isometrik.gs.ui.gifts.GiftsPagerFragment
import io.isometrik.gs.ui.gifts.response.Gift
import io.isometrik.gs.ui.hearts.HeartsRenderer
import io.isometrik.gs.ui.hearts.HeartsView
import io.isometrik.gs.ui.members.add.AddMembersFragment
import io.isometrik.gs.ui.members.add.RejoinFragment
import io.isometrik.gs.ui.members.list.MembersFragment
import io.isometrik.gs.ui.messages.MessagesAdapter
import io.isometrik.gs.ui.messages.MessagesModel
import io.isometrik.gs.ui.messages.PredefinedMessagesAdapter
import io.isometrik.gs.ui.messages.replies.MessageRepliesFragment
import io.isometrik.gs.ui.messages.replies.MessageReplyCallback
import io.isometrik.gs.ui.moderators.add.AddModeratorsFragment
import io.isometrik.gs.ui.moderators.alert.ModeratorAddedOrRemovedAlertFragment
import io.isometrik.gs.ui.moderators.leave.LeaveAsModeratorFragment
import io.isometrik.gs.ui.moderators.list.ModeratorsFragment
import io.isometrik.gs.ui.pk.challengsSettings.PkBattleStartResponse
import io.isometrik.gs.ui.pk.challengsSettings.PkChallengeSettingsFragment
import io.isometrik.gs.ui.pk.challengsSettings.PkSettingsActionCallbacks
import io.isometrik.gs.ui.pk.changehost.ChangeHostFragment
import io.isometrik.gs.ui.pk.changehost.ChangePkHostActionCallbacks
import io.isometrik.gs.ui.pk.end.EndStreamBottomSheetFragment
import io.isometrik.gs.ui.pk.invitationList.InviteUserFragment
import io.isometrik.gs.ui.pk.invitationList.InviteUserModel
import io.isometrik.gs.ui.pk.invitesent.PkInviteActionCallbacks
import io.isometrik.gs.ui.pk.invitesent.PkInviteFragment
import io.isometrik.gs.ui.pk.response.PkChangeStreamEvent
import io.isometrik.gs.ui.pk.response.PkInviteRelatedEvent
import io.isometrik.gs.ui.pk.response.PkResultData
import io.isometrik.gs.ui.pk.response.PkStats
import io.isometrik.gs.ui.pk.response.PkStopEvent
import io.isometrik.gs.ui.pk.response.PkStreamStartStopEvent
import io.isometrik.gs.ui.profile.UserInfoFragment
import io.isometrik.gs.ui.requests.RequestListActionCallback
import io.isometrik.gs.ui.requests.RequestsFragment
import io.isometrik.gs.ui.scrollable.ScrollableStreamsAdapter
import io.isometrik.gs.ui.settings.RemoteUserSettings
import io.isometrik.gs.ui.settings.SettingsFragment
import io.isometrik.gs.ui.settings.callbacks.SettingsActionCallback
import io.isometrik.gs.ui.settings.callbacks.SettingsActionEnum
import io.isometrik.gs.ui.settings.rtmp.RtmpDetailsFragment
import io.isometrik.gs.ui.streams.categories.StreamCategoryEnum
import io.isometrik.gs.ui.streams.list.LiveStreamsModel
import io.isometrik.gs.ui.utils.AlertProgress
import io.isometrik.gs.ui.utils.BitmapHelper
import io.isometrik.gs.ui.utils.Constants
import io.isometrik.gs.ui.utils.CountFormat
import io.isometrik.gs.ui.utils.GlideApp
import io.isometrik.gs.ui.utils.KeyboardUtil
import io.isometrik.gs.ui.utils.MessageTypeEnum
import io.isometrik.gs.ui.utils.PlaceholderUtils
import io.isometrik.gs.ui.utils.PredefinedMessagesUtil
import io.isometrik.gs.ui.utils.StreamDialog
import io.isometrik.gs.ui.utils.StreamDialogEnum
import io.isometrik.gs.ui.utils.StreamMemberInfo
import io.isometrik.gs.ui.utils.TimeDownView.DownTimeWatcher
import io.isometrik.gs.ui.utils.TimeUtil
import io.isometrik.gs.ui.utils.containers.VideoGridContainerRenderer
import io.isometrik.gs.ui.viewers.ViewersFragment
import io.isometrik.gs.ui.viewers.ViewersListModel
import io.isometrik.gs.ui.viewers.ViewersUtil
import io.livekit.android.room.track.Track
import io.livekit.android.room.track.VideoTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Timer
import java.util.TimerTask

/**
 * The Scrollable streams activity containing code for joining a broadcast as a publisher or
 * audience,
 * switch
 * profile from a viewer to publisher, send/receive gifts or likes, exchange/remove messages.
 * List of members, viewers, or copublish requests can also be seen in bottomsheet fragments, as
 * well a new copulish request can be created/deleted by viewer and that can be accepted/rejected
 * by the host.Publishers can also apply AR effects/switch camera and control various other
 * settings
 * like mute/unmute local or remote media.
 *
 *
 * It implements
 * ScrollableMultiliveStreamsContract.View[MultiliveStreamsContract.View]
 *
 * @see MultiliveStreamsContract.View
 *
 * @see MembersFragment
 *
 * @see RequestsFragment
 *
 * @see ViewersFragment
 *
 * @see SettingsFragment
 *
 * @see UserInfoFragment
 *
 * @see EffectsFragment
 *
 * @see RequestCopublishFragment
 *
 * @see CopublishRequestStatusFragment
 *
 * @see AcceptedCopublishRequestFragment
 *
 * @see RejectedCopublishRequestFragment
 *
 * @see EndLeaveFragment
 *
 * @see GiftsFragment
 *
 * @see GiftsPagerFragment
 *
 * @see AddMembersFragment
 *
 * @see FeaturedProductsFragment
 *
 * @see ProductDetailsFragment
 *
 * @see ProductAddedToCartAlertFragment
 *
 * @see CartItemsFragment
 *
 * @see FeaturingProductFragment
 *
 * @see CheckoutFragment
 *
 * @see ModeratorsFragment
 *
 * @see AddModeratorsFragment
 *
 * @see LeaveAsModeratorFragment
 *
 * @see ModeratorAddedOrRemovedAlertFragment
 *
 * @see MessageRepliesFragment
 */
class MultiliveStreamsActivity : AppCompatActivity(), SettingsActionCallback,
    MultiliveStreamsContract.View, CopublishActionCallbacks, GiftsActionCallback,
    UserFeedClickedCallback, RequestListActionCallback, EcommerceOperationsCallback,
    MessageReplyCallback, MultilLiveSessionEventsHandler,
    VideoGridContainerRenderer.AddUserListener,
    PkInviteActionCallbacks, PkSettingsActionCallbacks, ChangePkHostActionCallbacks,
    RejoinFragment.ReJoinActionCallbacks {
    private val messages = ArrayList<MessagesModel>()
    private val gifts = ArrayList<MessagesModel>()

    private val timerHandler: TimerHandler = TimerHandler()
    private val viewersListModels = ArrayList<ViewersListModel>()
    private val isometrik = IsometrikStreamSdk.getInstance().isometrik
    private val userSession = IsometrikStreamSdk.getInstance().userSession
    lateinit var scrollableMultiliveStreamsPresenter: MultiliveStreamsContract.Presenter
    private var messagesAdapter: MessagesAdapter? = null
    private var giftAdapter: MessagesAdapter? = null

    private var messagesLayoutManager: LinearLayoutManager? = null
    private var giftLayoutManager: LinearLayoutManager? = null

    private var unregisteredListeners = false
    private var alreadyStoppedPublishing = false
    private var streamId: String? = null


    private var isPkBattle = false
    private var isPkStreamer = false
    private var isPkInitiator = false
    private var isPkViewerFromHost = true
    private var isPkViewerOnGoing = false
    private var pkInvitedId = ""
    var MARGIN_CENTER = 80
    var MARGIN_TOP = 110
    var MARGIN_BOTTOM = 170
    var MARGIN_FOR_TIMER = 190
    private var countDownTimer: CountDownTimer? = null
    private var countDownGiftHide: CountDownTimer? = null
    private var countDownSpotlight: CountDownTimer? = null
    private var pkid = ""
    private var secondUserStreamUserId = ""

    private var secondUserStreamId = ""
    private var firstUserStreamUserId = ""

    private var firstUserStreamId = ""
    private var width = 0
    private var isPKOnGoing = false
    private var initiatorTotalCoin = 0
    private var guestTotalCoin = 0
    private lateinit var animRightProfile: Animation
    private lateinit var animLeftProfile: Animation
    private lateinit var animRightResult: Animation
    private lateinit var animLeftResult: Animation
    private var afterPkNeedToResetHost = false
    private var streamCategoryId = 0
    private var wasEligibleToJoinBroadcast = false
    private var isPaid = false
    private var coinsRequiredToUnlock = 0
    private var restream = false
    private var isStar = false

    private var current3DGifJob: Job? = null
    private var currentGifJob: Job? = null


    companion object {
        /**
         * The Increase timer.
         */
        const val INCREASE_TIMER = 1
    }

    /**
     * The Recyclerview's on scroll listener to fetch next set of messages on scroll.
     */
    private val messagesRecyclerViewOnScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy != 0 && messagesLayoutManager!!.findFirstVisibleItemPosition() == 0) {
                    scrollableMultiliveStreamsPresenter.requestStreamMessagesDataOnScroll(streamId)
                }
            }
        }
    private var model: HeartsView.Model? = null
    private var showingStreamDialog = false
    private var mTimer: Timer? = null
    private var timerPaused = false
    private var duration: Long = 0
    private var streamStartTime: Long = 0

    /**
     * For gifts
     */
    private var membersFragment: MembersFragment? = null
    private var requestsFragment: RequestsFragment? = null
    private var viewersFragment: ViewersFragment? = null
    private var settingsFragment: SettingsFragment? = null
    private var userInfoFragment: UserInfoFragment? = null
    private var effectsFragment: EffectsFragment? = null
    private var requestCopublishFragment: RequestCopublishFragment? = null
    private var copublishRequestStatusFragment: CopublishRequestStatusFragment? = null
    private var acceptedCopublishRequestFragment: AcceptedCopublishRequestFragment? = null
    private var rejectedCopublishRequestFragment: RejectedCopublishRequestFragment? = null
    private var endLeaveFragment: EndLeaveFragment? = null
    private var endPkFragment: EndPkFragment? = null
    private var giftsPagerFragment: GiftsPagerFragment? = null
    private var addMembersFragment: AddMembersFragment? = null
    private var remoteUserSettings: RemoteUserSettings? = null
    private var featuredProductsFragment: FeaturedProductsFragment? = null
    private var productDetailsFragment: ProductDetailsFragment? = null
    private var productAddedToCartAlertFragment: ProductAddedToCartAlertFragment? = null
    private var cartItemsFragment: CartItemsFragment? = null
    private var featuringProductFragment: FeaturingProductFragment? = null
    private var checkoutFragment: CheckoutFragment? = null
    private var moderatorsFragment: ModeratorsFragment? = null
    private var addModeratorsFragment: AddModeratorsFragment? = null
    private var leaveAsModeratorFragment: LeaveAsModeratorFragment? = null
    private var moderatorAddedOrRemovedAlertFragment: ModeratorAddedOrRemovedAlertFragment? = null
    private var messageRepliesFragment: MessageRepliesFragment? = null
    private var rtmpDetailsFragment: RtmpDetailsFragment? = null
    private var rejoinFragment: RejoinFragment? = null
    private var inviteUserFragment: InviteUserFragment? = null
    private var pkInviteFragment: PkInviteFragment? = null
    private var pkChallengeSettingsFragment: PkChallengeSettingsFragment? = null
    private var changeHostFragment: ChangeHostFragment? = null


    private var isPublic = true
    private var isBroadcaster = false
    private var beautificationApplied = false
    private var isAdmin = false
    private var audioOnly = false
    private var hdBroadcast = false
    private var productsLinked = false
    private var isModerator = false
    private var initiatorId: String? = null
    private var initiatorName: String? = null
    private var initiatorIdentifier: String? = null
    private var initiatorImageUrl: String? = null
    private var streamDescription: String? = null
    private var viewersUtil: ViewersUtil? = null
    private var alertProgress: AlertProgress? = null
    private var alertDialog: AlertDialog? = null

    /**
     * For copublish requests who joined as viewer
     */
    private var alreadyRequestedCopublish = false
    private var pending = false
    private var accepted = false
    private var countDownCompleted = false
    private var channelJoined = false
    private var viewersCount = 0
    private var productsCount = 0
    private var goLiveDialog: android.app.AlertDialog? = null
    private var alert: AlertDialog? = null

    /**
     * For live streams as scrollable list
     */
    private var streams: ArrayList<LiveStreamsModel>? = null
    private var streamsAdapter: ScrollableStreamsAdapter? = null
    private var streamsLayoutManager: LinearLayoutManager? = null
    private var membersCount = 0
    private lateinit var ismActivityScrollableMultiliveStreamsBinding: IsmActivityScrollableMultiliveStreamsBinding
    private lateinit var multiLiveBroadcastOperations: MultiLiveBroadcastOperations

    /**
     * The Recyclerview's on scroll listener to fetch next set of streams on scroll.
     */
    private val streamsRecyclerViewOnScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (streamsLayoutManager!!.findFirstVisibleItemPosition() > -1) {
                        if (!isBroadcaster) {
                            updateShimmerVisibility(false)
                            updateStreamInfo(streams!![streamsLayoutManager!!.findFirstVisibleItemPosition()])
                        }
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                scrollableMultiliveStreamsPresenter.requestLiveStreamsDataOnScroll(
                    streamsLayoutManager!!.findFirstVisibleItemPosition(),
                    streamsLayoutManager!!.childCount,
                    streamsLayoutManager!!.itemCount
                )
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ismActivityScrollableMultiliveStreamsBinding =
            IsmActivityScrollableMultiliveStreamsBinding.inflate(layoutInflater)
        val view: View = ismActivityScrollableMultiliveStreamsBinding.root
        setContentView(view)
        multiLiveBroadcastOperations = MultiLiveBroadcastOperations(this)
        scrollableMultiliveStreamsPresenter = MultiliveStreamsPresenter(this)
        viewersUtil = ViewersUtil(
            ismActivityScrollableMultiliveStreamsBinding.llViewersList.ivOne,
            ismActivityScrollableMultiliveStreamsBinding.llViewersList.ivTwo,
            ismActivityScrollableMultiliveStreamsBinding.llViewersList.ivThree,
            ismActivityScrollableMultiliveStreamsBinding.llViewersList.ivFour,
            ismActivityScrollableMultiliveStreamsBinding.llViewersList.ivFive,
            ismActivityScrollableMultiliveStreamsBinding.llViewersList.ivMore,
            ismActivityScrollableMultiliveStreamsBinding.tvNoOfViewers,
            this
        )

        MARGIN_TOP = (110 * resources.displayMetrics.density).toInt()
        MARGIN_CENTER = (80 * resources.displayMetrics.density).toInt()
        MARGIN_BOTTOM = (170 * resources.displayMetrics.density).toInt()
        MARGIN_FOR_TIMER = (190 * resources.displayMetrics.density).toInt()
        width = ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.getMeasuredWidth()

        membersFragment = MembersFragment()
        requestsFragment = RequestsFragment()
        viewersFragment = ViewersFragment()
        settingsFragment = SettingsFragment()
        userInfoFragment = UserInfoFragment()
        effectsFragment = EffectsFragment()
        requestCopublishFragment = RequestCopublishFragment()
        copublishRequestStatusFragment = CopublishRequestStatusFragment()
        acceptedCopublishRequestFragment = AcceptedCopublishRequestFragment()
        rejectedCopublishRequestFragment = RejectedCopublishRequestFragment()
        endLeaveFragment = EndLeaveFragment()
        addMembersFragment = AddMembersFragment()
        endPkFragment = EndPkFragment()
        rejoinFragment = RejoinFragment(this)
        giftsPagerFragment = GiftsPagerFragment()
        remoteUserSettings = RemoteUserSettings()
        featuredProductsFragment = FeaturedProductsFragment()
        productDetailsFragment = ProductDetailsFragment()
        productAddedToCartAlertFragment = ProductAddedToCartAlertFragment()
        cartItemsFragment = CartItemsFragment()
        featuringProductFragment = FeaturingProductFragment()
        checkoutFragment = CheckoutFragment()
        moderatorsFragment = ModeratorsFragment()
        addModeratorsFragment = AddModeratorsFragment()
        leaveAsModeratorFragment = LeaveAsModeratorFragment()
        moderatorAddedOrRemovedAlertFragment = ModeratorAddedOrRemovedAlertFragment()
        messageRepliesFragment = MessageRepliesFragment()
        rtmpDetailsFragment = RtmpDetailsFragment()
        inviteUserFragment = InviteUserFragment()
        pkInviteFragment = PkInviteFragment()
        pkChallengeSettingsFragment = PkChallengeSettingsFragment()
        changeHostFragment = ChangeHostFragment()




        goLiveDialog = android.app.AlertDialog.Builder(this).create()
//        isPublic = intent.getBooleanExtra("isPublic", false)
        audioOnly = intent.getBooleanExtra("audioOnly", false)
        isBroadcaster = intent.getBooleanExtra("isBroadcaster", true)
        isAdmin = intent.getBooleanExtra("isAdmin", false)
        hdBroadcast = intent.getBooleanExtra("hdBroadcast", false)
        productsLinked = intent.getBooleanExtra("productsLinked", false)
        productsCount = intent.getIntExtra("productsCount", 0)
        streamDescription = intent.getStringExtra("streamDescription")
        isModerator = intent.getBooleanExtra("isModerator", false)
        ismActivityScrollableMultiliveStreamsBinding.ivModerator.visibility =
            if (isModerator) View.VISIBLE else View.GONE
        streamId = intent.getStringExtra("streamId")
        initiatorId = intent.getStringExtra("initiatorId")
//        walletUserId = intent.getStringExtra("walletUserId")
//        if (walletUserId?.isNullOrEmpty() ?: true) {
//            walletUserId = initiatorId
//        }
        streamStartTime = intent.getLongExtra("startTime", 0)

        streamCategoryId = intent.getIntExtra("streamCategoryId", StreamCategoryEnum.All.value)
        isPkBattle = intent.getBooleanExtra("isPkChallenge", false)

        streams = intent.getSerializableExtra("streams") as ArrayList<LiveStreamsModel>?
        if (streams == null) {
            //For broadcaster streams are not passed
            streams = ArrayList()
        }


        // pk stuff
        animRightProfile = AnimationUtils.loadAnimation(
            this,
            R.anim.ism_right_pk_profile
        )
        animLeftProfile = AnimationUtils.loadAnimation(
            this,
            R.anim.ism_left_pk_profile
        )
        animLeftResult = AnimationUtils.loadAnimation(
            this,
            R.anim.ism_left_pk_result
        )
        animRightResult = AnimationUtils.loadAnimation(
            this,
            R.anim.ism_right_pk_result
        )

        setupForPKStuffUi()

        pkid = ""
        if (isPkBattle) {
            isPkBattle = true
            isPkStreamer = false
            isPkInitiator = false
            preparePkBattleView()
            pkid = intent.getStringExtra("pkId").orEmpty()
            secondUserStreamUserId = intent.getStringExtra("secondUserStreamUserId").orEmpty()
            secondUserStreamId = intent.getStringExtra("secondUserStreamId").orEmpty()
            ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.updatePkParam(
                isPkBattle,
                isPkStreamer,
                isPkInitiator
            )
            fetchedPkBattle()
        }

        ismActivityScrollableMultiliveStreamsBinding.imgPkStart.setOnClickListener { v ->
            if (!isFinishing && !pkChallengeSettingsFragment!!.isAdded) {
                dismissAllDialogs()
                pkChallengeSettingsFragment!!.updateParameters(
                    userSession.userProfilePic,
                    userSession.userName,
                    this
                )
                pkChallengeSettingsFragment!!.show(
                    supportFragmentManager,
                    PkChallengeSettingsFragment.TAG
                )
            }
        }
        // End Pk Stuff


        scrollableMultiliveStreamsPresenter.initialize(
            streamId,
            intent.getStringArrayListExtra("memberIds"),
            isAdmin,
            isBroadcaster,
            isModerator,
            isPkBattle
        )
        toggleRoleBasedActionButtonsVisibility(true, false)
        pkButtonVisibility(true)

        messagesLayoutManager = LinearLayoutManager(this)
        ismActivityScrollableMultiliveStreamsBinding.rvMessages.layoutManager =
            messagesLayoutManager
        messagesAdapter = MessagesAdapter(this, messages, false)
        ismActivityScrollableMultiliveStreamsBinding.rvMessages.addOnScrollListener(
            messagesRecyclerViewOnScrollListener
        )
        ismActivityScrollableMultiliveStreamsBinding.rvMessages.adapter = messagesAdapter


        giftLayoutManager = LinearLayoutManager(this)
        ismActivityScrollableMultiliveStreamsBinding.rvGifts.setLayoutManager(giftLayoutManager)
        giftAdapter = MessagesAdapter(this, gifts, true)
        ismActivityScrollableMultiliveStreamsBinding.rvGifts.setAdapter(giftAdapter)



        scrollableMultiliveStreamsPresenter.registerStreamViewersEventListener()
        scrollableMultiliveStreamsPresenter.registerStreamMembersEventListener()
        scrollableMultiliveStreamsPresenter.registerStreamsEventListener()
        scrollableMultiliveStreamsPresenter.registerStreamMessagesEventListener()
        scrollableMultiliveStreamsPresenter.registerConnectionEventListener()
        scrollableMultiliveStreamsPresenter.registerPresenceEventListener()
        scrollableMultiliveStreamsPresenter.registerModeratorEventListener()
        //Due to switch profile event have to register,even incase of private streams
        scrollableMultiliveStreamsPresenter.registerCopublishRequestsEventListener()
        scrollableMultiliveStreamsPresenter.registerPkEventListener()
        multiLiveBroadcastOperations.addMultilLiveSessionEventsHandler(this)

        if (intent.getBooleanExtra("selfHosted", false) && intent.getBooleanExtra(
                "rtmpIngest",
                false
            )
        ) {
            ismActivityScrollableMultiliveStreamsBinding.rlStreams.setBackgroundResource(R.drawable.stream_gaming_background)

            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.BLACK

            ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.updateGamingMode(true)
            ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.createInitiateGamingView(
                this,
                isBroadcaster,
                this
            )
            pkButtonVisibility(false)
            ismActivityScrollableMultiliveStreamsBinding.ivAnalytics.visibility = View.GONE
            ismActivityScrollableMultiliveStreamsBinding.ivSettings.visibility = View.INVISIBLE
        } else {
            ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.updateGamingMode(false)
        }


        if (isBroadcaster) {
            if (intent.getBooleanExtra("publishRequired", true)) {
                scrollableMultiliveStreamsPresenter.updateBroadcastingStatus(true, streamId, false)
            } else {
                if (intent.getBooleanExtra(
                        "selfHosted",
                        false
                    ) && intent.getBooleanExtra("rtmpIngest", false)
                ) {
                    ismActivityScrollableMultiliveStreamsBinding.ivRtmpDetails.visibility =
                        View.VISIBLE
                    ismActivityScrollableMultiliveStreamsBinding.ivSettings.visibility =
                        View.INVISIBLE
                    ismActivityScrollableMultiliveStreamsBinding.tvDuration.visibility =
                        View.INVISIBLE
                    ismActivityScrollableMultiliveStreamsBinding.ivSwitchCamera.visibility =
                        View.GONE
                    showRtmpDetails()

                    // join as viewer
                    multiLiveBroadcastOperations.joinBroadcastAsync(
                        this,
                        intent.getStringExtra("rtcToken")!!, hdBroadcast, !audioOnly,
                        false, IsometrikStreamSdk.getInstance().userSession.userId
                    )

                } else {

                    if (isAdmin) {
                        ismActivityScrollableMultiliveStreamsBinding.incDisclaimer.rlDisclaimer.visibility =
                            View.VISIBLE
                        ismActivityScrollableMultiliveStreamsBinding.tvCountDown.visibility =
                            View.VISIBLE
                        ismActivityScrollableMultiliveStreamsBinding.tvCountDown.downSecond(2)
                        ismActivityScrollableMultiliveStreamsBinding.tvCountDown.setOnTimeDownListener(
                            object : DownTimeWatcher {
                                override fun onTime(num: Int) {}
                                override fun onLastTime(num: Int) {}
                                override fun onLastTimeFinish(num: Int) {
                                    ismActivityScrollableMultiliveStreamsBinding.tvCountDown.visibility =
                                        View.GONE
                                    countDownCompleted = true
                                    if (channelJoined) {
                                        if (!intent.getBooleanExtra("publishRequired", true)) {
                                            if (!isFinishing && !isDestroyed) {
                                                dismissAllDialogs()
                                                showGoLiveView(goLiveDialog)
                                            }
                                        }
                                    }
                                }
                            })
                    }
                    onBroadcastStatusUpdated(
                        true,
                        false,
                        intent.getStringExtra("rtcToken")!!,
                        isModerator
                    )
                }
            }
        } else {
            scrollableMultiliveStreamsPresenter.joinAsViewer(streamId, false)
            pkButtonVisibility(false)
        }
        alertProgress = AlertProgress()
        ismActivityScrollableMultiliveStreamsBinding.rvPredefinedMessages.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        ismActivityScrollableMultiliveStreamsBinding.rvPredefinedMessages.adapter =
            PredefinedMessagesAdapter(PredefinedMessagesUtil.getPredefinedMessages(), this)
        initiatorName = intent.getStringExtra("initiatorName")
        if (isAdmin) {
            initiatorName = getString(R.string.ism_you, initiatorName)
        }
        initiatorIdentifier = intent.getStringExtra("initiatorIdentifier")
        initiatorImageUrl = intent.getStringExtra("initiatorImage")
        membersCount = intent.getIntExtra("membersCount", 0)
        viewersCount = intent.getIntExtra("viewersCount", 0)
        ismActivityScrollableMultiliveStreamsBinding.tvLiveStreamStatus.text =
            getString(R.string.ism_connecting_indicator)
        updateStreamInfo(null)
        streamsLayoutManager = LinearLayoutManager(this)
        ismActivityScrollableMultiliveStreamsBinding.rvStreams.layoutManager = streamsLayoutManager
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(ismActivityScrollableMultiliveStreamsBinding.rvStreams)

        streamsAdapter = ScrollableStreamsAdapter(this, streams)
        ismActivityScrollableMultiliveStreamsBinding.rvStreams.addOnScrollListener(
            streamsRecyclerViewOnScrollListener
        )
        ismActivityScrollableMultiliveStreamsBinding.rvStreams.adapter = streamsAdapter

        //Disable scrolls for publisher
        ismActivityScrollableMultiliveStreamsBinding.rvStreams.suppressLayout(isBroadcaster)
        if (isBroadcaster) {
            try {
                GlideApp.with(this).load(intent.getStringExtra("streamImage"))
                    .transform(CenterCrop())
                    .into(ismActivityScrollableMultiliveStreamsBinding.ivStreamImage)
            } catch (ignore: IllegalArgumentException) {
            } catch (ignore: NullPointerException) {
            }
            ismActivityScrollableMultiliveStreamsBinding.ivStreamImage.visibility = View.VISIBLE
        } else {
            val streamPosition = intent.getIntExtra("streamPosition", 0)
            try {
                //As view holder at that position was not inflated(due to notify dataset changed) returning null for findViewHolderForAdapterPosition method
                Handler(Looper.myLooper()!!).postDelayed({
                    updateStreamPreviewImageVisibility(
                        streamPosition,
                        true,
                        streamId
                    )
                }, 100)
            } catch (ignore: Exception) {
            }
            ismActivityScrollableMultiliveStreamsBinding.rvStreams.scrollToPosition(
                intent.getIntExtra(
                    "streamPosition",
                    0
                )
            )
        }
        ismActivityScrollableMultiliveStreamsBinding.refresh.setOnRefreshListener { fetchLatestStreams() }

        /*
         * Apply beautify options.
         */ismActivityScrollableMultiliveStreamsBinding.ivBeautify.setOnClickListener { view1: View? ->
            beautificationApplied = !beautificationApplied
            ismActivityScrollableMultiliveStreamsBinding.ivBeautify.isSelected =
                beautificationApplied
//            isometrik.multiBroadcastOperations.applyBeautifyOptions(beautificationApplied)
        }/*
         * Exit in case there are no live streams.
         */
        ismActivityScrollableMultiliveStreamsBinding.ibExit.setOnClickListener { view1: View? -> onBackPressed() }

        /*
         * Toggle camera between from and back camera.
         */ismActivityScrollableMultiliveStreamsBinding.ivSwitchCamera.setOnClickListener { view1: View? -> multiLiveBroadcastOperations.switchCamera() }

        /*
         * Send text message in a stream group.
         */ismActivityScrollableMultiliveStreamsBinding.ivSendMessage.setOnClickListener { view1: View? ->
            if (ismActivityScrollableMultiliveStreamsBinding.etSendMessage.text != null && ismActivityScrollableMultiliveStreamsBinding.etSendMessage.text.toString()
                    .trim { it <= ' ' }.isNotEmpty()
            ) {
                val textMessage =
                    ismActivityScrollableMultiliveStreamsBinding.etSendMessage.text.toString()
                        .trim { it <= ' ' }
                scrollableMultiliveStreamsPresenter.sendMessage(
                    streamId,
                    textMessage,
                    MessageTypeEnum.NormalMessage.value,
                    0,
                    "",
                    ""
                )
                ismActivityScrollableMultiliveStreamsBinding.etSendMessage.setText("")
            }
        }/*
         * Opens the settings popup based on the role, whether publisher or audience{@link
         * SettingsFragment}.
         *
         * @see SettingsFragment
         */
        ismActivityScrollableMultiliveStreamsBinding.ivSettings.setOnClickListener { view1: View? ->
            if (!isFinishing && !isDestroyed && !settingsFragment!!.isAdded) {
                dismissAllDialogs()
                settingsFragment!!.updateParameters(
                    isAdmin,
                    isBroadcaster,
                    streamId,
                    audioOnly,
                    this,
                    ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.localStreamerUid,
                    "",
                    isPkBattle
                )
                settingsFragment!!.updateMultiLiveBroadcastOperator(multiLiveBroadcastOperations)
                settingsFragment!!.show(supportFragmentManager, SettingsFragment.TAG)
            }
        }/*
         * Stop publishing in the stream group{@link EndLeaveFragment} or leave a broadcast or end a
         * broadcast popup.
         *
         * @see EndLeaveFragment
         */
        ismActivityScrollableMultiliveStreamsBinding.ivEndStream.setOnClickListener { view1: View? ->
            if (isBroadcaster) {
                if (!isFinishing && !isDestroyed && !endLeaveFragment!!.isAdded) {
                    dismissAllDialogs()
                    endLeaveFragment!!.updateParameters(isAdmin)
                    endLeaveFragment!!.show(supportFragmentManager, EndLeaveFragment.TAG)
                }
            } else {
                val bottomSheetFragment = EndStreamBottomSheetFragment()
                bottomSheetFragment.updateStreamId(
                    /*walletUserId*/ initiatorId ?: "",
                    streamId.orEmpty(),
                    isBroadcaster,
                    true
                )
                bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
            }
        }

        /*
         * Send a like in the stream group.
         */
        ismActivityScrollableMultiliveStreamsBinding.ivLike.setOnClickListener { view1: View? ->
            scrollableMultiliveStreamsPresenter.sendMessage(
                streamId,
                Constants.LIKE_URL,
                MessageTypeEnum.HeartMessage.value,
                0,
                "",
                ""
            )
        }


        /*
         * Show the EffectsFragment{@link EffectsFragment} to allow application of ar filters or audio
         * effects.
         *
         * @see EffectsFragment
         */ismActivityScrollableMultiliveStreamsBinding.ivEffects.setOnClickListener { view1: View? ->
            if (!isFinishing && !isDestroyed && !effectsFragment!!.isAdded) {
                dismissAllDialogs()
                effectsFragment!!.updateParameters(true, !audioOnly)
                effectsFragment!!.show(supportFragmentManager, EffectsFragment.TAG)
            }
        }/*
         * Show the GiftsFragment{@link GiftsFragment} to send gifts.
         *
         * @see GiftsFragment
         */
        ismActivityScrollableMultiliveStreamsBinding.ivGifts.setOnClickListener { view1: View? ->
            if (!isFinishing && !giftsPagerFragment!!.isAdded) {
                dismissAllDialogs()
                var receiverId: String? = /*walletUserId*/ initiatorId
                var receiverStreamId: String? = streamId
                var receiverStreamUserId = initiatorId
                var reciverUserType = "publisher"
                val currentHostId: String =
                    ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.getCurrentHostId()
                if (currentHostId.isNotEmpty() && isPkBattle) {
                    if (isPkViewerFromHost) {
                        // initiatorId will be real HOST id
                        if (currentHostId == initiatorId) {
                            // just in pk battle
                            //  current host will get gift no need to change anything
                        } else {
                            // pk battle on-going
                            // viewer change host and sending gift to real co-publisher
                            receiverStreamId = secondUserStreamId
                            receiverStreamUserId = secondUserStreamUserId
                            receiverId = secondUserStreamUserId
                            reciverUserType = "co-publisher"
                        }
                    } else {
                        // initiatorId will be real CO-HOST id
                        if (currentHostId == initiatorId) {
                            // just in pk battle
                            //  current co-host will get gift
                            receiverStreamId = intent.getStringExtra("streamId")
                            receiverStreamUserId = intent.getStringExtra("initiatorId")
                            receiverId = /*intent.getStringExtra("walletUserId")*/
                                receiverStreamUserId
                            reciverUserType = "co-publisher"
                        } else {
                            // pk battle on-going
                            // viewer click on make host and sending gift to real publisher
                            receiverStreamId = firstUserStreamId
                            receiverStreamUserId = firstUserStreamUserId
                            receiverId = firstUserStreamUserId
                            reciverUserType = "publisher"
                        }
                    }
                }

                Log.e("Gifts", "receiverId: " + receiverId)
                giftsPagerFragment!!.updateParameters(
                    this,
                    streamId,
                    receiverId,
                    initiatorName,
                    receiverStreamId,
                    receiverStreamUserId
                )
                if (isPkBattle) {
                    giftsPagerFragment!!.updatePkParameters(
                        isPkBattle,
                        pkid,
                        receiverId,
                        scrollableMultiliveStreamsPresenter.getUserFullNameByStreamUserId(
                            currentHostId
                        ),
                        pkInvitedId,
                        reciverUserType
                    )
                }
                giftsPagerFragment!!.show(
                    supportFragmentManager,
                    GiftsPagerFragment.TAG
                )
            }
        }


        /*
         * Open MembersFragment{@link MembersFragment} to show list of
         * members of the stream group.
         *
         * @see MembersFragment
         */
        ismActivityScrollableMultiliveStreamsBinding.tvNoOfMembers.setOnClickListener { view1: View? ->
            if (!isFinishing && !isDestroyed && !membersFragment!!.isAdded) {
                dismissAllDialogs()
                membersFragment!!.updateParameters(
                    streamId,
                    isAdmin,
                    ismActivityScrollableMultiliveStreamsBinding.tvNoOfMembers.text.toString(),
                    isModerator
                )
                membersFragment!!.show(supportFragmentManager, MembersFragment.TAG)
            }
        }

        /*
         * Open ViewersFragment{@link ViewersFragment} to show list
         * of viewers of the stream group.
         *
         * @see ViewersFragment
         */ismActivityScrollableMultiliveStreamsBinding.rlViewers.setOnClickListener { view1: View? ->
            if (!isFinishing && !isDestroyed && !viewersFragment!!.isAdded) {
                dismissAllDialogs()
                viewersFragment!!.updateParameters(
                    streamId,
                    viewersCount.toString(),
                    scrollableMultiliveStreamsPresenter.getMemberIds(),
                    isModerator
                )
                viewersFragment!!.show(supportFragmentManager, ViewersFragment.TAG)
            }
        }
        ismActivityScrollableMultiliveStreamsBinding.llViewersList.llViewers.setOnClickListener { view1: View? ->
            if (!isFinishing && !isDestroyed && !viewersFragment!!.isAdded) {
                dismissAllDialogs()
                viewersFragment!!.updateParameters(
                    streamId,
                    viewersCount.toString(),
                    scrollableMultiliveStreamsPresenter.getMemberIds(),
                    isModerator
                )
                viewersFragment!!.show(supportFragmentManager, ViewersFragment.TAG)
            }
        }


        ismActivityScrollableMultiliveStreamsBinding.ivRequest.setOnClickListener { view1: View? ->
            if (canWeAddMoreMember()) {
                onRequestMemberClick()
            }
        }
        ismActivityScrollableMultiliveStreamsBinding.rlInitiator.setOnClickListener { view1: View? ->
            if (!isFinishing && !isDestroyed && !userInfoFragment!!.isAdded) {
                dismissAllDialogs()
                userInfoFragment!!.updateParameters(
                    initiatorName,
                    initiatorIdentifier,
                    initiatorImageUrl
                )
                userInfoFragment!!.show(supportFragmentManager, UserInfoFragment.TAG)
            }
        }/*
         * Switch profile from a viewer to a broadcaster
         */
        ismActivityScrollableMultiliveStreamsBinding.ivJoin.setOnClickListener { view1: View? -> switchProfile() }

        /*
         * Hide live stream disclaimer/warning text
         */ismActivityScrollableMultiliveStreamsBinding.incDisclaimer.tvGotIt.setOnClickListener { view1: View? ->
            ismActivityScrollableMultiliveStreamsBinding.incDisclaimer.rlDisclaimer.visibility =
                View.GONE
        }/*
         * Open AddMembersFragment{@link AddMembersFragment} to add a user/viewer as member to a
         * broadcast.
         *
         * @see AddMembersFragment
         */
        ismActivityScrollableMultiliveStreamsBinding.ivAddMember.setOnClickListener { view1: View? ->
            if (isAdmin) {
                if (!isFinishing && !isDestroyed && !addMembersFragment!!.isAdded) {
                    dismissAllDialogs()
                    addMembersFragment!!.updateParameters(
                        streamId,
                        scrollableMultiliveStreamsPresenter.getMemberIds()
                    )
                    addMembersFragment!!.show(supportFragmentManager, AddMembersFragment.TAG)
                }
            }
        }
        ismActivityScrollableMultiliveStreamsBinding.tvNoOfProducts.setOnClickListener { view1: View? ->
            if (!isFinishing && !isDestroyed && !featuredProductsFragment!!.isAdded) {
                dismissAllDialogs()
                featuredProductsFragment!!.updateParameters(
                    streamId,
                    isAdmin,
                    initiatorName,
                    streamDescription,
                    initiatorImageUrl,
                    this,
                    false
                )
                featuredProductsFragment!!.show(
                    supportFragmentManager,
                    FeaturedProductsFragment.TAG
                )
            }
        }
        ismActivityScrollableMultiliveStreamsBinding.ivCart.setOnClickListener { v: View? ->
            if (!isFinishing && !isDestroyed && !cartItemsFragment!!.isAdded) {
                dismissAllDialogs()
                cartItemsFragment!!.updateParameters(streamId, initiatorName, this)
                cartItemsFragment!!.show(supportFragmentManager, CartItemsFragment.TAG)
            }
        }
        ismActivityScrollableMultiliveStreamsBinding.tvFeaturing.setOnClickListener { view1: View? ->
            if (!isFinishing && !isDestroyed && !featuringProductFragment!!.isAdded) {
                dismissAllDialogs()
                featuringProductFragment!!.updateParameters(streamId, isAdmin, this)
                featuringProductFragment!!.show(
                    supportFragmentManager,
                    FeaturingProductFragment.TAG
                )
            }
        }
        ismActivityScrollableMultiliveStreamsBinding.ivModerator.setOnClickListener { view1: View? ->
            if (!isFinishing && !isDestroyed && !leaveAsModeratorFragment!!.isAdded) {
                dismissAllDialogs()
                leaveAsModeratorFragment!!.updateParameters(streamId, isAdmin, this)
                leaveAsModeratorFragment!!.show(
                    supportFragmentManager,
                    LeaveAsModeratorFragment.TAG
                )
            }
        }
        ismActivityScrollableMultiliveStreamsBinding.ivRtmpDetails.setOnClickListener { view1: View? ->
            showRtmpDetails()
        }

        ismActivityScrollableMultiliveStreamsBinding.tvMakeHost.setOnClickListener {
            userSwipeHostClicked(0)
        }

        /*
        * show Live broadcaster to invite PK challenge
        */
        ismActivityScrollableMultiliveStreamsBinding.ivPk.setOnClickListener { view1 ->

            if (!isFinishing && !inviteUserFragment!!.isAdded) {
                dismissAllDialogs()
                inviteUserFragment!!.updateParameters(
                    streamId,
                    scrollableMultiliveStreamsPresenter.getMemberIds(),
                    this
                )
                inviteUserFragment!!.show(supportFragmentManager, InviteUserFragment.TAG)
            }
        }


        /*
         * <--- End PK --->
         *
         * @see
         */
        ismActivityScrollableMultiliveStreamsBinding.ivEndPK.setOnClickListener { view1 ->
            if (!isFinishing && !endPkFragment!!.isAdded()) {
                dismissAllDialogs()
                endPkFragment!!.updateText(isPKOnGoing)
                endPkFragment!!.show(supportFragmentManager, EndPkFragment.TAG)
            }
        }
        ismActivityScrollableMultiliveStreamsBinding.incShimmer.ivEndStreamShimmer.setOnClickListener {
            finish()
        }

        subscribeTopicIsometrik()

        scrollableMultiliveStreamsPresenter.fetchGifDataAdvance()
    }


    /**
     * To pass the touch to view underneath
     *
     * @param ev motion event
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (audioOnly) {
            ismActivityScrollableMultiliveStreamsBinding.audioGridContainer.dispatchTouchEvent(
                ev
            )
        } else {
            ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.dispatchTouchEvent(
                ev
            )
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * Fetch latest messages sent in the stream group
     */
    private fun fetchLatestMessages() {
        try {
            scrollableMultiliveStreamsPresenter.requestStreamMessagesData(streamId, 0, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Remove message from a stream group.
     *
     * @param messageId the id of the message to be removed from the stream group
     * @param timestamp the timestamp at which message to be removed was sent
     */
    fun removeMessage(messageId: String, timestamp: Long) {
        if (messageId.length == 24) {
            scrollableMultiliveStreamsPresenter.removeMessage(streamId, messageId, timestamp)
        } else {
            Toast.makeText(this, getString(R.string.ism_delete_unsent_message), Toast.LENGTH_SHORT)
                .show()
        }
    }

    /**
     * Fetch message replies for a message in the stream group.
     *
     * @param messageId         the id of the message for which to fetch the replies in the stream group
     * @param timestamp         the timestamp at which message for which to fetch the replies was sent
     * @param allowSendingReply whether to allow user to reply to a message or not
     * @param parentMessageText text of the parent message
     */
    fun fetchMessageReplies(
        messageId: String,
        timestamp: Long,
        allowSendingReply: Boolean,
        parentMessageText: String?
    ) {
        if (messageId.length == 24) {
            if (!isFinishing && !isDestroyed && !messageRepliesFragment!!.isAdded) {
                dismissAllDialogs()
                messageRepliesFragment!!.updateParameters(
                    streamId,
                    messageId,
                    parentMessageText,
                    timestamp,
                    allowSendingReply,
                    isModerator,
                    this
                )
                messageRepliesFragment!!.show(supportFragmentManager, MessageRepliesFragment.TAG)
            }
        } else {
            Toast.makeText(
                this,
                getString(R.string.ism_reply_to_unsent_message),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * @param messages       the messages fetched in the stream group
     * @param refreshRequest whether the request for fetching messages is for latest messages or
     * paging
     *
     *
     * [MultiliveStreamsContract.View.onStreamMessagesDataReceived]
     */
    override fun onStreamMessagesDataReceived(
        messages: ArrayList<MessagesModel>,
        refreshRequest: Boolean
    ) {

        //To avoid missing the message of self publish status change
        if (refreshRequest) {
            val size = this.messages.size
            for (i in size - 1 downTo 0) {
                if (this.messages[i].messageItemType != MessageTypeEnum.PresenceMessage.value) {
                    this.messages.removeAt(i)
                }
            }
            //this.messages.clear();
        }
        CoroutineScope(Main).launch {
            this@MultiliveStreamsActivity.messages.addAll(0, messages)
            messagesAdapter!!.notifyDataSetChanged()
            if (refreshRequest) scrollToLastMessage()
        }
    }

    /**
     * @param message    the message to be shown in the popup on stream group being offline
     * @param dialogType the dialog type to identify type of layout to inflate to be shown in the
     * popup
     * @param streamId   the id of the stream which is no longer live
     *
     *
     * [MultiliveStreamsContract.View.onStreamOffline]
     */
    override fun onStreamOffline(
        message: String,
        dialogType: Int,
        streamId: String,
        kickout: Boolean
    ) {
        //kickout is not used as of now
        dismissAllDialogs()

        if (isPkViewerOnGoing /*|| isPkStreamer && !isPkInitiator && streamId != intent.getStringExtra(
                "streamId"
            )*/
        ) {
            // for pk viewer need to skip as we getting streamOffline event
            // for co-publisher getting 2 time stream offline event (publisher offline event when publisher stop broadcasting ) so need to continue own stream so ignor it
            return
        }
        if (this.streamId == streamId) {
            if (isBroadcaster) {
                /*
                * In group stream if initiator stop stream then show dialog
                * */
//                if (!isPkBattle) {
                alreadyStoppedPublishing = true
                lifecycleScope.launch(Main) {
                    if (!isAdmin) {
                        showAlertDialog(
                            getString(R.string.ism_stream_offline_initiator_stop),
                            StreamDialogEnum.StreamOffline.value,
                            isBroadcaster
                        )
                    }
                    unregisterListeners()
                }

            } else {
                onStreamEnded(streamId, true)
            }
            if (isPkBattle && isPkStreamer) {
                lifecycleScope.launch(Main) {
                    onBroadcastEnded()
                }
            } else {
                if (!isPkStreamer) {
                    lifecycleScope.launch(Main) {
                        showAlertDialog(
                            message,
                            dialogType,
                            isBroadcaster
                        )
                    }
                }
            }
        } else {
            if (!isPkBattle) {
                onStreamEnded(streamId, false)
            }
        }
    }

    /**
     * @param message    message to be shown of either stream no longer live,or being kicked out of
     * publisher's group
     * @param dialogType type of dialog,whether stream no longer live or being kicked out of
     * publisher's group
     */
    private fun showAlertDialog(message: String, dialogType: Int, isBroadcaster: Boolean) {
        if (!showingStreamDialog) {
            showingStreamDialog = true
            val alertDialog =
                StreamDialog.getStreamDialog(this@MultiliveStreamsActivity, message, dialogType)
            alertDialog.setPositiveButton(getString(R.string.ism_ok)) { dialog: DialogInterface?, which: Int ->
                if (isBroadcaster) {
                    onBackPressed()
                } else {
                    if (alert != null) {
                        alert!!.cancel()
                        showingStreamDialog = false
                        if (streams!!.size == 0) {
                            ismActivityScrollableMultiliveStreamsBinding.tvNoBroadcaster.visibility =
                                View.VISIBLE
                            ismActivityScrollableMultiliveStreamsBinding.rlStreams.visibility =
                                View.GONE
                            updateShimmerVisibility(false)
                        }
                    }
                }
            }
            alert = alertDialog.create()
            alert!!.setCancelable(false)
            alert!!.setCanceledOnTouchOutside(false)
            if (!isFinishing && !isDestroyed) {
                if (!unregisteredListeners) alert!!.show()
            }
        }
    }

    /**
     * @param messagesModel the messages model MessagesModel[                      ][MessagesModel] containing details of the message received
     * @see MessagesModel
     *
     *
     * {@link MultiliveStreamsContract.View.onTextMessageReceived
     */
    override fun onTextMessageReceived(messagesModel: MessagesModel) {
        CoroutineScope(Main).launch {
            messages.add(messagesModel)
            messagesAdapter!!.notifyItemInserted(messages.size - 1)
        }
        scrollToLastMessage()
    }

    override fun onGiftMessageReceived(messagesModel: MessagesModel?) {
        CoroutineScope(Main).launch {
            gifts.add(messagesModel!!)
            giftAdapter!!.notifyItemInserted(gifts.size - 1)
            updateGifCountDown()
        }
        scrollToLastGiftMessage()
    }

    fun updateGifCountDown() {
        if (countDownGiftHide == null) {
            countDownGiftHide = object : CountDownTimer(2000, 500) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() {
                    gifts.clear()
                    giftAdapter!!.notifyDataSetChanged()
                }
            }.start()
        } else {
            countDownGiftHide!!.cancel()
            countDownGiftHide!!.start()
        }
    }

    private fun scrollToLastGiftMessage() {
        CoroutineScope(Main).launch {
            try {
                Handler().postDelayed({
                    giftLayoutManager!!.scrollToPositionWithOffset(giftAdapter!!.itemCount - 1, 0)
                }, 500)
            } catch (e: java.lang.IndexOutOfBoundsException) {
                e.printStackTrace()
            } catch (e: java.lang.NullPointerException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * @param messagesModel the messages model MessagesModel[                      ][MessagesModel] containing details of the message sent
     * @see MessagesModel
     *
     *
     * {@link MultiliveStreamsContract.View.onTextMessageSent
     */
    override fun onTextMessageSent(messagesModel: MessagesModel) {
        CoroutineScope(Main).launch {
            messages.add(messagesModel)
            messagesAdapter!!.notifyItemInserted(messages.size - 1)
        }
        scrollToLastMessage()
    }

    /**
     * @param membersCount the members count in the stream group
     * @param viewersCount the viewers count in the stream group
     *
     *
     * [MultiliveStreamsContract.View.updateMembersAndViewersCount]
     */
    override fun updateMembersAndViewersCount(membersCount: Int, viewersCount: Int) {
        CoroutineScope(Main).launch {
            if (membersCount != -1) {
                ismActivityScrollableMultiliveStreamsBinding.tvNoOfMembers.text =
                    membersCount.toString()
            }
            if (viewersCount != -1) {
                ismActivityScrollableMultiliveStreamsBinding.tvNoOfViewers.text =
                    getString(R.string.ism_viewers_count, viewersCount.toString())
            }
        }
    }

    /**
     * @param messagesModel the messages model MessagesModel[                      ][MessagesModel] containing details of the presence message received,ex. viewer joined/left/removed/timeout
     * or
     * member  start/stop publish/left/removed/timeout
     * @see MessagesModel
     *
     *
     * {@link MultiliveStreamsContract.View.onPresenceMessageEvent
     */
    override fun onPresenceMessageEvent(messagesModel: MessagesModel) {
        CoroutineScope(Main).launch {
            messages.add(messagesModel)
            messagesAdapter!!.notifyItemInserted(messages.size - 1)
        }
        scrollToLastMessage()
    }

    /**
     * @param messageId the id of the message which has been deleted by a user
     * @param message   the message to act as place holder for the deleted message
     *
     *
     * [MultiliveStreamsContract.View.onMessageRemovedEvent]
     */
    override fun onMessageRemovedEvent(messageId: String, message: String) {
        CoroutineScope(Main).launch {
            val size = messages.size
            for (i in size - 1 downTo 0) {
                if (messages[i].messageItemType == MessageTypeEnum.NormalMessage.value && messages[i].messageId == messageId) {
                    val messagesModel = messages[i]
                    messagesModel.messageItemType = MessageTypeEnum.RemovedMessage.value
                    messagesModel.message = message
                    messages[i] = messagesModel
                    messagesAdapter!!.notifyItemChanged(i)
                    break
                }
            }
        }
    }

    /**
     * @param messageId          the id of the message which was successfully sent
     * @param temporaryMessageId the temporary message id(local unique identifier for the message)
     *
     *
     * [MultiliveStreamsContract.View.onMessageDelivered]
     */
    override fun onMessageDelivered(messageId: String, temporaryMessageId: String) {
        CoroutineScope(Main).launch {
            val size = messages.size
            for (i in size - 1 downTo 0) {
                if (messages[i].messageItemType == MessageTypeEnum.NormalMessage.value && messages[i].messageId == temporaryMessageId) {
                    val messagesModel = messages[i]
                    messagesModel.setDelivered(true)
                    messagesModel.messageId = messageId
                    messages[i] = messagesModel
                    messagesAdapter!!.notifyItemChanged(i)
                    break
                }
            }
        }
    }

    /**
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     *
     *
     * [MultiliveStreamsContract.View.onError]
     */
    override fun onError(errorMessage: String) {
        if (ismActivityScrollableMultiliveStreamsBinding.refresh.isRefreshing) {
            ismActivityScrollableMultiliveStreamsBinding.refresh.isRefreshing = false
        }
        hideProgressDialog()
        CoroutineScope(Main).launch {
            if (errorMessage != null) {
                Toast.makeText(this@MultiliveStreamsActivity, errorMessage, Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(
                    this@MultiliveStreamsActivity,
                    getString(R.string.ism_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Initializes the view to allow hearts to be rendered on sending or receiving of the likes event.
     *
     *
     * [MultiliveStreamsContract.View.initializeHeartsViews]
     */
    override fun initializeHeartsViews() {
        val config = HeartsRenderer.Config(3f, 0.35f, 2f)
        val bitmap = BitmapHelper.getBitmapFromVectorDrawable(
            this@MultiliveStreamsActivity,
            R.drawable.ism_ic_heart
        )
        model = HeartsView.Model(0, bitmap)
        ismActivityScrollableMultiliveStreamsBinding.heartsView.applyConfig(config)
    }

    /**
     * @see MultiliveStreamsContract.View.connectionStateChanged
     */
    override fun connectionStateChanged(connected: Boolean) {
        CoroutineScope(Main).launch {
            ismActivityScrollableMultiliveStreamsBinding.incConnectionState.tvConnectionState.visibility =
                if (connected) View.GONE else View.VISIBLE
            timerPaused = !connected
            if (connected) {
                ismActivityScrollableMultiliveStreamsBinding.tvLiveStreamStatus.text =
                    getString(R.string.ism_live_indicator)
                subscribeTopicIsometrik()
            } else {
                ismActivityScrollableMultiliveStreamsBinding.tvLiveStreamStatus.text =
                    getString(R.string.ism_offline_indicator)
            }
        }
    }

    /**
     * Shows flying hearts on receipt of the like event.
     *
     *
     * [MultiliveStreamsContract.View.onLikeEvent]
     */
    override fun onLikeEvent() {
        try {
            if (model != null) {
                CoroutineScope(Main).launch {
                    ismActivityScrollableMultiliveStreamsBinding.heartsView.emitHeart(model!!)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Shows gifts view as overlay on gifts received.
     *
     * @param giftsModel gifts model containing details of the gift received
     * @see GiftsModel
     *
     *
     * {@link MultiliveStreamsContract.View.onGiftEvent
     */
    override fun onGiftEvent(giftsModel: GiftsModel) {
        try {
            if (isPKOnGoing) {
                storeCoinLocallyForPk(giftsModel)
            }
            if (giftsModel.giftName == null) {
                Log.e(
                    "Gift Sent but Name",
                    " not found for " + giftsModel.senderId + " " + giftsModel.senderName
                )
                return
            }
            lifecycleScope.launch(Main) {
                if (giftsModel.giftName.equals("dragon", ignoreCase = true)) {
                    startVideo(R.raw.dragon)
                }
                if (giftsModel.giftName.equals("panther", ignoreCase = true)) {
                    startVideo(R.raw.panther)
                }
                if (giftsModel.giftName.equals("wolf", ignoreCase = true)) {
                    startVideo(R.raw.wolf)
                }
                if (giftsModel.giftName.equals("fil", ignoreCase = true)) {
                    startVideo(R.raw.fil)
                }
                if (giftsModel.giftName.equals("dinazorx", ignoreCase = true)) {
                    startVideo(R.raw.dinazorx)
                }
                if (giftsModel.giftName.equals("kadinmelek", ignoreCase = true)) {
                    startVideo(R.raw.kadinmelek)
                }
                if (giftsModel.giftName.equals("kurukafa", ignoreCase = true)) {
                    startVideo(R.raw.kurukafa)
                }
                if (giftsModel.giftName.equals("yavrudinazor", ignoreCase = true)) {
                    startVideo(R.raw.yavrudinazor)
                }
                if (giftsModel.giftName.equals("çakirdostum", ignoreCase = true)) {
                    startVideo(R.raw.cakirdostum)
                }
                if (giftsModel.giftName.equals("ghosthorse", ignoreCase = true)) {
                    startVideo(R.raw.ghosthorse)
                }
                if (giftsModel.giftName.equals("ghostrider", ignoreCase = true)) {
                    startVideo(R.raw.ghostrider)
                }
                if (giftsModel.giftName.equals("kaplan", ignoreCase = true)) {
                    startVideo(R.raw.kaplan)
                }
//                if (giftsModel.senderId == userSession.userId) {
//                    onGiftMessageReceived(
//                        MessagesModel(
//                            giftsModel
//                        )
//                    )
//                }
            }
        } catch (e: java.lang.Exception) {
        }
    }

    fun startVideo(giftId: Int) {
        ismActivityScrollableMultiliveStreamsBinding.giftVideo.scaleX = 1f
        ismActivityScrollableMultiliveStreamsBinding.giftVideo.scaleY = 1f
        KeyboardUtil.hideKeyboard(this)
        dismissAllDialogs()
        ismActivityScrollableMultiliveStreamsBinding.giftVideo.setVideoPath("android.resource://$packageName/$giftId")
        ismActivityScrollableMultiliveStreamsBinding.giftVideo.start()
        ismActivityScrollableMultiliveStreamsBinding.giftVideo.setZOrderOnTop(true)
        ismActivityScrollableMultiliveStreamsBinding.giftVideo.setZOrderMediaOverlay(true)
        ismActivityScrollableMultiliveStreamsBinding.giftVideo.visibility = View.VISIBLE
        ismActivityScrollableMultiliveStreamsBinding.relGiftVideo.visibility = View.VISIBLE
    }

    override fun onFullGiftEvent(url: String?) {
        url?.let {
            start3DGift(it)
        }
    }

    fun storeCoinLocallyForPk(giftsModel: GiftsModel) {
        lifecycleScope.launch(Main) {
            if (ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.pkInitiatorId.equals(
                    giftsModel.reciverStreamUserId
                )
            ) {
                initiatorTotalCoin += giftsModel.coinValue
            } else {
                guestTotalCoin += giftsModel.coinValue
            }
            updateWhoIsWinningUi(false)
        }
    }

    private fun start3DGift(url: String) {
        Log.e("GIF==>", "start3DGift")

        lifecycleScope.launch {

            // Cancel the previous job if it exists (replaces the current GIF)
            current3DGifJob?.cancel()

            // Load and display the random GIF
            current3DGifJob = showFullScreenGif(url)

            // Delay for 5 seconds or until the job is cancelled
            current3DGifJob?.join()
        }
    }

    private fun showFullScreenGif(gifUrl: String): Job {
        return lifecycleScope.launch {
            withContext(Main) {
                Log.e("GIF==>", "showFullScreenGif")
                ismActivityScrollableMultiliveStreamsBinding.ivFullGif.visibility = View.VISIBLE

                Glide.with(this@MultiliveStreamsActivity)
                    .asGif()
                    .load(gifUrl)
                    .into(ismActivityScrollableMultiliveStreamsBinding.ivFullGif)


            }

            // Delay for 5 seconds
            delay(5000)

            // Dismiss the GIF
            withContext(Main) {
                Log.e("GIF==>", "showFullScreenGif Hide")

                ismActivityScrollableMultiliveStreamsBinding.ivFullGif.visibility = View.GONE
            }
        }
    }

    /**
     * Join broadcast after successfully updating status as publishing.
     *
     *
     * [MultiliveStreamsContract.View.onBroadcastStatusUpdated]
     */
    override fun onBroadcastStatusUpdated(
        startBroadcast: Boolean,
        rejoinRequest: Boolean,
        rtcToken: String,
        isModerator: Boolean
    ) {
        if (isModerator != null) {
            this.isModerator = isModerator
            ismActivityScrollableMultiliveStreamsBinding.ivModerator.visibility =
                if (isModerator) View.VISIBLE else View.GONE
        }
        if (startBroadcast) {
            CoroutineScope(Main).launch {
                if (!rejoinRequest) {
                    multiLiveBroadcastOperations.joinBroadcastAsync(
                        this@MultiliveStreamsActivity, rtcToken,
                        hdBroadcast, !audioOnly, true,
                        IsometrikStreamSdk.getInstance().userSession.userId
                    )
                    startTimer()
                }
                if (!isAdmin || rejoinRequest) {
                    fetchLatestMessages()
                }
            }
        }
    }

    /**
     * Exit on failure of updating broadcast status
     *
     *
     * [MultiliveStreamsContract.View.onBroadcastingStatusUpdateFailed]
     */
    override fun onBroadcastingStatusUpdateFailed(startBroadcast: Boolean, errorMessage: String) {
        if (startBroadcast) {
            onError(errorMessage)
            try {
                Handler().postDelayed({ onBackPressed() }, 1000)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Exit after user has successfully left stream group
     *
     *
     * [MultiliveStreamsContract.View.onStreamGroupLeft]
     */
    override fun onStreamGroupLeft() {
        alreadyStoppedPublishing = true
        onBackPressed()
    }

    /**
     * Exit after initiator has successfully ended the live broadcast
     *
     *
     * [MultiliveStreamsContract.View.onBroadcastEnded]
     */
    override fun onBroadcastEnded() {
        alreadyStoppedPublishing = true
        onBackPressed()
    }

    /**
     * Send preset messages in a stream group,which included commonly used emojis and messages.
     *
     * @param message the message to be send in the stream group
     */
    fun sendPresetMessage(message: String?) {
        scrollableMultiliveStreamsPresenter.sendMessage(
            streamId,
            message,
            MessageTypeEnum.NormalMessage.value,
            0,
            "",
            ""
        )
    }

    /**
     * Scrolls message list to last message on receipt of new text or presence message to show latest
     * message received.
     */
    private fun scrollToLastMessage() {
        CoroutineScope(Main).launch {
            try {
                Handler().postDelayed({
                    messagesLayoutManager!!.scrollToPositionWithOffset(
                        messagesAdapter!!.itemCount - 1,
                        0
                    )
                }, 500)
            } catch (e: IndexOutOfBoundsException) {
                e.printStackTrace()
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        }
    }

    override fun onBackPressed() {
        unregisterListeners()
        try {
            super.onBackPressed()
        } catch (ignore: Exception) {
        }
    }

    override fun onDestroy() {
        unregisterListeners()
        unSubscribeTopicIsometrik()
        super.onDestroy()
    }

    /**
     * Cleanup all realtime isometrik event listeners that were added at time of exit
     */
    private fun unregisterListeners() {
        if (!unregisteredListeners) {
            dismissAllDialogs()
            unregisteredListeners = true
            stopTimer()
//            AppController.getInstance().setLiveStreamEventCallbackForScroll(null)
            scrollableMultiliveStreamsPresenter.unregisterStreamViewersEventListener()
            scrollableMultiliveStreamsPresenter.unregisterStreamMembersEventListener()
            scrollableMultiliveStreamsPresenter.unregisterStreamsEventListener()
            scrollableMultiliveStreamsPresenter.unregisterStreamMessagesEventListener()
            scrollableMultiliveStreamsPresenter.unregisterConnectionEventListener()
            scrollableMultiliveStreamsPresenter.unregisterPresenceEventListener()
            scrollableMultiliveStreamsPresenter.unregisterCopublishRequestsEventListener()
            scrollableMultiliveStreamsPresenter.unregisterModeratorEventListener()
            if (isBroadcaster) {
                if (!alreadyStoppedPublishing) {
                    scrollableMultiliveStreamsPresenter.updateBroadcastingStatus(
                        false,
                        streamId,
                        false
                    )
                }

            } else {
                scrollableMultiliveStreamsPresenter.leaveAsViewer(streamId, false)
                CoroutineScope(Main).launch {
                    multiLiveBroadcastOperations.leaveBroadcast()
                }
            }
            try {
                CoroutineScope(Main).launch {
                    ismActivityScrollableMultiliveStreamsBinding.audioGridContainer.release()
                    ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.release()
                }
            } catch (ignore: java.lang.Exception) {
            }
        }
    }

    /**
     * Start timer and updates the textview to update live duration for stream group's.
     */
    fun startTimer() {
        duration = TimeUtil.getDuration(streamStartTime)
        mTimer = Timer()
        mTimer?.schedule(object : TimerTask() {
            override fun run() {
                duration += 1 //increase every sec
                timerHandler.obtainMessage(INCREASE_TIMER).sendToTarget()
            }
        }, 0, 1000)
    }

    /**
     * Stop timer.
     */
    fun stopTimer() {
        if (mTimer != null) {
            mTimer!!.cancel()
        }
    }

    /**
     * Toggles visibility of the action buttons based on the role of the user,whether publisher or
     * audience.
     *
     * @param firstTime      whether toggle of action buttons has been requested for firsttime
     * @param streamSwitched whether the stream has been switched
     */
    private fun toggleRoleBasedActionButtonsVisibility(
        firstTime: Boolean,
        streamSwitched: Boolean
    ) {
        if (firstTime) {
            ismActivityScrollableMultiliveStreamsBinding.rlCameraParent.visibility = View.GONE
            ismActivityScrollableMultiliveStreamsBinding.playerSurfaceView.visibility =
                View.GONE
            if (audioOnly) {
                ismActivityScrollableMultiliveStreamsBinding.audioGridContainer.visibility =
                    View.VISIBLE
                ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.visibility =
                    View.GONE
            } else {
                ismActivityScrollableMultiliveStreamsBinding.audioGridContainer.visibility =
                    View.GONE
                ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.visibility =
                    View.VISIBLE
            }
        }
        if (isBroadcaster) {
            //Broadcaster
            ismActivityScrollableMultiliveStreamsBinding.ivSettings.visibility = View.VISIBLE
            ismActivityScrollableMultiliveStreamsBinding.ivLike.visibility = View.INVISIBLE
            ismActivityScrollableMultiliveStreamsBinding.ivGifts.visibility = View.GONE
            ismActivityScrollableMultiliveStreamsBinding.ivJoin.visibility = View.GONE
            if (isometrik.isARFiltersEnabled) {
                ismActivityScrollableMultiliveStreamsBinding.ivEffects.visibility = View.VISIBLE
            } else {
                ismActivityScrollableMultiliveStreamsBinding.ivEffects.visibility = View.GONE
            }
            if (isAdmin) {
                ismActivityScrollableMultiliveStreamsBinding.ivAddMember.visibility = View.VISIBLE
                ismActivityScrollableMultiliveStreamsBinding.ivAnalytics.visibility = View.VISIBLE
            } else {
                ismActivityScrollableMultiliveStreamsBinding.ivAddMember.visibility = View.GONE
                ismActivityScrollableMultiliveStreamsBinding.ivAnalytics.visibility = View.GONE
            }
            if (audioOnly) {
                ismActivityScrollableMultiliveStreamsBinding.ivBeautify.visibility = View.GONE
                ismActivityScrollableMultiliveStreamsBinding.ivSwitchCamera.visibility = View.GONE
                pkButtonVisibility(false)
            } else {
                if (isometrik.isARFiltersEnabled) {
                    ismActivityScrollableMultiliveStreamsBinding.ivBeautify.visibility =
                        View.VISIBLE
                } else {
                    ismActivityScrollableMultiliveStreamsBinding.ivBeautify.visibility = View.GONE
                }
                ismActivityScrollableMultiliveStreamsBinding.ivSwitchCamera.visibility =
                    View.VISIBLE
                if (firstTime) {
                    pkButtonVisibility(true)
                }
            }
        } else {
            //Audience
            ismActivityScrollableMultiliveStreamsBinding.ivLike.visibility = View.VISIBLE
            ismActivityScrollableMultiliveStreamsBinding.ivGifts.visibility = View.VISIBLE
            ismActivityScrollableMultiliveStreamsBinding.ivEffects.visibility = View.GONE
            pkButtonVisibility(false)
            ismActivityScrollableMultiliveStreamsBinding.ivBeautify.visibility = View.GONE
            ismActivityScrollableMultiliveStreamsBinding.ivSwitchCamera.visibility = View.GONE
            ismActivityScrollableMultiliveStreamsBinding.ivAddMember.visibility = View.GONE
            ismActivityScrollableMultiliveStreamsBinding.ivAnalytics.visibility = View.GONE
            //If given user can publish already
            if (scrollableMultiliveStreamsPresenter.getMemberIds()
                    .contains(userSession.userId)
            ) {
                ismActivityScrollableMultiliveStreamsBinding.ivJoin.visibility = View.VISIBLE
                showRejoinDialog()
            } else {
                ismActivityScrollableMultiliveStreamsBinding.ivJoin.visibility = View.GONE
            }
        }
        if (isPublic) {
            if (firstTime) {
                if (isBroadcaster) {
                    ismActivityScrollableMultiliveStreamsBinding.ivRequest.visibility =
                        if (isPkBattle) View.GONE else View.VISIBLE

                } else {
                    if (scrollableMultiliveStreamsPresenter.getMemberIds()
                            .contains(userSession.userId)
                    ) {
                        ismActivityScrollableMultiliveStreamsBinding.ivRequest.visibility =
                            if (isPkBattle) View.GONE else View.VISIBLE

                    } else {
                        //Check if given user has already requested copublish
                        scrollableMultiliveStreamsPresenter.checkCopublishRequestStatus(streamId)
                    }
                }
            } else {
                ismActivityScrollableMultiliveStreamsBinding.ivRequest.visibility =
                    if (isPkBattle) View.GONE else View.VISIBLE

            }
        } else {
            ismActivityScrollableMultiliveStreamsBinding.ivRequest.visibility =
                if (isPkBattle) View.GONE else View.VISIBLE

        }
        if (firstTime && streamSwitched) {
            settingsFragment!!.streamSwitched()
        }
        if (productsLinked) {
            ismActivityScrollableMultiliveStreamsBinding.tvFeaturing.visibility = View.VISIBLE
            ismActivityScrollableMultiliveStreamsBinding.tvNoOfProducts.setText(productsCount.toString())
            ismActivityScrollableMultiliveStreamsBinding.tvNoOfProducts.visibility = View.VISIBLE
        } else {
            ismActivityScrollableMultiliveStreamsBinding.tvFeaturing.visibility = View.GONE
            ismActivityScrollableMultiliveStreamsBinding.tvNoOfProducts.visibility = View.GONE
        }
        if (isPaid) {
//            ismActivityScrollableMultiliveStreamsBinding.tvCoinsRequiredToUnlock.setText(
//                coinsRequiredToUnlock.toString()
//            )
//            ismActivityScrollableMultiliveStreamsBinding.tvCoinsRequiredToUnlock.visibility =
//                View.VISIBLE
            pkButtonVisibility(false)
        } else {
//            ismActivityScrollableMultiliveStreamsBinding.tvCoinsRequiredToUnlock.visibility =
//                View.GONE
        }
    }

    private fun showRejoinDialog() {
        if (!isFinishing && !rejoinFragment!!.isAdded) {
            dismissAllDialogs()
            rejoinFragment!!.show(supportFragmentManager, RejoinFragment.TAG)
        }
    }

    /**
     * Callback from settings fragment based on role to toggle visibility of chat messages or control
     * buttons[SettingsFragment]
     *
     * @param action  whether visibility for chat messages or control buttons to be toggled
     * @param enabled whether visibility has been enabled or disabled
     * @see SettingsFragment
     *
     *
     * {@link SettingsActionCallback.onSettingsChange
     */
    override fun onSettingsChange(action: Int, enabled: Boolean) {
        if (action == SettingsActionEnum.ChatMessagesVisibilityToggleAction.value) {
            //Toggle messages visibility.
            if (enabled) {
                ismActivityScrollableMultiliveStreamsBinding.rvMessages.visibility = View.VISIBLE
                ismActivityScrollableMultiliveStreamsBinding.rvPredefinedMessages.visibility =
                    View.VISIBLE
            } else {
                ismActivityScrollableMultiliveStreamsBinding.rvMessages.visibility = View.INVISIBLE
                ismActivityScrollableMultiliveStreamsBinding.rvPredefinedMessages.visibility =
                    View.INVISIBLE
            }
        } else if (action == SettingsActionEnum.ControlButtonsVisibilityToggleAction.value) {
            //Toggle control buttons visibility.
            if (enabled) {
                toggleRoleBasedActionButtonsVisibility(false, false)
            } else {
                ismActivityScrollableMultiliveStreamsBinding.ivLike.visibility = View.INVISIBLE
                ismActivityScrollableMultiliveStreamsBinding.ivGifts.visibility = View.GONE
                ismActivityScrollableMultiliveStreamsBinding.ivEffects.visibility = View.GONE
                ismActivityScrollableMultiliveStreamsBinding.ivBeautify.visibility = View.GONE
                ismActivityScrollableMultiliveStreamsBinding.ivSwitchCamera.visibility = View.GONE
                ismActivityScrollableMultiliveStreamsBinding.ivAddMember.visibility = View.GONE
                ismActivityScrollableMultiliveStreamsBinding.ivJoin.visibility = View.GONE
                ismActivityScrollableMultiliveStreamsBinding.ivRequest.visibility = View.GONE
                ismActivityScrollableMultiliveStreamsBinding.ivCart.visibility = View.GONE
            }
        } else if (action == SettingsActionEnum.NetworkQualityVisibilityToggleAction.value) {
            //Toggle network quality visibility.
            if (audioOnly) {
                ismActivityScrollableMultiliveStreamsBinding.audioGridContainer.toggleNetworkQualityIndicator(
                    enabled
                )
            } else {
                ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.toggleNetworkQualityIndicator(
                    enabled
                )
            }
            scrollableMultiliveStreamsPresenter.isNetworkQualityIndicatorEnabled = enabled
        }
    }

    /**
     * [SettingsActionCallback.onRemoteUserMediaSettingsUpdated]
     */
    override fun onRemoteUserMediaSettingsUpdated(
        streamId: String,
        userId: String,
        uid: Int,
        userName: String,
        audio: Boolean,
        muted: Boolean
    ) {

        if (audio) {
            multiLiveBroadcastOperations.updateRemoteAudioStatus(muted, userName)
        } else {
            multiLiveBroadcastOperations.updateRemoteVideoStatus(muted, userName)
        }
    }

    /**
     * [SettingsActionCallback.removeMemberRequested]
     */
    override fun removeMemberRequested(streamId: String, userId: String) {
        if (remoteUserSettings!!.dialog != null && remoteUserSettings!!.dialog!!.isShowing && !remoteUserSettings!!.isRemoving) {
            remoteUserSettings!!.dismiss()
        }
        showProgressDialog(getString(R.string.ism_removing_member))
        scrollableMultiliveStreamsPresenter.requestRemoveMember(streamId, userId)
    }

    /**
     * [MultiliveStreamsContract.View.onMemberRemovedResult]
     */
    override fun onMemberRemovedResult(memberId: String) {
        hideProgressDialog()
    }

    /**
     * [MultiliveStreamsContract.View.onStreamJoined]
     */
    override fun onStreamJoined(
        numOfViewers: Int,
        rejoinRequest: Boolean,
        rtcToken: String,
        isModerator: Boolean
    ) {
        if (isModerator != null) {
            this.isModerator = isModerator
            ismActivityScrollableMultiliveStreamsBinding.ivModerator.visibility =
                if (isModerator) View.VISIBLE else View.GONE
        }
        CoroutineScope(Main).launch {
            if (!rejoinRequest) {
                multiLiveBroadcastOperations.joinBroadcastAsync(
                    this@MultiliveStreamsActivity, rtcToken,
                    hdBroadcast, !audioOnly, false,
                    IsometrikStreamSdk.getInstance().userSession.userId
                )
                startTimer()
            }
            fetchLatestMessages()
            if (numOfViewers != -1) {
                ismActivityScrollableMultiliveStreamsBinding.tvNoOfViewers.text =
                    getString(R.string.ism_viewers_count, numOfViewers.toString())
            }
        }
    }

    /**
     * Leave the stream group as member.
     */
    override fun leaveBroadcastRequested() {
        scrollableMultiliveStreamsPresenter.leaveStreamGroup(streamId)
    }

    /**
     * Stop publishing requested by a member.
     */
    override fun stopPublishingRequested() {
        onBackPressed()
    }

    /**
     * End broadcast requested.
     */
    override fun endBroadcastRequested() {
        if (isPkBattle && !isPkInitiator) {
            scrollableMultiliveStreamsPresenter.endPKRequested(pkInvitedId, true)
        } else {
            scrollableMultiliveStreamsPresenter.stopBroadcast(streamId, pkInvitedId)
        }
    }

    /**
     * [MultiliveStreamsContract.View.onCopublishRequestStatusFetched]
     */
    override fun onCopublishRequestStatusFetched(
        alreadyRequestedCopublish: Boolean,
        pending: Boolean,
        accepted: Boolean
    ) {
        this.alreadyRequestedCopublish = alreadyRequestedCopublish
        this.pending = pending
        this.accepted = accepted
        CoroutineScope(Main).launch {
            ismActivityScrollableMultiliveStreamsBinding.ivRequest.visibility = View.VISIBLE
        }
    }

    /**
     * [MultiliveStreamsContract.View.onStreamJoinError]
     */
    override fun onStreamJoinError(errorMessage: String) {
        onError(errorMessage)
        try {
            Handler().postDelayed({ onBackPressed() }, 1000)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * [MultiliveStreamsContract.View.onProfileSwitched]
     */
    override fun onProfileSwitched(rtcToken: String) {
        hideProgressDialog()
        isBroadcaster = true
        CoroutineScope(Main).launch {
            ismActivityScrollableMultiliveStreamsBinding.ivJoin.visibility = View.GONE

            //Disable scrolls for publisher
            ismActivityScrollableMultiliveStreamsBinding.rvStreams.suppressLayout(true)
            if (audioOnly) {
                ismActivityScrollableMultiliveStreamsBinding.audioGridContainer.release()
                multiLiveBroadcastOperations.switchProfileToBroadcaster(
                    applicationContext,
                    rtcToken,
                    hdBroadcast,
                    false
                )
            } else {
                ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.release()
                multiLiveBroadcastOperations.switchProfileToBroadcaster(
                    this@MultiliveStreamsActivity,
                    rtcToken,
                    hdBroadcast,
                    true
                )
            }
            removeViewerEvent(userSession.userId, viewersListModels.size - 1)
            toggleRoleBasedActionButtonsVisibility(false, false)
            scrollableMultiliveStreamsPresenter.updateBroadcasterStatus(true)
            updateJoinChatMessage()
        }
    }

    /**
     * Switch profile from a viewer to a broadcaster
     */
    fun switchProfile() {
        checkStreamingPermissions()
    }

    /**
     * [CopublishActionCallbacks.startPublishingOnCopublishRequestAccepted]
     */
    override fun startPublishingOnCopublishRequestAccepted() {
        if ((acceptedCopublishRequestFragment!!.dialog != null && acceptedCopublishRequestFragment!!.dialog!!.isShowing) && !acceptedCopublishRequestFragment!!.isRemoving) {
            acceptedCopublishRequestFragment!!.dismiss()
        }
        if ((copublishRequestStatusFragment!!.dialog != null && copublishRequestStatusFragment!!.dialog!!.isShowing) && !copublishRequestStatusFragment!!.isRemoving) {
            copublishRequestStatusFragment!!.dismiss()
        }
        switchProfile()
    }

    /**
     * [CopublishActionCallbacks.exitOnNoLongerBeingAMember]
     */
    override fun exitOnNoLongerBeingAMember() {
        onBackPressed()
    }

    /**
     * [CopublishActionCallbacks.exitOnCopublishRequestRejected]
     */
    override fun exitOnCopublishRequestRejected() {
        onBackPressed()
    }

    /**
     * [CopublishActionCallbacks.continueWatching]
     */
    override fun continueWatching() {
        if ((rejectedCopublishRequestFragment!!.dialog != null && rejectedCopublishRequestFragment!!.dialog!!.isShowing) && !rejectedCopublishRequestFragment!!.isRemoving) {
            rejectedCopublishRequestFragment!!.dismiss()
        }
        if ((copublishRequestStatusFragment!!.dialog != null && copublishRequestStatusFragment!!.dialog!!.isShowing) && !copublishRequestStatusFragment!!.isRemoving) {
            copublishRequestStatusFragment!!.dismiss()
        }
    }

    /**
     * [CopublishActionCallbacks.requestCopublish]
     */
    override fun requestCopublish() {
        if (requestCopublishFragment!!.dialog != null && requestCopublishFragment!!.dialog!!.isShowing && !requestCopublishFragment!!.isRemoving) {
            requestCopublishFragment!!.dismiss()
        }
        showProgressDialog(getString(R.string.ism_adding_request))
        scrollableMultiliveStreamsPresenter.addCopublishRequest(streamId)
    }

    /**
     * [CopublishActionCallbacks.deleteCopublishRequest]
     */
    override fun deleteCopublishRequest() {
        if ((copublishRequestStatusFragment!!.dialog != null && copublishRequestStatusFragment!!.dialog!!.isShowing) && !copublishRequestStatusFragment!!.isRemoving) {
            copublishRequestStatusFragment!!.dismiss()
        }
        showProgressDialog(getString(R.string.ism_removing_request))
        scrollableMultiliveStreamsPresenter.removeCopublishRequest(streamId)
    }

    /**
     * [UserFeedClickedCallback.userFeedClicked]
     */
    override fun userFeedClicked(uid: Int) {
        val members = scrollableMultiliveStreamsPresenter.getMemberDetails(streamId, uid)
        if (members != null) {
            if (!isAdmin && members.isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.ism_settings_unavailable),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                if (!isFinishing && !isDestroyed && !remoteUserSettings!!.isAdded) {
                    dismissAllDialogs()
                    try {
                        remoteUserSettings!!.updateParameters(
                            streamId,
                            isModerator,
                            members["userId"] as String?,
                            uid,
                            members["userName"] as String?,
                            members["audioMuted"] as Boolean,
                            members["videoMuted"] as Boolean,
                            audioOnly,
                            initiatorId,
                            intent.getBooleanExtra("selfHosted", false)
                        )
                        remoteUserSettings!!.show(supportFragmentManager, RemoteUserSettings.TAG)
                    } catch (ignore: NullPointerException) {
                    }
                }
            }
        }
    }

    /**
     * [MultiliveStreamsContract.View.onCopublishRequestAdded]
     */
    override fun onCopublishRequestAdded() {
        hideProgressDialog()
        alreadyRequestedCopublish = true
        pending = true
    }

    /**
     * [MultiliveStreamsContract.View.onCopublishRequestRemoved]
     */
    override fun onCopublishRequestRemoved() {
        hideProgressDialog()
        alreadyRequestedCopublish = false
        pending = false
    }

    /**
     * [MultiliveStreamsContract.View.onCopublishRequestAcceptedEvent]
     */
    override fun onCopublishRequestAcceptedEvent() {
        //No need to dismiss other dialog manually,as they will automatically be dismissed if new dialog is show
        if (!isFinishing && !isDestroyed && !acceptedCopublishRequestFragment!!.isAdded) {
            dismissAllDialogs()
            acceptedCopublishRequestFragment!!.updateParameters(
                initiatorImageUrl,
                initiatorName,
                this
            )
            acceptedCopublishRequestFragment!!.show(
                supportFragmentManager,
                AcceptedCopublishRequestFragment.TAG
            )
        }
        CoroutineScope(Main).launch {
            ismActivityScrollableMultiliveStreamsBinding.ivJoin.visibility = View.VISIBLE
        }
        pending = false
        accepted = true
    }

    /**
     * Accept copublish request.
     *
     * @param userId the user id
     */
    fun acceptCopublishRequest(userId: String?) {
        showProgressDialog(getString(R.string.ism_accepting_request))
        scrollableMultiliveStreamsPresenter.acceptCopublishRequest(userId, streamId)
    }

    /**
     * Decline copublish request.
     *
     * @param userId the user id
     */
    fun declineCopublishRequest(userId: String?) {
        showProgressDialog(getString(R.string.ism_declining_request))
        scrollableMultiliveStreamsPresenter.declineCopublishRequest(userId, streamId)
    }

    /**
     * [MultiliveStreamsContract.View.onCopublishRequestAcceptedApiResult]
     */
    override fun onCopublishRequestAcceptedApiResult(userId: String) {
        hideProgressDialog()
        updateCopublishChatMessage(userId)
    }

    /**
     * [MultiliveStreamsContract.View.onCopublishRequestDeclinedApiResult]
     */
    override fun onCopublishRequestDeclinedApiResult(userId: String) {
        hideProgressDialog()
        updateCopublishChatMessage(userId)
    }

    /**
     * [MultiliveStreamsContract.View.onMemberAdded]
     */
    override fun onMemberAdded(added: Boolean, initiatorName: String) {
        CoroutineScope(Main).launch {
            if (added) {
                ismActivityScrollableMultiliveStreamsBinding.ivJoin.visibility = View.VISIBLE
            } else {
                ismActivityScrollableMultiliveStreamsBinding.ivJoin.visibility = View.GONE
                if (isBroadcaster) {
                    onStreamOffline(
                        getString(R.string.ism_member_kicked_out, initiatorName),
                        StreamDialogEnum.KickedOut.value,
                        streamId!!,
                        true
                    )
                }
            }
        }
    }

    /**
     * [MultiliveStreamsContract.View.onCopublishRequestDeclinedEvent]
     */
    override fun onCopublishRequestDeclinedEvent() {
        //No need to dismiss other dialog manually,as they will automatically be dismissed if new dialog is show
        if (!isFinishing && !isDestroyed && !rejectedCopublishRequestFragment!!.isAdded) {
            dismissAllDialogs()
            rejectedCopublishRequestFragment!!.updateParameters(
                initiatorImageUrl,
                initiatorName,
                this
            )
            rejectedCopublishRequestFragment!!.show(
                supportFragmentManager,
                RejectedCopublishRequestFragment.TAG
            )
        }
        pending = false
        accepted = false
    }

    /**
     * [MultiliveStreamsContract.View.updateRemoteUserDetails]
     */
    override fun updateRemoteUserDetails(
        members: Map<String, String>,
        membersList: ArrayList<FetchMembersResult.StreamMember>
    ) {
        CoroutineScope(Main).launch {
            if (!scrollableMultiliveStreamsPresenter.memberIds.contains(userSession.userId) && ismActivityScrollableMultiliveStreamsBinding.ivJoin.visibility == VISIBLE) {
                ismActivityScrollableMultiliveStreamsBinding.ivJoin.visibility = View.GONE
            }
            if (audioOnly) {
                scrollableMultiliveStreamsPresenter.addRemoteUserDetailsForAudioOnlyStreaming(
                    membersList,
                    userSession.userId,
                    ismActivityScrollableMultiliveStreamsBinding.audioGridContainer,
                    this@MultiliveStreamsActivity
                )
            } else {

                // for pk if there is another video not rendering
//                arrangeLayoutWhenVideoRenderNotSameWithMemberCount();
                Log.e("updateRemoteUserDetails", "===>$isPkBattle")
                if (isPkBattle) {
                    for (member in membersList) {
                        if (member.admin) {
                            ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.setCurrentHostId(
                                member.userId
                            )
                            ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.setPkInitiatorId(
                                member.userId
                            )
                        } else {
                            ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.setPkCoPublisherId(
                                member.userId
                            )
                        }
                    }
                    updatePkUserProfile(membersList)
                } else {
                    for (member in membersList) {
                        if (member.admin) {
                            val uid = UserIdGenerator.getUid(member.userId)
                            ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.setAdminUId(
                                uid
                            )
                        }
                    }
                }
            }
        }
    }

    fun updatePkUserProfile(membersList: java.util.ArrayList<FetchMembersResult.StreamMember>) {
        subscribeTopicIsometrik()
        ismActivityScrollableMultiliveStreamsBinding.rvStreams.suppressLayout(true)
        CoroutineScope(Main).launch {
            for (member in membersList) {
                if (member.admin) {
                    setPkLeftProfileData(member, null)
                } else {
                    setPkRightProfileData(member, null)
                }
            }
            if (!isPkViewerFromHost) {
                clickedOnChangeHost(false)
            }
            setupForPKStuffUi()
            if (isPKOnGoing) {
                ismActivityScrollableMultiliveStreamsBinding.relPkBottomView.setVisibility(View.GONE)
                ismActivityScrollableMultiliveStreamsBinding.relPkBattleBottomView.setVisibility(
                    VISIBLE
                )
            } else {
                ismActivityScrollableMultiliveStreamsBinding.relPkBottomView.setVisibility(VISIBLE)
                ismActivityScrollableMultiliveStreamsBinding.relPkBattleBottomView.setVisibility(
                    View.GONE
                )
            }
        }
    }

    fun setPkLeftProfileData(
        member: FetchMembersResult.StreamMember?,
        streamMemberInfo: StreamMemberInfo?
    ) {
//        ismActivityScrollableMultiliveStreamsBinding.ivStar.setVisibility(View.GONE)
        if (member != null) {
            if (!PlaceholderUtils.isValidImageUrl(member.userProfileImageUrl)) {
                PlaceholderUtils.setTextRoundDrawable(
                    this,
                    member.userName,
                    ismActivityScrollableMultiliveStreamsBinding.siHostProfile,
                    0,
                    12
                )
                PlaceholderUtils.setTextRoundDrawable(
                    this,
                    member.userName,
                    ismActivityScrollableMultiliveStreamsBinding.siLeftProfile,
                    0,
                    12
                )
                PlaceholderUtils.setTextRoundDrawable(
                    this,
                    member.userName,
                    ismActivityScrollableMultiliveStreamsBinding.siLeftSpotProfile,
                    0,
                    12
                )
                PlaceholderUtils.setTextRoundDrawable(
                    this,
                    member.userName,
                    ismActivityScrollableMultiliveStreamsBinding.ivInitiatorImage,
                    13
                )
            } else {
                try {
                    Glide.with(this).load(member.userProfileImageUrl)
                        .placeholder(R.drawable.ism_default_profile_image) //                            .transform(new CircleCrop())
                        .into(ismActivityScrollableMultiliveStreamsBinding.siHostProfile)
                    Glide.with(this).load(member.userProfileImageUrl)
                        .placeholder(R.drawable.ism_default_profile_image) //                            .transform(new CircleCrop())
                        .into(ismActivityScrollableMultiliveStreamsBinding.siLeftProfile)
                    Glide.with(this).load(member.userProfileImageUrl)
                        .placeholder(R.drawable.ism_default_profile_image) //                            .transform(new CircleCrop())
                        .into(ismActivityScrollableMultiliveStreamsBinding.siLeftSpotProfile)
                } catch (e: java.lang.IllegalArgumentException) {
                    e.printStackTrace()
                } catch (e: java.lang.NullPointerException) {
                    e.printStackTrace()
                }
                try {
                    Glide.with(this).load(member.userProfileImageUrl)
                        .placeholder(R.drawable.ism_default_profile_image).transform(CircleCrop())
                        .into(ismActivityScrollableMultiliveStreamsBinding.ivInitiatorImage)
                } catch (ignore: Exception) {
                }
            }

//            ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.setCurrentHostId(member.getUserId());
            ismActivityScrollableMultiliveStreamsBinding.tvHostName.text = member.userName
            ismActivityScrollableMultiliveStreamsBinding.tvLeftUserName.text = member.userName
            ismActivityScrollableMultiliveStreamsBinding.tvInitiatorIdentifier.text =
                member.userName
            ismActivityScrollableMultiliveStreamsBinding.tvInitiatorName.setText(member.userName)
        } else {
            if (streamMemberInfo != null) {
                if (!PlaceholderUtils.isValidImageUrl(streamMemberInfo.userProfilePic)) {
                    PlaceholderUtils.setTextRoundDrawable(
                        this,
                        streamMemberInfo.userName,
                        ismActivityScrollableMultiliveStreamsBinding.siHostProfile,
                        0,
                        12
                    )
                    PlaceholderUtils.setTextRoundDrawable(
                        this,
                        streamMemberInfo.userName,
                        ismActivityScrollableMultiliveStreamsBinding.siLeftProfile,
                        0,
                        12
                    )
                    PlaceholderUtils.setTextRoundDrawable(
                        this,
                        streamMemberInfo.userName,
                        ismActivityScrollableMultiliveStreamsBinding.siLeftSpotProfile,
                        0,
                        12
                    )
                    PlaceholderUtils.setTextRoundDrawable(
                        this,
                        streamMemberInfo.userName,
                        ismActivityScrollableMultiliveStreamsBinding.ivInitiatorImage,
                        13
                    )
                } else {
                    try {
                        Glide.with(this).load(streamMemberInfo.userProfilePic)
                            .placeholder(R.drawable.ism_default_profile_image) //                            .transform(new CircleCrop())
                            .into(ismActivityScrollableMultiliveStreamsBinding.siHostProfile)
                        Glide.with(this).load(streamMemberInfo.userProfilePic)
                            .placeholder(R.drawable.ism_default_profile_image) //                            .transform(new CircleCrop())
                            .into(ismActivityScrollableMultiliveStreamsBinding.siLeftProfile)
                        Glide.with(this).load(streamMemberInfo.userProfilePic)
                            .placeholder(R.drawable.ism_default_profile_image) //                            .transform(new CircleCrop())
                            .into(ismActivityScrollableMultiliveStreamsBinding.siLeftSpotProfile)
                    } catch (e: java.lang.IllegalArgumentException) {
                        e.printStackTrace()
                    } catch (e: java.lang.NullPointerException) {
                        e.printStackTrace()
                    }
                    try {
                        Glide.with(this).load(streamMemberInfo.userProfilePic)
                            .placeholder(R.drawable.ism_default_profile_image)
                            .transform(CircleCrop())
                            .into(ismActivityScrollableMultiliveStreamsBinding.ivInitiatorImage)
                    } catch (ignore: java.lang.IllegalArgumentException) {
                    } catch (ignore: java.lang.NullPointerException) {
                    }
                }
                ismActivityScrollableMultiliveStreamsBinding.tvHostName.text =
                    streamMemberInfo.userName
                ismActivityScrollableMultiliveStreamsBinding.tvLeftUserName.text =
                    streamMemberInfo.userName
                ismActivityScrollableMultiliveStreamsBinding.tvInitiatorIdentifier.text =
                    streamMemberInfo.userName
                ismActivityScrollableMultiliveStreamsBinding.tvInitiatorName.setText(
                    streamMemberInfo.userName
                )
            }

        }
    }

    fun setPkRightProfileData(
        member: FetchMembersResult.StreamMember?,
        streamMemberInfo: StreamMemberInfo?
    ) {
        if (member != null) {
            if (member.userProfileImageUrl == null || member.userProfileImageUrl.isEmpty()) {
                PlaceholderUtils.setTextRoundDrawable(
                    this,
                    member.userName,
                    ismActivityScrollableMultiliveStreamsBinding.siGuestProfile,
                    0,
                    12
                )
                PlaceholderUtils.setTextRoundDrawable(
                    this,
                    member.userName,
                    ismActivityScrollableMultiliveStreamsBinding.siRightProfile,
                    0,
                    12
                )
                PlaceholderUtils.setTextRoundDrawable(
                    this,
                    member.userName,
                    ismActivityScrollableMultiliveStreamsBinding.siRightSpotProfile,
                    0,
                    12
                )
            } else {
                try {
                    Glide.with(this).load(member.userProfileImageUrl)
                        .placeholder(R.drawable.ism_default_profile_image) //                            .transform(new CircleCrop())
                        .into(ismActivityScrollableMultiliveStreamsBinding.siGuestProfile)
                    Glide.with(this).load(member.userProfileImageUrl)
                        .placeholder(R.drawable.ism_default_profile_image) //                            .transform(new CircleCrop())
                        .into(ismActivityScrollableMultiliveStreamsBinding.siRightProfile)
                    Glide.with(this).load(member.userProfileImageUrl)
                        .placeholder(R.drawable.ism_default_profile_image) //                            .transform(new CircleCrop())
                        .into(ismActivityScrollableMultiliveStreamsBinding.siRightSpotProfile)
                } catch (e: java.lang.IllegalArgumentException) {
                    e.printStackTrace()
                } catch (e: java.lang.NullPointerException) {
                    e.printStackTrace()
                }
            }
            ismActivityScrollableMultiliveStreamsBinding.tvGuestName.text = member.userName
            ismActivityScrollableMultiliveStreamsBinding.tvRightUserName.text = member.userName
        } else {
            if (streamMemberInfo != null) {
                if (streamMemberInfo.userProfilePic == null || streamMemberInfo.userProfilePic.isEmpty()) {
                    PlaceholderUtils.setTextRoundDrawable(
                        this,
                        streamMemberInfo.userName,
                        ismActivityScrollableMultiliveStreamsBinding.siGuestProfile,
                        0,
                        12
                    )
                    PlaceholderUtils.setTextRoundDrawable(
                        this,
                        streamMemberInfo.userName,
                        ismActivityScrollableMultiliveStreamsBinding.siRightProfile,
                        0,
                        12
                    )
                    PlaceholderUtils.setTextRoundDrawable(
                        this,
                        streamMemberInfo.userName,
                        ismActivityScrollableMultiliveStreamsBinding.siRightSpotProfile,
                        0,
                        12
                    )
                } else {
                    try {
                        Glide.with(this).load(streamMemberInfo.userProfilePic)
                            .placeholder(R.drawable.ism_default_profile_image) //                            .transform(new CircleCrop())
                            .into(ismActivityScrollableMultiliveStreamsBinding.siGuestProfile)
                        Glide.with(this).load(streamMemberInfo.userProfilePic)
                            .placeholder(R.drawable.ism_default_profile_image) //                            .transform(new CircleCrop())
                            .into(ismActivityScrollableMultiliveStreamsBinding.siRightProfile)
                        Glide.with(this).load(streamMemberInfo.userProfilePic)
                            .placeholder(R.drawable.ism_default_profile_image) //                            .transform(new CircleCrop())
                            .into(ismActivityScrollableMultiliveStreamsBinding.siRightSpotProfile)
                    } catch (e: java.lang.IllegalArgumentException) {
                        e.printStackTrace()
                    } catch (e: java.lang.NullPointerException) {
                        e.printStackTrace()
                    }
                }
                ismActivityScrollableMultiliveStreamsBinding.tvGuestName.text =
                    streamMemberInfo.userName
                ismActivityScrollableMultiliveStreamsBinding.tvRightUserName.text =
                    streamMemberInfo.userName
            }

        }
    }

    override fun clickedOnChangeHost(needToUpdateVar: Boolean) {
        if (needToUpdateVar) {
            afterPkNeedToResetHost = !afterPkNeedToResetHost
        }

        ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.swipeStreamersStream(
            scrollableMultiliveStreamsPresenter.getMembers()
        )
        swipePkUserProfile(scrollableMultiliveStreamsPresenter.getMembersInfo())

        Timer().schedule(object : TimerTask() {
            override fun run() {
                CoroutineScope(Main).launch { updateWhoIsWinningUi(false) }
            }
        }, 1000)
    }

    fun swipePkUserProfile(membersInfo: Map<Int?, StreamMemberInfo>) {
        CoroutineScope(Main).launch {
            var newHostId = ""

            for ((_, streamMemberInfo) in membersInfo) {
                if (streamMemberInfo.userId == ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.currentHostId) {
                    setPkRightProfileData(null, streamMemberInfo)
                } else {
                    newHostId = streamMemberInfo.userId
                    setPkLeftProfileData(null, streamMemberInfo)
                }
            }
            Log.e("set new CurrentHostId", "===>$newHostId")
            ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.setCurrentHostId(
                newHostId
            )
        }
    }

    fun updateWhoIsWinningUi(makeItVisible: Boolean) {
        var winningPercentage = 0
        if (initiatorTotalCoin != 0 || guestTotalCoin != 0) {
            winningPercentage = initiatorTotalCoin * 100 / (initiatorTotalCoin + guestTotalCoin)
        }

        if (!ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.isAdminIsLeftSide()) {
            ismActivityScrollableMultiliveStreamsBinding.tvCoinHost.setText(guestTotalCoin.toString() + "")
            ismActivityScrollableMultiliveStreamsBinding.tvCoinGuest.setText(initiatorTotalCoin.toString() + "")
            if (initiatorTotalCoin == 0 && guestTotalCoin == 0) {
                ismActivityScrollableMultiliveStreamsBinding.seekbarProgress.setProgress(50)
                ismActivityScrollableMultiliveStreamsBinding.linerIndicatorProgress.setProgress(50)
            } else {
                ismActivityScrollableMultiliveStreamsBinding.seekbarProgress.setProgress(100 - winningPercentage)
                ismActivityScrollableMultiliveStreamsBinding.linerIndicatorProgress.setProgress(100 - winningPercentage)
            }

            val paramLeft =
                ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT)
            paramLeft.matchConstraintPercentWidth = (100 - winningPercentage + 3).toFloat()
            ismActivityScrollableMultiliveStreamsBinding.viewLeftProgress.requestLayout()

            val paramRight =
                ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT)
            paramRight.matchConstraintPercentWidth = (winningPercentage + 3).toFloat()
            ismActivityScrollableMultiliveStreamsBinding.viewRightProgress.requestLayout()
        } else {
            ismActivityScrollableMultiliveStreamsBinding.tvCoinHost.setText(initiatorTotalCoin.toString() + "")
            ismActivityScrollableMultiliveStreamsBinding.tvCoinGuest.setText(guestTotalCoin.toString() + "")
            if (initiatorTotalCoin == 0 && guestTotalCoin == 0) {
                ismActivityScrollableMultiliveStreamsBinding.seekbarProgress.setProgress(50)
                ismActivityScrollableMultiliveStreamsBinding.linerIndicatorProgress.setProgress(50)
            } else {
                ismActivityScrollableMultiliveStreamsBinding.seekbarProgress.setProgress(
                    winningPercentage
                )
                ismActivityScrollableMultiliveStreamsBinding.linerIndicatorProgress.setProgress(
                    winningPercentage
                )
            }
            val paramLeft =
                ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT)
            paramLeft.matchConstraintPercentWidth = (winningPercentage + 3).toFloat()
            ismActivityScrollableMultiliveStreamsBinding.viewLeftProgress.requestLayout()

            val paramRight =
                ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT)
            paramRight.matchConstraintPercentWidth = (100 - winningPercentage + 3).toFloat()
            ismActivityScrollableMultiliveStreamsBinding.viewRightProgress.requestLayout()
        }
    }


    /**
     * Check permissions to switch start streaming.
     */
    fun checkStreamingPermissions() {
        //WRITE_EXTERNAL_STORAGE for agora logs
        if (ContextCompat.checkSelfPermission(
                this@MultiliveStreamsActivity,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this@MultiliveStreamsActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this@MultiliveStreamsActivity,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MultiliveStreamsActivity,
                    Manifest.permission.CAMERA
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MultiliveStreamsActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MultiliveStreamsActivity,
                    Manifest.permission.RECORD_AUDIO
                )
            ) {
                val snackbar = Snackbar.make(
                    ismActivityScrollableMultiliveStreamsBinding.rlRootLayout,
                    R.string.ism_permission_start_streaming,
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(getString(R.string.ism_ok)) { view: View? -> this.requestPermissions() }
                snackbar.show()
//                (snackbar.view.findViewById<View>(R.id.snackbar_text) as TextView).gravity =
//                    Gravity.CENTER_HORIZONTAL
            } else {
                requestPermissions()
            }
        } else {
            requestSwitchProfile()
        }
    }

    private fun showProgressDialog(message: String) {
        alertDialog = alertProgress!!.getProgressDialog(this, message)
        if (!isFinishing && !isDestroyed) alertDialog?.show()
    }

    private fun hideProgressDialog() {
        if (alertDialog != null && alertDialog!!.isShowing) alertDialog!!.dismiss()
    }

    /**
     * Dismisses all of the bottom sheet dialogs before showing a new dialog.
     */
    private fun dismissAllDialogs() {
        if (!isFinishing && !isDestroyed) {
            try {
                if (goLiveDialog != null && goLiveDialog!!.isShowing) {
                    goLiveDialog!!.dismiss()
                } else if (membersFragment!!.dialog != null && membersFragment!!.dialog!!.isShowing && !membersFragment!!.isRemoving) {
                    membersFragment!!.dismiss()
                } else if (requestsFragment!!.dialog != null && requestsFragment!!.dialog!!.isShowing && !requestsFragment!!.isRemoving) {
                    requestsFragment!!.dismiss()
                } else if (viewersFragment!!.dialog != null && viewersFragment!!.dialog!!.isShowing && !viewersFragment!!.isRemoving) {
                    viewersFragment!!.dismiss()
                } else if (settingsFragment!!.dialog != null && settingsFragment!!.dialog!!.isShowing && !settingsFragment!!.isRemoving) {
                    settingsFragment!!.dismiss()
                } else if (userInfoFragment!!.dialog != null && userInfoFragment!!.dialog!!.isShowing && !userInfoFragment!!.isRemoving) {
                    userInfoFragment!!.dismiss()
                } else if (effectsFragment!!.dialog != null && effectsFragment!!.dialog!!.isShowing && !effectsFragment!!.isRemoving) {
                    effectsFragment!!.dismiss()
                } else if (requestCopublishFragment!!.dialog != null && requestCopublishFragment!!.dialog!!.isShowing && !requestCopublishFragment!!.isRemoving) {
                    requestCopublishFragment!!.dismiss()
                } else if (copublishRequestStatusFragment!!.dialog != null && copublishRequestStatusFragment!!.dialog!!.isShowing && !copublishRequestStatusFragment!!.isRemoving) {
                    copublishRequestStatusFragment!!.dismiss()
                } else if (acceptedCopublishRequestFragment!!.dialog != null && acceptedCopublishRequestFragment!!.dialog!!.isShowing && !acceptedCopublishRequestFragment!!.isRemoving) {
                    acceptedCopublishRequestFragment!!.dismiss()
                } else if (rejectedCopublishRequestFragment!!.dialog != null && rejectedCopublishRequestFragment!!.dialog!!.isShowing && !rejectedCopublishRequestFragment!!.isRemoving) {
                    rejectedCopublishRequestFragment!!.dismiss()
                } else if (endLeaveFragment!!.dialog != null && endLeaveFragment!!.dialog!!.isShowing && !endLeaveFragment!!.isRemoving) {
                    endLeaveFragment!!.dismiss()
                } else if (giftsPagerFragment!!.dialog != null && giftsPagerFragment!!.dialog!!.isShowing && !giftsPagerFragment!!.isRemoving) {
                    giftsPagerFragment!!.dismiss()
                } else if (remoteUserSettings!!.dialog != null && remoteUserSettings!!.dialog!!.isShowing && !remoteUserSettings!!.isRemoving) {
                    remoteUserSettings!!.dismiss()
                } else if (featuredProductsFragment!!.dialog != null && featuredProductsFragment!!.dialog!!.isShowing && !featuredProductsFragment!!.isRemoving) {
                    featuredProductsFragment!!.dismiss()
                } else if (productDetailsFragment!!.dialog != null && productDetailsFragment!!.dialog!!.isShowing && !productDetailsFragment!!.isRemoving) {
                    productDetailsFragment!!.dismiss()
                } else if (productAddedToCartAlertFragment!!.dialog != null && productAddedToCartAlertFragment!!.dialog!!.isShowing && !productAddedToCartAlertFragment!!.isRemoving) {
                    productAddedToCartAlertFragment!!.dismiss()
                } else if (cartItemsFragment!!.dialog != null && cartItemsFragment!!.dialog!!.isShowing && !cartItemsFragment!!.isRemoving) {
                    cartItemsFragment!!.dismiss()
                } else if (featuringProductFragment!!.dialog != null && featuringProductFragment!!.dialog!!.isShowing && !featuringProductFragment!!.isRemoving) {
                    featuringProductFragment!!.dismiss()
                } else if (checkoutFragment!!.dialog != null && checkoutFragment!!.dialog!!.isShowing && !checkoutFragment!!.isRemoving) {
                    checkoutFragment!!.dismiss()
                } else if (moderatorsFragment!!.dialog != null && moderatorsFragment!!.dialog!!.isShowing && !moderatorsFragment!!.isRemoving) {
                    moderatorsFragment!!.dismiss()
                } else if (addModeratorsFragment!!.dialog != null && addModeratorsFragment!!.dialog!!.isShowing && !addModeratorsFragment!!.isRemoving) {
                    addModeratorsFragment!!.dismiss()
                } else if (leaveAsModeratorFragment!!.dialog != null && leaveAsModeratorFragment!!.dialog!!.isShowing && !leaveAsModeratorFragment!!.isRemoving) {
                    leaveAsModeratorFragment!!.dismiss()
                } else if (moderatorAddedOrRemovedAlertFragment!!.dialog != null && moderatorAddedOrRemovedAlertFragment!!.dialog!!.isShowing && !moderatorAddedOrRemovedAlertFragment!!.isRemoving) {
                    moderatorAddedOrRemovedAlertFragment!!.dismiss()
                } else if (messageRepliesFragment!!.dialog != null && messageRepliesFragment!!.dialog!!.isShowing && !messageRepliesFragment!!.isRemoving) {
                    messageRepliesFragment!!.dismiss()
                } else if (rtmpDetailsFragment!!.dialog != null && rtmpDetailsFragment!!.dialog!!.isShowing && !rtmpDetailsFragment!!.isRemoving) {
                    rtmpDetailsFragment!!.dismiss()
                } else if (inviteUserFragment!!.dialog != null && inviteUserFragment!!.dialog?.isShowing == true && !inviteUserFragment!!.isRemoving) {
                    inviteUserFragment!!.dismiss()
                } else if (pkInviteFragment!!.dialog != null && pkInviteFragment!!.dialog?.isShowing == true && !pkInviteFragment!!.isRemoving) {
                    pkInviteFragment!!.dismiss()
                } else if (pkChallengeSettingsFragment!!.dialog != null && pkChallengeSettingsFragment!!.dialog?.isShowing == true && !pkChallengeSettingsFragment!!.isRemoving) {
                    pkChallengeSettingsFragment!!.dismiss()
                } else if (changeHostFragment!!.dialog != null && changeHostFragment!!.dialog?.isShowing == true && !changeHostFragment!!.isRemoving) {
                    changeHostFragment!!.dismiss()
                } else if (endPkFragment!!.dialog != null && endPkFragment!!.dialog?.isShowing == true && !endPkFragment!!.isRemoving) {
                    endPkFragment!!.dismiss()
                } else if (rejoinFragment!!.dialog != null && rejoinFragment!!.dialog?.isShowing == true && !rejoinFragment!!.isRemoving) {
                    rejoinFragment!!.dismiss()
                }
            } catch (ignore: Exception) {
            }
        }
    }

    /**
     * [MultiliveStreamsContract.View.onLiveStreamsDataReceived]
     */
    override fun onLiveStreamsDataReceived(
        streams: ArrayList<LiveStreamsModel>,
        latestStreams: Boolean
    ) {
        if (!isBroadcaster) {
            if (latestStreams) {
                this.streams!!.clear()
            }
            this.streams!!.addAll(streams)
            CoroutineScope(Main).launch {
                updateShimmerVisibility(false)
                if (this@MultiliveStreamsActivity.streams!!.size > 0) {
                    ismActivityScrollableMultiliveStreamsBinding.tvNoBroadcaster.visibility =
                        View.GONE
                    ismActivityScrollableMultiliveStreamsBinding.rlStreams.visibility = View.VISIBLE
                    streamsAdapter!!.notifyDataSetChanged()
                    val streamsModel = streams[0]
                    updateStreamInfo(streamsModel)
                    if (streamsModel.streamId == streamId) {
                        try {
                            //As view holder at that position was not inflated(due to notify dataset changed) returning null for findViewHolderForAdapterPosition method
                            Handler(Looper.myLooper()!!).postDelayed({
                                updateStreamPreviewImageVisibility(
                                    0,
                                    false,
                                    streamId
                                )
                            }, 100)
                        } catch (ignore: Exception) {
                        }
                    }
                } else {
                    ismActivityScrollableMultiliveStreamsBinding.tvNoBroadcaster.visibility =
                        View.VISIBLE
                    ismActivityScrollableMultiliveStreamsBinding.rlStreams.visibility = View.GONE
                }
            }
            hideProgressDialog()
            if (ismActivityScrollableMultiliveStreamsBinding.refresh.isRefreshing) {
                ismActivityScrollableMultiliveStreamsBinding.refresh.isRefreshing = false
            }
        }
    }

    /**
     * Request permissions to start broadcasting after switching profile from
     * a viewer to a publisher.
     */
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this@MultiliveStreamsActivity,
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO),
            0
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        var permissionDenied = false
        if (requestCode == 0) {
            for (grantResult in grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    permissionDenied = true
                    break
                }
            }
            if (permissionDenied) {
                Toast.makeText(
                    this,
                    getString(R.string.ism_permission_start_streaming_denied),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                requestSwitchProfile()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * Switch profile from a viewer to a publisher.
     */
    private fun requestSwitchProfile() {
        showProgressDialog(getString(R.string.ism_switching_profile))
        scrollableMultiliveStreamsPresenter.switchProfile(streamId, isPublic)
    }

    /**
     * Send a gift in a broadcast.
     *
     * @see GiftsActionCallback.sendGift
     */
    override fun onGiftSentSuccessfully(streamId: String, gift: Gift, categoryTitle: String) {

        if (categoryTitle.equals("3D", ignoreCase = true)) {
            scrollableMultiliveStreamsPresenter.sendMessage(
                streamId, gift.giftAnimationImage,
                MessageTypeEnum.ThreeDMessage.value, 0, "", ""
            )
        }

//        if (this.streamId == streamId) {
//            var coinsValue = 0.0
//            try {
//                coinsValue = gift.virtualCurrency.toDouble()
//            } catch (ignore: java.lang.Exception) {
//            }
//            onGiftEvent(
//                GiftsModel(
//                    userSession.userId,
//                    userSession.userName,
//                    userSession.userProfilePic,
//                    userSession.userIdentifier,
//                    gift.giftAnimationImage,
//                    coinsValue.toInt(),
//                    gift.giftTitle,
//                    gift.giftImage,
//                    gift.reciverId,
//                    gift.reciverStreamUserId,
//                    gift.receiverName
//                )
//            )
//        }
    }

    /**
     * [MultiliveStreamsContract.View.onStreamViewersDataReceived]
     */
    override fun onStreamViewersDataReceived(viewersListModels: ArrayList<ViewersListModel>) {
        CoroutineScope(Main).launch {
            this@MultiliveStreamsActivity.viewersListModels.clear()
            this@MultiliveStreamsActivity.viewersListModels.addAll(viewersListModels)
            viewersCount = viewersListModels.size
            viewersUtil!!.updateViewers(viewersListModels)
            if (isPkBattle) {
                pkButtonVisibility(false)
//                ismActivityScrollableMultiliveStreamsBinding.ivEndPK.setVisibility(View.GONE);
            }
        }
    }

    /**
     * [MultiliveStreamsContract.View.removeViewerEvent]
     */
    override fun removeViewerEvent(viewerId: String, viewersCount: Int) {
        CoroutineScope(Main).launch {
            val size = viewersListModels.size
            for (i in 0 until size) {
                if (viewersListModels[i].viewerId == viewerId) {
                    viewersListModels.removeAt(i)
                    break
                }
            }
            viewersUtil!!.updateViewers(viewersListModels)
            this@MultiliveStreamsActivity.viewersCount = viewersCount
            if (viewersCount != viewersListModels.size) {
                //To re-sync viewers
                scrollableMultiliveStreamsPresenter.requestStreamViewersData(streamId)
            }
        }
    }

    /**
     * [MultiliveStreamsContract.View.addViewerEvent]
     */
    override fun addViewerEvent(viewersListModel: ViewersListModel, viewersCount: Int) {
        CoroutineScope(Main).launch {
            viewersListModels.add(0, viewersListModel)
            viewersUtil!!.updateViewers(viewersListModels)
            this@MultiliveStreamsActivity.viewersCount = viewersCount
            if (viewersCount != viewersListModels.size) {
                //To re-sync viewers
                scrollableMultiliveStreamsPresenter.requestStreamViewersData(streamId)
            }
        }
    }

    /**
     * [MultiliveStreamsContract.View.onStreamEnded]
     */
    override fun onStreamEnded(streamId: String, activeStream: Boolean) {
        CoroutineScope(Main).launch {
            val size = streams!!.size
            if (size > 0) {
                for (i in 0 until size) {
                    if (streamId == streams!![i].streamId) {
                        streams!!.remove(streams!![i])
                        streamsAdapter!!.notifyItemRemoved(i)
                        //streamsAdapter.notifyDataSetChanged();
                        if (!isBroadcaster) {
                            if (size == 1) {
                                ismActivityScrollableMultiliveStreamsBinding.tvNoBroadcaster.visibility =
                                    View.VISIBLE
                                ismActivityScrollableMultiliveStreamsBinding.rlStreams.visibility =
                                    View.GONE
                            } else {
                                if (activeStream) {
                                    updateShimmerVisibility(true)
                                    stopTimer()
                                }
                            }
                        }
                        break
                    }
                }
            }
        }
    }

    /**
     * [MultiliveStreamsContract.View.onStreamStarted]
     */
    override fun onStreamStarted(liveStreamsModel: LiveStreamsModel) {
        if (!isBroadcaster) {
            CoroutineScope(Main).launch {
                updateShimmerVisibility(false)
                if (streams!!.size == 0) {
                    ismActivityScrollableMultiliveStreamsBinding.tvNoBroadcaster.visibility =
                        View.GONE
                    ismActivityScrollableMultiliveStreamsBinding.rlStreams.visibility = View.VISIBLE
                    updateStreamInfo(liveStreamsModel)
                }
                var position = -1
                for (i in streams!!.indices) {
                    if (streams!![i].streamId == liveStreamsModel.streamId) {
                        position = i
                        break
                    }
                }
                if (position != -1) {
                    streams!![position] = liveStreamsModel
                    streamsAdapter!!.notifyItemChanged(position)
                } else {
                    streams!!.add(0, liveStreamsModel)
                    streamsAdapter!!.notifyItemInserted(0)
                }
            }
        }
    }

    /**
     * [MultiliveStreamsContract.View.updateVisibilityForJoinButton]
     */
    override fun updateVisibilityForJoinButton() {
        if (!isAdmin && !isBroadcaster) {
            CoroutineScope(Main).launch {
                ismActivityScrollableMultiliveStreamsBinding.ivJoin.visibility = View.VISIBLE
            }
        }
    }

    /**
     * To fetch list of streams on pull to refresh
     */
    private fun fetchLatestStreams() {
        if (isBroadcaster) {
            if (ismActivityScrollableMultiliveStreamsBinding.refresh.isRefreshing) {
                ismActivityScrollableMultiliveStreamsBinding.refresh.isRefreshing = false
            }
        } else {
            showProgressDialog(getString(R.string.ism_fetching_streams))
            try {
                scrollableMultiliveStreamsPresenter.requestLiveStreamsData(
                    Constants.STREAMS_PAGE_SIZE,
                    true
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * [MultiliveStreamsContract.View.updateMembersAndViewersCountInList]
     */
    override fun updateMembersAndViewersCountInList(
        streamId: String,
        membersCount: Int,
        viewersCount: Int
    ) {
        CoroutineScope(Main).launch {
            for (i in streams!!.indices) {
                if (streams!![i].streamId == streamId) {
                    val streamsModel = streams!![i]
                    if (membersCount != -1) streamsModel.membersCount = membersCount
                    if (viewersCount != -1) streamsModel.viewersCount = viewersCount
                    streams!![i] = streamsModel
                    streamsAdapter!!.notifyItemChanged(i)
                    //streamsAdapter.notifyDataSetChanged();
                    break
                }
            }
        }
    }

    /**
     * [RequestListActionCallback.copublishRequestAction]
     */
    override fun copublishRequestAction(accepted: Boolean, userId: String) {
        updateCopublishChatMessage(userId)
    }

    /**
     * Shows go live dialog,once the host is live.
     *
     * @param dialog the golive dialog
     */
    private fun showGoLiveView(dialog: android.app.AlertDialog?) {
        val ismBottomsheetLiveNotificationBinding =
            IsmBottomsheetLiveNotificationBinding.inflate(LayoutInflater.from(this))
        ismBottomsheetLiveNotificationBinding.tvLive.setOnClickListener { v: View? ->
            if (goLiveDialog != null && goLiveDialog!!.isShowing) {
                goLiveDialog!!.dismiss()
            }
        }
        val translate = TranslateAnimation(
            Animation.RELATIVE_TO_SELF,
            0.0f,
            Animation.RELATIVE_TO_SELF,
            0.0f,
            Animation.RELATIVE_TO_SELF,
            0.0f,
            Animation.RELATIVE_TO_SELF,
            1.0f
        )
        translate.duration = 200
        translate.fillAfter = false
        dialog!!.show()
        dialog.setContentView(ismBottomsheetLiveNotificationBinding.root)
        dialog.setCanceledOnTouchOutside(true)
        if (dialog.window != null) {
            val params = dialog.window!!.attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
            dialog.window!!.setGravity(Gravity.BOTTOM)
            params.dimAmount = 0.7f
            dialog.window!!.attributes = params
            dialog.window!!.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.color.ism_transparent
                )
            )
            dialog.window!!.setWindowAnimations(R.style.golive_dialog_style)
        }
    }

    private fun updateStreamInfo(streamsModel: LiveStreamsModel?) {
        if (streamsModel != null && !ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.isPkBattle()) {
            if (streamsModel.streamId != streamId) {
                if (streamsModel.isPaid && !streamsModel.isAlreadyPaid) {
                    ismActivityScrollableMultiliveStreamsBinding.incBuyStream.tvCoinsRequiredToUnlock.setText(
                        streamsModel.paymentAmount.toString()
                    )
                    ismActivityScrollableMultiliveStreamsBinding.incBuyStream.btUnlock.setOnClickListener { v ->
                        TODO()
//                        buyStream(streamsModel)
                    }
                    ismActivityScrollableMultiliveStreamsBinding.incBuyStream.rlLockedStream.setVisibility(
                        VISIBLE
                    )
                } else {
                    ismActivityScrollableMultiliveStreamsBinding.incBuyStream.rlLockedStream.setVisibility(
                        View.GONE
                    )
                }
                updateStreamPreviewImageVisibility(
                    streamsLayoutManager!!.findFirstVisibleItemPosition(),
                    true,
                    streamsModel.streamId
                )
                ismActivityScrollableMultiliveStreamsBinding.tvLiveStreamStatus.setText(getString(R.string.ism_connecting_indicator))
                if (wasEligibleToJoinBroadcast) {

                    //Mute remote media,when channel switched
//                    isometrik.multiBroadcastOperations.updatePreviewRemoteMediaSettings(false)
//                    isometrik.multiBroadcastOperations.leaveChannelOnSwitchPreviewBroadcast()
                    ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.release()
                    ismActivityScrollableMultiliveStreamsBinding.audioGridContainer.release()

                    //if (wasEligibleToJoinBroadcast) {
                    //Leave old stream as viewer
                    scrollableMultiliveStreamsPresenter.leaveAsViewer(streamId, false)
                    multiLiveBroadcastOperations.leaveBroadcast()

                    //}
                }
                streamId = streamsModel.streamId
                if (streamsModel.pkId != null) {
                    pkid = streamsModel.pkId
                }
                giftsPagerFragment?.updatePkId(pkid)
                //Clear messages and join new stream as viewer
                messages.clear()
                messagesAdapter!!.notifyDataSetChanged()
                streamStartTime = streamsModel.startTime * 1000
                stopTimer()
                if (!streamsModel.isPaid || streamsModel.isAlreadyPaid) {
                    wasEligibleToJoinBroadcast = true
                    ismActivityScrollableMultiliveStreamsBinding.tvDuration.setText(getString(R.string.ism_duration_placeholder))
                    scrollableMultiliveStreamsPresenter.joinAsViewer(streamId, false)
                } else {
                    wasEligibleToJoinBroadcast = false
                    duration = TimeUtil.getDuration(streamStartTime)
                    ismActivityScrollableMultiliveStreamsBinding.tvDuration.setText(
                        TimeUtil.getDurationString(
                            duration
                        )
                    )
                }
//                isPublic = streamsModel.isPublic
                audioOnly = streamsModel.isAudioOnly
                productsLinked = streamsModel.isProductsLinked
                productsCount = streamsModel.productsCount
                isPaid = streamsModel.isPaid
                if (isPaid) {
                    coinsRequiredToUnlock = streamsModel.paymentAmount
                }
                streamDescription = streamsModel.streamDescription
//                ismActivityScrollableMultiliveStreamsBinding.tvStreamDescription.setText(
//                    streamDescription
//                )

                isBroadcaster = false
                isModerator = false
                ismActivityScrollableMultiliveStreamsBinding.ivModerator.setVisibility(View.GONE)
                isAdmin = streamsModel.initiatorId == userSession.userId
                hdBroadcast = streamsModel.isHdBroadcast
                restream = streamsModel.isRestream

                scrollableMultiliveStreamsPresenter.initialize(
                    streamId,
                    streamsModel.memberIds,
                    isAdmin,
                    isBroadcaster,
                    isModerator,
                    isPkBattle
                )
                if (streamCategoryId == StreamCategoryEnum.PK.value) {
                    fetchedPkBattle()
                } else {
                    hidePkRelatedUi()
                }
                toggleRoleBasedActionButtonsVisibility(true, true)
                membersCount = streamsModel.membersCount
                viewersCount = streamsModel.viewersCount
                initiatorName = streamsModel.initiatorName
                initiatorIdentifier = streamsModel.initiatorIdentifier
                initiatorImageUrl = streamsModel.initiatorImage
                initiatorId = streamsModel.initiatorId
//                walletUserId = streamsModel.walletUserId
                isStar = streamsModel.isStar
                viewersListModels.clear()
                viewersUtil!!.updateViewers(viewersListModels)
                alreadyRequestedCopublish = false
                pending = false
                accepted = false
                subscribeTopicIsometrik()
            }
        }
        ismActivityScrollableMultiliveStreamsBinding.tvNoOfMembers.setText(
            CountFormat.format(
                membersCount.toLong()
            )
        )
//        ismActivityScrollableMultiliveStreamsBinding.tvNoOfViewers.setText(
//                getString(com.appscrip.socialecom.app.R.string.ism_viewers_count, viewersCount.toString())
//        )
        ismActivityScrollableMultiliveStreamsBinding.tvNoOfViewers.setText(viewersCount.toString())
        setTopProfileData()
    }

    private fun updateStreamPreviewImageVisibility(
        position: Int,
        show: Boolean,
        streamId: String?
    ) {
        if (position > -1) {
            try {
                if (streams!![position].streamId == streamId) {
                    val streamsViewHolder =
                        ismActivityScrollableMultiliveStreamsBinding.rvStreams.findViewHolderForAdapterPosition(
                            position
                        ) as ScrollableStreamsAdapter.StreamsViewHolder?
                    if (streamsViewHolder != null) {
                        CoroutineScope(Main).launch {
                            if (show) {
                                if (streamsViewHolder.ismScrollableStreamsItemBinding.ivStreamImage.visibility == View.GONE) {
                                    val animationFadeIn = AnimationUtils.loadAnimation(
                                        this@MultiliveStreamsActivity,
                                        R.anim.ism_fade_in
                                    )
                                    animationFadeIn.setAnimationListener(object :
                                        Animation.AnimationListener {
                                        override fun onAnimationStart(animation: Animation) {
                                            //Todo nothing
                                        }

                                        override fun onAnimationEnd(animation: Animation) {
                                            streamsViewHolder.ismScrollableStreamsItemBinding.ivStreamImage.visibility =
                                                View.VISIBLE
                                        }

                                        override fun onAnimationRepeat(animation: Animation) {
                                            //Todo nothing
                                        }
                                    })
                                    streamsViewHolder.ismScrollableStreamsItemBinding.ivStreamImage.startAnimation(
                                        animationFadeIn
                                    )
                                }
                            } else {
                                if (streamsViewHolder.ismScrollableStreamsItemBinding.ivStreamImage.visibility == View.VISIBLE) {
                                    val animationFadeOut = AnimationUtils.loadAnimation(
                                        this@MultiliveStreamsActivity,
                                        R.anim.ism_fade_out
                                    )
                                    animationFadeOut.setAnimationListener(object :
                                        Animation.AnimationListener {
                                        override fun onAnimationStart(animation: Animation) {
                                            //Todo nothing
                                        }

                                        override fun onAnimationEnd(animation: Animation) {
                                            streamsViewHolder.ismScrollableStreamsItemBinding.ivStreamImage.visibility =
                                                View.GONE
                                        }

                                        override fun onAnimationRepeat(animation: Animation) {
                                            //Todo nothing
                                        }
                                    })
                                    streamsViewHolder.ismScrollableStreamsItemBinding.ivStreamImage.startAnimation(
                                        animationFadeOut
                                    )
                                }
                            }
                        }
                    }
                }
            } catch (ignore: Exception) {
            }
        }
    }

    private fun updateShimmerVisibility(visible: Boolean) {
        if (visible) {
            //Keyborad is hidden to avoid click on send message button or preset messages,when shimmer is showing
            ismActivityScrollableMultiliveStreamsBinding.etSendMessage.setText("")
            KeyboardUtil.hideKeyboard(this)
            ismActivityScrollableMultiliveStreamsBinding.incShimmer.shimmerFrameLayout.startShimmer()
            ismActivityScrollableMultiliveStreamsBinding.incShimmer.shimmerFrameLayout.visibility =
                View.VISIBLE
        } else {
            if (ismActivityScrollableMultiliveStreamsBinding.incShimmer.shimmerFrameLayout.visibility == View.VISIBLE) {
                ismActivityScrollableMultiliveStreamsBinding.incShimmer.shimmerFrameLayout.visibility =
                    View.GONE
                ismActivityScrollableMultiliveStreamsBinding.incShimmer.shimmerFrameLayout.stopShimmer()
            }
        }
    }

    /**
     * Update status of copublish request in chat message,when copublish request has been
     * accepted/rejected
     */
    private fun updateCopublishChatMessage(userId: String) {
        CoroutineScope(Main).launch {
            val size = messages.size
            for (i in size - 1 downTo 0) {
                if (messages[i].messageItemType == MessageTypeEnum.CopublishRequestMessage.value) {
                    if (messages[i].senderId == userId) {
                        val messagesModel = messages[i]
                        if (messagesModel.isInitiator) {
                            messagesModel.isInitiator = false
                            messagesAdapter!!.notifyItemChanged(i)
                        }
                        break
                    }
                }
            }
        }
    }

    /**
     * Update status of join button in chat message,when user switches role
     */
    private fun updateJoinChatMessage() {
        val userId = userSession.userId
        val size = messages.size
        for (i in size - 1 downTo 0) {
            if (messages[i].messageItemType == MessageTypeEnum.CopublishRequestAcceptedMessage.value) {
                if (messages[i].senderId == userId) {
                    val messagesModel = messages[i]
                    if (messagesModel.isCanJoin) {
                        messagesModel.isCanJoin = false
                        messagesAdapter!!.notifyItemChanged(i)
                    }
                    break
                }
            }
        }
    }

    /**
     * @see EcommerceOperationsCallback.viewCartRequested
     */
    override fun viewCartRequested() {
        if (!isFinishing && !isDestroyed && !cartItemsFragment!!.isAdded) {
            dismissAllDialogs()
            cartItemsFragment!!.updateParameters(streamId, initiatorName, this)
            cartItemsFragment!!.show(supportFragmentManager, CartItemsFragment.TAG)
        }
    }

    /**
     * @see EcommerceOperationsCallback.viewProductDetails
     */
    override fun viewProductDetails(product: ProductDetailModel) {
        if (!isFinishing && !isDestroyed && !productDetailsFragment!!.isAdded) {
            dismissAllDialogs()
            productDetailsFragment!!.updateParameters(
                product,
                streamId,
                isAdmin,
                initiatorName,
                initiatorImageUrl,
                false,
                this
            )
            productDetailsFragment!!.show(supportFragmentManager, ProductDetailsFragment.TAG)
        }
    }

    /**
     * @see EcommerceOperationsCallback.productAddedToCartSuccessfully
     */
    override fun productAddedToCartSuccessfully(
        productName: String,
        productPrice: CharSequence,
        productImageUrl: String,
        unitsAddedToCart: Int
    ) {
        if (!isFinishing && !isDestroyed && !productAddedToCartAlertFragment!!.isAdded) {
            dismissAllDialogs()
            productAddedToCartAlertFragment!!.updateParameters(
                productImageUrl,
                productName,
                productPrice,
                unitsAddedToCart,
                this
            )
            productAddedToCartAlertFragment!!.show(
                supportFragmentManager,
                ProductAddedToCartAlertFragment.TAG
            )
        }
    }

    /**
     * @see EcommerceOperationsCallback.checkoutRequested
     */
    override fun checkoutRequested(checkoutUrl: String) {
        if (!isFinishing && !isDestroyed && !checkoutFragment!!.isAdded) {
            dismissAllDialogs()
            checkoutFragment!!.updateParameters(checkoutUrl)
            checkoutFragment!!.show(supportFragmentManager, CheckoutFragment.TAG)
        }
    }

    /**
     * [SettingsActionCallback.fetchModerators]
     */
    override fun fetchModerators(streamId: String) {
        if (!isFinishing && !isDestroyed && !moderatorsFragment!!.isAdded) {
            dismissAllDialogs()
            moderatorsFragment!!.updateParameters(streamId, isAdmin, this)
            moderatorsFragment!!.show(supportFragmentManager, ModeratorsFragment.TAG)
        }
    }

    /**
     * [SettingsActionCallback.addModerator]
     */
    override fun addModerator(moderatorIds: ArrayList<String>) {
        if (!isFinishing && !isDestroyed && !addModeratorsFragment!!.isAdded) {
            dismissAllDialogs()
            addModeratorsFragment!!.updateParameters(streamId, moderatorIds)
            addModeratorsFragment!!.show(supportFragmentManager, AddModeratorsFragment.TAG)
        }
    }

    /**
     * [SettingsActionCallback.moderatorLeft]
     */
    override fun moderatorLeft() {
        isModerator = false
        ismActivityScrollableMultiliveStreamsBinding.ivModerator.visibility = View.GONE
        val size = messages.size
        for (i in size - 1 downTo 0) {
            if (messages[i].messageItemType == MessageTypeEnum.NormalMessage.value) {
                val messagesModel = messages[i]
                messagesModel.setCanRemoveMessage(false)
                messagesModel.setCanReplyMessage(messagesModel.senderId == IsometrikStreamSdk.getInstance().userSession.userId)
                messages[i] = messagesModel
                messagesAdapter!!.notifyItemChanged(i)
            }
        }
    }

    fun clickOnPkStop(selectedMin: Int) {
        if (pkInvitedId.isNotEmpty()) {
            scrollableMultiliveStreamsPresenter.clickOnStopPkBattle(selectedMin, pkid)
        } else {
            Toast.makeText(
                this,
                getString(R.string.pk_invite_id_validation),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun endPKRequested() {
        if (isPKOnGoing) {
            clickOnPkStop(0)
        } else {
            scrollableMultiliveStreamsPresenter.endPKRequested(pkInvitedId, false)
        }
    }

    /**
     * [MultiliveStreamsContract.View.moderatorStatusUpdated]
     */
    override fun moderatorStatusUpdated(
        addedAsModerator: Boolean,
        initiatorName: String,
        moderatorName: String
    ) {
        CoroutineScope(Main).launch {
            isModerator = addedAsModerator
            ismActivityScrollableMultiliveStreamsBinding.ivModerator.visibility =
                if (addedAsModerator) View.VISIBLE else View.GONE
            dismissAllDialogs()
            if (!isFinishing && !isDestroyed && !moderatorAddedOrRemovedAlertFragment!!.isAdded) {
                moderatorAddedOrRemovedAlertFragment!!.updateParameters(
                    streamId,
                    isAdmin,
                    this@MultiliveStreamsActivity,
                    addedAsModerator,
                    initiatorName,
                    moderatorName
                )
                moderatorAddedOrRemovedAlertFragment!!.show(
                    supportFragmentManager,
                    ModeratorAddedOrRemovedAlertFragment.TAG
                )
            }
            val size = messages.size
            for (i in size - 1 downTo 0) {
                if (messages[i].messageItemType == MessageTypeEnum.NormalMessage.value) {
                    val messagesModel = messages[i]
                    messagesModel.setCanRemoveMessage(addedAsModerator)
                    messagesModel.setCanReplyMessage((messagesModel.senderId == IsometrikStreamSdk.getInstance().userSession.userId) || addedAsModerator)
                    messages[i] = messagesModel
                    messagesAdapter!!.notifyItemChanged(i)
                }
            }
        }
    }

    override fun onMessageReplyReceived(messageId: String) {
        CoroutineScope(Main).launch {
            val size = messages.size
            for (i in size - 1 downTo 0) {
                if (messages[i].messageItemType == MessageTypeEnum.NormalMessage.value && messages[i].messageId == messageId) {
                    if (!messages[i].hasReplies()) {
                        val messagesModel = messages[i]
                        messagesModel.setHasReplies(true)
                        messages[i] = messagesModel
                        messagesAdapter!!.notifyItemChanged(i)
                    }
                    break
                }
            }
        }
    }

    override fun onMessageReplyAdded(messageId: String) {
        onMessageReplyReceived(messageId)
    }

    //@Override
    //public void userCanPublish() {
    //  if (!isBroadcaster) {
    //    runOnUiThread(
    //        () -> ismActivityScrollableMultiliveStreamsBinding.ivJoin.setVisibility(View.VISIBLE));
    //  }
    //}
    override fun onMultiLiveReconnecting(streamId: String?) {
        connectionStateChanged(false)
        //if (isBroadcaster) {
        //  if (!audioOnly) {
        //    multiLiveBroadcastOperations.muteLocalVideo(true);
        //  }
        //}
    }

    override fun onMultiLiveReconnected(streamId: String?) {
        connectionStateChanged(true)
        //if (isBroadcaster) {
        //  if (!audioOnly) {
        //    multiLiveBroadcastOperations.muteLocalVideo(false);
        //  }
        //}
    }

    override fun onMultiLiveDisconnect(streamId: String?, exception: Exception?) {
        //TODO Nothing
        Log.e("onMultiLiveDisconnect", "===> go Back")
    }

    override fun onMultiLiveParticipantConnected(
        streamId: String?,
        memberId: String?,
        memberUid: Int
    ) {
        //TODO Nothing
    }

    override fun onMultiLiveParticipantDisconnected(
        streamId: String?,
        memberId: String?,
        memberUid: Int
    ) {
//        if (this.streamId == streamId && isPkBattle) {
//            if (memberId != IsometrikUiSdk.getInstance().userSession.userId) {
//                CoroutineScope(Main).launch {
//                    if (audioOnly) {
//                        ismActivityScrollableMultiliveStreamsBinding.audioGridContainer.removeUserAudio(
//                            memberUid,
//                            false
//                        )
//                    } else {
//                        ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.removeUserVideo(
//                            this@MultiliveStreamsActivity,
//                            this@MultiliveStreamsActivity,
//                            memberUid,
//                            false,
//                            isPkBattle
//                        )
//                    }
//                }
//            }
//        }
    }

    override fun onMultiLiveFailedToConnect(streamId: String?, error: Throwable?) {
        if (this.streamId == streamId) {
            val errorMessage = getString(R.string.ism_streaming_join_error, error!!.message)
            onError(errorMessage)
            try {
                CoroutineScope(Main).launch { Handler().postDelayed({ onBackPressed() }, 1000) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onMultiLiveVideoTrackMuteStateChange(
        streamId: String?,
        memberId: String?,
        memberUid: Int,
        muted: Boolean
    ) {
        if (this.streamId == streamId) {
            CoroutineScope(Main).launch {
                if (!audioOnly) {
                    ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.updateVideoState(
                        memberUid,
                        muted
                    )
                }
            }
        }
    }

    override fun onMultiLiveAudioTrackMuteStateChange(
        streamId: String?,
        memberId: String?,
        memberUid: Int,
        muted: Boolean
    ) {
        if (this.streamId == streamId) {
            CoroutineScope(Main).launch {
                if (audioOnly) {
                    ismActivityScrollableMultiliveStreamsBinding.audioGridContainer.updateAudioState(
                        memberUid,
                        muted
                    )
                } else {
                    ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.updateAudioState(
                        memberUid,
                        muted
                    )
                }
            }
        }
    }

    override fun oMultiLiveConnectionQualityChanged(
        memberId: String?,
        memberUid: Int,
        quality: ConnectionQuality?
    ) {
//         CoroutineScope(Main).launch {
//            if (audioOnly) {
//                ismActivityScrollableMultiliveStreamsBinding.audioGridContainer.updateNetworkQualityIndicator(
//                    memberUid,
//                    quality
//                )
//            } else {
//                ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.updateNetworkQualityIndicator(
//                    memberUid,
//                    quality
//                )
//            }
//        }
    }

    override suspend fun onRemoteTrackSubscribed(
        streamId: String?,
        memberId: String?,
        memberUid: Int,
        memberName: String?,
        track: Track?
    ) {
        Log.e("log1", "onRemoteTrackSubscribed $memberName")
        if (this.streamId == streamId) {
            if (audioOnly) {
                withContext(Main) {
                    scrollableMultiliveStreamsPresenter.renderRemoteUserAudio(
                        memberUid,
                        this@MultiliveStreamsActivity,
                        ismActivityScrollableMultiliveStreamsBinding.audioGridContainer
                    )

                    //To hide preview image of broadcast
                    updateStreamPreviewImageVisibility(
                        streamsLayoutManager!!.findFirstVisibleItemPosition(),
                        false,
                        streamId.orEmpty()
                    )
                }
            } else {
                if (track is VideoTrack) {
                    withContext(Main) {
                        val initial =
                            ItemVideoStreamInitialBinding.inflate(LayoutInflater.from(this@MultiliveStreamsActivity))

                        addMemberInStream(initial, memberName.orEmpty(), "", memberUid, false)

                        multiLiveBroadcastOperations.initVideoRenderer(initial.textureViewRenderer)
                        delay(300)
                        track.addRenderer(initial.textureViewRenderer)


                        //To hide preview image of broadcast
                        updateStreamPreviewImageVisibility(
                            streamsLayoutManager!!.findFirstVisibleItemPosition(),
                            false,
                            streamId
                        )
                    }
                }
            }
        }
    }

    override suspend fun onLocalTrackSubscribed(
        streamId: String?,
        memberId: String?,
        memberUid: Int,
        memberName: String?,
        track: Track?,
        videoTrack: Boolean
    ) {
        if (this.streamId == streamId) {
            if (!audioOnly && videoTrack && track is VideoTrack) {
                withContext(Main) {

                    val initial =
                        ItemVideoStreamInitialBinding.inflate(LayoutInflater.from(this@MultiliveStreamsActivity))

                    addMemberInStream(initial, memberName.orEmpty(), "", memberUid, true)

                    // delay(200)

                    multiLiveBroadcastOperations.initVideoRenderer(initial.textureViewRenderer)
                    track.addRenderer(initial.textureViewRenderer)
                }
            } else if (audioOnly && !videoTrack) {
                withContext(Main) {
                    ismActivityScrollableMultiliveStreamsBinding.audioGridContainer.addUserAudioSurface(
                        memberUid,
                        true,
                        getString(R.string.ism_you, memberName),
                        userSession.userProfilePic,
                        scrollableMultiliveStreamsPresenter.isNetworkQualityIndicatorEnabled,
                        this@MultiliveStreamsActivity,
                        true
                    )
                }
            }
            withContext(Main) {
                if (isAdmin) {
                    channelJoined = true
                    if (countDownCompleted) {
                        if (!intent.getBooleanExtra("publishRequired", true)) {
                            if (!isFinishing && !isDestroyed) {
                                dismissAllDialogs()
                                if (!ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.isPkBattle) {
                                    showGoLiveView(goLiveDialog)
                                }
                            }
                        }
                    }
                } else {
                    if (audioOnly) {
                        updateStreamPreviewImageVisibility(
                            streamsLayoutManager!!.findFirstVisibleItemPosition(),
                            false,
                            streamId
                        )
                    }
                }
                if (isBroadcaster) {
                    val animationFadeOut = AnimationUtils.loadAnimation(
                        this@MultiliveStreamsActivity,
                        R.anim.ism_fade_out
                    )
                    animationFadeOut.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation) {
                            //Todo nothing
                        }

                        override fun onAnimationEnd(animation: Animation) {
                            ismActivityScrollableMultiliveStreamsBinding.ivStreamImage.visibility =
                                View.GONE
                        }

                        override fun onAnimationRepeat(animation: Animation) {
                            //Todo nothing
                        }
                    })
                    ismActivityScrollableMultiliveStreamsBinding.ivStreamImage.startAnimation(
                        animationFadeOut
                    )
                }
            }
        }
    }

    private fun addMemberInStream(
        initial: ItemVideoStreamInitialBinding,
        userName: String,
        userProfile: String,
        uId: Int,
        isLocal: Boolean
    ) {
        var moreGone = true
        initial.conRoot.setBackgroundResource(R.drawable.stream_gaming_background)
        initial.tvUserName.text = userName
        initial.imgAddJoin.visibility = View.GONE
        initial.siProfile.visibility = VISIBLE
        initial.imgIndicator.visibility = VISIBLE
        initial.frameStreamRoot.visibility = VISIBLE
//        initial.imgMore.setOnClickListener {
//            userFeedClicked(uId)
//        }
        if (isLocal) {
            moreGone = true
        } else {
            if (isBroadcaster) {
                moreGone =
                    ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.memberUids.size == 0
            } else {
                moreGone =
                    ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.memberUids.size <= 1
            }
        }
        if (userProfile.isNotEmpty()) {
            try {
                Glide.with(this)
                    .load(userProfile)
                    .into(initial.siProfile)
            } catch (ignore: java.lang.Exception) {
            }
        }
        ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.addUserVideoSurface(
            uId,
            initial,
            isLocal,
            userProfile,
            moreGone,
            this@MultiliveStreamsActivity, isPkBattle
        )
    }

    override fun onMultiLiveConnected(streamId: String?) {
        CoroutineScope(Main).launch {
            ismActivityScrollableMultiliveStreamsBinding.tvLiveStreamStatus.text =
                getString(R.string.ism_live_indicator)
        }
    }

    override fun onActiveSpeakersChanged(
        streamId: String?,
        remoteUsersAudioLevelInfo: ArrayList<RemoteUsersAudioLevelInfo>?
    ) {
        if (audioOnly) {
            CoroutineScope(Main).launch {
                scrollableMultiliveStreamsPresenter.updateDominantSpeaker(
                    remoteUsersAudioLevelInfo,
                    ismActivityScrollableMultiliveStreamsBinding.audioGridContainer
                )
            }
        }
    }

    /**
     * The class to update stream live duration on UI thread.
     */
    inner class TimerHandler : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == Companion.INCREASE_TIMER) {
                if (!timerPaused) {
                    ismActivityScrollableMultiliveStreamsBinding.tvDuration.text =
                        TimeUtil.getDurationString(duration)
                }
            }
        }

    }

    private fun subscribeTopicIsometrik() {
        IsometrikStreamSdk.getInstance().isometrik.subscribeToTopics(
            IsometrikStreamSdk.getInstance().userSession.userId,
            streamId
        )
    }

    private fun unSubscribeTopicIsometrik() {
        IsometrikStreamSdk.getInstance().isometrik.unsubscribeToTopics(
            IsometrikStreamSdk.getInstance().userSession.userId,
            streamId
        )
    }

    fun showRtmpDetails() {
        if (!isFinishing && !isDestroyed && !rtmpDetailsFragment!!.isAdded) {
            dismissAllDialogs()
            rtmpDetailsFragment!!.updateParameters(intent.getStringExtra("rtmpIngestUrl"))
            rtmpDetailsFragment!!.show(supportFragmentManager, RtmpDetailsFragment.TAG)
        }
    }

    fun pkButtonVisibility(makeVisible: Boolean) {
        ismActivityScrollableMultiliveStreamsBinding.ivPk.visibility =
            if (makeVisible && IsometrikStreamSdk.getInstance().isPkEnable) View.VISIBLE else View.GONE
    }

    override fun onAddUserClick() {
        if (canWeAddMoreMember()) {
            if (isBroadcaster) {
                onAddMemberClick()
            } else {
                onRequestMemberClick()
            }
        }
    }

    fun canWeAddMoreMember(): Boolean {
        if (scrollableMultiliveStreamsPresenter.memberIds.size >= 5 && ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.isGamingMode) {
            Toast.makeText(this, "Member limit exceed!", Toast.LENGTH_SHORT).show()

            return false
        } else {
            return true
        }
    }

    fun onAddMemberClick() {
        if (isAdmin && canWeAddMoreMember()) {
            if (!isFinishing && !isDestroyed && !addMembersFragment!!.isAdded) {
                dismissAllDialogs()
                addMembersFragment!!.updateParameters(
                    streamId,
                    scrollableMultiliveStreamsPresenter.getMemberIds()
                )
                addMembersFragment!!.show(supportFragmentManager, AddMembersFragment.TAG)
            }
        }
    }

    private fun onRequestMemberClick() {
        if (scrollableMultiliveStreamsPresenter.getMemberIds().contains(userSession.userId)) {


            //A member can see all the copublish requests
            if (!isFinishing && !isDestroyed && !requestsFragment!!.isAdded) {
                dismissAllDialogs()
                requestsFragment!!.updateParameters(
                    streamId,
                    isAdmin,
                    this@MultiliveStreamsActivity
                )
                requestsFragment!!.show(supportFragmentManager, RequestsFragment.TAG)
            }
        } else {
            if (alreadyRequestedCopublish) {
                if (!isFinishing && !isDestroyed && !copublishRequestStatusFragment!!.isAdded) {
                    dismissAllDialogs()
                    copublishRequestStatusFragment!!.updateParameters(
                        initiatorImageUrl,
                        userSession.userProfilePic,
                        initiatorName,
                        pending,
                        accepted,
                        this@MultiliveStreamsActivity,
                        userSession.getUserName()
                    )
                    copublishRequestStatusFragment!!.show(
                        supportFragmentManager,
                        CopublishRequestStatusFragment.TAG
                    )
                }
            } else {
                //A viewer can make a copublish request
                if (!isFinishing && !isDestroyed && !requestCopublishFragment!!.isAdded) {
                    dismissAllDialogs()
                    requestCopublishFragment!!.updateParameters(
                        initiatorImageUrl,
                        userSession.userProfilePic,
                        this@MultiliveStreamsActivity,
                        userSession.getUserName(),
                        if (initiatorName.isNullOrBlank()) initiatorIdentifier else initiatorName
                    )
                    requestCopublishFragment!!.show(
                        supportFragmentManager,
                        RequestCopublishFragment.TAG
                    )
                }
            }
        }
    }

    fun startSpotLightEffect() {
        CoroutineScope(Main).launch {
            width =
                ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.getMeasuredWidth()
            val MARGIN_TOP = (110 * resources.displayMetrics.density).toInt()

            val param =
                RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2 * width / 3)
            param.setMargins(0, MARGIN_TOP, 0, 0)
            ismActivityScrollableMultiliveStreamsBinding.consSpotlight.setLayoutParams(param)
            ismActivityScrollableMultiliveStreamsBinding.consSpotlight.setVisibility(VISIBLE)
            ismActivityScrollableMultiliveStreamsBinding.siRightSpotProfile.startAnimation(
                animRightProfile
            )
            ismActivityScrollableMultiliveStreamsBinding.siLeftSpotProfile.startAnimation(
                animLeftProfile
            )


            if (countDownSpotlight == null) {
                countDownSpotlight = object : CountDownTimer(2000, 500) {
                    override fun onTick(millisUntilFinished: Long) {
                        if (millisUntilFinished == 1000L) {
//                         CoroutineScope(Main).launch {}
                        }
                    }

                    override fun onFinish() {
                        CoroutineScope(Main).launch {
                            ismActivityScrollableMultiliveStreamsBinding.consSpotlight.setVisibility(
                                View.GONE
                            )
                            ismActivityScrollableMultiliveStreamsBinding.relPkBottomView.setVisibility(
                                View.GONE
                            )
                            ismActivityScrollableMultiliveStreamsBinding.relPkBattleBottomView.setVisibility(
                                VISIBLE
                            )
                        }
                    }
                }
                countDownSpotlight!!.start()
            } else {
                countDownSpotlight!!.cancel()
                countDownSpotlight!!.start()
            }
        }
    }

    fun startPkEndCountDown(sec: Int) {
        Log.e("Started Countdown", "====> $sec")
        val params =
            ismActivityScrollableMultiliveStreamsBinding.tvPkCountDown.getLayoutParams() as RelativeLayout.LayoutParams
        params.setMargins(0, MARGIN_TOP, 0, 0)
        ismActivityScrollableMultiliveStreamsBinding.tvPkCountDown.setVisibility(VISIBLE)
        ismActivityScrollableMultiliveStreamsBinding.tvPkCountDown.downSecond(sec)
        ismActivityScrollableMultiliveStreamsBinding.tvPkCountDown.setOnTimeDownListener(object :
            DownTimeWatcher {
            override fun onTime(num: Int) {}
            override fun onLastTime(num: Int) {}
            override fun onLastTimeFinish(num: Int) {
                ismActivityScrollableMultiliveStreamsBinding.tvPkCountDown.setVisibility(View.GONE)
            }
        })
    }

    fun startPkCountDown(mileStone: Int, isInitiate: Boolean) {
        if (mileStone < 0) {
            return
        }
        if (isInitiate) {
            startSpotLightEffect()
        }

        setPkBattleInitialView(isInitiate)
        CoroutineScope(Main).launch {
            if (countDownTimer != null) {
                countDownTimer!!.cancel()
                countDownTimer = null
            }
            countDownTimer = object : CountDownTimer(mileStone.toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    var millisUntilFinished = millisUntilFinished
                    val secondsInMilli: Long = 1000
                    val minutesInMilli = secondsInMilli * 60
                    val hoursInMilli = minutesInMilli * 60

//                long elapsedHours = millisUntilFinished / hoursInMilli;
//                millisUntilFinished = millisUntilFinished % hoursInMilli;
                    val elapsedMinutes = millisUntilFinished / minutesInMilli
                    millisUntilFinished = millisUntilFinished % minutesInMilli
                    val elapsedSeconds = millisUntilFinished / secondsInMilli

//                Log.e("onTick Time ", "====> " + elapsedSeconds);
                    val yy = String.format("%02d:%02d", elapsedMinutes, elapsedSeconds)
                    CoroutineScope(Main).launch {
                        ismActivityScrollableMultiliveStreamsBinding.tvPkBattleTimer.setText(yy)
                        if (elapsedSeconds == 5L && elapsedMinutes == 0L) {
                            startPkEndCountDown(4)
                        }
                        if (ismActivityScrollableMultiliveStreamsBinding.progressWhoWinner.getVisibility() == View.GONE) {
                            if (guestTotalCoin > 0 || initiatorTotalCoin > 0) {
                                setupWhoIsWinningProgress()
                            }
                        }
                    }
                }

                override fun onFinish() {
                    stopPkCountDown(false)
                }
            }.start()
        }
    }

    fun setupWhoIsWinningProgress() {
        if (ismActivityScrollableMultiliveStreamsBinding.relWhoWinner.getVisibility() == View.GONE) {
            val MARGIN_TOP = (100 * resources.displayMetrics.density).toInt()
            val paramsTvWhoWinning = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            paramsTvWhoWinning.setMargins(0, MARGIN_TOP, 0, 0)
            ismActivityScrollableMultiliveStreamsBinding.relWhoWinner.setLayoutParams(
                paramsTvWhoWinning
            )
            ismActivityScrollableMultiliveStreamsBinding.relWhoWinner.setVisibility(VISIBLE)
        }
    }

    fun setPkBattleInitialView(isInitiate: Boolean) {
        CoroutineScope(Main).launch {
            isPKOnGoing = true
            ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.hidePkWinnerLooser()
            ismActivityScrollableMultiliveStreamsBinding.tvCongratsWinner.visibility = View.GONE

            if (!isPkStreamer) {
                ismActivityScrollableMultiliveStreamsBinding.tvMakeHost.visibility = VISIBLE
            }

            width = ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.measuredWidth
            if (!isInitiate) {
                ismActivityScrollableMultiliveStreamsBinding.relPkBottomView.visibility = View.GONE
                ismActivityScrollableMultiliveStreamsBinding.relPkBattleBottomView.visibility =
                    VISIBLE
            }

            ismActivityScrollableMultiliveStreamsBinding.imgPkStart.visibility = View.GONE
            ismActivityScrollableMultiliveStreamsBinding.ivDrawTag.visibility = View.GONE
            ismActivityScrollableMultiliveStreamsBinding.relWhoWinner.visibility = View.GONE

            if (isPkStreamer) {
                ismActivityScrollableMultiliveStreamsBinding.ivEndPK.setImageResource(R.drawable.ic_cross_back)
            }
            ismActivityScrollableMultiliveStreamsBinding.ivEndPK.visibility =
                if (isPkStreamer) VISIBLE else View.GONE
            initiatorTotalCoin = 0
            guestTotalCoin = 0
            updateWhoIsWinningUi(false)
        }
    }


    override fun clickOnPkAccept(
        invitedId: String?,
        initiatorStreamId: String?,
        pkInviteRelatedEvent: PkInviteRelatedEvent?
    ) {
        isPkStreamer = true

        scrollableMultiliveStreamsPresenter.clickOnPKRequestAccept(
            invitedId,
            initiatorStreamId,
            "Accepted",
            pkInviteRelatedEvent
        )
        pkInvitedId = invitedId!!
    }

    override fun clickOnPkReject(invitedId: String?, initiatorStreamId: String?) {
        if (pkInviteFragment!!.dialog != null && pkInviteFragment!!.dialog?.isShowing == true && !pkInviteFragment!!.isRemoving) {
            pkInviteFragment!!.dismiss()
        }
        scrollableMultiliveStreamsPresenter.clickOnPKRequestReject(
            invitedId,
            initiatorStreamId,
            "Rejected"
        )
    }

    override fun onPkInviteSent(inviteUserModel: InviteUserModel?) {
        if (inviteUserFragment!!.dialog != null && inviteUserFragment!!.dialog?.isShowing == true && !inviteUserFragment!!.isRemoving) {
            inviteUserFragment!!.dismiss()
        }
        if (!isFinishing && !pkInviteFragment!!.isAdded) {
            dismissAllDialogs()
            pkInviteFragment!!.updateParameters(inviteUserModel)
            pkInviteFragment!!.show(supportFragmentManager, PkInviteFragment.TAG)
        }
    }

    override fun pkRequestAcceptAPIResult(
        isAccept: Boolean,
        newStreamId: String?,
        pkInviteRelatedEvent: PkInviteRelatedEvent
    ) {
        if (isAccept) {
            onBroadcastEndedForPk()
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    streamId = newStreamId.orEmpty()
                    isPkBattle = true
                    isPkStreamer = true
                    isPkInitiator = false
                    ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.updatePkParam(
                        isPkBattle,
                        isPkStreamer,
                        isPkInitiator
                    )
                    scrollableMultiliveStreamsPresenter.initialize(
                        streamId,
                        java.util.ArrayList<String>(),
                        false,
                        true,
                        isModerator,
                        isPkBattle
                    )


                    dismissAllDialogs()
                    try {
                        CoroutineScope(Main).launch { multiLiveBroadcastOperations.leaveBroadcast() }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    Log.e("streamID", "new ==> " + newStreamId)
                    scrollableMultiliveStreamsPresenter.updateBroadcastingStatus(
                        true,
                        newStreamId,
                        false
                    )
                    preparePkBattleView()
                }
            }, 2000)
        }
    }

    fun preparePkBattleView() {
        CoroutineScope(Main).launch {
            pkButtonVisibility(false)
            ismActivityScrollableMultiliveStreamsBinding.ivRequest.visibility = View.GONE
            ismActivityScrollableMultiliveStreamsBinding.ivAddMember.visibility = View.GONE
            if (isPkStreamer) {
                ismActivityScrollableMultiliveStreamsBinding.ivEndPK.setImageResource(R.drawable.ism_ic_join_member)
            }
            ismActivityScrollableMultiliveStreamsBinding.ivEndPK.visibility =
                if (isPkStreamer) VISIBLE else View.GONE
        }
    }

    override fun onPkInviteStatusChange(pkInviteRelatedEvent: PkInviteRelatedEvent) {
        dismissAllDialogs()
        if (pkInviteRelatedEvent.metaData.status?.equals("Rejected") == true) {
            pkInviteFragment?.finishTimer()
        } else {
            if (pkInviteRelatedEvent.metaData.inviteId.isNotEmpty()) {
                if (streamId == pkInviteRelatedEvent.metaData.senderStreamId) {
                    isPkBattle = true
                    isPkStreamer = true
                    isPkInitiator = true
                    ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.updatePkParam(
                        isPkBattle,
                        isPkStreamer,
                        isPkInitiator
                    )
                    width =
                        ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.getMeasuredWidth()
                    if (isPkBattle) {
                        Timer().schedule(object : TimerTask() {
                            override fun run() {
                                CoroutineScope(Main).launch {
                                    val params = RelativeLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                                        RelativeLayout.LayoutParams.WRAP_CONTENT
                                    )
                                    params.setMargins(0, MARGIN_BOTTOM + 2 * width / 3 / 2, 0, 0)

                                    ismActivityScrollableMultiliveStreamsBinding.imgPkStart.setLayoutParams(
                                        params
                                    )
                                    params.addRule(
                                        RelativeLayout.CENTER_HORIZONTAL,
                                        RelativeLayout.TRUE
                                    )
                                    ismActivityScrollableMultiliveStreamsBinding.imgPkStart.setVisibility(
                                        VISIBLE
                                    )
                                    pkButtonVisibility(false)
                                    scrollableMultiliveStreamsPresenter.fetchLatestMembers(streamId)
                                    pkInvitedId = pkInviteRelatedEvent.metaData.inviteId
                                }
                            }
                        }, 3000)
                    }
                    preparePkBattleView()
                }
            }
        }
    }

    override fun onBroadcastEndedForPk() {
        alreadyStoppedPublishing = true
        dismissAllDialogs()

        stopTimer()

        if (isBroadcaster) {
            try {
                CoroutineScope(Main).launch {
                    ismActivityScrollableMultiliveStreamsBinding.audioGridContainer.release()
                    ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.release()
                }
            } catch (ignore: java.lang.Exception) {
            }
        }

    }

    override fun pkInviteReceived(pkInviteRelatedEvent: PkInviteRelatedEvent) {
        if (!isFinishing && !pkInviteFragment!!.isAdded && isBroadcaster) {
            dismissAllDialogs()
            pkInviteFragment!!.updateParameters(
                pkInviteRelatedEvent.metaData.profilepic,
                userSession.userProfilePic,
                this,
                userSession.userName,
                pkInviteRelatedEvent.userName,
                true,
                pkInviteRelatedEvent.metaData.inviteId,
                pkInviteRelatedEvent.metaData.streamId,
                pkInviteRelatedEvent
            )
            pkInviteFragment!!.show(supportFragmentManager, PkInviteFragment.TAG)
        }
    }

    private fun setupForPKStuffUi() {
        width = ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.getMeasuredWidth()


        //setup for  giftVideo
//        val VIDEO_GIFT_MARGIN_TOP = (150 * resources.displayMetrics.density).toInt()
        //                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ismActivityScrollableMultiliveStreamsBinding.giftVideo.getLayoutParams();
        val params = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            (300 * resources.displayMetrics.density).toInt()
        )
        //        width = ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.getMeasuredWidth();
//        params.setMargins(0, VIDEO_GIFT_MARGIN_TOP + ((2 * width) / 3), 0, 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        ismActivityScrollableMultiliveStreamsBinding.relGiftVideo.setLayoutParams(params)


        // setup for make host button
        val MAKE_HOST_MARGIN_TOP = (320 * resources.displayMetrics.density).toInt()
        val relMakeHostParam = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        relMakeHostParam.setMargins(width / 2 + 25, MAKE_HOST_MARGIN_TOP, 0, 0)
        ismActivityScrollableMultiliveStreamsBinding.tvMakeHost.setLayoutParams(relMakeHostParam)


        // setup for top-progressive view and profile view
        val PK_BOTTOM_VIEW_MARGIN_TOP = (110 * resources.displayMetrics.density).toInt()
        val PROFILE_SIZE = (50 * resources.displayMetrics.density).toInt()
        val relPkParam =
            RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, PROFILE_SIZE)
        //        width = ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.getMeasuredWidth();
        relPkParam.setMargins(0, PK_BOTTOM_VIEW_MARGIN_TOP, 0, 0)
        ismActivityScrollableMultiliveStreamsBinding.relPkBottomView.setLayoutParams(relPkParam)


        // setup for top-pk-battle view
        val PK_BATTLE_MARGIN_TOP = (60 * resources.displayMetrics.density).toInt()
        val PK_BATTLE_PROFILE_SIZE = (80 * resources.displayMetrics.density).toInt()
        val relPkBattleParam = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            PK_BATTLE_PROFILE_SIZE * 2
        )
        relPkBattleParam.setMargins(0, PK_BATTLE_MARGIN_TOP, 0, 0)
        ismActivityScrollableMultiliveStreamsBinding.relPkBattleBottomView.setLayoutParams(
            relPkBattleParam
        )
    }

    fun fetchedPkBattle() {
        isPkStreamer = false
        scrollableMultiliveStreamsPresenter.fetchPkStreamData(streamId, pkid)
    }

    override fun clickOnPkStart(selectedMin: Int) {
        if (pkChallengeSettingsFragment!!.dialog != null && pkChallengeSettingsFragment!!.dialog?.isShowing == true && !pkChallengeSettingsFragment!!.isRemoving) {
            pkChallengeSettingsFragment!!.dismiss()
        }

        ismActivityScrollableMultiliveStreamsBinding.imgPkStart.setVisibility(View.GONE)
        ismActivityScrollableMultiliveStreamsBinding.tvPkBattleTimer.setText("$selectedMin:00")
        if (!pkInvitedId.isEmpty()) {
            scrollableMultiliveStreamsPresenter.clickOnStartPkBattle(selectedMin, pkInvitedId)
        } else {
            Toast.makeText(
                this,
                getString(R.string.pk_invite_id_validation),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun pkEnded(intentToStop: Boolean) {

        if (intentToStop) {
            CoroutineScope(Main).launch {
                dismissAllDialogs()
                onBroadcastEndedForPk()
                CoroutineScope(Main).launch {
                    multiLiveBroadcastOperations.leaveBroadcast()
                }
                onBackPressed()
            }
        } else {
//            hidePkRelatedUi()
//            ismActivityScrollableMultiliveStreamsBinding.ivEndPK.visibility = View.GONE
//            ismActivityScrollableMultiliveStreamsBinding.ivPk.visibility = VISIBLE
//            restartCoPublisherStream()
            // co-publisher can't start stream again as per backend dev
            if (isPkStreamer && !isPkInitiator) {
                finish()
            }
        }
    }

    fun restartCoPublisherStream() {
        // co - publisher start stream again from here
        intent.getStringExtra("rtcToken")?.let {
            onBroadcastStatusUpdated(
                true, false, it,
                isModerator
            )
            lifecycleScope.launch(Main) {
                pkButtonVisibility(true)
            }
            setTopProfileData()
        }

    }

    fun remoteUserActionClicked(
        senderId: String,
        senderName: String,
        appUserId: String,
        senderImageUrl: String
    ) {
        TODO()
//        if (!isFinishing && !remoteUserActionsFragment!!.isAdded) {
//            dismissAllDialogs()
//            remoteUserActionsFragment!!.updateParameters(
//                streamId,
//                senderId,
//                senderName,
//                senderImageUrl,
//                appUserId,
//                this,
//                isModerator
//            )
//            remoteUserActionsFragment!!.show(supportFragmentManager, ReportReasonsFragment.TAG)
//        }
    }

    fun setPkBattleStopView() {
        CoroutineScope(Main).launch {

            isPKOnGoing = false
            if (isPkInitiator) {
                ismActivityScrollableMultiliveStreamsBinding.imgPkStart.visibility = VISIBLE
            } else {
//            ismActivityScrollableMultiliveStreamsBinding.imgPkVs.setVisibility(View.VISIBLE);
            }
            if (isPkStreamer) {
                ismActivityScrollableMultiliveStreamsBinding.ivEndPK.setImageResource(R.drawable.ism_ic_join_member)
            }
            ismActivityScrollableMultiliveStreamsBinding.ivEndPK.visibility =
                if (isPkStreamer) VISIBLE else View.GONE
            ismActivityScrollableMultiliveStreamsBinding.ivJoin.visibility = View.GONE
            ismActivityScrollableMultiliveStreamsBinding.relPkBattleBottomView.visibility =
                View.GONE
            ismActivityScrollableMultiliveStreamsBinding.tvPkCountDown.visibility = View.GONE
            ismActivityScrollableMultiliveStreamsBinding.relPkBottomView.visibility = VISIBLE
            ismActivityScrollableMultiliveStreamsBinding.tvMakeHost.visibility = View.GONE
        }
    }

    fun stopPkCountDown(isForceStop: Boolean) {
        countDownTimer?.cancel()
        isPKOnGoing = false
        if (pkid.isNotEmpty()) {
            scrollableMultiliveStreamsPresenter.fetchPkBattleWinners(pkid)
        }
        setPkBattleStopView()
        pkid = ""
    }

    override fun viewerPkStreamStopped(pkStopEvent: PkStopEvent) {
        Log.e("PK:", "viewerPkStreamStopped")
        if (pkStopEvent.payload.type.equals("FORCE_STOP", ignoreCase = true)) {
            stopPkCountDown(true)
        }
    }

    override fun broadcastersPkStreamStopped(pkStopEvent: PkStopEvent) {
        if (pkStopEvent.payload.type.equals("FORCE_STOP", ignoreCase = true)) {
            stopPkCountDown(true)
        }
    }

    fun hidePkRelatedUi() {
        lifecycleScope.launch(Main) {
            isPKOnGoing = false
            ismActivityScrollableMultiliveStreamsBinding.imgPkVs.setVisibility(View.GONE)
            ismActivityScrollableMultiliveStreamsBinding.imgPkStart.setVisibility(View.GONE)
            ismActivityScrollableMultiliveStreamsBinding.relPkBattleBottomView.setVisibility(View.GONE)
            ismActivityScrollableMultiliveStreamsBinding.relPkBottomView.setVisibility(View.GONE)
            ismActivityScrollableMultiliveStreamsBinding.tvCongratsWinner.setVisibility(View.GONE)
            ismActivityScrollableMultiliveStreamsBinding.tvPkCountDown.setVisibility(View.GONE)
            ismActivityScrollableMultiliveStreamsBinding.tvMakeHost.setVisibility(View.GONE)
            ismActivityScrollableMultiliveStreamsBinding.progressWhoWinner.setVisibility(View.GONE)
            ismActivityScrollableMultiliveStreamsBinding.ivEndPK.setVisibility(View.GONE)
            ismActivityScrollableMultiliveStreamsBinding.ivDrawTag.setVisibility(View.GONE)
            ismActivityScrollableMultiliveStreamsBinding.relGiftVideo.setVisibility(View.GONE)
            countDownTimer?.cancel()
            ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.hidePkRelatedView()
//            scrollableMultiliveStreamsPresenter.requestStreamViewersData(streamId)
        }
    }

    override fun onFetchedPkStats(pkStats: PkStats) {
        if (streamId == pkStats?.streamId) {
            pkid = pkStats.pkId.orEmpty()
            if (pkStats.timeRemain != null) {
                if (pkStats.timeRemain!! > 0) {
                    giftsPagerFragment?.updatePkId(pkid)
                    startPkCountDown(pkStats.timeRemain!! * 1000, false)
                    if (pkStats.firstUserCoins!! > 0 || pkStats.secondUserCoins!! > 0) {
                        Timer().schedule(object : TimerTask() {
                            override fun run() {
                                CoroutineScope(Main).launch {
                                    if (ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.currentHostId.isEmpty() || ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.currentHostId.equals(
                                            pkStats.firstUserDetails!!.isometrikUserId
                                        )
                                    ) {
                                        initiatorTotalCoin = pkStats.firstUserCoins!!
                                        guestTotalCoin = pkStats.secondUserCoins!!
                                    } else {
                                        initiatorTotalCoin = pkStats.secondUserCoins!!
                                        guestTotalCoin = pkStats.firstUserCoins!!
                                    }
                                    setupWhoIsWinningProgress()
                                    updateWhoIsWinningUi(true)
                                }
                            }
                        }, 3000)
                    }
                }
            }
        }
    }

    private fun setTopProfileData() {
        ismActivityScrollableMultiliveStreamsBinding.tvInitiatorName.text = initiatorName
        ismActivityScrollableMultiliveStreamsBinding.tvInitiatorIdentifier.text =
            initiatorIdentifier
//        ismActivityScrollableMultiliveStreamsBinding.ivStar.visibility =
//            if (isStar) View.GONE else View.GONE
        if (PlaceholderUtils.isValidImageUrl(initiatorImageUrl)) {
            try {
                Glide.with(this).load(initiatorImageUrl)
                    .placeholder(R.drawable.ism_default_profile_image)
                    .transform(CircleCrop())
                    .into(ismActivityScrollableMultiliveStreamsBinding.ivInitiatorImage)
            } catch (ignore: java.lang.IllegalArgumentException) {
            } catch (ignore: java.lang.NullPointerException) {
            }
        } else {
            PlaceholderUtils.setTextRoundDrawable(
                this,
                initiatorName,
                ismActivityScrollableMultiliveStreamsBinding.ivInitiatorImage,
                13
            )
        }
    }

    fun userSwipeHostClicked(uid: Int) {
        if (!isFinishing && !changeHostFragment!!.isAdded) {
            dismissAllDialogs()
            changeHostFragment!!.updateParameters(isAdmin, this)
            changeHostFragment!!.show(supportFragmentManager, ChangeHostFragment.TAG)
        }
    }


    override fun pkBattleStarted(pkBattleStartResponse: PkBattleStartResponse?) {
        if (pkBattleStartResponse!!.pkId.isNotEmpty()) {
            pkid = pkBattleStartResponse.pkId
            giftsPagerFragment?.updatePkId(pkid)
        }
    }

    /**
     * used mostly changeToPkForViewersOfHost (publisher ) Streamer
     * */
    override fun changeToPkEvent() {
        if (!isBroadcaster) {
            isPkBattle = true
            isPkStreamer = false
            isPkViewerFromHost = true
            preparePkBattleView()
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    scrollableMultiliveStreamsPresenter.fetchLatestMembers(streamId)
                }
            }, 3000)
        }

    }

    /** why intentToStop: if co-host not want to continue with his ghosted stream and also want to leave
    on-going PK that time he sent intentToStop = true.

    use for mostly changeToPkForViewersOfGuest (co-publisher)
     */

    override fun changeStreamEvent(pkChangeStreamEvent: PkChangeStreamEvent) {
        Log.e("removeUserVideo", "===> change Stream")
        if (pkChangeStreamEvent.action == "ACCEPT_PK" && isBroadcaster) {
            return
        }
        pkChangeStreamEvent.streamData?.inviteId?.let {
            pkInvitedId = it
        }
        if (pkChangeStreamEvent.isIntentToStop) {
            lifecycleScope.launch(Main) {
                showAlertDialog(
                    IsometrikStreamSdk.getInstance()
                        .context
                        .getString(R.string.ism_stream_offline_initiator_stop),
                    StreamDialogEnum.StreamOffline.value,
                    isBroadcaster
                )
            }
            scrollableMultiliveStreamsPresenter.stopBroadcast(
                intent.getStringExtra("streamId"),
                pkInvitedId
            )
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    finish()
                }
            }, 1500)
            return
        }
        lifecycleScope.launch(Main) {
            ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.release()
        }
        streamId = pkChangeStreamEvent.streamId.orEmpty()
        if (pkChangeStreamEvent.action?.contains("END") == true) {
            isPkBattle = false
            pkInvitedId = ""
        } else {
            isPkBattle = true
            isPkStreamer = isBroadcaster
        }
        if (isPkStreamer && !isPkInitiator) {
            // co-publisher need to start publishing on his original-streamId as this event call for
            //  broadcaster when pk end as well

            try {
                lifecycleScope.launch(Main) { multiLiveBroadcastOperations.leaveBroadcast() }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            streamId = intent.getStringExtra("streamId").orEmpty()
        } else {
            if (!isBroadcaster) {
                if (!isPkBattle) {
                    // viewer need to join his original broadcaster when pk End

                    try {
                        lifecycleScope.launch(Main) { multiLiveBroadcastOperations.leaveBroadcast() }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    streamId = intent.getStringExtra("streamId").orEmpty()
                }
            }
        }
        scrollableMultiliveStreamsPresenter.initialize(
            streamId,
            java.util.ArrayList<String>(),
            isAdmin,
            isBroadcaster,
            isModerator,
            isPkBattle
        )

        ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.updatePkParam(
            isPkBattle,
            isPkStreamer,
            isPkInitiator
        )
        hidePkRelatedUi()
        if (isPkStreamer && !isPkInitiator) {
            // co-publisher can't resume own stream

            finish()

            TODO("isometrik analytics api not found for now")
//            try {
//                if (!isFinishing && !analyticsFragment!!.isAdded) {
//                    dismissAllDialogs()
//                    analyticsFragment!!.updateParameters(
//                        streamId, true, initiatorImageUrl, initiatorName,
//                        streamStartTime, this@MultiliveStreamsActivity
//                    )
//                    analyticsFragment!!.show(supportFragmentManager, AnalyticsFragment.TAG)
//                }
//            } catch (e: java.lang.Exception) {
//            }


            TODO("Flow change as per bwlow")
//                co-publisher start stream again from here
//                onBroadcastStatusUpdated(
//                    true, false, intent.getStringExtra("rtcToken"),
//                    isModerator
//                )
//                lifecycleScope.launch(Main) {
//                    ismActivityScrollableMultiliveStreamsBinding.ivPk.setVisibility(
//                        View.VISIBLE
//                    )
//                }
//                setTopProfileData()
        } else {
            if (!isBroadcaster) {
                lifecycleScope.launch(Main) {
                    isPkViewerFromHost = false
                    isPkViewerOnGoing = true
                    scrollableMultiliveStreamsPresenter.joinAsViewer(streamId, false)
                    setTopProfileData()
                }
            }
        }
        subscribeTopicIsometrik()
        Log.e("Called", "===> requestStreamViewersData 44")
        scrollableMultiliveStreamsPresenter.requestStreamViewersData(streamId)
        ismActivityScrollableMultiliveStreamsBinding.ivEndPK.visibility = View.GONE

    }

    override fun broadcastersPkStreamStarted(pkStreamStartStopEvent: PkStreamStartStopEvent) {
        if (pkStreamStartStopEvent.pkId?.isNotEmpty() == true) {
            pkid = pkStreamStartStopEvent.pkId
            giftsPagerFragment?.updatePkId(pkid)

        }
        startPkCountDown(pkStreamStartStopEvent.timeInMin * 60 * 1000, true)
    }

    override fun viewerPkStreamStarted(pkStreamStartStopEvent: PkStreamStartStopEvent?) {
        if (pkStreamStartStopEvent!!.streamData.streamId == streamId) {

            // disable  swipe
            ismActivityScrollableMultiliveStreamsBinding.rvStreams.suppressLayout(true)

            pkStreamStartStopEvent.pkId?.let {
                pkid = it
                giftsPagerFragment?.updatePkId(it)
            }
            startPkCountDown(pkStreamStartStopEvent.timeInMin * 60 * 1000, true)

            pkStreamStartStopEvent.streamData.firstUserDetails.streamId?.let {
                firstUserStreamId = it
            }
            pkStreamStartStopEvent.streamData.firstUserDetails.userId?.let {
                firstUserStreamUserId = it
            }

            pkStreamStartStopEvent.streamData.secondUserDetails.streamId?.let {
                secondUserStreamId = it
            }
            pkStreamStartStopEvent.streamData.secondUserDetails.userId?.let {
                secondUserStreamUserId = it
            }


        }

    }

    override fun onMemberLeft(memberLeaveEvent: MemberLeaveEvent?) {
        // pk flow end by guest
        if (isPkStreamer && isPkInitiator && isPkBattle) {
            forceToResumeOwnStreamAgain(memberLeaveEvent!!.memberId, memberLeaveEvent.streamId)
            return
        }

        lifecycleScope.launch(Main) {

            if (isPkBattle) {
                isPkBattle = false
                pkInvitedId = ""
                hidePkRelatedUi()
            }
            // for group streaming
            if (!audioOnly) {
                ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.removeUserVideo(
                    this@MultiliveStreamsActivity, this@MultiliveStreamsActivity,
                    UserIdGenerator.getUid(memberLeaveEvent!!.memberId),
                    false,
                    isPkBattle
                )
            }
        }
    }

    override fun onMemberRemove(memberRemoveEvent: MemberRemoveEvent?) {

        if (memberRemoveEvent!!.memberId == IsometrikStreamSdk.getInstance().userSession.userId) {
            ismActivityScrollableMultiliveStreamsBinding.ivJoin.visibility = View.GONE
            if (isBroadcaster) {
                onStreamOffline(
                    getString(R.string.ism_member_kicked_out, initiatorName),
                    StreamDialogEnum.KickedOut.value,
                    streamId!!,
                    true
                )
            }
            val senderName = IsometrikStreamSdk.getInstance().context.getString(
                R.string.ism_you,
                memberRemoveEvent!!.memberName
            )
            onPresenceMessageEvent(
                MessagesModel(
                    IsometrikStreamSdk.getInstance().context.getString(
                        R.string.ism_member_removed,
                        senderName,
                        memberRemoveEvent!!.initiatorName
                    ), MessageTypeEnum.PresenceMessage.value, memberRemoveEvent!!.timestamp
                )
            )
        } else {
            onPresenceMessageEvent(
                MessagesModel(
                    IsometrikStreamSdk.getInstance().context.getString(
                        R.string.ism_member_removed,
                        memberRemoveEvent!!.memberName,
                        memberRemoveEvent!!.initiatorName
                    ), MessageTypeEnum.PresenceMessage.value, memberRemoveEvent!!.timestamp
                )
            )
        }

        forceToResumeOwnStreamAgain(memberRemoveEvent!!.memberId, memberRemoveEvent.streamId)

    }

    fun forceToResumeOwnStreamAgain(memberId: String, streamId: String?) {
        Log.e("removeUserVideo", "forceToResumeOwnStreamAgain  isPkBattle $isPkBattle")
        if (isPkBattle) {
            if (!isPkStreamer) {
                if (ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.pkInitiatorId.equals(
                        memberId
                    )
                ) {
//                    if (streamId.equals(this.streamId)) {
                    // show dialog
                    lifecycleScope.launch(Main) {
                        showAlertDialog(
                            IsometrikStreamSdk.getInstance()
                                .context
                                .getString(R.string.ism_stream_offline_initiator_stop),
                            StreamDialogEnum.StreamOffline.value,
                            isBroadcaster
                        )
                    }
                }
            } else {
                //  if pk stream kill app that time member left event call. if
                scrollableMultiliveStreamsPresenter.endPKRequested(pkInvitedId, false)
            }
            if (!memberId.isEmpty()) {
                Log.e("removeUserVideo", "999")
                lifecycleScope.launch(Main) {
                    ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.removeUserVideo(
                        this@MultiliveStreamsActivity, this@MultiliveStreamsActivity,
                        UserIdGenerator.getUid(memberId),
                        false,
                        isPkBattle
                    )
                }
            }
            isPkBattle = false
            lifecycleScope.launch(Main) {
                hidePkRelatedUi()
                pkInvitedId = ""
                if (isPkStreamer) {
                    pkButtonVisibility(true)
                }
            }
        }
    }

    override fun pkBattleStoped() {
//        pkid = "";
        stopPkCountDown(true)
//        setPkBattleStopView();
//        activityScrollableStreamsBinding.imgPkStop.setVisibility(View.GONE);
    }

    fun hidePkWinnerUI() {
        ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.hidePkWinnerLooser()
        ismActivityScrollableMultiliveStreamsBinding.tvCongratsWinner.setVisibility(View.GONE)
        //        ismActivityScrollableMultiliveStreamsBinding.tvPunishment.setVisibility(View.GONE);
        ismActivityScrollableMultiliveStreamsBinding.relWhoWinner.setVisibility(View.GONE)
        ismActivityScrollableMultiliveStreamsBinding.ivDrawTag.setVisibility(View.GONE)
        if (isPkBattle) {
            if (afterPkNeedToResetHost) {
                clickedOnChangeHost(false)
            }
        }
    }

    fun hideWinnerUi() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                CoroutineScope(Main).launch { hidePkWinnerUI() }
            }
        }, 10000)
    }

    override fun onPkWinnerFetched(pkResultData: PkResultData?, isDraw: Boolean) {
        hideWinnerUi()
        if (pkResultData != null) {
            val MARGIN_TOP = (95 * resources.displayMetrics.density).toInt()
//            val MARGIN_TOP_PUNISH = (135 * resources.displayMetrics.density).toInt()
            val paramsTvCongrats = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            paramsTvCongrats.setMargins(0, MARGIN_TOP, 0, 0)
            ismActivityScrollableMultiliveStreamsBinding.tvCongratsWinner.setLayoutParams(
                paramsTvCongrats
            )
            paramsTvCongrats.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE)
            val winnerUserName: String =
                scrollableMultiliveStreamsPresenter.getUserNameByStreamUserId(pkResultData.winnerId)
            ismActivityScrollableMultiliveStreamsBinding.tvCongratsWinner.setText("Congratulations to @$winnerUserName")
            ismActivityScrollableMultiliveStreamsBinding.tvCongratsWinner.setVisibility(VISIBLE)

            ismActivityScrollableMultiliveStreamsBinding.relWhoWinner.setVisibility(View.GONE)
            ismActivityScrollableMultiliveStreamsBinding.videoGridContainer.showPkWinnerLooser(
                pkResultData.winnerId,
                winnerUserName,
                animLeftResult,
                animRightResult
            )
        } else {
            if (isDraw) {
                val MARGIN_TOP = (110 * resources.displayMetrics.density).toInt()
                val MARGIN_CENTER = (150 * resources.displayMetrics.density).toInt()
                val paramsTvCongrats = RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                paramsTvCongrats.setMargins(0, MARGIN_TOP, 0, 0)
                ismActivityScrollableMultiliveStreamsBinding.tvCongratsWinner.setLayoutParams(
                    paramsTvCongrats
                )
                paramsTvCongrats.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE)
                val params =
                    ismActivityScrollableMultiliveStreamsBinding.ivDrawTag.getLayoutParams() as RelativeLayout.LayoutParams
                params.setMargins(0, MARGIN_CENTER, 0, 0)
                ismActivityScrollableMultiliveStreamsBinding.ivDrawTag.setLayoutParams(params)
                ismActivityScrollableMultiliveStreamsBinding.ivDrawTag.setVisibility(VISIBLE)
                ismActivityScrollableMultiliveStreamsBinding.tvCongratsWinner.setText("This Battle Tied")
                ismActivityScrollableMultiliveStreamsBinding.tvCongratsWinner.setVisibility(VISIBLE)
                ismActivityScrollableMultiliveStreamsBinding.relWhoWinner.setVisibility(View.GONE)
            }
        }
    }

    override fun clickOnJoin() {
        if (rejoinFragment!!.dialog != null && rejoinFragment!!.dialog?.isShowing == true && !rejoinFragment!!.isRemoving) {
            rejoinFragment!!.dismiss()
        }
        switchProfile()
    }

    override fun clickOnCancel() {
        if (rejoinFragment!!.dialog != null && rejoinFragment!!.dialog?.isShowing == true && !rejoinFragment!!.isRemoving) {
            rejoinFragment!!.dismiss()
        }
    }
}