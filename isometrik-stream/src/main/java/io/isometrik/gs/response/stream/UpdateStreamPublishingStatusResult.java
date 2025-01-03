package io.isometrik.gs.response.stream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.stream.UpdateStreamPublishingStatusQuery;

/**
 * The class to parse update stream publishing status result of updating the publishing status of a
 * user in a stream group query UpdateStreamPublishingStatusQuery{@link
 * UpdateStreamPublishingStatusQuery}.
 *
 * @see UpdateStreamPublishingStatusQuery
 */
public class UpdateStreamPublishingStatusResult implements Serializable {
  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("rtcToken")
  @Expose
  private String rtcToken;

  @SerializedName("isModerator")
  @Expose
  private boolean isModerator;
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

  /**
   * Gets whether given user whose publishing status is being updated, is the moderator of the
   * stream or not
   *
   * @return whether given user whose publishing status is being updated, is the moderator of the
   * stream or not
   */
  public boolean isModerator() {
    return isModerator;
  }

  public long getUserUid() {
    return userUid;
  }
}
