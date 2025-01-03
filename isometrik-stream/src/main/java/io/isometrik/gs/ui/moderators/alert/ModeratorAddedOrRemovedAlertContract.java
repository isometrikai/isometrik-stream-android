package io.isometrik.gs.ui.moderators.alert;

import io.isometrik.gs.ui.utils.BasePresenter;

/**
 * The interface moderator added or removed alert contract containing interfaces Presenter and View
 * to be implemented
 * by the
 * ModeratorAddedOrRemovedAlertPresenter{@link ModeratorAddedOrRemovedAlertPresenter} and
 * ModeratorAddedOrRemovedAlertFragment{@link ModeratorAddedOrRemovedAlertFragment} respectively.
 *
 * @see ModeratorAddedOrRemovedAlertPresenter
 * @see ModeratorAddedOrRemovedAlertFragment
 */
public interface ModeratorAddedOrRemovedAlertContract {

  /**
   * The interface ModeratorAddedOrRemovedAlertContract.Presenter to be implemented by
   * ModeratorAddedOrRemovedAlertPresenter{@link
   * ModeratorAddedOrRemovedAlertPresenter}
   *
   * @see ModeratorAddedOrRemovedAlertPresenter
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
   * The interface ModeratorAddedOrRemovedAlertContract.View to be implemented by
   * ModeratorAddedOrRemovedAlertFragment{@link
   * ModeratorAddedOrRemovedAlertFragment}
   *
   * @see ModeratorAddedOrRemovedAlertFragment
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
