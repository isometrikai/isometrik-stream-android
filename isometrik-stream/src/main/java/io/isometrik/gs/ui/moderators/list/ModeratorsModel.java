package io.isometrik.gs.ui.moderators.list;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.events.moderator.ModeratorAddEvent;
import io.isometrik.gs.response.moderator.FetchModeratorsResult;

/**
 * The type Moderators model.
 */
class ModeratorsModel {

  private final boolean isAdmin;
  private final String moderatorId;
  private final String moderatorName;
  private final String moderatorIdentifier;
  private final String moderatorProfilePic;

  private final boolean canRemoveModerator;

  /**
   * Instantiates a new Moderators model.
   *
   * @param moderator the moderator
   * @param canRemoveModerator the can remove moderator
   */
  ModeratorsModel(FetchModeratorsResult.Moderator moderator, boolean canRemoveModerator) {

    moderatorId = moderator.getUserId();

    if (moderatorId.equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId())) {

      moderatorName = IsometrikStreamSdk.getInstance()
          .getContext()
          .getString(R.string.ism_you, moderator.getUserName());
    } else {
      moderatorName = moderator.getUserName();
    }

    moderatorIdentifier = moderator.getUserIdentifier();
    moderatorProfilePic = moderator.getUserProfileImageUrl();
    isAdmin = moderator.isAdmin();
    if (moderatorId.equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId())) {
      this.canRemoveModerator = false;
    } else {

      this.canRemoveModerator = canRemoveModerator;
    }
  }

  /**
   * Instantiates a new Moderators model.
   *
   * @param moderatorAddEvent the moderator add event
   * @param canRemoveModerator the can remove moderator
   */
  ModeratorsModel(ModeratorAddEvent moderatorAddEvent, boolean canRemoveModerator) {

    moderatorId = moderatorAddEvent.getModeratorId();

    if (moderatorId.equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId())) {

      moderatorName = IsometrikStreamSdk.getInstance()
          .getContext()
          .getString(R.string.ism_you, moderatorAddEvent.getModeratorName());
    } else {
      moderatorName = moderatorAddEvent.getModeratorName();
    }

    moderatorIdentifier = moderatorAddEvent.getModeratorIdentifier();
    moderatorProfilePic = moderatorAddEvent.getModeratorProfilePic();

    isAdmin = false;
    if (moderatorId.equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId())) {
      this.canRemoveModerator = false;
    } else {

      this.canRemoveModerator = canRemoveModerator;
    }
  }

  /**
   * Is admin boolean.
   *
   * @return the boolean
   */
  boolean isAdmin() {
    return isAdmin;
  }

  /**
   * Gets moderator id.
   *
   * @return the moderator id
   */
  String getModeratorId() {
    return moderatorId;
  }

  /**
   * Gets moderator name.
   *
   * @return the moderator name
   */
  String getModeratorName() {
    return moderatorName;
  }

  /**
   * Gets moderator identifier.
   *
   * @return the moderator identifier
   */
  String getModeratorIdentifier() {
    return moderatorIdentifier;
  }

  /**
   * Gets moderator profile pic.
   *
   * @return the moderator profile pic
   */
  String getModeratorProfilePic() {
    return moderatorProfilePic;
  }

  /**
   * Is can remove moderator boolean.
   *
   * @return the boolean
   */
  boolean isCanRemoveModerator() {
    return canRemoveModerator;
  }
}
