package io.isometrik.gs.events.moderator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * The class containing moderator leave event details.
 */
public class ModeratorLeaveEvent implements Serializable {

  @SerializedName("action")
  @Expose
  private String action;

  @SerializedName("streamId")
  @Expose
  private String streamId;

  @SerializedName("timestamp")
  @Expose
  private long timestamp;

  @SerializedName("moderatorId")
  @Expose
  private String moderatorId;

  @SerializedName("moderatorName")
  @Expose
  private String moderatorName;

  @SerializedName("moderatorsCount")
  @Expose
  private int moderatorsCount;

  /**
   * Gets action.
   *
   * @return the action specifying the moderator left event
   */
  public String getAction() {
    return action;
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group from which moderator has left
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets timestamp.
   *
   * @return the timestamp at which moderator left the stream group
   */
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * Gets moderator id.
   *
   * @return the id of the moderator who left the stream group
   */
  public String getModeratorId() {
    return moderatorId;
  }

  /**
   * Gets moderator name.
   *
   * @return the name of the moderator who left the stream group
   */
  public String getModeratorName() {
    return moderatorName;
  }

  /**
   * Gets moderators count.
   *
   * @return the moderators count of the stream group at the time of given event
   */
  public int getModeratorsCount() {
    return moderatorsCount;
  }
}
