package io.isometrik.gs.response.copublish;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.copublish.SwitchProfileQuery;

/**
 * The class to parse switch profile result of switching profile from viewer to a broadcaster in a
 * stream group query
 * SwitchProfileQuery{@link SwitchProfileQuery}.
 *
 * @see SwitchProfileQuery
 */
public class SwitchProfileResult implements Serializable {

  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("rtcToken")
  @Expose
  private String rtcToken;

  @SerializedName("uid")
  @Expose
  private long userUid;

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

  public long getUserUid() {
    return userUid;
  }
}