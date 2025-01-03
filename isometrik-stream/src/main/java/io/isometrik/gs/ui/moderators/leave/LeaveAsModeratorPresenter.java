package io.isometrik.gs.ui.moderators.leave;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.moderator.LeaveModeratorQuery;

/**
 * The moderators presenter to allow a moderator to leave a broadcast.
 * It implements ModeratorsContract.Presenter{@link LeaveAsModeratorContract.Presenter}
 *
 * @see LeaveAsModeratorContract.Presenter
 */
public class LeaveAsModeratorPresenter implements LeaveAsModeratorContract.Presenter {

  private final Isometrik isometrik = IsometrikStreamSdk.getInstance().getIsometrik();
  private LeaveAsModeratorContract.View leaveAsModeratorView;

  /**
   * Instantiates a new leave as moderator presenter.
   */
  LeaveAsModeratorPresenter() {

  }

  /**
   * {@link LeaveAsModeratorContract.Presenter#leaveAsModerator(String)}
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
            if (leaveAsModeratorView != null) {
              leaveAsModeratorView.onLeftAsModeratorSuccessfully();
            }
          } else {

            if (leaveAsModeratorView != null) {
              leaveAsModeratorView.onError(var2.getErrorMessage());
            }
          }
        });
  }

  @Override
  public void attachView(LeaveAsModeratorContract.View addModeratorView) {
    this.leaveAsModeratorView = addModeratorView;
  }

  @Override
  public void detachView() {
    this.leaveAsModeratorView = null;
  }
}
