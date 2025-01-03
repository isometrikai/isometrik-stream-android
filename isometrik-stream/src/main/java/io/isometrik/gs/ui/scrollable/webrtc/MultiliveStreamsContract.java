package io.isometrik.gs.ui.scrollable.webrtc;

import io.isometrik.gs.events.member.MemberLeaveEvent;
import io.isometrik.gs.events.member.MemberRemoveEvent;
import io.isometrik.gs.ui.gifts.GiftsModel;
import io.isometrik.gs.ui.messages.MessagesModel;
import io.isometrik.gs.ui.pk.challengsSettings.PkBattleStartResponse;
import io.isometrik.gs.ui.pk.response.PkChangeStreamEvent;
import io.isometrik.gs.ui.pk.response.PkInviteRelatedEvent;
import io.isometrik.gs.ui.pk.response.PkResultData;
import io.isometrik.gs.ui.pk.response.PkStats;
import io.isometrik.gs.ui.pk.response.PkStopEvent;
import io.isometrik.gs.ui.pk.response.PkStreamStartStopEvent;
import io.isometrik.gs.ui.streams.list.LiveStreamsModel;
import io.isometrik.gs.ui.utils.StreamMemberInfo;
import io.isometrik.gs.ui.utils.containers.AudioGridContainerRenderer;
import io.isometrik.gs.ui.utils.containers.VideoGridContainerRenderer;
import io.isometrik.gs.ui.viewers.ViewersListModel;
import io.isometrik.gs.response.member.FetchMembersResult;
import io.isometrik.gs.rtcengine.rtc.webrtc.RemoteUsersAudioLevelInfo;
import io.isometrik.gs.rtcengine.utils.UserFeedClickedCallback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The interface scrollable streams contract containing interfaces Presenter and View to be
 * implemented by
 * the
 * ScrollableMultiliveStreamsPresenter{@link MultiliveStreamsPresenter} and
 * ScrollableMultiliveStreamsActivity{@link MultiliveStreamsActivity} respectively.
 *
 * @see MultiliveStreamsPresenter
 * @see MultiliveStreamsActivity
 */
public interface MultiliveStreamsContract {

  /**
   * The interface ScrollableMultiliveStreamsContract.Presenter to be implemented by
   * ScrollableMultiliveStreamsPresenter{@link
   * MultiliveStreamsPresenter}
   *
   * @see MultiliveStreamsPresenter
   */
  interface Presenter {

    /**
     * Request live streams data.
     *
     * @param offset the offset
     * @param refreshRequest the refresh request
     */
    void requestLiveStreamsData(int offset, boolean refreshRequest);

    /**
     * Request live streams data on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void requestLiveStreamsDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);

    /**
     * Request latest stream messages list sent in the stream group.
     *
     * @param streamId the id of the stream group whose messages are to be fetched
     * @param offset the messages offset
     * @param refreshRequest whether to fetch latest messages or with paging
     */
    void requestStreamMessagesData(String streamId, int offset, boolean refreshRequest);

    /**
     * Request stream messages on scroll to fetch previous messages in the stream group.
     *
     * @param streamId the id of the stream group whose messages are to be fetched
     */
    void requestStreamMessagesDataOnScroll(String streamId);

    /**
     * Register connection event listener.
     */
    void registerConnectionEventListener();

    /**
     * Unregister connection event listener.
     */
    void unregisterConnectionEventListener();

    /**
     * Register stream members event listener.
     */
    void registerStreamMembersEventListener();

    /**
     * Unregister stream members event listener.
     */
    void unregisterStreamMembersEventListener();

    /**
     * Register stream viewers event listener.
     */
    void registerStreamViewersEventListener();

    /**
     * Unregister stream viewers event listener.
     */
    void unregisterStreamViewersEventListener();

    /**
     * Register stream messages event listener.
     */
    void registerStreamMessagesEventListener();

    /**
     * Unregister stream messages event listener.
     */
    void unregisterStreamMessagesEventListener();

    /**
     * Register copublish requests event listener.
     */
    void registerCopublishRequestsEventListener();

    /**
     * Unregister copublish requests event listener.
     */
    void unregisterCopublishRequestsEventListener();

    /**
     * Register streams event listener.
     */
    void registerStreamsEventListener();

    /**
     * Unregister streams event listener.
     */
    void unregisterStreamsEventListener();

    /**
     * Register presence event listener.
     */
    void registerPresenceEventListener();

