package io.isometrik.gs.ui.moderators.leave;

import io.isometrik.gs.ui.utils.BasePresenter;

/**
 * The interface leave as moderator contract containing interfaces Presenter and View to be
 * implemented
 * by the
 * LeaveAsModeratorPresenter{@link LeaveAsModeratorPresenter} and
 * LeaveAsModeratorFragment{@link LeaveAsModeratorFragment} respectively.
 *
 * @see LeaveAsModeratorPresenter
 * @see LeaveAsModeratorFragment
 */
public interface LeaveAsModeratorContract {

  /**
   * The interface LeaveAsModeratorContract.Presenter to be implemented by
   * LeaveAsModeratorPresenter{@link
   * LeaveAsModeratorPresenter}
   *
   * @see LeaveAsModeratorPresenter
   */
  interface Presenter extends BasePresenter<View> {

    /**
     * Leave as moderator.
     *
     * @param streamId the stream id
     */
    void leaveAsModerator(String streamId);
  }

  /**
   * The interface LeaveAsModeratorContract.View to be implemented by LeaveAsModeratorFragment{@link
   * LeaveAsModeratorFragment}
   *
   * @see LeaveAsModeratorFragment
   */
  interface View {

    /**
     * On moderator left successfully.
     */
    void onLeftAsModeratorSuccessfully();

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);
  }
}
