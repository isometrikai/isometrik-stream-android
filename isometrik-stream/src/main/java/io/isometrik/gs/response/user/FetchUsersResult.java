package io.isometrik.gs.response.user;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import io.isometrik.gs.builder.user.FetchUsersQuery;

/**
 * The class to parse fetch users result of fetching the list of users query FetchUsersQuery{@link
 * FetchUsersQuery}.
 *
 * @see FetchUsersQuery
 */
public class FetchUsersResult implements Serializable {

  @SerializedName("users")
  @Expose
  private ArrayList<User> users;

  @SerializedName("msg")
  @Expose
  private String message;

  /**
   * The class containing details of the users.
   */
  public static class User {
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("userProfileImageUrl")
    @Expose
    private String userProfileImageUrl;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("userIdentifier")
    @Expose
    private String userIdentifier;

    @SerializedName("notification")
    @Expose
    private boolean notification;

    @SerializedName("metaData")
    @Expose
    private Object metaData;
    @SerializedName("updatedAt")
    @Expose
    private long updatedAt;
    @SerializedName("createdAt")
    @Expose
    private long createdAt;

    public String getUserId() {
      return userId;
    }

    /**
     * Gets user profile image url.
     *
     * @return the user profile image url
     */
    public String getUserProfileImageUrl() {
      return userProfileImageUrl;
    }

    /**
     * Gets user name.
     *
     * @return the user name
     */
    public String getUserName() {
      return userName;
    }

    /**
     * Gets user identifier.
     *
     * @return the user identifier
     */
    public String getUserIdentifier() {
      return userIdentifier;
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

      try {
        return new JSONObject(new Gson().toJson(metaData));
      } catch (Exception ignore) {
        return new JSONObject();
      }
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

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets users.
   *
   * @return the list of users
   * @see User
   */
  public ArrayList<User> getUsers() {
    return users;
  }
}
