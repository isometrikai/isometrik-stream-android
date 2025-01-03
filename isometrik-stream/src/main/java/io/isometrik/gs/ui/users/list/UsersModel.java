package io.isometrik.gs.ui.users.list;

import io.isometrik.gs.response.user.FetchUsersResult;
import org.json.JSONObject;

/**
 * The helper class for inflating items in the users list.
 */
public class UsersModel {

  private final String userName;
  private final String userIdentifier;
  private final String userProfileImageUrl;
  private final boolean notification;
  private final JSONObject metaData;
  private final long updatedAt;
  private final long createdAt;

  /**
   * Instantiates a new Users model.
   *
   * @param user the user
   */
  UsersModel(FetchUsersResult.User user) {

    userName = user.getUserName();
    userIdentifier = user.getUserIdentifier();
    userProfileImageUrl = user.getUserProfileImageUrl();
    notification = user.isNotification();
    metaData = user.getMetaData();
    updatedAt = user.getUpdatedAt();
    createdAt = user.getCreatedAt();
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
   * Gets user profile image url.
   *
   * @return the user profile image url
   */
  String getUserProfileImageUrl() {
    return userProfileImageUrl;
  }

  /**
   * Is notification boolean.
   *
   * @return the boolean
   */
  public boolean isNotification() {
    return notification;
  }

  /**
   * Gets meta data.
   *
   * @return the meta data
   */
  public JSONObject getMetaData() {
    return metaData;
  }

  /**
   * Gets updated at.
   *
   * @return the updated at
   */
  public long getUpdatedAt() {
    return updatedAt;
  }

  /**
   * Gets created at.
   *
   * @return the created at
   */
  public long getCreatedAt() {
    return createdAt;
  }
}