    /**
     * Unregister presence event listener.
     */
    void unregisterPresenceEventListener();

    /**
     * Register moderator event listener.
     */
    void registerModeratorEventListener();

    /**
     * Unregister moderator event listener.
     */
    void unregisterModeratorEventListener();

    void fetchLatestMembers(String streamId);

    /**
     * Register PK event listener.
     */
    void registerPkEventListener();

    /**
     * Unregister PK event listener.
     */
    void unregisterPkEventListener();

    /**
     * Initialize.
     *
     * @param streamId the id of the currently active stream group
     * @param isAdmin whether given user is the admin of the stream group
     * @param isBroadcaster whether given user is the broadcaster or viewer in the stream group
     * @param isModerator whether given user is the moderator of the stream group or not
     */
    void initialize(String streamId, ArrayList<String> memberIds, boolean isAdmin,
                    boolean isBroadcaster, boolean isModerator, boolean isPkBattle);

    /**
     * Send message.
     *
     * @param streamId the id of the stream group in which to send the message
     * @param message the message to be sent in the stream group
     * @param messageType the type of message sent in the stream group
     * @param coinsValue the coins value of the gift(in case message sent is of gift type)
     * @param giftName the name of the gift(in case message sent is of gift type)
     */
    void sendMessage(String streamId, String message, int messageType, int coinsValue,
                     String giftName, String giftThumbnailUrl);

    /**
     * Remove message.
     *
     * @param streamId the id of the stream group from which to remove the message
     * @param messageId the id fo the message to be removed
     * @param timestamp the timestamp at which message which is to be removed, was sent
     */
    void removeMessage(String streamId, String messageId, long timestamp);

    /**
     * Gets member ids.
     *
     * @return the list containing ids of the members in the stream group
     */
    ArrayList<String> getMemberIds();

    /**
     * Gets members.
     *
     * @return the map containing mapping of the memberId to memberName of members in the stream group
     */
    Map<String, String> getMembers();

    /**
     * Update broadcasting status.
     *
     * @param startBroadcast whether to update publishing status as true or false for a stream group
     * @param streamId the id of the stream group in which to update the publishing status
     * @param rejoinRequest whether update publishing status request is a firsttime request or
     * re-request after reconnection
     */
    void updateBroadcastingStatus(boolean startBroadcast, String streamId, boolean rejoinRequest);

    /**
     * Leave stream group.
     *
     * @param streamId the id of the stream group which is to be left as member
     */
    void leaveStreamGroup(String streamId);

    /**
     * Stop broadcast.
     *
     * @param streamId the id of the stream group in which to stop publishing
     */
    void stopBroadcast(String streamId, String pkInviteId);

    /**
     * Join as viewer.
     *
     * @param streamId the id of the stream group which is to be joined as viewer
     * @param rejoinRequest whether the stream group join request id first time request or a rejoin
     * request after connection loss
     */
    void joinAsViewer(String streamId, boolean rejoinRequest);

    /**
     * Leave as viewer.
     *
     * @param streamId the id of the stream group from which to leave as the viewer
     */
    void leaveAsViewer(String streamId, boolean removeLocally);

    void checkCopublishRequestStatus(String streamId);

    String getUserFullNameByStreamUserId(String streamUserId);


    ///**
    // * Check copublish request status for a viewer.
    // *
    // * @param streamId the id of the stream group from which to fetch the status of the copublish
    // * request
    // */
    //void checkCopublishRequestStatus(String streamId);

    /**
     * Add copublish request for a stream by a viewer.
     *
     * @param streamId the id of the stream group for which to add the copublish
     * request
     */
    void addCopublishRequest(String streamId);

    /**
     * Remove copublish request for a stream by a viewer.
     *
     * @param streamId the id of the stream group for which to remove the copublish
     * request
     */
    void removeCopublishRequest(String streamId);

    /**
     * Switches profile from a viewer to a broadcaster for a stream group.
     *
     * @param streamId the id of the stream group for which to switch profile from viewer to the
     * broadcaster
     */
    void switchProfile(String streamId, boolean isPublic);

    /**
     * Accept copublish request.
     *
     * @param userId the id of the user whose copublish request is to be accepted
     * @param streamId the id of the stream group for which to accept copublish request
     */
    void acceptCopublishRequest(String userId, String streamId);

    /**
     * Decline copublish request.
     *
     * @param userId the id of the user whose copublish request is to be declined
     * @param streamId the id of the stream group for which to decline copublish request
     */
    void declineCopublishRequest(String userId, String streamId);

