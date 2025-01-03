package io.isometrik.gs.ui.moderators.add;

import io.isometrik.gs.ui.utils.BasePresenter;
import java.util.ArrayList;

/**
 * The interface add moderators contract containing interfaces Presenter and View to be implemented
 * by the
 * AddModeratorsPresenter{@link AddModeratorsPresenter} and
 * AddModeratorsFragment{@link AddModeratorsFragment} respectively.
 *
 * @see AddModeratorsPresenter
 * @see AddModeratorsFragment
 */
public interface AddModeratorsContract {

  /**
   * The interface AddModeratorsContract.Presenter to be implemented by AddModeratorsPresenter{@link
   * AddModeratorsPresenter}
   *
   * @see AddModeratorsPresenter
   */
  interface Presenter extends BasePresenter<View> {
    /**
     * Initialize.
     *
     * @param streamId the stream id
     * @param moderatorIds the moderator ids
     */
    void initialize(String streamId, ArrayList<String> moderatorIds);

    /**
     * Request users data.
     *
     * @param offset the offset
     * @param refreshRequest the refresh request
     * @param isSearchRequest whether fetch users request is from the search or not
     * @param searchTag the user's name to be searched
     */
    void requestUsersData(int offset, boolean refreshRequest, boolean isSearchRequest,
        String searchTag);

    /**
     * Request users data on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void requestUsersDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);

    /**
     * Add moderator.
     *
     * @param moderatorId the moderator id
     */
    void addModerator(String moderatorId);
  }

  /**
   * The interface AddModeratorsContract.View to be implemented by AddModeratorsFragment{@link
   * AddModeratorsFragment}
   *
   * @see AddModeratorsFragment
   */
  interface View {
    /**
     * On users data received.
     *
     * @param users the users
     * @param refreshRequest the refresh users request or not
     */
    void onUsersDataReceived(ArrayList<AddModeratorsModel> users, boolean refreshRequest);

    /**
     * On moderator added.
     *
     * @param moderatorId the moderator id
     */
    void onModeratorAdded(String moderatorId);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);
  }
}
