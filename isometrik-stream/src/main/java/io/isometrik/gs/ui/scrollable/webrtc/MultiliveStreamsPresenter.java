package io.isometrik.gs.ui.scrollable.webrtc;

import android.util.Log;

import com.google.gson.Gson;

import io.isometrik.gs.builder.gift.GiftByCategoryQuery;
import io.isometrik.gs.builder.gift.GiftCategoriesQuery;
import io.isometrik.gs.callbacks.PkEventCallback;
import io.isometrik.gs.response.stream.Streams;
import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.gifts.response.Gift;
import io.isometrik.gs.ui.gifts.response.GiftCategory;
import io.isometrik.gs.ui.gifts.GiftsModel;
import io.isometrik.gs.ui.messages.MessagesModel;
import io.isometrik.gs.ui.pk.challengsSettings.PkBattleEndQuery;
import io.isometrik.gs.ui.pk.challengsSettings.PkBattleStartQuery;
import io.isometrik.gs.ui.pk.challengsSettings.PkBattleStopQuery;
import io.isometrik.gs.ui.pk.challengsSettings.PkBattleWinnersQuery;
import io.isometrik.gs.ui.pk.challengsSettings.PkStreamStatQuery;
import io.isometrik.gs.ui.pk.invitesent.PkInviteAcceptRejectQuery;
import io.isometrik.gs.ui.pk.response.PkChangeStreamEvent;
import io.isometrik.gs.ui.pk.response.PkInviteRelatedEvent;
import io.isometrik.gs.ui.pk.response.PkStopEvent;
import io.isometrik.gs.ui.pk.response.PkStreamStartStopEvent;
import io.isometrik.gs.ui.streams.list.LiveStreamsModel;
import io.isometrik.gs.ui.utils.Constants;
import io.isometrik.gs.ui.utils.DateUtil;
import io.isometrik.gs.ui.utils.MessageTypeEnum;
import io.isometrik.gs.ui.utils.StreamDialogEnum;
import io.isometrik.gs.ui.utils.StreamMemberInfo;
import io.isometrik.gs.ui.utils.UserSession;
import io.isometrik.gs.ui.utils.containers.AudioGridContainerRenderer;
import io.isometrik.gs.ui.utils.containers.VideoGridContainerRenderer;
import io.isometrik.gs.ui.viewers.ViewersListModel;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.copublish.AcceptCopublishRequestQuery;
import io.isometrik.gs.builder.copublish.AddCopublishRequestQuery;
import io.isometrik.gs.builder.copublish.DeleteCopublishRequestQuery;
import io.isometrik.gs.builder.copublish.DenyCopublishRequestQuery;
import io.isometrik.gs.builder.copublish.FetchCopublishRequestStatusQuery;
import io.isometrik.gs.builder.copublish.SwitchProfileQuery;
import io.isometrik.gs.builder.member.FetchMemberDetailsQuery;
import io.isometrik.gs.builder.member.FetchMembersQuery;
import io.isometrik.gs.builder.member.LeaveMemberQuery;
import io.isometrik.gs.builder.member.RemoveMemberQuery;
import io.isometrik.gs.builder.message.DeleteMessageQuery;
import io.isometrik.gs.builder.message.FetchMessagesQuery;
import io.isometrik.gs.builder.message.SendMessageQuery;
import io.isometrik.gs.builder.stream.FetchStreamsQuery;
import io.isometrik.gs.builder.stream.StopStreamQuery;
import io.isometrik.gs.builder.stream.UpdateStreamPublishingStatusQuery;
import io.isometrik.gs.builder.viewer.AddViewerQuery;
import io.isometrik.gs.builder.viewer.FetchViewersCountQuery;
import io.isometrik.gs.builder.viewer.FetchViewersQuery;
import io.isometrik.gs.builder.viewer.LeaveViewerQuery;
import io.isometrik.gs.callbacks.ConnectionEventCallback;
import io.isometrik.gs.callbacks.CopublishEventCallback;
import io.isometrik.gs.callbacks.MemberEventCallback;
import io.isometrik.gs.callbacks.MessageEventCallback;
import io.isometrik.gs.callbacks.ModeratorEventCallback;
import io.isometrik.gs.callbacks.PresenceEventCallback;
import io.isometrik.gs.callbacks.StreamEventCallback;
import io.isometrik.gs.callbacks.ViewerEventCallback;
import io.isometrik.gs.events.connection.ConnectEvent;
import io.isometrik.gs.events.connection.ConnectionFailedEvent;
import io.isometrik.gs.events.connection.DisconnectEvent;
import io.isometrik.gs.events.copublish.CopublishRequestAcceptEvent;
import io.isometrik.gs.events.copublish.CopublishRequestAddEvent;
import io.isometrik.gs.events.copublish.CopublishRequestDenyEvent;
import io.isometrik.gs.events.copublish.CopublishRequestRemoveEvent;
import io.isometrik.gs.events.copublish.CopublishRequestSwitchProfileEvent;
import io.isometrik.gs.events.member.MemberAddEvent;
import io.isometrik.gs.events.member.MemberLeaveEvent;
import io.isometrik.gs.events.member.MemberRemoveEvent;
import io.isometrik.gs.events.member.MemberTimeoutEvent;
import io.isometrik.gs.events.member.NoPublisherLiveEvent;
import io.isometrik.gs.events.member.PublishStartEvent;
import io.isometrik.gs.events.member.PublishStopEvent;
import io.isometrik.gs.events.message.MessageAddEvent;
import io.isometrik.gs.events.message.MessageRemoveEvent;
import io.isometrik.gs.events.message.MessageReplyAddEvent;
import io.isometrik.gs.events.message.MessageReplyRemoveEvent;
import io.isometrik.gs.events.moderator.ModeratorAddEvent;
import io.isometrik.gs.events.moderator.ModeratorLeaveEvent;
import io.isometrik.gs.events.moderator.ModeratorRemoveEvent;
import io.isometrik.gs.events.presence.PresenceStreamStartEvent;
import io.isometrik.gs.events.presence.PresenceStreamStopEvent;
import io.isometrik.gs.events.stream.StreamStartEvent;
import io.isometrik.gs.events.stream.StreamStopEvent;
import io.isometrik.gs.events.viewer.ViewerJoinEvent;
import io.isometrik.gs.events.viewer.ViewerLeaveEvent;
import io.isometrik.gs.events.viewer.ViewerRemoveEvent;
import io.isometrik.gs.events.viewer.ViewerTimeoutEvent;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.member.FetchMembersResult;
import io.isometrik.gs.response.message.utils.StreamMessage;
import io.isometrik.gs.response.viewer.FetchViewersResult;
import io.isometrik.gs.rtcengine.rtc.webrtc.RemoteUsersAudioLevelInfo;
import io.isometrik.gs.rtcengine.utils.UserFeedClickedCallback;
import io.isometrik.gs.rtcengine.utils.UserIdGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The scrollable streams presenter to listen for connections, viewers, members, streams, messages
 * event for
 * the stream group as well as to send/remove message in/from a stream group, fetch messages sent
 * or viewers count in a stream group, to start/stop publishing a stream group as member and to
 * fetch status of a copublish request, make a copublish request, accept/decline a copublish
 * request.
 * It implements
 * ScrollableMultiliveStreamsContract.Presenter{@link MultiliveStreamsContract.Presenter}
 *
 * @see MultiliveStreamsContract.Presenter
 */
public class MultiliveStreamsPresenter implements MultiliveStreamsContract.Presenter {

    /**
     * Instantiates a new scrollable streams presenter.
     */
    MultiliveStreamsPresenter(MultiliveStreamsContract.View scrollableMultiliveStreamsView) {
        this.scrollableMultiliveStreamsView = scrollableMultiliveStreamsView;
        this.scrollableMultiliveStreamsView.initializeHeartsViews();
    }

    private final MultiliveStreamsContract.View scrollableMultiliveStreamsView;

    private boolean isLastStreamsPage;
    private boolean isLoadingStreams;
    private boolean isLastMessagesPage;
    private boolean isLoadingMessages;
    private boolean isAdmin;
    private boolean isBroadcaster;
    private boolean isModerator;
    private boolean isPublic;
    private String streamId;
    private ArrayList<String> memberIds = new ArrayList<>();
    private Map<String, String> members = new HashMap<>();
    private Map<Integer, StreamMemberInfo> membersInfo = new HashMap<>();

    private Map<Integer, Object> memberMutedState = new HashMap<>();

    private final Isometrik isometrik = IsometrikStreamSdk.getInstance().getIsometrik();
    private int streamsOffset, messagesOffset;
    private boolean isPkBattle;


    private boolean networkQualityIndicatorEnabled;


    @Override
    public void initialize(String streamId, ArrayList<String> memberIds, boolean isAdmin, boolean isBroadcaster, boolean isModerator, boolean isPkBattle) {
        this.streamId = streamId;
        if (memberIds != null) {
            this.memberIds = memberIds;
        }
        this.isAdmin = isAdmin;
        this.isBroadcaster = isBroadcaster;
        this.isModerator = isModerator;
        members = new HashMap<>();
        membersInfo = new HashMap<>();
        if (memberIds != null) {
            for (int i = 0; i < memberIds.size(); i++) {
                members.put(memberIds.get(i), "");
            }
        }
        memberMutedState = new HashMap<>();
        isLastMessagesPage = false;
        isLoadingMessages = false;
        this.isPkBattle = isPkBattle;

        if (isBroadcaster && isAdmin) {
            //For the case of messages being fetched twice for host on scroll

            isLastMessagesPage = true;
            if (memberIds != null && memberIds.size() > 1) {
                fetchLatestMembersOnBroadcastStart(streamId);
            }
        }
    }