    /**
     * Update broadcaster status.
     *
     * @param isBroadcaster whether given user is broadcaster or viewer for the stream group
     */
    void updateBroadcasterStatus(boolean isBroadcaster);

    /**
     * Request stream viewers data.
     *
     * @param streamId the id of the stream whose list of viewers is to be fetched
     */
    void requestStreamViewersData(String streamId);

    /**
     * Gets member mute audio/video states.
     *
     * @param streamId id of the stream for which to fetch remote user's setting
     * @param uid the uid of the user whose video/audio mute state is to be fetched
     * @return the map of members with their mute state
     */
    HashMap<String, Object> getMemberDetails(String streamId, int uid);

    /**
     * To save updated video and audio settings of the remote user
     *
     * @param streamId id of the stream for which to update remote user's setting
     * @param userId userId of the user whose setting has been updated
     * @param uid uid of the user whose setting has been updated
     * @param userName name of the user whose setting has been updated
     * @param audio whether audio or video setting has been updated
     * @param muted whether muted or unmuted
     */
    void onRemoteUserMediaSettingsUpdated(String streamId, String userId, int uid, String userName,
        boolean audio, boolean muted);

    /**
     * Request remove member.
     *
     * @param streamId the id of the stream from which to remove a member
     * @param memberId the id of the member to be removed
     */
    void requestRemoveMember(String streamId, String memberId);

    void addRemoteUserDetailsForAudioOnlyStreaming(
        ArrayList<FetchMembersResult.StreamMember> membersList, String userId,
        AudioGridContainerRenderer audioGridContainerRenderer, UserFeedClickedCallback userFeedClickedCallback);

    void addUserNameOnRemoteVideoFeed(int uid, VideoGridContainerRenderer videoGridContainerRenderer);

    void updateAllRemoteUserNames(Map<String, String> members, String userId,
        VideoGridContainerRenderer videoGridContainerRenderer);

    void renderRemoteUserAudio(int uid, UserFeedClickedCallback userFeedClickedCallback,
        AudioGridContainerRenderer audioGridContainerRenderer);

    void updateDominantSpeaker(List<RemoteUsersAudioLevelInfo> remoteUsersAudioLevelInfo,
        AudioGridContainerRenderer audioGridContainerRenderer);

    void setNetworkQualityIndicatorEnabled(boolean networkQualityIndicatorEnabled);

    boolean isNetworkQualityIndicatorEnabled();

    /**
     * click on Accept PK Request
     */
    void clickOnPKRequestAccept(String inviteId, String streamId, String response, PkInviteRelatedEvent pkInviteRelatedEvent);

    Map<Integer, StreamMemberInfo> getMembersInfo();

    Map<Integer, Object> getMembersMuteState();

    /**
     * click on Reject PK Request
     */
    void clickOnPKRequestReject(String inviteId, String streamId, String response);

    void clickOnStartPkBattle(int selectedMin, String inviteId);

    void clickOnStopPkBattle(int selectedMin, String pkId);

    void endPKRequested(String inviteId, boolean intentToStop);

    void fetchPkStreamData(String streamId, String pkId);

    void fetchPkBattleWinners(String pkId);

    String getUserNameByStreamUserId(String streamUserId);

    void fetchGifDataAdvance();

  }

  /**
   * The interface ScrollableMultiliveStreamsContract.View to be implemented by
   * ScrollableMultiliveStreamsActivity{@link
   * MultiliveStreamsActivity}
   *
   * @see MultiliveStreamsActivity
   */
  interface View {
    /**
     * On live streams data received.
     *
     * @param streams the streams
     * @param latestStreams the latest streams
     */
    void onLiveStreamsDataReceived(ArrayList<LiveStreamsModel> streams, boolean latestStreams);

    /**
     * Initialize hearts views for the like event.
     */
    void initializeHeartsViews();

    /**
     * On stream messages data received.
     *
     * @param messages the list of messages sent in the stream group
     * @param refreshRequest whether the list of messages fetched is the latest messages,or messages
     * on specific page with paging
     */
    void onStreamMessagesDataReceived(ArrayList<MessagesModel> messages, boolean refreshRequest);

    /**
     * On stream offline.
     *
     * @param message the message to be shown in popup when stream is no longer live
     * @param dialogType the dialog type to identify whether dialog is for stream end or for kicked
     * out of a stream group
     * @param streamId the id of the stream which is no longer live
     * @param kickout whether user has been kicked out of stream group
     */
    void onStreamOffline(String message, int dialogType, String streamId, boolean kickout);

