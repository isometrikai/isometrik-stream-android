package io.isometrik.gs.events.moderator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The class containing moderator remove event details.
 */
public class ModeratorRemoveEvent implements Serializable {

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

  @SerializedName("initiatorId")
  @Expose
  private String initiatorId;

  @SerializedName("moderatorName")
  @Expose
  private String moderatorName;

  @SerializedName("initiatorName")
  @Expose
  private String initiatorName;

  @SerializedName("moderatorsCount")
  @Expose
  private int moderatorsCount;

  /**
   * Gets action.
   *
   * @return the action specifying the moderator removed event
   */
  public String getAction() {
    return action;
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group from which moderator has been removed
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets timestamp.
   *
   * @return the timestamp at which moderator was removed from the stream group
   */
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * Gets moderator id.
   *
   * @return the id of the moderator removed from the stream group
   */
  public String getModeratorId() {
    return moderatorId;
  }

  /**
   * Gets initiator id.
   *
   * @return the id of the user who removed a moderator from the stream group
   */
  public String getInitiatorId() {
    return initiatorId;
  }

  /**
   * Gets moderator name.
   *
   * @return the name of the moderator removed from the stream group
   */
  public String getModeratorName() {
    return moderatorName;
  }

  /**
   * Gets initiator name.
   *
   * @return the name of the user who removed a moderator from the stream group
   */
  public String getInitiatorName() {
    return initiatorName;
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
