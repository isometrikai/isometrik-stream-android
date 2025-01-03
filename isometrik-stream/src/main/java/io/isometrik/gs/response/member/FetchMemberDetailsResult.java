package io.isometrik.gs.response.member;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;

import io.isometrik.gs.builder.member.FetchMemberDetailsQuery;

/**
 * The class to parse fetch user details result of fetching the user details query
 * FetchUserDetailsQuery{@link FetchMemberDetailsQuery}.
 *
 * @see FetchMemberDetailsQuery
 */
public class FetchMemberDetailsResult implements Serializable {
  @SerializedName("msg")
  @Expose
  private String message;
  @SerializedName("isPublishing")
  @Expose
  private boolean isPublishing;
  @SerializedName("isAdmin")
  @Expose
  private boolean isAdmin;
  @SerializedName("joinTime")
  @Expose
  private Long joinTime;

  @SerializedName("userId")
  @Expose
  private String userId;
  @SerializedName("userName")
  @Expose
  private String userName;
  @SerializedName("userIdentifier")
  @Expose
  private String userIdentifier;
  @SerializedName("userProfileImageUrl")
  @Expose
  private String userProfileImageUrl;
  @SerializedName("metaData")
  @Expose
  private Object metaData;

  public String getUserId() {
    return userId;
  }

  public String getUserName() {
    return userName;
  }

  public String getUserIdentifier() {
    return userIdentifier;
  }

  public String getUserProfileImageUrl() {
    return userProfileImageUrl;
  }

  /**
   * Gets publishing.
   *
   * @return whether the given member is publishing or not
   */
  public boolean getPublishing() {
    return isPublishing;
  }

  /**
   * Gets admin.
   *
   * @return whether the given member is creator of the stream group
   */
  public boolean getAdmin() {
    return isAdmin;
  }

  /**
   * Gets join time.
   *
   * @return time at which given member started publishing
   */
  public Long getJoinTime() {
    return joinTime;
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

  public String getMessage() {
    return message;
  }
}