    /**
     * Update members and viewers count.
     *
     * @param membersCount the current members count in the stream group
     * @param viewersCount the current viewers count in the stream group
     */
    void updateMembersAndViewersCount(int membersCount, int viewersCount);

    /**
     * Update members and viewers count.
     *
     * @param streamId the stream for which to update members and viewers count
     * @param membersCount the current members count in the stream group
     * @param viewersCount the current viewers count in the stream group
     */
    void updateMembersAndViewersCountInList(String streamId, int membersCount, int viewersCount);

    /**
     * On text message sent.
     *
     * @param messagesModel the messages model containing details of the text message sent
     * @see MessagesModel
     */
    void onTextMessageSent(MessagesModel messagesModel);

    /**
     * On text message received.
     *
     * @param messagesModel the messages model containing details of the text message received
     * @see MessagesModel
     */
    void onTextMessageReceived(MessagesModel messagesModel);

    void onGiftMessageReceived(MessagesModel messagesModel);


    /**
     * On presence message event.
     *
     * @param messagesModel the messages model containing details of the message received
     * @see MessagesModel
     */
    void onPresenceMessageEvent(MessagesModel messagesModel);

    /**
     * On message removed event.
     *
     * @param messageId the id of the message which was deleted
     * @param message the placeholder message to be updated in the UI,in place of the deleted
     * message
     */
    void onMessageRemovedEvent(String messageId, String message);

    /**
     * On message delivered.
     *
     * @param messageId the id of the message which was successfully sent
     * @param temporaryMessageId the temporary(local) message id of the message, which has been
     * delivered
     */
    void onMessageDelivered(String messageId, String temporaryMessageId);

    /**
     * On like received event.
     */
    void onLikeEvent();

    /**
     * On gift received event.
     *
     * @param giftsModel the gifts model
     * @see GiftsModel
     */
    void onGiftEvent(GiftsModel giftsModel);

    void onFullGiftEvent(String url);

    /**
     * On broadcast status updated.
     *
     * @param startBroadcast whether the status update request was to start publish or to stop
     * publish
     * @param rejoinRequest whether the broadcast status update request was a firsttime or a rejoin
     * after network failure,request
     * @param rtcToken the token for authentication while joining a stream group
     * @param isModerator whether given user is a moderator of the broadcast or not
     */
    void onBroadcastStatusUpdated(boolean startBroadcast, boolean rejoinRequest, String rtcToken,
        Boolean isModerator);

    /**
     * On stream group left successfully by a member.
     */
    void onStreamGroupLeft();

    /**
     * Callback to be triggered when request to update publishing status in a stream group failed.
     *
     * @param startBroadcast whether the status update request was to start publish or to stop
     * publish
     * @param errorMessage the error message containing details of the error encountered while
     * updating publishing status in a stream group
     */
    void onBroadcastingStatusUpdateFailed(boolean startBroadcast, String errorMessage);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);

    /**
     * Callback to be triggered when the broadcast has been ended.
     */
    void onBroadcastEnded();

    /**
     * Connection state changed.
     *
     * @param connected whether connection to receive realtime events has been made or broken
     */
    void connectionStateChanged(boolean connected);

    /**
     * On stream joined.
     *
     * @param numOfViewers the num of viewers in the stream group
     * @param rejoinRequest whether the request to join stream as audience was first time request or
     * re-request
     * @param rtcToken the token for authentication while joining a stream group
     * @param isModerator whether given user is a moderator of the broadcast or not
     */
    void onStreamJoined(int numOfViewers, boolean rejoinRequest, String rtcToken,
        Boolean isModerator);

    /**
     * On stream join error.
     *
     * @param errorMessage the error message containing details of reason why user failed to join
     * the stream group as audience
     */
    void onStreamJoinError(String errorMessage);

    /**
     * @param alreadyRequested whether given viewer has already requested copublish
     * @param pending whether copublish request is pending
     * @param accepted whether copublish request has been accepted already
     */
    void onCopublishRequestStatusFetched(boolean alreadyRequested, boolean pending,
        boolean accepted);

    /**
     * Callback to be triggered when the copublish request has been added.
     */
    void onCopublishRequestAdded();

    /**
     * Callback to be triggered when the copublish request has been removed.
     */
    void onCopublishRequestRemoved();

