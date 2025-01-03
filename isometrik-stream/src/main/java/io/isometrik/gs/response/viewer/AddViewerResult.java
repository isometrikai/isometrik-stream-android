package io.isometrik.gs.response.viewer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.viewer.AddViewerQuery;

/**
 * The class to parse add viewer result of joining the stream group as viewer query
 * AddViewerQuery{@link AddViewerQuery}.
 *
 * @see AddViewerQuery
 */
public class AddViewerResult implements Serializable {
  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("numberOfViewers")
  @Expose
  private int numberOfViewers;
  @SerializedName("rtcToken")
  @Expose
  private String rtcToken;
  @SerializedName("playbackUrl")
  @Expose
  private String playbackUrl;

  @SerializedName("isModerator")
  @Expose
  private boolean isModerator;
  @SerializedName("uid")
  @Expose
  private long userUid;

  /**
   * Gets number of viewers.
   *
   * @return the number of viewers in the stream group
   */
  public int getNumberOfViewers() {
    return numberOfViewers;
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
   * Gets rtc token.
   *
   * @return the rtc token received
   */
  public String getRtcToken() {
    return rtcToken;
  }

  /**
   * Gets playback url for remote media stream
   *
   * @return the url for the media playback for non multi-live stream
   */
  public String getPlaybackUrl() {
    return playbackUrl;
  }

  /**
   * Gets whether given user who is joining stream as viewer, is the moderator of the stream or not
   *
   * @return whether given user who is joining stream as viewer, is the moderator of the stream or not
   */
  public boolean isModerator() {
    return isModerator;
  }

  public long getUserUid() {
    return userUid;
  }
}
