package io.isometrik.gs.response.moderator;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import io.isometrik.gs.builder.moderator.FetchModeratorsQuery;

/**
 * The class to parse fetch moderators result of fetching moderators list in a stream group query
 * FetchModeratorsQuery{@link FetchModeratorsQuery}.
 *
 * @see FetchModeratorsQuery
 */
public class FetchModeratorsResult implements Serializable {
  @SerializedName("moderatorsCount")
  @Expose
  private int moderatorsCount;
  @SerializedName("msg")
  @Expose
  private String message;
  @SerializedName("moderators")
  @Expose
  private ArrayList<Moderator> moderators;

  /**
   * The class containing details of the stream group moderators.
   */
  public static class Moderator {

    @SerializedName("isAdmin")
    @Expose
    private boolean isAdmin;

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

    public boolean isAdmin() {
      return isAdmin;
    }

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

    public JSONObject getMetaData() {

      try {
        return new JSONObject(new Gson().toJson(metaData));
      } catch (Exception ignore) {
        return new JSONObject();
      }
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
   * Gets stream moderators.
   *
   * @return the list of the stream group moderators
   * @see Moderator
   */
  public ArrayList<Moderator> getStreamModerators() {
    return moderators;
  }

  public int getModeratorsCount() {
    return moderatorsCount;
  }
}
