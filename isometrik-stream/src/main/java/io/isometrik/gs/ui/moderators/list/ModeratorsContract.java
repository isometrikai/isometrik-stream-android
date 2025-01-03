package io.isometrik.gs.ui.moderators.list;

import io.isometrik.gs.ui.utils.BasePresenter;
import java.util.ArrayList;

/**
 * The interface moderators contract containing interfaces Presenter and View to be implemented
 * by the
 * ModeratorsPresenter{@link ModeratorsPresenter} and
 * ModeratorsBottomSheetFragment{@link ModeratorsFragment} respectively.
 *
 * @see ModeratorsPresenter
 * @see ModeratorsFragment
 */
public interface ModeratorsContract {

  /**
   * The interface ModeratorsContract.Presenter to be implemented by ModeratorsPresenter{@link
   * ModeratorsPresenter}
   *
   * @see ModeratorsPresenter
   */
  interface Presenter extends BasePresenter<View> {

    /**
     * Request stream moderators data.
     *
     * @param offset the offset
     * @param refreshRequest the refresh request
     * @param isSearchRequest whether fetch moderators request is from the search or not
     * @param searchTag the moderator's name to be searched
     */
    void requestModeratorsData(int offset, boolean refreshRequest, boolean isSearchRequest,
        String searchTag);

    /**
     * Request stream moderators data on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void requestModeratorsDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);

    /**
     * Register stream moderators event listener.
     */
    void registerStreamModeratorsEventListener();

    /**
     * Unregister stream moderators event listener.
     */
    void unregisterStreamModeratorsEventListener();

    /**
     * Request remove moderator.
     *
     * @param moderatorId the moderator id
     */
    void requestRemoveModerator(String moderatorId);

    /**
     * Initialize.
     *
     * @param streamId the stream id
     * @param isAdmin the is admin
     */
    void initialize(String streamId, boolean isAdmin);

    /**
     * Gets moderator ids.
     *
     * @param moderators the moderators
     * @return the moderator ids
     */
    ArrayList<String> getModeratorIds(ArrayList<ModeratorsModel> moderators);
  }

  /**
   * The interface ModeratorsContract.View to be implemented by ModeratorsBottomSheetFragment{@link
   * ModeratorsFragment}
   *
   * @see ModeratorsFragment
   */
  interface View {

    /**
     * On stream moderators data received.
     *
     * @param moderators the moderators
     */
    void onStreamModeratorsDataReceived(ArrayList<ModeratorsModel> moderators,
        boolean refreshRequest, int moderatorsCount);

    /**
     * On moderator removed result.
     *
     * @param moderatorId the moderator id
     */
    void onModeratorRemovedResult(String moderatorId);

    /**
     * Add moderator event.
     *
     * @param moderatorsModel the moderators model
     * @param moderatorsCount the moderators count
     */
    void addModeratorEvent(ModeratorsModel moderatorsModel, int moderatorsCount);

    /**
     * Remove moderator event.
     *
     * @param moderatorId the moderator id
     * @param moderatorsCount the moderators count
     */
    void removeModeratorEvent(String moderatorId, int moderatorsCount);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);
  }
}
