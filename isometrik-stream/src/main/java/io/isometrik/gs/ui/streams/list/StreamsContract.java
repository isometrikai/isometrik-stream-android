package io.isometrik.gs.ui.streams.list;

import io.isometrik.gs.ui.utils.BasePresenter;
import io.isometrik.gs.events.copublish.CopublishRequestAcceptEvent;
import io.isometrik.gs.events.member.MemberAddEvent;
import io.isometrik.gs.events.member.MemberLeaveEvent;
import io.isometrik.gs.events.member.MemberRemoveEvent;
import io.isometrik.gs.events.member.PublishStartEvent;
import io.isometrik.gs.events.member.PublishStopEvent;
import java.util.ArrayList;

/**
 * The interface streams contract containing interfaces Presenter and View to be implemented
 * by the
 * StreamsPresenter{@link StreamsPresenter} and
 * StreamsActivity{@link StreamsListActivity} respectively.
 *
 * @see StreamsPresenter
 * @see StreamsListActivity
 */
public interface StreamsContract {

  /**
   * The interface StreamsContract.Presenter to be implemented by StreamsPresenter{@link
   * StreamsPresenter}
   *
   * @see StreamsPresenter
   */
  interface Presenter extends BasePresenter<StreamsContract.View> {

    /**
     * Request streams data.
     *
     * @param offset the offset
     * @param refreshRequest the refresh request
     * @param isSearchRequest whether fetch members request is from the search or not
     * @param searchTag the member's name to be searched
     */
    void requestStreamsData(int offset, boolean refreshRequest, boolean isSearchRequest,
        String searchTag, int activeStreamCategoryTab);

    /**
     * Request streams data on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void requestStreamsDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);

    /**
     * Register streams event listener.
     */
    void registerStreamsEventListener();

    /**
     * Unregister streams event listener.
     */
    void unregisterStreamsEventListener();

    /**
     * Register copublish requests event listener.
     */
    void registerCopublishRequestsEventListener();

    /**
     * Unregister copublish requests event listener.
     */
    void unregisterCopublishRequestsEventListener();

    /**
     * Register presence event listener.
     */
    void registerPresenceEventListener();

    /**
     * Unregister presence event listener.
     */
    void unregisterPresenceEventListener();

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
  }

  /**
   * The interface StreamsContract.View to be implemented by StreamsActivity{@link
   * StreamsListActivity}
   *
   * @see StreamsListActivity
   */
  interface View {

    /**
     * On live streams data received.
     *
     * @param streams the streams
     * @param refreshRequest whether the streams are of refresh request result or not
     */
    void onStreamsDataReceived(ArrayList<LiveStreamsModel> streams, boolean refreshRequest);

    /**
     * Update members and viewers count.
     *
     * @param streamId the stream id
     * @param membersCount the members count
     * @param viewersCount the viewers count
     */
    void updateMembersAndViewersCount(String streamId, int membersCount, int viewersCount);

    /**
     * On member added.
     *
     * @param memberAddEvent the member add event
     * @param givenMemberAdded the given member added
     */
    void onMemberAdded(MemberAddEvent memberAddEvent, boolean givenMemberAdded);

    /**
     * On member removed.
     *
     * @param memberRemoveEvent the member remove event
     * @param givenMemberRemoved the given member removed
     */
    void onMemberRemoved(MemberRemoveEvent memberRemoveEvent, boolean givenMemberRemoved);

    /**
     * On member left.
     *
     * @param memberLeftEvent the member left event
     */
    void onMemberLeft(MemberLeaveEvent memberLeftEvent, boolean givenUserLeft);

    /**
     * On stream ended.
     *
     * @param streamId the stream id
     */
    void onStreamEnded(String streamId);

    /**
     * On stream started.
     *
     * @param liveStreamsModel the streams model
     */
    void onStreamStarted(LiveStreamsModel liveStreamsModel);

    /**
     * On publish started.
     *
     * @param publishStartEvent the publish start event
     * @param userId the user id
     */
    void onPublishStarted(PublishStartEvent publishStartEvent, String userId);

    /**
     * On publish stopped.
     *
     * @param publishStopEvent the publish stop event
     * @param userId the user id
     */
    void onPublishStopped(PublishStopEvent publishStopEvent, String userId);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);

    /**
     * On copublish request accepted.
     *
     * @param copublishRequestAcceptEvent the copublish request accepted event
     */
    void onCopublishRequestAccepted(CopublishRequestAcceptEvent copublishRequestAcceptEvent, boolean givenUserCopublishRequestAccepted);
  }
}
