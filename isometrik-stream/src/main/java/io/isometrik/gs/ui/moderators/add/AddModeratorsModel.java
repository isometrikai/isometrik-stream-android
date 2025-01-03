package io.isometrik.gs.ui.moderators.add;

import io.isometrik.gs.response.user.FetchUsersResult;

/**
 * The type Moderators model.
 */
class AddModeratorsModel {

  private final String userId;
  private final String userName;
  private final String userIdentifier;
  private final String userProfilePic;

  /**
   * Instantiates a new Add moderator model.
   *
   * @param user the user
   */
  AddModeratorsModel(FetchUsersResult.User user) {

    userId = user.getUserId();
    userName = user.getUserName();
    userIdentifier = user.getUserIdentifier();
    userProfilePic = user.getUserProfileImageUrl();
  }

  /**
   * Gets user id.
   *
   * @return the user id
   */
  public String getUserId() {
    return userId;
  }

  /**
   * Gets user name.
   *
   * @return the user name
   */
  String getUserName() {
    return userName;
  }

  /**
   * Gets user identifier.
   *
   * @return the user identifier
   */
  String getUserIdentifier() {
    return userIdentifier;
  }

  /**
   * Gets user profile pic.
   *
   * @return the user profile pic
   */
  String getUserProfilePic() {
    return userProfilePic;
  }
}
