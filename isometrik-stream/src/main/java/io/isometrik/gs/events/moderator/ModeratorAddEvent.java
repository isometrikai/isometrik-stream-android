package io.isometrik.gs.events.moderator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * The class containing moderator add event details.
 */
public class ModeratorAddEvent implements Serializable {

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

  @SerializedName("moderatorIdentifier")
  @Expose
  private String moderatorIdentifier;

  @SerializedName("moderatorProfilePic")
  @Expose
  private String moderatorProfilePic;

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
   * @return the action specifying the moderator added event
   */
  public String getAction() {
    return action;
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group in which moderator has been added
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets timestamp.
   *
   * @return the timestamp at which moderator was added to the stream group
   */
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * Gets moderator id.
   *
   * @return the id of the moderator added to the stream group
   */
  public String getModeratorId() {
    return moderatorId;
  }

  /**
   * Gets initiator id.
   *
   * @return the id of the user who added a moderator to the stream group
   */
  public String getInitiatorId() {
    return initiatorId;
  }

  /**
   * Gets moderator name.
   *
   * @return the name of the moderator added to the stream group
   */
  public String getModeratorName() {
    return moderatorName;
  }

  /**
   * Gets initiator name.
   *
   * @return the name of the user who added a moderator to the stream group
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

  /**
   * Gets moderator identifier.
   *
   * @return the identifier of the moderator added
   */
  public String getModeratorIdentifier() {
    return moderatorIdentifier;
  }

  /**
   * Gets moderator profile pic.
   *
   * @return the profile pic of the moderator added
   */
  public String getModeratorProfilePic() {
    return moderatorProfilePic;
  }
}
