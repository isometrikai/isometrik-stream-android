package io.isometrik.gs.ui.moderators.alert;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.moderator.LeaveModeratorQuery;

/**
 * The moderators presenter to allow a moderator to leave a broadcast.
 * It implements ModeratorsContract.Presenter{@link ModeratorAddedOrRemovedAlertContract.Presenter}
 *
 * @see ModeratorAddedOrRemovedAlertContract.Presenter
 */
public class ModeratorAddedOrRemovedAlertPresenter
    implements ModeratorAddedOrRemovedAlertContract.Presenter {

  private final Isometrik isometrik = IsometrikStreamSdk.getInstance().getIsometrik();
  private ModeratorAddedOrRemovedAlertContract.View moderatorAddedOrRemovedAlertView;

  /**
   * Instantiates a new leave as moderator presenter.
   */
  ModeratorAddedOrRemovedAlertPresenter() {

  }

  /**
   * {@link ModeratorAddedOrRemovedAlertContract.Presenter#leaveAsModerator(String)}
   */
  @Override
  public void leaveAsModerator(String streamId) {

    isometrik.getRemoteUseCases()
        .getModeratorsUseCases()
        .leaveModerator(new LeaveModeratorQuery.Builder().setUserToken(
                IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
            .setStreamId(streamId)
            .build(), (var1, var2) -> {

          if (var1 != null) {
            if (moderatorAddedOrRemovedAlertView != null) {
              moderatorAddedOrRemovedAlertView.onLeftAsModeratorSuccessfully();
            }
          } else {

            if (moderatorAddedOrRemovedAlertView != null) {
              moderatorAddedOrRemovedAlertView.onError(var2.getErrorMessage());
            }
          }
        });
  }

  @Override
  public void attachView(ModeratorAddedOrRemovedAlertContract.View addModeratorView) {
    this.moderatorAddedOrRemovedAlertView = addModeratorView;
  }

  @Override
  public void detachView() {
    this.moderatorAddedOrRemovedAlertView = null;
  }
}
