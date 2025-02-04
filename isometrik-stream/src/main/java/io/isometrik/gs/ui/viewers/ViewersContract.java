package io.isometrik.gs.ui.viewers;

import io.isometrik.gs.ui.utils.BasePresenter;
import java.util.ArrayList;

/**
 * The interface viewers contract containing interfaces Presenter and View to be implemented
 * by the
 * ViewersPresenter{@link ViewersPresenter} and
 * ViewersBottomSheetFragment{@link ViewersFragment} respectively.
 *
 * @see ViewersPresenter
 * @see ViewersFragment
 */
public interface ViewersContract {

  /**
   * The interface ViewersContract.Presenter implemented by ViewersPresenter{@link
   * ViewersPresenter}.
   *
   * @see ViewersPresenter
   */
  interface Presenter extends BasePresenter<ViewersContract.View> {

    /**
     * Request stream viewers data.
     *
     * @param offset the offset
     * @param refreshRequest the refresh request
     * @param isSearchRequest whether fetch viewers request is from the search or not
     * @param searchTag the viewer's name to be searched
     */
    void requestStreamViewersData(int offset, boolean refreshRequest, boolean isSearchRequest,
        String searchTag);

    /**
     * Request stream viewers data on scroll.
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
     * Register stream members event listener.
     */
    void registerStreamMembersEventListener();

    /**
     * Unregister stream members event listener.
     */
    void unregisterStreamMembersEventListener();

    /**
     * Register copublish requests event listener.
     */
    void registerCopublishRequestsEventListener();

    /**
     * Unregister copublish requests event listener.
     */
    void unregisterCopublishRequestsEventListener();

    /**
     * Request remove viewer.
     *
     * @param viewerId the id of the viewer to be removed from the stream group
     */
    void requestRemoveViewer(String viewerId);

    /**
     * Initialize.
     *
     * @param streamId the id of the stream group which is joined at viewer
     * @param memberIds the list containing ids of the members of the stream group
     * @param isModerator whether given user is a moderator of the stream group or not
     */
    void initialize(String streamId, ArrayList<String> memberIds, boolean isModerator);
  }

  /**
   * The interface ViewersContract.View implemented by the ViewersBottomSheetFragment{@link
   * ViewersFragment}.
   *
   * @see ViewersFragment
   */
  interface View {

    /**
     * On stream viewers data received.
     *
     * @param viewers the list of viewers ViewersModel{@link ViewersModel}
     * in a stream group
     * @param refreshRequest whether the viewers are of refresh request result or not
     * @see ViewersModel
     */
    void onStreamViewersDataReceived(ArrayList<ViewersModel> viewers, boolean refreshRequest,
        int viewersCount);

    /**
     * On viewer removed from a stream group result.
     *
     * @param viewerId the id of the viewer removed from the stream group
     */
    void onViewerRemovedResult(String viewerId);

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
     * @param viewersModel the viewers model
     * @param viewersCount the number of viewers in the stream group
     * @see ViewersModel
     */
    void addViewerEvent(ViewersModel viewersModel, int viewersCount);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);

    /**
     * On stream offline.
     *
     * @param message the message to be shown in the popup
     * @param dialogType the type od dialog whether for stream offline or being kicked out of
     * audience
     */
    void onStreamOffline(String message, int dialogType);

    /**
     * On switch of profile from viewer to broadcaster.
     *
     * @param viewerId the id of the viewer who switched profile in the stream group
     */
    void onProfileSwitched(String viewerId);
  }
}
