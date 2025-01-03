package io.isometrik.gs.response.member;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import io.isometrik.gs.builder.member.FetchMembersQuery;

/**
 * The class to parse fetch eligible members result of fetching eligible members list in a stream
 * group query
 * FetchMembersQuery{@link FetchMembersQuery}.
 *
 * @see FetchMembersQuery
 */
public class FetchEligibleMembersResult implements Serializable {

  @SerializedName("msg")
  @Expose
  private String message;
  @SerializedName("streamEligibleMembers")
  @Expose
  private ArrayList<StreamEligibleMember> streamEligibleMembers;

  /**
   * The class containing details of the stream group members.
   */
  public static class StreamEligibleMember {

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
  }

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  public ArrayList<StreamEligibleMember> getStreamEligibleMembers() {
    return streamEligibleMembers;
  }
}
