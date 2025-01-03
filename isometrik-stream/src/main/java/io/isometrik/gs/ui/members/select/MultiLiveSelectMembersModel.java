package io.isometrik.gs.ui.members.select;

import io.isometrik.gs.response.user.FetchUsersResult;

/**
 * The type Select members model.
 */
public class MultiLiveSelectMembersModel {

  private final String userId;
  private final String userName;
  private final String userIdentifier;
  private final String userProfilePic;

  private boolean selected;

  /**
   * Instantiates a new Select members model.
   *
   * @param user the user
   */
  MultiLiveSelectMembersModel(FetchUsersResult.User user) {

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

  /**
   * Is selected boolean.
   *
   * @return the boolean
   */
  boolean isSelected() {
    return selected;
  }

  /**
   * Sets selected.
   *
   * @param selected the selected
   */
  void setSelected(boolean selected) {
    this.selected = selected;
  }
}