    /**
     * Callback to be triggered when the profile is switched from a viewer to a broadcaster.
     *
     * @param rtcToken the token for authentication while joining a stream group
     */
    void onProfileSwitched(String rtcToken);

    /**
     * On copublish request being successfully accepted.
     *
     * @param userId the userId
     */
    void onCopublishRequestAcceptedApiResult(String userId);

    /**
     * On copublish request being successfully denied.
     *
     * @param userId the userId
     */
    void onCopublishRequestDeclinedApiResult(String userId);

    /**
     * On copublish request being successfully accepted realtime event.
     */
    void onCopublishRequestAcceptedEvent();

    /**
     * On copublish request being successfully denied realtime event.
     */
    void onCopublishRequestDeclinedEvent();

    /**
     * On member add/remove event.
     *
     * @param added whether member has been added or removed
     * @param initiatorName name of the stream initiator
     */
    void onMemberAdded(boolean added, String initiatorName);

    /**
     * On member removed result.
     *
     * @param memberId the member id
     */
    void onMemberRemovedResult(String memberId);

    /**
     * Viewer removed from a stream group event.
     *
     * @param viewerId the id of the viewer to be added to a stream group
     * @param viewersCount the number of viewers in the stream group
     */
    void removeViewerEvent(String viewerId, int viewersCount);

    /**
     * Viewer added to a stream group event.
     *
     * @param viewersListModel the viewers list model
     * @param viewersCount the number of viewers in the stream group
     * @see ViewersListModel
     */
    void addViewerEvent(ViewersListModel viewersListModel, int viewersCount);

    /**
     * On stream viewers data received.
     *
     * @param viewers the list of viewers ViewersListModel{@link ViewersListModel}
     * in a stream group
     * @see ViewersListModel
     */
    void onStreamViewersDataReceived(ArrayList<ViewersListModel> viewers);

    /**
     * On stream ended.
     *
     * @param streamId the stream id
     * @param activeStream whether given stream is currently being viewed
     */
    void onStreamEnded(String streamId, boolean activeStream);

    /**
     * On stream started.
     *
     * @param liveStreamsModel the streams model
     */
    void onStreamStarted(LiveStreamsModel liveStreamsModel);

    /**
     * Update visibility for the join button
     */
    void updateVisibilityForJoinButton();

    /**
     * To update user name over remote video feed
     *
     * @param members the map of members of the stream group containing mapping of memberId to
     * memberName
     * @param membersList the list of members of the stream group containing member details
     */
    void updateRemoteUserDetails(Map<String, String> members,
        ArrayList<FetchMembersResult.StreamMember> membersList);

    /**
     * To alert user on being added or removed as moderator
     *
     * @param addedAsModerator whether user has been added or removed as moderator
     * @param initiatorName name of the user who added/removed moderator
     * @param moderatorName name of the moderator who was added/removed
     */
    void moderatorStatusUpdated(boolean addedAsModerator, String initiatorName,
        String moderatorName);

    /**
     * Callback to update chat message ui to show view replies text, if reply for a message has been
     * received
     *
     * @param messageId id of the message for which reply has been received
     */
    void onMessageReplyReceived(String messageId);

    void pkInviteReceived(PkInviteRelatedEvent pkInviteRelatedEvent);

    void viewerPkStreamStopped(PkStopEvent pkStopEvent);

    void broadcastersPkStreamStopped(PkStopEvent pkStopEvent);

    void pkRequestAcceptAPIResult(boolean isAccept, String newStreamId, PkInviteRelatedEvent pkInviteRelatedEvent);

    void onPkInviteStatusChange(PkInviteRelatedEvent pkInviteRelatedEvent);

    void onBroadcastEndedForPk();

    void onFetchedPkStats(PkStats pkStats);

    void pkBattleStarted(PkBattleStartResponse pkBattleStartResponse);

    void changeToPkEvent();

    void changeStreamEvent(PkChangeStreamEvent pkChangeStreamEvent);

    void broadcastersPkStreamStarted(PkStreamStartStopEvent pkStreamStartStopEvent);

    void viewerPkStreamStarted(PkStreamStartStopEvent pkStreamStartStopEvent);

    void pkEnded(boolean intentToStop);

    void pkBattleStoped();

    void onPkWinnerFetched(PkResultData pkResultData, boolean isDraw);

    void onMemberLeft(MemberLeaveEvent memberLeaveEvent);

    void onMemberRemove(MemberRemoveEvent memberRemoveEvent);


  }
}
