package io.isometrik.gs.ui.members.add;

import io.isometrik.gs.ui.utils.BasePresenter;
import java.util.ArrayList;

/**
 * The interface members contract containing interfaces Presenter and View to be implemented
 * by the
 * AddMembersPresenter{@link AddMembersPresenter} and
 * AddMembersFragment{@link AddMembersFragment} respectively.
 *
 * @see AddMembersPresenter
 * @see AddMembersFragment
 */
public interface AddMembersContract {

  /**
   * The interface AddMembersContract.Presenter to be implemented by AddMembersPresenter{@link
   * AddMembersPresenter}
   *
   * @see AddMembersPresenter
   */
  interface Presenter extends BasePresenter<View> {
    /**
     * Initialize.
     *
     * @param streamId the stream id
     * @param memberIds the member ids
     */
    void initialize(String streamId, ArrayList<String> memberIds);

    /**
     * Request eligible users data.
     *
     * @param offset the offset
     * @param refreshRequest the refresh request
     * @param isSearchRequest whether fetch eligible members request is from the search or not
     * @param searchTag the eligible member's name to be searched
     */
    void requestEligibleUsersData(int offset, boolean refreshRequest, boolean isSearchRequest,
        String searchTag);

    /**
     * Request eligible users data on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void requestEligibleUsersDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);

    /**
     * Request stream viewers data.
     */
    void requestViewersData(int offset, boolean refreshRequest, boolean isSearchRequest,
        String searchTag);

    /**
     * Request viewers data on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void requestViewersDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);

    /**
     * Register stream viewers event listener.
     */
    void registerStreamViewersEventListener();

    /**
     * Unregister stream viewers event listener.
     */
    void unregisterStreamViewersEventListener();

    /**
     * Register copublish requests event listener.
     */
    void registerCopublishRequestsEventListener();

    /**
     * Unregister copublish requests event listener.
     */
    void unregisterCopublishRequestsEventListener();

    /**
     * Add member.
     *
     * @param memberId the member id
     * @param streamId the stream id
     */
    void addMember(String memberId, String streamId);
  }

  /**
   * The interface AddMembersContract.View to be implemented by AddMembersFragment{@link
   * AddMembersFragment}
   *
   * @see AddMembersFragment
   */
  interface View {
    /**
     * On eligible users data received.
     *
     * @param users the users
     * @param refreshRequest the refresh request or not
     */
    void onEligibleUsersDataReceived(ArrayList<AddMembersModel> users, boolean refreshRequest);

    /**
     * On member added.
     *
     * @param memberId the member id
     */
    void onMemberAdded(String memberId);

    /**
     * On stream viewers data received.
     *
     * @param viewers the list of viewers AddMembersModel{@link AddMembersModel}
     * in a stream group
     * @see AddMembersModel
     */
    void onViewersDataReceived(ArrayList<AddMembersModel> viewers, boolean refreshRequest);

    /**
     * Viewer removed from a stream group event.
     *
     * @param viewerId the id of the viewer to be added to a stream group
     */
    void removeViewerEvent(String viewerId);

    /**
     * Viewer added to a stream group event.
     *
     * @param addMembersModel the add members model
     * @see AddMembersModel
     */
    void addViewerEvent(AddMembersModel addMembersModel);

    /**
     * On stream offline.
     *
     * @param message the message
     * @param dialogType the dialog type
     */
    void onStreamOffline(String message, int dialogType);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);

    /**
     * On switch of profile from viewer to broadcaster.
     *
     * @param viewerId the id of the viewer who switched profile in the stream group
     */
    void onProfileSwitched(String viewerId);
  }
}