    private void fetchLatestMembersOnBroadcastStart(String streamId) {

        isometrik.getRemoteUseCases().getMembersUseCases().fetchMembers(new FetchMembersQuery.Builder().setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken()).setStreamId(streamId).build(), (var1, var2) -> {

            if (var1 != null) {
                if (this.streamId.equals(streamId)) {
                    memberIds = new ArrayList<>();
                    members = new HashMap<>();
                    memberMutedState = new HashMap<>();
                    membersInfo = new HashMap<>();

                    ArrayList<FetchMembersResult.StreamMember> members = var1.getStreamMembers();
                    int size = members.size();
                    FetchMembersResult.StreamMember member;
                    Map<String, Object> memberInfo;

                    int uid;
                    for (int i = 0; i < size; i++) {
                        member = members.get(i);
                        memberIds.add(member.getUserId());
                        memberInfo = new HashMap<>();
                        memberInfo.put("audioMuted", false);
                        memberInfo.put("videoMuted", false);
                        memberInfo.put("userId", member.getUserId());
                        memberInfo.put("userName", member.getUserName());

                        uid = UserIdGenerator.getUid(member.getUserId());
                        //IF a remote user has been muted before response of this api,than it might lead to incorrect mute state,thats why disabled updating settings before response
                        memberMutedState.put(uid, memberInfo);
                        this.members.put(member.getUserId(), member.getUserName());

                        membersInfo.put(uid, new StreamMemberInfo(member.getUserId(), member.getUserIdentifier(), member.getUserName(), member.getUserProfileImageUrl(), member.getPublishing()));
                    }
                }
            }
        });
    }


    /**
     * @see CopublishEventCallback
     */
    private final CopublishEventCallback copublishEventCallback = new CopublishEventCallback() {
        @Override
        public void copublishRequestAccepted(@NotNull Isometrik isometrik,
                                             @NotNull CopublishRequestAcceptEvent copublishRequestAcceptEvent) {
            if (copublishRequestAcceptEvent.getStreamId().equals(streamId)) {

                if (!memberIds.contains(copublishRequestAcceptEvent.getUserId())) {
                    memberIds.add(copublishRequestAcceptEvent.getUserId());
                }
                HashMap<String, Object> map = new HashMap<>();
                map.put("videoMuted", false);
                map.put("audioMuted", false);
                map.put("userName", copublishRequestAcceptEvent.getUserName());
                map.put("userId", copublishRequestAcceptEvent.getUserId());

                int uid = UserIdGenerator.getUid(copublishRequestAcceptEvent.getUserId());
                memberMutedState.put(uid, map);

                members.put(copublishRequestAcceptEvent.getUserId(),
                        copublishRequestAcceptEvent.getUserName());

                membersInfo.put(uid, new StreamMemberInfo(copublishRequestAcceptEvent.getUserId(),
                        copublishRequestAcceptEvent.getUserIdentifier(),
                        copublishRequestAcceptEvent.getUserName(),
                        copublishRequestAcceptEvent.getUserProfilePic(), false));

                boolean canJoin = IsometrikStreamSdk.getInstance()
                        .getUserSession()
                        .getUserId()
                        .equals(copublishRequestAcceptEvent.getUserId());

                scrollableMultiliveStreamsView.onPresenceMessageEvent(new MessagesModel(
                        IsometrikStreamSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_copublish_accepted_message,
                                        copublishRequestAcceptEvent.getInitiatorName(),
                                        copublishRequestAcceptEvent.getUserName()),
                        MessageTypeEnum.CopublishRequestAcceptedMessage.getValue(),
                        copublishRequestAcceptEvent.getUserId(),
                        copublishRequestAcceptEvent.getUserIdentifier(),
                        copublishRequestAcceptEvent.getUserProfilePic(),
                        copublishRequestAcceptEvent.getUserName(), copublishRequestAcceptEvent.getTimestamp(),
                        isAdmin, canJoin));

                if (canJoin) {
                    scrollableMultiliveStreamsView.onCopublishRequestAcceptedEvent();
                }

                scrollableMultiliveStreamsView.updateMembersAndViewersCount(
                        copublishRequestAcceptEvent.getMembersCount(),
                        copublishRequestAcceptEvent.getViewersCount());
            } else {

                if (!isBroadcaster) {
                    scrollableMultiliveStreamsView.updateMembersAndViewersCountInList(
                            copublishRequestAcceptEvent.getStreamId(),
                            copublishRequestAcceptEvent.getMembersCount(),
                            copublishRequestAcceptEvent.getViewersCount());
                }
            }
        }

        @Override
        public void copublishRequestDenied(@NotNull Isometrik isometrik,
                                           @NotNull CopublishRequestDenyEvent copublishRequestDenyEvent) {
            if (copublishRequestDenyEvent.getStreamId().equals(streamId)) {

                scrollableMultiliveStreamsView.onPresenceMessageEvent(new MessagesModel(
                        IsometrikStreamSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_copublish_rejected_message,
                                        copublishRequestDenyEvent.getInitiatorName(),
                                        copublishRequestDenyEvent.getUserName()),
                        MessageTypeEnum.CopublishRequestActionMessage.getValue(),
                        copublishRequestDenyEvent.getUserId(), copublishRequestDenyEvent.getUserIdentifier(),
                        copublishRequestDenyEvent.getUserProfilePic(), copublishRequestDenyEvent.getUserName(),
                        copublishRequestDenyEvent.getTimestamp(), isAdmin, false));

                if (IsometrikStreamSdk.getInstance()
                        .getUserSession()
                        .getUserId()
                        .equals(copublishRequestDenyEvent.getUserId())) {

                    scrollableMultiliveStreamsView.onCopublishRequestDeclinedEvent();
                }

                scrollableMultiliveStreamsView.updateMembersAndViewersCount(
                        copublishRequestDenyEvent.getMembersCount(),
                        copublishRequestDenyEvent.getViewersCount());
            } else {

                if (!isBroadcaster) {
                    scrollableMultiliveStreamsView.updateMembersAndViewersCountInList(
                            copublishRequestDenyEvent.getStreamId(), copublishRequestDenyEvent.getMembersCount(),
                            copublishRequestDenyEvent.getViewersCount());
                }
            }
        }

        @Override
        public void copublishRequestAdded(@NotNull Isometrik isometrik,
                                          @NotNull CopublishRequestAddEvent copublishRequestAddEvent) {
            if (copublishRequestAddEvent.getStreamId().equals(streamId)) {

                scrollableMultiliveStreamsView.onPresenceMessageEvent(new MessagesModel(
                        IsometrikStreamSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_request_message, copublishRequestAddEvent.getUserName()),
                        MessageTypeEnum.CopublishRequestMessage.getValue(),
                        copublishRequestAddEvent.getUserId(), copublishRequestAddEvent.getUserIdentifier(),
                        copublishRequestAddEvent.getUserProfilePic(), copublishRequestAddEvent.getUserName(),
                        copublishRequestAddEvent.getTimestamp(), isAdmin, false));

                scrollableMultiliveStreamsView.updateMembersAndViewersCount(
                        copublishRequestAddEvent.getMembersCount(), copublishRequestAddEvent.getViewersCount());
            } else {

                if (!isBroadcaster) {
                    scrollableMultiliveStreamsView.updateMembersAndViewersCountInList(
                            copublishRequestAddEvent.getStreamId(), copublishRequestAddEvent.getMembersCount(),
                            copublishRequestAddEvent.getViewersCount());
                }
            }
        }

        @Override
        public void copublishRequestRemoved(@NotNull Isometrik isometrik,
                                            @NotNull CopublishRequestRemoveEvent copublishRequestRemoveEvent) {
            if (copublishRequestRemoveEvent.getStreamId().equals(streamId)) {

                scrollableMultiliveStreamsView.onPresenceMessageEvent(new MessagesModel(
                        IsometrikStreamSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_copublish_deleted_message,
                                        copublishRequestRemoveEvent.getUserName()),
                        MessageTypeEnum.CopublishRequestActionMessage.getValue(),
                        copublishRequestRemoveEvent.getUserId(),
                        copublishRequestRemoveEvent.getUserIdentifier(),
                        copublishRequestRemoveEvent.getUserProfilePic(),
                        copublishRequestRemoveEvent.getUserName(), copublishRequestRemoveEvent.getTimestamp(),
                        isAdmin, false));

                scrollableMultiliveStreamsView.updateMembersAndViewersCount(
                        copublishRequestRemoveEvent.getMembersCount(),
                        copublishRequestRemoveEvent.getViewersCount());
            } else {

                if (!isBroadcaster) {
                    scrollableMultiliveStreamsView.updateMembersAndViewersCountInList(
                            copublishRequestRemoveEvent.getStreamId(),
                            copublishRequestRemoveEvent.getMembersCount(),
                            copublishRequestRemoveEvent.getViewersCount());
                }
            }
        }

        @Override
        public void switchProfile(@NotNull Isometrik isometrik,
                                  @NotNull CopublishRequestSwitchProfileEvent copublishRequestSwitchProfileEvent) {
            if (copublishRequestSwitchProfileEvent.getStreamId().equals(streamId)) {

                HashMap<String, Object> map = new HashMap<>();
                map.put("videoMuted", false);
                map.put("audioMuted", false);
                map.put("userName", copublishRequestSwitchProfileEvent.getUserName());
                map.put("userId", copublishRequestSwitchProfileEvent.getUserId());
                int uid = UserIdGenerator.getUid(copublishRequestSwitchProfileEvent.getUserId());
                memberMutedState.put(uid, map);

                membersInfo.put(uid, new StreamMemberInfo(copublishRequestSwitchProfileEvent.getUserId(),
                        copublishRequestSwitchProfileEvent.getUserIdentifier(),
                        copublishRequestSwitchProfileEvent.getUserName(),
                        copublishRequestSwitchProfileEvent.getUserProfilePic(), true));

                members.put(copublishRequestSwitchProfileEvent.getUserId(),
                        copublishRequestSwitchProfileEvent.getUserName());

                scrollableMultiliveStreamsView.onPresenceMessageEvent(new MessagesModel(
                        IsometrikStreamSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_switch_profile_message,
                                        copublishRequestSwitchProfileEvent.getUserName()),
                        MessageTypeEnum.CopublishRequestActionMessage.getValue(),
                        copublishRequestSwitchProfileEvent.getUserId(),
                        copublishRequestSwitchProfileEvent.getUserIdentifier(),
                        copublishRequestSwitchProfileEvent.getUserProfilePic(),
                        copublishRequestSwitchProfileEvent.getUserName(),
                        copublishRequestSwitchProfileEvent.getTimestamp(), isAdmin, false));

                scrollableMultiliveStreamsView.updateMembersAndViewersCount(
                        copublishRequestSwitchProfileEvent.getMembersCount(),
                        copublishRequestSwitchProfileEvent.getViewersCount());

                scrollableMultiliveStreamsView.removeViewerEvent(
                        copublishRequestSwitchProfileEvent.getUserId(),
                        copublishRequestSwitchProfileEvent.getViewersCount());
            } else {

                if (!isBroadcaster) {
                    scrollableMultiliveStreamsView.updateMembersAndViewersCountInList(
                            copublishRequestSwitchProfileEvent.getStreamId(),
                            copublishRequestSwitchProfileEvent.getMembersCount(),
                            copublishRequestSwitchProfileEvent.getViewersCount());
                }
            }
        }
    };

    /**
     * @see ConnectionEventCallback
     */
    private final ConnectionEventCallback connectionEventCallback = new ConnectionEventCallback() {
        @Override
        public void disconnected(@NotNull Isometrik isometrik,
                                 @NotNull DisconnectEvent disconnectEvent) {

            scrollableMultiliveStreamsView.connectionStateChanged(false);
        }

        @Override
        public void connected(@NotNull Isometrik isometrik, @NotNull ConnectEvent connectEvent) {

            scrollableMultiliveStreamsView.connectionStateChanged(true);

            if (isBroadcaster) {
                updateBroadcastingStatus(true, streamId, true);
            } else {
                joinAsViewer(streamId, true);
            }
        }

        @Override
        public void connectionFailed(@NotNull Isometrik isometrik,
                                     @NotNull IsometrikError isometrikError) {

            scrollableMultiliveStreamsView.connectionStateChanged(false);
        }

        @Override
        public void failedToConnect(@NotNull Isometrik isometrik,
                                    @NotNull ConnectionFailedEvent connectionFailedEvent) {
            scrollableMultiliveStreamsView.connectionStateChanged(false);
        }
    };

    /**
     * @see StreamEventCallback
     */
    private final StreamEventCallback streamEventCallback = new StreamEventCallback() {
        @Override
        public void streamStarted(@NotNull Isometrik isometrik,
                                  @NotNull StreamStartEvent streamStartEvent) {
            String userId = IsometrikStreamSdk.getInstance().getUserSession().getUserId();

            if (!userId.equals(streamStartEvent.getInitiatorId())) {

                boolean givenUserIsMember = false;
                List<StreamStartEvent.StreamMember> members = streamStartEvent.getMembers();

                ArrayList<String> memberIds = new ArrayList<>();
                String memberId;
                int size = members.size();
                for (int i = 0; i < size; i++) {
                    memberId = members.get(i).getMemberId();
                    memberIds.add(memberId);
                    if (memberId.equals(userId)) {
                        givenUserIsMember = true;
                    }
                }

                scrollableMultiliveStreamsView.onStreamStarted(
                        new LiveStreamsModel(streamStartEvent.getStreamId(), streamStartEvent.getStreamImage(),
                                streamStartEvent.getStreamDescription(), streamStartEvent.getMembersCount(), 0, 1,
                                streamStartEvent.getInitiatorId(), streamStartEvent.getInitiatorName(),
                                streamStartEvent.getInitiatorIdentifier(), streamStartEvent.getInitiatorImage(),
                                streamStartEvent.getTimestamp(), memberIds, givenUserIsMember,
                                streamStartEvent.isPublic(), streamStartEvent.isAudioOnly(),
                                streamStartEvent.isMultiLive(), streamStartEvent.isHdBroadcast(),
                                streamStartEvent.isRestream(), streamStartEvent.isProductsLinked(),
                                streamStartEvent.getProductsCount(), streamStartEvent.isSelfHosted()));
            }
        }

        @Override
        public void streamStopped(@NotNull Isometrik isometrik,
                                  @NotNull StreamStopEvent streamStopEvent) {
            Log.e("streamStopped", "StreamStopEvent "+streamStopEvent.getStreamId()+" : "+streamId);

            if (streamStopEvent.getStreamId().equals(streamId)) {

                scrollableMultiliveStreamsView.onStreamOffline(IsometrikStreamSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_stream_offline_initiator_stop),
                        StreamDialogEnum.StreamOffline.getValue(), streamId, false);
            }

            if (!streamStopEvent.getInitiatorId()
                    .equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId())) {

                scrollableMultiliveStreamsView.onStreamEnded(streamStopEvent.getStreamId(),
                        streamStopEvent.getStreamId().equals(streamId));
            }
        }
    };

    /**
     * @see PresenceEventCallback
     */
    private final PresenceEventCallback presenceEventCallback = new PresenceEventCallback() {
        @Override
        public void streamStarted(@NotNull Isometrik isometrik,
                                  @NotNull PresenceStreamStartEvent presenceStreamStartEvent) {

            String userId = IsometrikStreamSdk.getInstance().getUserSession().getUserId();

            if (!userId.equals(presenceStreamStartEvent.getInitiatorId())) {

                boolean givenUserIsMember = false;
                List<PresenceStreamStartEvent.StreamMember> members = presenceStreamStartEvent.getMembers();

                ArrayList<String> memberIds = new ArrayList<>();
                String memberId;
                int size = members.size();
                for (int i = 0; i < size; i++) {
                    memberId = members.get(i).getMemberId();
                    memberIds.add(memberId);
                    if (memberId.equals(userId)) {
                        givenUserIsMember = true;
                    }
                }

                scrollableMultiliveStreamsView.onStreamStarted(
                        new LiveStreamsModel(presenceStreamStartEvent.getStreamId(),
                                presenceStreamStartEvent.getStreamImage(),
                                presenceStreamStartEvent.getStreamDescription(),
                                presenceStreamStartEvent.getMembersCount(), 0, 1,
                                presenceStreamStartEvent.getInitiatorId(),
                                presenceStreamStartEvent.getInitiatorName(),
                                presenceStreamStartEvent.getInitiatorIdentifier(),
                                presenceStreamStartEvent.getInitiatorImage(),
                                presenceStreamStartEvent.getTimestamp(), memberIds, givenUserIsMember,
                                presenceStreamStartEvent.isPublic(), presenceStreamStartEvent.isAudioOnly(),
                                presenceStreamStartEvent.isMultiLive(), presenceStreamStartEvent.isHdBroadcast(),
                                presenceStreamStartEvent.isRestream(), presenceStreamStartEvent.isProductsLinked(),
                                presenceStreamStartEvent.getProductsCount(),
                                presenceStreamStartEvent.isSelfHosted()));
            }
        }

        @Override
        public void streamStopped(@NotNull Isometrik isometrik,
                                  @NotNull PresenceStreamStopEvent presenceStreamStopEvent) {
            Log.e("streamStopped", "PresenceStreamStopEvent "+presenceStreamStopEvent.getStreamId()+" : "+streamId);

            if (presenceStreamStopEvent.getStreamId().equals(streamId)) {

                scrollableMultiliveStreamsView.onStreamOffline(IsometrikStreamSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_stream_offline_initiator_stop),
                        StreamDialogEnum.StreamOffline.getValue(), streamId, false);
            }
            if (!presenceStreamStopEvent.getInitiatorId()
                    .equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId())) {

                scrollableMultiliveStreamsView.onStreamEnded(presenceStreamStopEvent.getStreamId(),
                        presenceStreamStopEvent.getStreamId().equals(streamId));
            }
        }
    };

    /**
     * @see MessageEventCallback
     */
    private final MessageEventCallback messageEventCallback = new MessageEventCallback() {
        @Override
        public void messageAdded(@NotNull Isometrik isometrik,
                                 @NotNull MessageAddEvent messageAddEvent) {

            if (messageAddEvent.getStreamId().equals(streamId)) {
                if (messageAddEvent.getMessageType() == MessageTypeEnum.NormalMessage.getValue()) {
                    if (!messageAddEvent.getSenderId().equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId())) {
                        scrollableMultiliveStreamsView.onTextMessageReceived(new MessagesModel(messageAddEvent, isModerator));
                    }
                } else if (messageAddEvent.getMessageType() == MessageTypeEnum.ViewerBoughtProductMessage.getValue()) {
                    if (!messageAddEvent.getSenderId().equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId())) {
                        scrollableMultiliveStreamsView.onTextMessageReceived(new MessagesModel(messageAddEvent, false));
                    }
                } else if (messageAddEvent.getMessageType() == MessageTypeEnum.HeartMessage.getValue()) {
                    if (!messageAddEvent.getSenderId().equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId())) {
                        scrollableMultiliveStreamsView.onLikeEvent();
                    }
                } else if (messageAddEvent.getMessageType() == MessageTypeEnum.GiftMessage.getValue()) {

//                    if (!messageAddEvent.getSenderId().equals(IsometrikUiSdk.getInstance().getUserSession().getUserId())) {
                    /**
                     * For sender also need to depends on mqtt event for show gift because while receiving gift coin amount
                     * in event getting lesser than actual, so keep received coin amount same in both sender and receiver sides
                     * taking coins from event data only.
                    * */
                        try {
                            scrollableMultiliveStreamsView.onGiftMessageReceived(new MessagesModel(messageAddEvent));
                            JSONObject jsonObject = messageAddEvent.getMetaData();
                            scrollableMultiliveStreamsView.onGiftEvent(new GiftsModel(messageAddEvent.getSenderId(), messageAddEvent.getSenderName(), messageAddEvent.getSenderProfileImageUrl(), messageAddEvent.getSenderIdentifier(), jsonObject.getString("message"), jsonObject.getInt("coinsValue"), jsonObject.getString("giftName"), jsonObject.getString("giftThumbnailUrl"), jsonObject.getString("reciverId"), jsonObject.getString("reciverStreamUserId"), messageAddEvent.getReceiverName()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                    }
                } else if (messageAddEvent.getMessageType() == MessageTypeEnum.PinnedProduct.getValue()) {
//                    if (messageAddEvent.getStreamId().equals(streamId)) {
//                        //  pin product message event
//                        JSONObject jsonObject = messageAddEvent.getMetaData();
//
//                        try {
//                            pinnedProductId = jsonObject.getString("pinProductId");
//                            Product product = fetchPinnedProductDetails();
//
//                            if (product == null) {
//                                fetchTaggedProducts(streamId);
//                            } else {
//                                scrollableMultiliveStreamsView.onPinnedProductFetchedOrUpdatedSuccessfully(product);
//                            }
//                        } catch (JSONException ignore) {
//                        }
//                    }
                } else if (messageAddEvent.getMessageType() == MessageTypeEnum.OfferUpdated.getValue()) {
//                    if (messageAddEvent.getStreamId().equals(streamId)) {
//                        if (!isBroadcaster) {
//                            //To prevent duplicate api call, as locally updated via callback for broadcaster
//                            try {
//                                Handler handler = new Handler(Looper.getMainLooper());
//                                handler.postDelayed(() -> fetchTaggedProducts(streamId), com.appscrip.socialecom.app.livejet.utils.Constants.DELAY_TO_FETCH_UPDATED_TAGGED_PRODUCTS);
//                            } catch (Exception ignore) {
//                            }
//                        }
//                    }
                } else if (messageAddEvent.getMessageType() == MessageTypeEnum.ChangeToPk.getValue()) {
                    scrollableMultiliveStreamsView.changeToPkEvent();

                } else if (messageAddEvent.getMessageType() == MessageTypeEnum.ChangeStream.getValue()) {
                    try {
                        JSONObject jsonObject = messageAddEvent.getMetaData();
                        Gson gson = new Gson();

                        PkChangeStreamEvent pkChangeStreamEvent = gson.fromJson(jsonObject.toString(), PkChangeStreamEvent.class);
                        scrollableMultiliveStreamsView.changeStreamEvent(pkChangeStreamEvent);
                    } catch (Exception ignored) {
                    }
                } else if (messageAddEvent.getMessageType() == MessageTypeEnum.StartPk.getValue()) {
                    Log.d("PK: ", "getMessageType: " + messageAddEvent.getMessageType());

                    try {
                        JSONObject jsonObject = messageAddEvent.getMetaData();
                        Gson gson = new Gson();
                        PkStreamStartStopEvent pkStreamStartStopEvent = gson.fromJson(jsonObject.toString(), PkStreamStartStopEvent.class);
                        // differentiate user is broadcaster or viewer

                        String loggedInUserId = IsometrikStreamSdk.getInstance().getUserSession().getUserId();

                        if (pkStreamStartStopEvent.getStreamData().getFirstUserDetails().getUserId().equals(loggedInUserId)
                                || pkStreamStartStopEvent.getStreamData().getSecondUserDetails().getUserId().equals(loggedInUserId)) {
                            scrollableMultiliveStreamsView.broadcastersPkStreamStarted(pkStreamStartStopEvent);
                        } else {
                            scrollableMultiliveStreamsView.viewerPkStreamStarted(pkStreamStartStopEvent);
                        }
                    } catch (Exception ignored) {
                        Log.e("PK:", "Exception " + ignored);
                    }
                }
            }
        }

        @Override
        public void messageRemoved(@NotNull Isometrik isometrik,
                                   @NotNull MessageRemoveEvent messageRemoveEvent) {
            if (messageRemoveEvent.getStreamId().equals(streamId)) {
                scrollableMultiliveStreamsView.onMessageRemovedEvent(messageRemoveEvent.getMessageId(),
                        IsometrikStreamSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_message_removed, messageRemoveEvent.getInitiatorName(),
                                        DateUtil.getDate(messageRemoveEvent.getTimestamp())));
            }
        }

        @Override
        public void messageReplyAdded(@NotNull Isometrik isometrik,
                                      @NotNull MessageReplyAddEvent messageReplyAddEvent) {
            if (messageReplyAddEvent.getStreamId().equals(streamId)) {
                scrollableMultiliveStreamsView.onMessageReplyReceived(
                        messageReplyAddEvent.getParentMessageId());
            }
        }

        @Override
        public void messageReplyRemoved(@NotNull Isometrik isometrik,
                                        @NotNull MessageReplyRemoveEvent messageReplyRemoveEvent) {
            //TODO Nothing
        }
    };

    /**
     * @see MemberEventCallback
     */
    private final MemberEventCallback memberEventCallback = new MemberEventCallback() {
        @Override
        public void memberAdded(@NotNull Isometrik isometrik, @NotNull MemberAddEvent memberAddEvent) {
            if (memberAddEvent.getStreamId().equals(streamId)) {

                if (!memberIds.contains(memberAddEvent.getMemberId())) {
                    memberIds.add(memberAddEvent.getMemberId());
                }

                HashMap<String, Object> map = new HashMap<>();
                map.put("videoMuted", false);
                map.put("audioMuted", false);
                map.put("userName", memberAddEvent.getMemberName());
                map.put("userId", memberAddEvent.getMemberId());

                int uid = UserIdGenerator.getUid(memberAddEvent.getMemberId());
                memberMutedState.put(uid, map);

                members.put(memberAddEvent.getMemberId(), memberAddEvent.getMemberName());

                membersInfo.put(uid,
                        new StreamMemberInfo(memberAddEvent.getMemberId(), memberAddEvent.getMemberIdentifier(),
                                memberAddEvent.getMemberName(), memberAddEvent.getMemberProfilePic(), false));

                if (memberAddEvent.getMemberId()
                        .equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId())) {
                    scrollableMultiliveStreamsView.onMemberAdded(true, memberAddEvent.getInitiatorName());
                }

                scrollableMultiliveStreamsView.onPresenceMessageEvent(new MessagesModel(
                        IsometrikStreamSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_member_added, memberAddEvent.getMemberName(),
                                        memberAddEvent.getInitiatorName()), MessageTypeEnum.PresenceMessage.getValue(),
                        memberAddEvent.getTimestamp()));

                scrollableMultiliveStreamsView.updateMembersAndViewersCount(
                        memberAddEvent.getMembersCount(), memberAddEvent.getViewersCount());
            } else {

                if (!isBroadcaster) {
                    scrollableMultiliveStreamsView.updateMembersAndViewersCountInList(
                            memberAddEvent.getStreamId(), memberAddEvent.getMembersCount(),
                            memberAddEvent.getViewersCount());
                }
            }
        }

        @Override
        public void memberLeft(@NotNull Isometrik isometrik,
                               @NotNull MemberLeaveEvent memberLeaveEvent) {
            if (memberLeaveEvent.getStreamId().equals(streamId)) {

                memberIds.remove(memberLeaveEvent.getMemberId());
                members.remove(memberLeaveEvent.getMemberId());
                int uid = UserIdGenerator.getUid(memberLeaveEvent.getMemberId());
                memberMutedState.remove(uid);
                membersInfo.remove(uid);
                scrollableMultiliveStreamsView.onPresenceMessageEvent(new MessagesModel(
                        IsometrikStreamSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_member_left, memberLeaveEvent.getMemberName()),
                        MessageTypeEnum.PresenceMessage.getValue(), memberLeaveEvent.getTimestamp()));

                scrollableMultiliveStreamsView.updateMembersAndViewersCount(
                        memberLeaveEvent.getMembersCount(), memberLeaveEvent.getViewersCount());
            } else {

                if (!isBroadcaster) {
                    scrollableMultiliveStreamsView.updateMembersAndViewersCountInList(
                            memberLeaveEvent.getStreamId(), memberLeaveEvent.getMembersCount(),
                            memberLeaveEvent.getViewersCount());
                }
            }
            scrollableMultiliveStreamsView.onMemberLeft(memberLeaveEvent);
        }

        @Override
        public void memberRemoved(@NotNull Isometrik isometrik,
                                  @NotNull MemberRemoveEvent memberRemoveEvent) {
            if (memberRemoveEvent.getStreamId().equals(streamId)) {

                memberIds.remove(memberRemoveEvent.getMemberId());
                members.remove(memberRemoveEvent.getMemberId());
                int uid = UserIdGenerator.getUid(memberRemoveEvent.getMemberId());
                memberMutedState.remove(uid);
                membersInfo.remove(uid);

//                if (memberRemoveEvent.getMemberId()
//                        .equals(IsometrikUiSdk.getInstance().getUserSession().getUserId())) {
//
//                    scrollableMultiliveStreamsView.onMemberAdded(false, memberRemoveEvent.getInitiatorName());
//
//                    String senderName = IsometrikUiSdk.getInstance()
//                            .getContext()
//                            .getString(R.string.ism_you, memberRemoveEvent.getMemberName());
//
//                    scrollableMultiliveStreamsView.onPresenceMessageEvent(new MessagesModel(
//                            IsometrikUiSdk.getInstance()
//                                    .getContext()
//                                    .getString(R.string.ism_member_removed, senderName,
//                                            memberRemoveEvent.getInitiatorName()),
//                            MessageTypeEnum.PresenceMessage.getValue(), memberRemoveEvent.getTimestamp()));
//                } else {
//
//                    scrollableMultiliveStreamsView.onPresenceMessageEvent(new MessagesModel(
//                            IsometrikUiSdk.getInstance()
//                                    .getContext()
//                                    .getString(R.string.ism_member_removed, memberRemoveEvent.getMemberName(),
//                                            memberRemoveEvent.getInitiatorName()),
//                            MessageTypeEnum.PresenceMessage.getValue(), memberRemoveEvent.getTimestamp()));
//                }
                scrollableMultiliveStreamsView.onMemberRemove(memberRemoveEvent);
                scrollableMultiliveStreamsView.updateMembersAndViewersCount(
                        memberRemoveEvent.getMembersCount(), memberRemoveEvent.getViewersCount());
            } else {

                if (!isBroadcaster) {
                    scrollableMultiliveStreamsView.updateMembersAndViewersCountInList(
                            memberRemoveEvent.getStreamId(), memberRemoveEvent.getMembersCount(),
                            memberRemoveEvent.getViewersCount());
                }
            }
        }

        @Override
        public void memberTimedOut(@NotNull Isometrik isometrik,
                                   @NotNull MemberTimeoutEvent memberTimeoutEvent) {
            if (memberTimeoutEvent.getStreamId().equals(streamId)) {

                members.put(memberTimeoutEvent.getMemberId(), memberTimeoutEvent.getMemberName());

                scrollableMultiliveStreamsView.onPresenceMessageEvent(new MessagesModel(
                        IsometrikStreamSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_member_timeout, memberTimeoutEvent.getMemberName()),
                        MessageTypeEnum.PresenceMessage.getValue(), memberTimeoutEvent.getTimestamp()));

                scrollableMultiliveStreamsView.updateMembersAndViewersCount(
                        memberTimeoutEvent.getMembersCount(), memberTimeoutEvent.getViewersCount());
            } else {

                if (!isBroadcaster) {
                    scrollableMultiliveStreamsView.updateMembersAndViewersCountInList(
                            memberTimeoutEvent.getStreamId(), memberTimeoutEvent.getMembersCount(),
                            memberTimeoutEvent.getViewersCount());
                }
            }
        }

        @Override
        public void memberPublishStarted(@NotNull Isometrik isometrik,
                                         @NotNull PublishStartEvent publishStartEvent) {
            if (publishStartEvent.getStreamId().equals(streamId)) {

                members.put(publishStartEvent.getMemberId(), publishStartEvent.getMemberName());

                scrollableMultiliveStreamsView.onPresenceMessageEvent(new MessagesModel(
                        IsometrikStreamSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_member_publish_start, publishStartEvent.getMemberName()),
                        MessageTypeEnum.PresenceMessage.getValue(), publishStartEvent.getTimestamp()));

                scrollableMultiliveStreamsView.updateMembersAndViewersCount(
                        publishStartEvent.getMembersCount(), publishStartEvent.getViewersCount());
            } else {

                if (!isBroadcaster) {
                    scrollableMultiliveStreamsView.updateMembersAndViewersCountInList(
                            publishStartEvent.getStreamId(), publishStartEvent.getMembersCount(),
                            publishStartEvent.getViewersCount());
                }
            }
        }

        @Override
        public void memberPublishStopped(@NotNull Isometrik isometrik,
                                         @NotNull PublishStopEvent publishStopEvent) {
            if (publishStopEvent.getStreamId().equals(streamId)) {

                members.put(publishStopEvent.getMemberId(), publishStopEvent.getMemberName());

                scrollableMultiliveStreamsView.onPresenceMessageEvent(new MessagesModel(
                        IsometrikStreamSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_member_publish_stop, publishStopEvent.getMemberName()),
                        MessageTypeEnum.PresenceMessage.getValue(), publishStopEvent.getTimestamp()));

                scrollableMultiliveStreamsView.updateMembersAndViewersCount(
                        publishStopEvent.getMembersCount(), publishStopEvent.getViewersCount());
            } else {

                if (!isBroadcaster) {
                    scrollableMultiliveStreamsView.updateMembersAndViewersCountInList(
                            publishStopEvent.getStreamId(), publishStopEvent.getMembersCount(),
                            publishStopEvent.getViewersCount());
                }
            }
        }

        @Override
        public void noMemberPublishing(@NotNull Isometrik isometrik,
                                       @NotNull NoPublisherLiveEvent noPublisherLiveEvent) {
            if (noPublisherLiveEvent.getStreamId().equals(streamId)) {
                scrollableMultiliveStreamsView.onStreamOffline(IsometrikStreamSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_stream_offline_no_publisher),
                        StreamDialogEnum.StreamOffline.getValue(), streamId, false);
            }
        }
    };

    /**
     * @see ViewerEventCallback
     */
    private final ViewerEventCallback viewerEventCallback = new ViewerEventCallback() {
        @Override
        public void viewerJoined(@NotNull Isometrik isometrik,
                                 @NotNull ViewerJoinEvent viewerJoinEvent) {

            if (viewerJoinEvent.getStreamId().equals(streamId)) {

                scrollableMultiliveStreamsView.onPresenceMessageEvent(new MessagesModel(
                        IsometrikStreamSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_viewer_joined, viewerJoinEvent.getViewerName()),
                        MessageTypeEnum.PresenceMessage.getValue(), viewerJoinEvent.getTimestamp()));

                scrollableMultiliveStreamsView.updateMembersAndViewersCount(
                        viewerJoinEvent.getMembersCount(), viewerJoinEvent.getViewersCount());

                scrollableMultiliveStreamsView.addViewerEvent(
                        new ViewersListModel(viewerJoinEvent.getViewerId(),
                                viewerJoinEvent.getViewerProfilePic()), viewerJoinEvent.getViewersCount());
            } else {

                if (!isBroadcaster) {
                    scrollableMultiliveStreamsView.updateMembersAndViewersCountInList(
                            viewerJoinEvent.getStreamId(), viewerJoinEvent.getMembersCount(),
                            viewerJoinEvent.getViewersCount());
                }
            }
        }

        @Override
        public void viewerLeft(@NotNull Isometrik isometrik,
                               @NotNull ViewerLeaveEvent viewerLeaveEvent) {
            if (viewerLeaveEvent.getStreamId().equals(streamId)) {

                scrollableMultiliveStreamsView.onPresenceMessageEvent(new MessagesModel(
                        IsometrikStreamSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_viewer_left, viewerLeaveEvent.getViewerName()),
                        MessageTypeEnum.PresenceMessage.getValue(), viewerLeaveEvent.getTimestamp()));

                scrollableMultiliveStreamsView.updateMembersAndViewersCount(
                        viewerLeaveEvent.getMembersCount(), viewerLeaveEvent.getViewersCount());

                scrollableMultiliveStreamsView.removeViewerEvent(viewerLeaveEvent.getViewerId(),
                        viewerLeaveEvent.getViewersCount());
            } else {

                if (!isBroadcaster) {
                    scrollableMultiliveStreamsView.updateMembersAndViewersCountInList(
                            viewerLeaveEvent.getStreamId(), viewerLeaveEvent.getMembersCount(),
                            viewerLeaveEvent.getViewersCount());
                }
            }
        }

        @Override
        public void viewerRemoved(@NotNull Isometrik isometrik,
                                  @NotNull ViewerRemoveEvent viewerRemoveEvent) {

            if (viewerRemoveEvent.getStreamId().equals(streamId)) {

                members.put(viewerRemoveEvent.getInitiatorId(), viewerRemoveEvent.getInitiatorName());
                if (viewerRemoveEvent.getViewerId()
                        .equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId()) && !isBroadcaster) {

                    scrollableMultiliveStreamsView.onStreamOffline(IsometrikStreamSdk.getInstance()
                                    .getContext()
                                    .getString(R.string.ism_viewer_kicked_out, viewerRemoveEvent.getInitiatorName()),
                            StreamDialogEnum.KickedOut.getValue(), streamId, true);
                } else {

                    scrollableMultiliveStreamsView.onPresenceMessageEvent(new MessagesModel(
                            IsometrikStreamSdk.getInstance()
                                    .getContext()
                                    .getString(R.string.ism_viewer_removed, viewerRemoveEvent.getViewerName(),
                                            viewerRemoveEvent.getInitiatorName()),
                            MessageTypeEnum.PresenceMessage.getValue(), viewerRemoveEvent.getTimestamp()));

                    scrollableMultiliveStreamsView.updateMembersAndViewersCount(
                            viewerRemoveEvent.getMembersCount(), viewerRemoveEvent.getViewersCount());

                    scrollableMultiliveStreamsView.removeViewerEvent(viewerRemoveEvent.getViewerId(),
                            viewerRemoveEvent.getViewersCount());
                }
            } else {

                if (!isBroadcaster) {
                    scrollableMultiliveStreamsView.updateMembersAndViewersCountInList(
                            viewerRemoveEvent.getStreamId(), viewerRemoveEvent.getMembersCount(),
                            viewerRemoveEvent.getViewersCount());
                }
            }
        }

        @Override
        public void viewerTimedOut(@NotNull Isometrik isometrik,
                                   @NotNull ViewerTimeoutEvent viewerTimeoutEvent) {
            if (viewerTimeoutEvent.getStreamId().equals(streamId)) {

                scrollableMultiliveStreamsView.onPresenceMessageEvent(new MessagesModel(
                        IsometrikStreamSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_viewer_timeout, viewerTimeoutEvent.getViewerName()),
                        MessageTypeEnum.PresenceMessage.getValue(), viewerTimeoutEvent.getTimestamp()));

                scrollableMultiliveStreamsView.updateMembersAndViewersCount(
                        viewerTimeoutEvent.getMembersCount(), viewerTimeoutEvent.getViewersCount());

                scrollableMultiliveStreamsView.removeViewerEvent(viewerTimeoutEvent.getViewerId(),
                        viewerTimeoutEvent.getViewersCount());
            } else {

                if (!isBroadcaster) {
                    scrollableMultiliveStreamsView.updateMembersAndViewersCountInList(
                            viewerTimeoutEvent.getStreamId(), viewerTimeoutEvent.getMembersCount(),
                            viewerTimeoutEvent.getViewersCount());
                }
            }
        }
    };

    private final ModeratorEventCallback moderatorEventCallback = new ModeratorEventCallback() {
        @Override
        public void moderatorAdded(@NotNull Isometrik isometrik,
                                   @NotNull ModeratorAddEvent moderatorAddEvent) {
            if (moderatorAddEvent.getStreamId().equals(streamId)) {
                if (moderatorAddEvent.getModeratorId()
                        .equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId())) {
                    scrollableMultiliveStreamsView.moderatorStatusUpdated(true,
                            moderatorAddEvent.getInitiatorName(), moderatorAddEvent.getModeratorName());
                    isModerator = true;
                }
            }
        }

        @Override
        public void moderatorLeft(@NotNull Isometrik isometrik,
                                  @NotNull ModeratorLeaveEvent moderatorLeaveEvent) {
            if (moderatorLeaveEvent.getStreamId().equals(streamId)) {
                if (moderatorLeaveEvent.getModeratorId()
                        .equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId())) {
                    isModerator = false;
                }
            }
        }

        @Override
        public void moderatorRemoved(@NotNull Isometrik isometrik,
                                     @NotNull ModeratorRemoveEvent moderatorRemoveEvent) {
            if (moderatorRemoveEvent.getStreamId().equals(streamId)) {
                if (moderatorRemoveEvent.getModeratorId()
                        .equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId())) {
                    scrollableMultiliveStreamsView.moderatorStatusUpdated(false,
                            moderatorRemoveEvent.getInitiatorName(), moderatorRemoveEvent.getModeratorName());
                    isModerator = false;
                }
            }
        }
    };

    /**
     * @see PkEventCallback
     */
    private final PkEventCallback pkEventCallback = new PkEventCallback() {
        @Override
        public void pkInviteReceived(@NotNull Isometrik isometrik, @NotNull PkInviteRelatedEvent pkInviteRelatedEvent) {
            Log.d("PK:", "==> " + pkInviteRelatedEvent.getMetaData().getStreamId());
//               if(pkInviteReceivedEvent.getMetaData().getStreamId().equals(streamId)){
            scrollableMultiliveStreamsView.pkInviteReceived(pkInviteRelatedEvent);
//               }
        }

        @Override
        public void pkInviteStatus(@NotNull Isometrik isometrik, @NotNull PkInviteRelatedEvent pkInviteRelatedEvent) {
            scrollableMultiliveStreamsView.onPkInviteStatusChange(pkInviteRelatedEvent);
        }

        @Override
        public void pkStopForViewer(@NotNull Isometrik isometrik, @NotNull PkStopEvent pkStopEvent) {
            scrollableMultiliveStreamsView.viewerPkStreamStopped(pkStopEvent);
        }

        @Override
        public void pkStopForPublisher(@NotNull Isometrik isometrik, @NotNull PkStopEvent pkStopEvent) {
            scrollableMultiliveStreamsView.broadcastersPkStreamStopped(pkStopEvent);
        }
    };

    /**
     * {@link MultiliveStreamsContract.Presenter#registerModeratorEventListener()}
     */
    @Override
    public void registerModeratorEventListener() {
        isometrik.getRealtimeEventsListenerManager().addModeratorEventListener(moderatorEventCallback);
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#unregisterModeratorEventListener()}
     */
    @Override
    public void unregisterModeratorEventListener() {
        isometrik.getRealtimeEventsListenerManager()
                .removeModeratorEventListener(moderatorEventCallback);
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#registerConnectionEventListener()}
     */
    @Override
    public void registerConnectionEventListener() {
        isometrik.getRealtimeEventsListenerManager()
                .addConnectionEventListener(connectionEventCallback);
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#unregisterConnectionEventListener()}
     */
    @Override
    public void unregisterConnectionEventListener() {
        isometrik.getRealtimeEventsListenerManager()
                .removeConnectionEventListener(connectionEventCallback);
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#registerStreamsEventListener()}
     */
    @Override
    public void registerStreamsEventListener() {
        isometrik.getRealtimeEventsListenerManager().addStreamEventListener(streamEventCallback);
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#unregisterStreamsEventListener()}
     */
    @Override
    public void unregisterStreamsEventListener() {
        isometrik.getRealtimeEventsListenerManager().removeStreamEventListener(streamEventCallback);
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#registerPresenceEventListener()}
     */
    @Override
    public void registerPresenceEventListener() {
        isometrik.getRealtimeEventsListenerManager().addPresenceEventListener(presenceEventCallback);
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#unregisterPresenceEventListener()}
     */
    @Override
    public void unregisterPresenceEventListener() {
        isometrik.getRealtimeEventsListenerManager().removePresenceEventListener(presenceEventCallback);
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#registerStreamMessagesEventListener()}
     */
    @Override
    public void registerStreamMessagesEventListener() {
        isometrik.getRealtimeEventsListenerManager().addMessageEventListener(messageEventCallback);
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#unregisterStreamMessagesEventListener()}
     */
    @Override
    public void unregisterStreamMessagesEventListener() {
        isometrik.getRealtimeEventsListenerManager().removeMessageEventListener(messageEventCallback);
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#registerStreamMembersEventListener()}
     */
    @Override
    public void registerStreamMembersEventListener() {
        isometrik.getRealtimeEventsListenerManager().addMemberEventListener(memberEventCallback);
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#unregisterStreamMembersEventListener()}
     */
    @Override
    public void unregisterStreamMembersEventListener() {
        isometrik.getRealtimeEventsListenerManager().removeMemberEventListener(memberEventCallback);
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#registerStreamViewersEventListener()}
     */
    @Override
    public void registerStreamViewersEventListener() {
        isometrik.getRealtimeEventsListenerManager().addViewerEventListener(viewerEventCallback);
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#unregisterStreamViewersEventListener()}
     */
    @Override
    public void unregisterStreamViewersEventListener() {
        isometrik.getRealtimeEventsListenerManager().removeViewerEventListener(viewerEventCallback);
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#registerCopublishRequestsEventListener()}
     */
    @Override
    public void registerCopublishRequestsEventListener() {
        isometrik.getRealtimeEventsListenerManager().addCopublishEventListener(copublishEventCallback);
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#unregisterCopublishRequestsEventListener()}
     */
    @Override
    public void unregisterCopublishRequestsEventListener() {
        isometrik.getRealtimeEventsListenerManager()
                .removeCopublishEventListener(copublishEventCallback);
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#registerPkEventListener()}
     */
    @Override
    public void registerPkEventListener() {
        isometrik.getRealtimeEventsListenerManager().addPkEventListener(pkEventCallback);

    }

    /**
     * {@link MultiliveStreamsContract.Presenter#unregisterPkEventListener()}
     */
    @Override
    public void unregisterPkEventListener() {
        isometrik.getRealtimeEventsListenerManager().removePKEventListener(pkEventCallback);
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#requestStreamMessagesData(String, int,
     * boolean)}
     */
    @Override
    public void requestStreamMessagesData(String streamId, int messagesOffset,
                                          boolean refreshRequest) {
        isLoadingMessages = true;

        if (refreshRequest) {
            this.messagesOffset = 0;
            isLastMessagesPage = false;
        }

        isometrik.getRemoteUseCases()
                .getMessagesUseCases()
                .fetchMessages(new FetchMessagesQuery.Builder().setUserToken(
                                IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                        .setLimit(Constants.MESSAGES_PAGE_SIZE)
                        .setStreamId(streamId)
                        .setSkip(messagesOffset)
                        .setMessageTypes(Collections.singletonList(MessageTypeEnum.NormalMessage.getValue()))
                        .build(), (var1, var2) -> {
                    if (this.streamId.equals(streamId)) {
                        if (var1 != null) {

                            ArrayList<StreamMessage> messages = var1.getStreamMessages();
                            int size = messages.size();
                            String userId = IsometrikStreamSdk.getInstance().getUserSession().getUserId();

                            if (size < Constants.MESSAGES_PAGE_SIZE) {

                                isLastMessagesPage = true;
                            }

                            ArrayList<MessagesModel> messagesModels = new ArrayList<>();

                            for (int i = 0; i < size; i++) {

                                messagesModels.add(0, new MessagesModel(messages.get(i), isModerator, userId));
                            }

                            scrollableMultiliveStreamsView.onStreamMessagesDataReceived(messagesModels,
                                    refreshRequest);
                        } else {
                            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {

                                if (refreshRequest) {
                                    //No messages found

                                    scrollableMultiliveStreamsView.onStreamMessagesDataReceived(new ArrayList<>(),
                                            true);
                                } else {
                                    isLastMessagesPage = true;
                                }
                            } else {

                                scrollableMultiliveStreamsView.onError(var2.getErrorMessage());
                            }
                        }
                    }
                    isLoadingMessages = false;
                });
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#requestStreamMessagesDataOnScroll(String)}
     */
    @Override
    public void requestStreamMessagesDataOnScroll(String streamId) {
        if (!isLoadingMessages && !isLastMessagesPage) {
            messagesOffset++;
            requestStreamMessagesData(streamId, messagesOffset * Constants.MESSAGES_PAGE_SIZE, false);
        }
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#sendMessage(String, String,
     * int, int, String, String)}
     */
    @Override
    public void sendMessage(String streamId, String message, int messageType, int coinsValue,
                            String giftName, String giftThumbnailUrl) {

        long timestamp = System.currentTimeMillis();

        UserSession userSession = IsometrikStreamSdk.getInstance().getUserSession();

        SendMessageQuery.Builder sendMessageQueryBuilder =
                new SendMessageQuery.Builder().setBody(message)
                        .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                        .setStreamId(streamId)
                        .setSearchableTags(new ArrayList<>(Collections.singletonList(message)))
                        .setDeviceId(IsometrikStreamSdk.getInstance().getUserSession().getDeviceId());

        switch (messageType) {

            case 0:
                if (this.streamId.equals(streamId)) {
                    scrollableMultiliveStreamsView.onTextMessageSent(
                            new MessagesModel(message, String.valueOf(timestamp),
                                    MessageTypeEnum.NormalMessage.getValue(), timestamp, isModerator, userSession));
                }

                sendMessageQueryBuilder.setMessageType(MessageTypeEnum.NormalMessage.getValue());
                JSONObject jsonObject3 = new JSONObject();
                try {
                    jsonObject3.put("userProfile", userSession.getUserProfilePic());
                    jsonObject3.put("fullName", userSession.getUserName());
                    jsonObject3.put("userName", userSession.getUserName());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sendMessageQueryBuilder.setMetaData(jsonObject3);
                break;

            case 1:
            case 3:

                sendMessageQueryBuilder.setMessageType(MessageTypeEnum.HeartMessage.getValue());
                break;

            case 2:

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("coinsValue", coinsValue);
                    jsonObject.put("message", message);
                    jsonObject.put("giftName", giftName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sendMessageQueryBuilder.setBody(jsonObject.toString());
                sendMessageQueryBuilder.setMessageType(MessageTypeEnum.GiftMessage.getValue());
                break;
            case 10:
                JSONObject jsonObject2 = new JSONObject();
                try {
                    jsonObject2.put("giftThumbnailUrl", message);
                    jsonObject2.put("message", message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sendMessageQueryBuilder.setBody(jsonObject2.toString());
                sendMessageQueryBuilder.setMessageType(MessageTypeEnum.ThreeDMessage.getValue());
                break;
        }

        isometrik.getRemoteUseCases()
                .getMessagesUseCases()
                .sendMessage(sendMessageQueryBuilder.build(), (var1, var2) -> {

                    if (var1 != null) {
                        if (this.streamId.equals(streamId)) {
                            switch (messageType) {
                                case 0:
                                    scrollableMultiliveStreamsView.onMessageDelivered(var1.getMessageId(),
                                            String.valueOf(timestamp));
                                    break;

                                case 1:
                                case 3:
                                    scrollableMultiliveStreamsView.onLikeEvent();
                                    break;

                                case 2:
                                    scrollableMultiliveStreamsView.onGiftEvent(
                                            new GiftsModel(userSession.getUserId(), userSession.getUserName(),
                                                    userSession.getUserProfilePic(), userSession.getUserIdentifier(), message,
                                                    coinsValue, giftName, giftThumbnailUrl, "", "", ""));

                                    break;

                                case 10:
                                    scrollableMultiliveStreamsView.onFullGiftEvent(message);
                                    break;
                            }
                        }
                    } else {
                        if (var2.getHttpResponseCode() == 403 && var2.getRemoteErrorCode() == 1) {
                            //Stream not live anymore
                            scrollableMultiliveStreamsView.onStreamOffline(
                                    IsometrikStreamSdk.getInstance().getContext().getString(R.string.ism_stream_offline),
                                    StreamDialogEnum.StreamOffline.getValue(), streamId, false);
                        } else {
                            if (this.streamId.equals(streamId)) {
                                scrollableMultiliveStreamsView.onError(var2.getErrorMessage());
                            }
                        }
                    }
                });
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#removeMessage(String, String, long)}
     */
    @Override
    public void removeMessage(String streamId, String messageId, long timestamp) {

        UserSession userSession = IsometrikStreamSdk.getInstance().getUserSession();

        DeleteMessageQuery deleteMessageQuery = new DeleteMessageQuery.Builder().setMessageId(messageId)
                .setStreamId(streamId)
                .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                .build();

        isometrik.getRemoteUseCases()
                .getMessagesUseCases()
                .deleteMessage(deleteMessageQuery, (var1, var2) -> {
                    if (var1 != null) {
                        if (this.streamId.equals(streamId)) {
                            scrollableMultiliveStreamsView.onMessageRemovedEvent(messageId,
                                    IsometrikStreamSdk.getInstance()
                                            .getContext()
                                            .getString(R.string.ism_message_removed, userSession.getUserName(),
                                                    DateUtil.getDate(var1.getDeletedAt())));
                        }
                    } else {

                        if (this.streamId.equals(streamId)) {
                            scrollableMultiliveStreamsView.onError(var2.getErrorMessage());
                        }
                    }
                });
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#updateBroadcastingStatus(boolean, String,
     * boolean)}
     */
    @Override
    public void updateBroadcastingStatus(boolean startBroadcast, String streamId,
                                         boolean rejoinRequest) {

        isometrik.getRemoteUseCases()
                .getStreamsUseCases()
                .updateStreamPublishingStatus(
                        new UpdateStreamPublishingStatusQuery.Builder().setStreamId(streamId)
                                .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                                .setStartPublish(startBroadcast)
                                .build(), (var1, var2) -> {

                            if (startBroadcast) {
                                if (var1 != null) {
                                    if (this.streamId.equals(streamId)) {
                                        scrollableMultiliveStreamsView.onBroadcastStatusUpdated(true, rejoinRequest,
                                                var1.getRtcToken(), var1.isModerator());
                                        isModerator = var1.isModerator();
                                        fetchLatestMembers(streamId);
                                        //fetchViewersCount();
                                        requestStreamViewersData(streamId);
                                    }
                                } else {

                                    if (var2.getHttpResponseCode() == 409 && var2.getRemoteErrorCode() == 0) {
                                        if (this.streamId.equals(streamId)) {
                                            //Member already publishing
                                            scrollableMultiliveStreamsView.onBroadcastStatusUpdated(true, rejoinRequest,
                                                    null, null);

                                            fetchLatestMembers(streamId);
                                            //fetchViewersCount();
                                            requestStreamViewersData(streamId);
                                        }
                                    } else {
                                        if (this.streamId.equals(streamId)) {
                                            if (var2.getErrorMessage() != null) {
                                                scrollableMultiliveStreamsView.onBroadcastingStatusUpdateFailed(true,
                                                        var2.getErrorMessage());
                                            }
                                        }
                                    }
                                }
                            }
                        });
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#leaveStreamGroup(String)}
     */
    @Override
    public void leaveStreamGroup(String streamId) {

        isometrik.getRemoteUseCases()
                .getMembersUseCases()
                .leaveMember(new LeaveMemberQuery.Builder().setStreamId(streamId)
                        .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                        .build(), (var1, var2) -> {

                    if (var1 != null) {
                        if (this.streamId.equals(streamId)) {
                            scrollableMultiliveStreamsView.onStreamGroupLeft();
                        }
                    } else {

                        if (this.streamId.equals(streamId)) {
                            scrollableMultiliveStreamsView.onError(var2.getErrorMessage());
                        }
                    }
                });
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#stopBroadcast(String, String)}
     */
    @Override
    public void stopBroadcast(String streamIdParam, String pkInviteId) {

        isometrik.getRemoteUseCases()
                .getStreamsUseCases()
                .stopStream(new StopStreamQuery.Builder().setStreamId(streamId)
                        .setUserId(IsometrikStreamSdk.getInstance().getUserSession().getUserId())
                        .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                        .build(), (var1, var2) -> {

                    if (var1 != null) {
                        if (this.streamId.equals(streamId)) {
                            scrollableMultiliveStreamsView.onBroadcastEnded();
                        }
                    } else {

                        if (this.streamId.equals(streamId)) {
                            scrollableMultiliveStreamsView.onError(var2.getErrorMessage());
                        }
                        if (this.streamId.equals(streamId)) {   // even error we need to close screen
                            scrollableMultiliveStreamsView.onBroadcastEnded();
                        }

                    }
                });
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#getMemberIds()}
     */
    @Override
    public ArrayList<String> getMemberIds() {
        return memberIds;
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#getMembers()}
     */
    @Override
    public Map<String, String> getMembers() {
        return members;
    }

    /**
     * Update list of members on reconnect request or when first time stream group is joined as
     * member.
     */
    public void fetchLatestMembers(String streamId) {

        isometrik.getRemoteUseCases()
                .getMembersUseCases()
                .fetchMembers(new FetchMembersQuery.Builder().setStreamId(streamId)
                        .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                        .build(), (var1, var2) -> {

                    if (var1 != null) {
                        if (this.streamId.equals(streamId)) {
                            memberIds = new ArrayList<>();
                            members = new HashMap<>();
                            memberMutedState = new HashMap<>();
                            membersInfo = new HashMap<>();

                            ArrayList<FetchMembersResult.StreamMember> members = var1.getStreamMembers();
                            int size = members.size();
                            FetchMembersResult.StreamMember member;
                            Map<String, Object> memberInfo;

                            int uid;
                            for (int i = 0; i < size; i++) {
                                member = members.get(i);
                                memberIds.add(member.getUserId());
                                memberInfo = new HashMap<>();
                                memberInfo.put("audioMuted", false);
                                memberInfo.put("videoMuted", false);
                                memberInfo.put("userId", member.getUserId());
                                memberInfo.put("userName", member.getUserName());

                                uid = UserIdGenerator.getUid(member.getUserId());
                                //IF a remote user has been muted before response of this api,than it might lead to incorrect mute state,thats why disabled updating settings before response
                                memberMutedState.put(uid, memberInfo);
                                this.members.put(member.getUserId(), member.getUserName());

                                membersInfo.put(uid,
                                        new StreamMemberInfo(member.getUserId(), member.getUserIdentifier(),
                                                member.getUserName(), member.getUserProfileImageUrl(),
                                                member.getPublishing()));
                            }

                            scrollableMultiliveStreamsView.updateMembersAndViewersCount(size, -1);

                            if (memberIds.contains(IsometrikStreamSdk.getInstance().getUserSession().getUserId())) {
                                scrollableMultiliveStreamsView.updateVisibilityForJoinButton();
                            }
                            scrollableMultiliveStreamsView.updateRemoteUserDetails(this.members, members);
                        } else {

                            if (!isBroadcaster) {
                                scrollableMultiliveStreamsView.updateMembersAndViewersCountInList(streamId,
                                        var1.getStreamMembers().size(), -1);
                            }
                        }
                    } else {
                        if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {
                            if (this.streamId.equals(streamId)) {
                                //No stream members
                                memberIds = new ArrayList<>();
                                members = new HashMap<>();
                                memberMutedState = new HashMap<>();
                                membersInfo = new HashMap<>();
                            }
                        } else {
                            if (this.streamId.equals(streamId)) {
                                scrollableMultiliveStreamsView.onError(var2.getErrorMessage());
                            }
                        }
                    }
                });
    }

    /**
     * Fetch viewers count in a stream group.
     */
    private void fetchViewersCount(String streamId) {
        isometrik.getRemoteUseCases()
                .getViewersUseCases()
                .fetchViewersCount(new FetchViewersCountQuery.Builder().setStreamId(streamId)
                        .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                        .build(), (var1, var2) -> {

                    if (var1 != null) {
                        if (this.streamId.equals(streamId)) {
                            scrollableMultiliveStreamsView.updateMembersAndViewersCount(-1,
                                    var1.getNumberOfViewers());
                        } else {
                            if (!isBroadcaster) {
                                scrollableMultiliveStreamsView.updateMembersAndViewersCountInList(streamId, -1,
                                        var1.getNumberOfViewers());
                            }
                        }
                    } else {
                        if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {

                            if (this.streamId.equals(streamId)) {
                                //No viewers found
                                scrollableMultiliveStreamsView.updateMembersAndViewersCount(-1, 0);
                            } else {
                                if (!isBroadcaster) {
                                    scrollableMultiliveStreamsView.updateMembersAndViewersCountInList(streamId, -1,
                                            0);
                                }
                            }
                        } else {
                            if (this.streamId.equals(streamId)) {
                                scrollableMultiliveStreamsView.onError(var2.getErrorMessage());
                            }
                        }
                    }
                });
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#joinAsViewer(String, boolean)}
     */
    @Override
    public void joinAsViewer(String streamId, boolean rejoinRequest) {
        isometrik.getRemoteUseCases()
                .getViewersUseCases()
                .addViewer(new AddViewerQuery.Builder().setStreamId(streamId)
                        .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                        .build(), (var1, var2) -> {

                    if (var1 != null) {
                        if (this.streamId.equals(streamId)) {
                            scrollableMultiliveStreamsView.onStreamJoined(var1.getNumberOfViewers(),
                                    rejoinRequest, var1.getRtcToken(), var1.isModerator());
                            isModerator = var1.isModerator();
                            fetchLatestMembers(streamId);
                            requestStreamViewersData(streamId);
                            if (isPublic) {
                                if (!rejoinRequest) {
                                    checkCopublishRequestStatus(streamId);
                                }
                            }
                        }
                    } else {

                        if (var2.getHttpResponseCode() == 409 && var2.getRemoteErrorCode() == 0) {

                            if (this.streamId.equals(streamId)) {
                                //User already a viewer of stream
                                scrollableMultiliveStreamsView.onStreamJoined(-1, rejoinRequest, null, null);

                                fetchLatestMembers(streamId);
                                //fetchViewersCount();
                                requestStreamViewersData(streamId);
                            }
                        } else {
                            if (this.streamId.equals(streamId)) {
                                scrollableMultiliveStreamsView.onStreamJoinError(var2.getErrorMessage());
                            }
                        }
                    }
                });
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#leaveAsViewer(String, boolean)}
     */
    @Override
    public void leaveAsViewer(String streamId, boolean locally) {

        isometrik.getRemoteUseCases()
                .getViewersUseCases()
                .leaveViewer(new LeaveViewerQuery.Builder().setStreamId(streamId)
                        .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                        .build(), (var1, var2) -> {

                });
    }

    @Override
    public String getUserFullNameByStreamUserId(String streamUserId) {
        String userName = "";
        for (Map.Entry<Integer, StreamMemberInfo> entry : membersInfo.entrySet()) {

            StreamMemberInfo streamMemberInfo = entry.getValue();
//            for (Map.Entry<String, String> entry : members.entrySet()) {
            if (streamMemberInfo.getUserId().equals(streamUserId)) {
                userName = streamMemberInfo.getUserName();
                break;
            }
        }

        return userName;
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#checkCopublishRequestStatus(String)}
     */
    @Override
    public void checkCopublishRequestStatus(String streamId) {
        isometrik.getRemoteUseCases()
                .getCopublishUseCases()
                .fetchCopublishRequestStatus(
                        new FetchCopublishRequestStatusQuery.Builder().setStreamId(streamId)
                                .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                                .build(), (var1, var2) -> {

                            if (var1 != null) {
                                if (this.streamId.equals(streamId)) {
                                    scrollableMultiliveStreamsView.onCopublishRequestStatusFetched(true,
                                            var1.isPending(), var1.isAccepted());
                                }
                            } else {

                                if (var2.getHttpResponseCode() == 400) {

                                    if (this.streamId.equals(streamId)) {
                                        //Copublish request not found
                                        scrollableMultiliveStreamsView.onCopublishRequestStatusFetched(false, false,
                                                false);
                                    }
                                } else {
                                    if (this.streamId.equals(streamId)) {
                                        scrollableMultiliveStreamsView.onError(var2.getErrorMessage());
                                    }
                                }
                            }
                        });
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#addCopublishRequest(String)}
     */
    @Override
    public void addCopublishRequest(String streamId) {
        isometrik.getRemoteUseCases()
                .getCopublishUseCases()
                .addCopublishRequest(new AddCopublishRequestQuery.Builder().setStreamId(streamId)
                        .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                        .build(), (var1, var2) -> {

                    if (var1 != null) {
                        if (this.streamId.equals(streamId)) {
                            scrollableMultiliveStreamsView.onCopublishRequestAdded();
                        }
                    } else {

                        if (this.streamId.equals(streamId)) {
                            scrollableMultiliveStreamsView.onError(var2.getErrorMessage());
                        }
                    }
                });
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#removeCopublishRequest(String)}
     */
    @Override
    public void removeCopublishRequest(String streamId) {
        isometrik.getRemoteUseCases()
                .getCopublishUseCases()
                .deleteCopublishRequest(new DeleteCopublishRequestQuery.Builder().setStreamId(streamId)
                        .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                        .build(), (var1, var2) -> {

                    if (var1 != null) {
                        if (this.streamId.equals(streamId)) {

                            scrollableMultiliveStreamsView.onCopublishRequestRemoved();
                        }
                    } else {

                        if (this.streamId.equals(streamId)) {
                            scrollableMultiliveStreamsView.onError(var2.getErrorMessage());
                        }
                    }
                });
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#switchProfile(String, boolean)}
     */
    @Override
    public void switchProfile(String streamId, boolean isPublic) {
        isometrik.getRemoteUseCases()
                .getCopublishUseCases()
                .switchProfile(new SwitchProfileQuery.Builder().setStreamId(streamId)
                        .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                        .build(), (var1, var2) -> {

                    if (var1 != null) {
                        if (this.streamId.equals(streamId)) {

                            scrollableMultiliveStreamsView.onProfileSwitched(var1.getRtcToken());
                        }
                    } else {

                        if (this.streamId.equals(streamId)) {
                            scrollableMultiliveStreamsView.onError(var2.getErrorMessage());
                        }
                    }
                });
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#acceptCopublishRequest(String, String)}
     */
    @Override
    public void acceptCopublishRequest(String userId, String streamId) {

        isometrik.getRemoteUseCases()
                .getCopublishUseCases()
                .acceptCopublishRequest(new AcceptCopublishRequestQuery.Builder().setStreamId(streamId)
                        .setUserId(userId)
                        .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                        .build(), (var1, var2) -> {

                    if (var1 != null) {
                        if (this.streamId.equals(streamId)) {

                            scrollableMultiliveStreamsView.onCopublishRequestAcceptedApiResult(userId);
                        }
                    } else {

                        if (this.streamId.equals(streamId)) {
                            scrollableMultiliveStreamsView.onError(var2.getErrorMessage());
                        }
                    }
                });
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#declineCopublishRequest(String, String)}
     */
    @Override
    public void declineCopublishRequest(String userId, String streamId) {

        isometrik.getRemoteUseCases()
                .getCopublishUseCases()
                .denyCopublishRequest(new DenyCopublishRequestQuery.Builder().setStreamId(streamId)
                        .setUserId(userId)
                        .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                        .build(), (var1, var2) -> {

                    if (var1 != null) {
                        if (this.streamId.equals(streamId)) {

                            scrollableMultiliveStreamsView.onCopublishRequestDeclinedApiResult(userId);
                        }
                    } else {

                        if (this.streamId.equals(streamId)) {
                            scrollableMultiliveStreamsView.onError(var2.getErrorMessage());
                        }
                    }
                });
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#requestStreamViewersData(String)}
     */
    @Override
    public void requestStreamViewersData(String streamId) {

        Log.e("called","==> requestStreamViewersData");
        isometrik.getRemoteUseCases()
                .getViewersUseCases()
                .fetchViewers(new FetchViewersQuery.Builder().setStreamId(streamId)
                        .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                        .build(), (var1, var2) -> {

                    if (var1 != null) {
                        if (this.streamId.equals(streamId)) {
                            ArrayList<ViewersListModel> viewersModels = new ArrayList<>();

                            ArrayList<FetchViewersResult.StreamViewer> viewers = var1.getStreamViewers();
                            int size = viewers.size();

                            FetchViewersResult.StreamViewer viewer;

                            for (int i = 0; i < size; i++) {

                                viewer = viewers.get(i);

                                viewersModels.add(
                                        new ViewersListModel(viewer.getUserId(), viewer.getUserProfileImageUrl()));
                            }

                            scrollableMultiliveStreamsView.onStreamViewersDataReceived(viewersModels);
                        } else {

                            if (!isBroadcaster) {
                                scrollableMultiliveStreamsView.updateMembersAndViewersCountInList(streamId, -1,
                                        var1.getStreamViewers().size());
                            }
                        }
                    } else {
                        if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {
                            //No viewers found
                            if (this.streamId.equals(streamId)) {
                                scrollableMultiliveStreamsView.onStreamViewersDataReceived(new ArrayList<>());
                            } else {
                                if (!isBroadcaster) {
                                    scrollableMultiliveStreamsView.updateMembersAndViewersCountInList(streamId, -1,
                                            0);
                                }
                            }
                        } else {
                            if (this.streamId.equals(streamId)) {
                                scrollableMultiliveStreamsView.onError(var2.getErrorMessage());
                            }
                        }
                    }
                });
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#requestRemoveMember(String, String)}
     */
    @Override
    public void requestRemoveMember(String streamId, String memberId) {

        isometrik.getRemoteUseCases()
                .getMembersUseCases()
                .removeMember(new RemoveMemberQuery.Builder().setStreamId(streamId)
                        .setMemberId(memberId)
                        .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                        .build(), (var1, var2) -> {

                    if (var1 != null) {
                        if (this.streamId.equals(streamId)) {
                            scrollableMultiliveStreamsView.onMemberRemovedResult(memberId);
                        }
                    } else {

                        if (this.streamId.equals(streamId)) {
                            scrollableMultiliveStreamsView.onError(var2.getErrorMessage());
                        }
                    }
                });
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#requestLiveStreamsData(int, boolean)}
     */
    @Override
    public void requestLiveStreamsData(int streamsOffset, boolean refreshRequest) {
        isLoadingStreams = true;

        if (refreshRequest) {
            this.streamsOffset = 0;
            isLastStreamsPage = false;
        }

        isometrik.getRemoteUseCases()
                .getStreamsUseCases()
                .fetchStreams(new FetchStreamsQuery.Builder().setUserToken(
                                IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                        .setLimit(Constants.STREAMS_PAGE_SIZE)
                        .setSkip(streamsOffset)
                        .build(), (var1, var2) -> {

                    if (var1 != null) {

                        ArrayList<Streams> streams = var1.getStreams();
                        int size = streams.size();

                        if (size < Constants.STREAMS_PAGE_SIZE) {

                            isLastStreamsPage = true;
                        }

                        ArrayList<LiveStreamsModel> liveStreamsModels = new ArrayList<>();

                        for (int i = 0; i < size; i++) {

                            liveStreamsModels.add(new LiveStreamsModel(streams.get(i)));
                        }

                        scrollableMultiliveStreamsView.onLiveStreamsDataReceived(liveStreamsModels, refreshRequest);
                    } else {
                        if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {

                            if (refreshRequest) {
                                //No streams found

                                scrollableMultiliveStreamsView.onLiveStreamsDataReceived(new ArrayList<>(), true);
                            } else {
                                isLastStreamsPage = true;
                            }
                        } else {

                            scrollableMultiliveStreamsView.onError(var2.getErrorMessage());
                        }
                    }
                    isLoadingStreams = false;
                });
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#requestLiveStreamsDataOnScroll(int, int,
     * int)}
     */
    @Override
    public void requestLiveStreamsDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
                                               int totalItemCount) {
        if (!isLoadingStreams && !isLastStreamsPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
                    && firstVisibleItemPosition >= 0
                    && (totalItemCount) >= Constants.STREAMS_PAGE_SIZE) {

                streamsOffset++;
                requestLiveStreamsData(streamsOffset * Constants.STREAMS_PAGE_SIZE, false);
            }
        }
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#updateBroadcasterStatus(boolean)}
     */
    @Override
    public void updateBroadcasterStatus(boolean isBroadcaster) {
        this.isBroadcaster = isBroadcaster;
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#getMemberDetails(String, int)}
     */
    @Override
    public HashMap<String, Object> getMemberDetails(String streamId, int uid) {

        if (this.streamId.equals(streamId)) {
            Object object = memberMutedState.get(uid);
            HashMap<String, Object> map;

            if (object == null) {

                map = new HashMap<>();
            } else {
                //noinspection unchecked
                map = (HashMap<String, Object>) (object);
            }

            return map;
        } else {
            return null;
        }
    }

    /**
     * {@link MultiliveStreamsContract.Presenter#onRemoteUserMediaSettingsUpdated(String,
     * String,
     * int,
     * String,
     * boolean,
     * boolean)}
     */
    @Override
    public void onRemoteUserMediaSettingsUpdated(String streamId, String userId, int uid,
                                                 String userName, boolean audio, boolean muted) {
        if (this.streamId.equals(streamId)) {

            Object object = memberMutedState.get(uid);
            HashMap<String, Object> map;
            if (object == null) {
                map = new HashMap<>();
                if (audio) {
                    map.put("audioMuted", muted);
                    map.put("videoMuted", false);
                } else {
                    map.put("audioMuted", false);
                    map.put("videoMuted", muted);
                }
            } else {
                //noinspection unchecked
                map = (HashMap<String, Object>) (object);
                if (audio) {
                    map.put("audioMuted", muted);
                } else {
                    map.put("videoMuted", muted);
                }
            }

            map.put("userName", userName);
            map.put("userId", userId);
            memberMutedState.put(uid, map);
        }
    }
    //////////////

    /**
     * Update name over remote user's video feed.
     *
     * @param membersList                the list of members of the stream group containing member details
     * @param userId                     the id of the self user
     * @param audioGridContainerRenderer UI container in which to add the remote user details
     */
    public void addRemoteUserDetailsForAudioOnlyStreaming(
            ArrayList<FetchMembersResult.StreamMember> membersList, String userId,
            AudioGridContainerRenderer audioGridContainerRenderer,
            UserFeedClickedCallback userFeedClickedCallback) {
        //boolean networkQualityIndicatorEnabled = IsometrikUiSdk.getInstance()
        //    .getIsometrik()
        //    .getBroadcastOperations()
        //    .isNetworkQualityIndicatorEnabled();

        FetchMembersResult.StreamMember streamMember;
        for (int i = 0; i < membersList.size(); i++) {
            streamMember = membersList.get(i);
            if (!streamMember.getUserId().equals(userId)) {
                //  audioGridContainerRenderer.addUserAudioSurface(UserIdGenerator.getUid(streamMember.getUserId()),
                //      false, streamMember.getUserName(), streamMember.getUserProfileImageUrl(),
                //      networkQualityIndicatorEnabled, userFeedClickedCallback, streamMember.getPublishing());

                audioGridContainerRenderer.updateUserDetails(
                        UserIdGenerator.getUid(streamMember.getUserId()), streamMember.getUserProfileImageUrl(),
                        streamMember.getUserName(), streamMember.getPublishing());
            }
        }
    }

    public void addUserNameOnRemoteVideoFeed(int uid,
                                             VideoGridContainerRenderer videoGridContainerRenderer) {

        boolean memberIdExists = false;
        for (Map.Entry<String, String> entry : members.entrySet()) {

            if (UserIdGenerator.getUid(entry.getKey()) == uid) {
                memberIdExists = true;
                if (!entry.getValue().isEmpty()) {

                    videoGridContainerRenderer.addUserName(uid, entry.getValue());
                } else {

                    isometrik.getRemoteUseCases()
                            .getMembersUseCases()
                            .fetchMemberDetails(new FetchMemberDetailsQuery.Builder().setMemberId(entry.getKey())
                                    .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                                    .setStreamId(streamId)
                                    .build(), (var1, var2) -> {

                                if (var1 != null) {

                                    videoGridContainerRenderer.addUserName(uid, var1.getUserName());
                                }
                            });
                }
                break;
            }
        }

        if (!memberIdExists) {
            isometrik.getRemoteUseCases()
                    .getMembersUseCases()
                    .fetchMemberDetails(new FetchMemberDetailsQuery.Builder().setUserUid(uid)
                            .setStreamId(streamId)
                            .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                            .build(), (var1, var2) -> {

                        if (var1 != null) {

                            videoGridContainerRenderer.addUserName(uid, var1.getUserName());
                        }
                    });
        }
    }

    /**
     * Update name over remote user's video feed.
     *
     * @param members                    the map of members of the stream group containing mapping of memberId to
     *                                   memberName
     * @param userId                     the id of the self user
     * @param videoGridContainerRenderer UI container in which to add the remote video in split screen
     */
    public void updateAllRemoteUserNames(Map<String, String> members, String userId,
                                         VideoGridContainerRenderer videoGridContainerRenderer) {

        int selfUid = UserIdGenerator.getUid(userId);
        for (Map.Entry<String, String> entry : members.entrySet()) {

            int uid = UserIdGenerator.getUid(entry.getKey());

            if (selfUid != uid) {

                videoGridContainerRenderer.addUserName(uid, entry.getValue());
            }
        }
    }

    public void renderRemoteUserAudio(int uid, UserFeedClickedCallback userFeedClickedCallback,
                                      AudioGridContainerRenderer audioGridContainerRenderer) {

        StreamMemberInfo streamMemberInfo = null;
        for (Map.Entry<Integer, StreamMemberInfo> entry : membersInfo.entrySet()) {

            if (entry.getKey() == uid) {
                streamMemberInfo = entry.getValue();
                break;
            }
        }

        if (streamMemberInfo == null) {

            audioGridContainerRenderer.addUserAudioSurface(uid, false, null, null,
                    networkQualityIndicatorEnabled, userFeedClickedCallback, true);

            isometrik.getRemoteUseCases()
                    .getMembersUseCases()
                    .fetchMemberDetails(new FetchMemberDetailsQuery.Builder().setUserUid(uid)
                            .setStreamId(streamId)
                            .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                            .build(), (var1, var2) -> {

                        if (var1 != null) {

                            audioGridContainerRenderer.updateUserDetails(uid, var1.getUserProfileImageUrl(),
                                    var1.getUserName(), var1.getPublishing());
                        }
                    });
        } else {

            audioGridContainerRenderer.addUserAudioSurface(uid, false, streamMemberInfo.getUserName(),
                    streamMemberInfo.getUserProfilePic(), networkQualityIndicatorEnabled,
                    userFeedClickedCallback, true);
        }
    }

    public void updateDominantSpeaker(List<RemoteUsersAudioLevelInfo> remoteUsersAudioLevelInfo,
                                      AudioGridContainerRenderer audioGridContainerRenderer) {
        audioGridContainerRenderer.updateDominantSpeakers(remoteUsersAudioLevelInfo,
                UserIdGenerator.getUid(IsometrikStreamSdk.getInstance().getUserSession().getUserId()));
    }

    @Override
    public void setNetworkQualityIndicatorEnabled(boolean networkQualityIndicatorEnabled) {
        this.networkQualityIndicatorEnabled = networkQualityIndicatorEnabled;
    }

    @Override
    public boolean isNetworkQualityIndicatorEnabled() {
        return networkQualityIndicatorEnabled;
    }

    @Override
    public void clickOnPKRequestAccept(String inviteId, String streamId, String responseAction, PkInviteRelatedEvent pkInviteRelatedEvent) {
        PkInviteAcceptRejectQuery.Builder pkInviteAcceptRejectQuery = new PkInviteAcceptRejectQuery.Builder()
                .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                .setInviteId(inviteId)
                .setStreamId(streamId)
                .setResponseAction(responseAction);

        isometrik.getRemoteUseCases()
                .getPkUseCases().clickOnPKRequestAccept(pkInviteAcceptRejectQuery.build(), ((var1, var2) -> {
                    if (var1 != null) {
                        scrollableMultiliveStreamsView.pkRequestAcceptAPIResult(true, streamId, pkInviteRelatedEvent);
                    } else {
                        scrollableMultiliveStreamsView.onError(IsometrikStreamSdk.getInstance().getContext().getString(R.string.accept_invite_failed));

                    }
                }));


    }

    @Override
    public void clickOnPKRequestReject(String inviteId, String streamId, String responseAction) {
        PkInviteAcceptRejectQuery.Builder pkInviteAcceptRejectQuery = new PkInviteAcceptRejectQuery.Builder()
                .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                .setInviteId(inviteId)
                .setStreamId(streamId)
                .setResponseAction(responseAction);

        isometrik.getRemoteUseCases()
                .getPkUseCases().clickOnPKRequestAccept(pkInviteAcceptRejectQuery.build(), ((var1, var2) -> {
                    if (var1 != null) {
                        // do nothing as manage locally
                    } else {

                        scrollableMultiliveStreamsView.onError(IsometrikStreamSdk.getInstance().getContext().getString(R.string.accept_invite_failed));

                    }
                }));
    }

    @Override
    public void clickOnStartPkBattle(int selectedMin, String inviteId) {
        PkBattleStartQuery.Builder pkBattleStartQuery = new PkBattleStartQuery.Builder()
                .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                .setInviteId(inviteId)
                .setBattleTimeInMin(selectedMin);

        isometrik.getRemoteUseCases()
                .getPkUseCases().startPkBattle(pkBattleStartQuery.build(), ((var1, var2) -> {
                    if (var1 != null) {
                        scrollableMultiliveStreamsView.pkBattleStarted(var1);
                    } else {

                        scrollableMultiliveStreamsView.onError(IsometrikStreamSdk.getInstance().getContext().getString(R.string.start_battle_failed));
                    }
                }));

    }

    @Override
    public void clickOnStopPkBattle(int selectedMin, String pkId) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("action", "FORCE_STOP");
        requestBody.put("pkId", pkId);


        PkBattleStopQuery.Builder pkBattleStopQuery = new PkBattleStopQuery.Builder()
                .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                .setPkId(pkId)
                .setAction("FORCE_STOP");

        isometrik.getRemoteUseCases()
                .getPkUseCases().stopPkBattle(pkBattleStopQuery.build(), ((var1, var2) -> {
                    if (var1 != null) {
                        scrollableMultiliveStreamsView.pkBattleStoped();
                    } else {
                        scrollableMultiliveStreamsView.onError(IsometrikStreamSdk.getInstance().getContext().getString(R.string.start_battle_failed));

                    }
                }));
    }

    @Override
    public void endPKRequested(String inviteId, boolean intentToStop) {

        PkBattleEndQuery.Builder pkBattleEndQuery = new PkBattleEndQuery.Builder()
                .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                .setInviteId(inviteId)
                .setIntentToStop(intentToStop)
                .setAction("END");

        isometrik.getRemoteUseCases()
                .getPkUseCases().endPkBattle(pkBattleEndQuery.build(), ((var1, var2) -> {
                    if (var1 != null) {
                        if (scrollableMultiliveStreamsView != null) {
                            scrollableMultiliveStreamsView.pkEnded(intentToStop);
                        }
                    } else {
                        scrollableMultiliveStreamsView.onError(IsometrikStreamSdk.getInstance().getContext().getString(R.string.start_battle_failed));
                    }
                }));

    }

    @Override
    public void fetchPkStreamData(String streamId, String pkId) {
        PkStreamStatQuery.Builder pkStreamStatQuery = new PkStreamStatQuery.Builder()
                .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                .setStreamId(streamId);

        isometrik.getRemoteUseCases()
                .getPkUseCases().fetchPkStatDetails(pkStreamStatQuery.build(), ((var1, var2) -> {
                    if (var1 != null) {
                        scrollableMultiliveStreamsView.onFetchedPkStats(var1.getData());
                    } else {
//                        scrollableMultiliveStreamsView.onError(IsometrikUiSdk.getInstance().getContext().getString(R.string.start_battle_failed));

                    }
                }));
    }

    @Override
    public void fetchPkBattleWinners(String pkId) {

        PkBattleWinnersQuery.Builder pkBattleWinnersQuery = new PkBattleWinnersQuery.Builder()
                .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                .setPkId(pkId);

        isometrik.getRemoteUseCases()
                .getPkUseCases().getPkBattleWinners(pkBattleWinnersQuery.build(), ((var1, var2) -> {
                    Log.e("Winner:"," "+var1+" pkId"+pkId );

                    if (var1 != null) {
                        if (var1.getData() != null) {
                            scrollableMultiliveStreamsView.onPkWinnerFetched(var1.getData(), false);
                        } else {
                            scrollableMultiliveStreamsView.onPkWinnerFetched(null, true);
                        }

                    } else {
                        if(var2.getErrorMessage().isEmpty()){
                            scrollableMultiliveStreamsView.onError(IsometrikStreamSdk.getInstance().getContext().getString(R.string.pk_winner_failed));
                        }else{
                            scrollableMultiliveStreamsView.onError(var2.getErrorMessage());
                        }


                    }
                }));
    }

    @Override
    public Map<Integer, StreamMemberInfo> getMembersInfo() {
        return membersInfo;
    }

    @Override
    public Map<Integer, Object> getMembersMuteState() {
        return memberMutedState;
    }

    @Override
    public String getUserNameByStreamUserId(String streamUserId) {
        String userName = "";
        for (Map.Entry<Integer, StreamMemberInfo> entry : membersInfo.entrySet()) {

            StreamMemberInfo streamMemberInfo = entry.getValue();
//            for (Map.Entry<String, String> entry : members.entrySet()) {
            if (streamMemberInfo.getUserId().equals(streamUserId)) {
                userName = streamMemberInfo.getUserName();
                break;
            }
        }

        return userName;
    }

    @Override
    public void fetchGifDataAdvance() {

        GiftCategoriesQuery.Builder giftCategoriesQuery = new GiftCategoriesQuery.Builder()
                .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken());

        isometrik.getRemoteUseCases().getGiftUseCases()
                .fetchGiftCategories(giftCategoriesQuery.build(), ((var1, var2) -> {
                    if (var1 != null) {
                        List<GiftCategory> gifts = var1.getGiftCategories();
                        IsometrikStreamSdk.getInstance().getUserSession().storeGiftCategories(new Gson().toJson(gifts));
                        Log.e("gifts category", "Fetched and store ");
                        if (gifts != null && !gifts.isEmpty()) {
                            fetchGiftsByCategory(gifts.get(0).getId());
                        }

                    } else {
                        Log.e("Error: ", "gifts category");

                    }
                }));
    }

    public void fetchGiftsByCategory(String categoryId) {
        GiftByCategoryQuery.Builder giftCategoriesQuery = new GiftByCategoryQuery.Builder()
                .setCategoryId(categoryId)
                .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken());

        isometrik.getRemoteUseCases().getGiftUseCases()
                .fetchGiftByCategory(giftCategoriesQuery.build(), ((var1, var2) -> {
                    if (var1 != null) {
                        List<Gift> gifts = var1.getGits();

                        IsometrikStreamSdk.getInstance().getUserSession().setGiftData(new Gson().toJson(gifts));
                        Log.e("gifts ", "Fetched and store");

                    } else {
                        Log.e("Error: ", "gifts");

                    }
                }));
    }
}
