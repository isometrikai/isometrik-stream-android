package io.isometrik.gs.response.viewer;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import io.isometrik.gs.builder.viewer.FetchViewersQuery;

/**
 * The class to parse fetch viewers result of the fetch list of viewers in the stream group query
 * FetchViewersQuery{@link FetchViewersQuery}.
 *
 * @see FetchViewersQuery
 */
public class FetchViewersResult implements Serializable {

  @SerializedName("viewers")
  @Expose
  private ArrayList<StreamViewer> streamViewers;

  @SerializedName("viewersCount")
  @Expose
  private int viewersCount;
  @SerializedName("msg")
  @Expose
  private String message;

  /**
   * The class containing details of the viewers in the stream group.
   */
  public static class StreamViewer {

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
    @SerializedName("sessionStartTime")
    @Expose
    private long sessionStartTime;
    @SerializedName("metaData")
    @Expose
    private Object metaData;

    /**
     * Gets viewer id.
     *
     * @return the id of the viewer
     */
    public String getUserId() {
      return userId;
    }

    /**
     * Gets viewer name.
     *
     * @return the name of the viewer
     */
    public String getUserName() {
      return userName;
    }

    /**
     * Gets viewer identifier.
     *
     * @return the identifier of the viewer
     */
    public String getUserIdentifier() {
      return userIdentifier;
    }

    /**
     * Gets viewer profile pic.
     *
     * @return the profile pic of the viewer
     */
    public String getUserProfileImageUrl() {
      return userProfileImageUrl;
    }

    /**
     * Gets join time.
     *
     * @return the time at which viewer joined the stream group
     */
    public long getSessionStartTime() {
      return sessionStartTime;
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

  /**
   * Gets stream viewers.
   *
   * @return the list of viewers in the stream group
   * @see StreamViewer
   */
  public ArrayList<StreamViewer> getStreamViewers() {
    return streamViewers;
  }

  public int getViewersCount() {
    return viewersCount;
  }
}
