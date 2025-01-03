package io.isometrik.gs.events.stream;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * The class containing stream start event details, which is triggered if user is the member of the
 * stream group.
 */
public class StreamStartEvent implements Serializable {
  @SerializedName("metaData")
  @Expose
  private Object metaData;
  @SerializedName("action")
  @Expose
  private String action;

  @SerializedName("streamId")
  @Expose
  private String streamId;

  @SerializedName("streamDescription")
  @Expose
  private String streamDescription;

  @SerializedName("streamImage")
  @Expose
  private String streamImage;

  @SerializedName("timestamp")
  @Expose
  private long timestamp;

  @SerializedName("membersCount")
  @Expose
  private int membersCount;

  @SerializedName("initiatorId")
  @Expose
  private String initiatorId;

  @SerializedName("initiatorName")
  @Expose
  private String initiatorName;

  @SerializedName("initiatorIdentifier")
  @Expose
  private String initiatorIdentifier;

  @SerializedName("initiatorImage")
  @Expose
  private String initiatorImage;

  @SerializedName("members")
  @Expose
  private List<StreamMember> members;

  @SerializedName("isPublic")
  @Expose
  private boolean isPublic;

  @SerializedName("audioOnly")
  @Expose
  private boolean audioOnly;

  @SerializedName("multiLive")
  @Expose
  private boolean multiLive;

  @SerializedName("hdBroadcast")
  @Expose
  private boolean hdBroadcast;

  @SerializedName("restream")
  @Expose
  private boolean restream;

  @SerializedName("restreamChannelsCount")
  @Expose
  private int restreamChannelsCount;

  @SerializedName("productsLinked")
  @Expose
  private boolean productsLinked;

  @SerializedName("productsCount")
  @Expose
  private int productsCount;

  @SerializedName("moderatorsCount")
  @Expose
  private int moderatorsCount;

  @SerializedName("selfHosted")
  @Expose
  private boolean selfHosted;

  /**
   * The type Stream member.
   */
  public static class StreamMember {

    @SerializedName("memberId")
    @Expose
    public String memberId;

    @SerializedName("memberName")
    @Expose
    public String memberName;

    @SerializedName("memberIdentifier")
    @Expose
    public String memberIdentifier;

    @SerializedName("isAdmin")
    @Expose
    public boolean isAdmin;

    /**
     * Gets member id.
     *
     * @return the member id
     */
    public String getMemberId() {
      return memberId;
    }

    /**
     * Gets member name.
     *
     * @return the member name
     */
    public String getMemberName() {
      return memberName;
    }

    /**
     * Gets member identifier.
     *
     * @return the member identifier
     */
    public String getMemberIdentifier() {
      return memberIdentifier;
    }

    /**
     * Gets admin.
     *
     * @return whether given user is the admin of the stream group
     */
    public boolean getAdmin() {
      return isAdmin;
    }
  }

  /**
   * Gets action.
   *
   * @return the action specifying the stream group creation event
   */
  public String getAction() {
    return action;
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group created
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets timestamp.
   *
   * @return the timestamp at which stream group was created
   */
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * Gets members count.
   *
   * @return the members count of the stream group
   */
  public int getMembersCount() {
    return membersCount;
  }

  /**
   * Gets initiator id.
   *
   * @return the id of the stream group creator
   */
  public String getInitiatorId() {
    return initiatorId;
  }

  /**
   * Gets initiator name.
   *
   * @return the name of the stream group creator
   */
  public String getInitiatorName() {
    return initiatorName;
  }

  /**
   * Gets initiator identifier.
   *
   * @return the identifier of creator of the stream group
   */
  public String getInitiatorIdentifier() {
    return initiatorIdentifier;
  }

  /**
   * Gets initiator image.
   *
   * @return the image of creator of the stream group
   */
  public String getInitiatorImage() {
    return initiatorImage;
  }

  /**
   * Gets members.
   *
   * @return the members of the stream group
   */
  public List<StreamMember> getMembers() {
    return members;
  }

  /**
   * Gets stream description.
   *
   * @return the description of the stream group
   */
  public String getStreamDescription() {
    return streamDescription;
  }

  /**
   * Gets stream image.
   *
   * @return the image of the stream group
   */
  public String getStreamImage() {
    return streamImage;
  }

  /**
   * Gets whether the stream is public.
   *
   * @return whether stream is public
   */
  public boolean isPublic() {
    return isPublic;
  }

  /**
   * Gets whether the stream is audioOnly.
   *
   * @return whether stream is audioOnly
   */
  public boolean isAudioOnly() {
    return audioOnly;
  }

  /**
   * Gets whether the stream is multiLive.
   *
   * @return whether stream is multiLive
   */
  public boolean isMultiLive() {
    return multiLive;
  }

  /**
   * Gets whether the stream is hd or not.
   *
   * @return whether stream is hd or not
   */
  public boolean isHdBroadcast() {
    return hdBroadcast;
  }

  /**
   * Gets whether the stream is restreamed or not.
   *
   * @return whether stream is restreamed or not
   */
  public boolean isRestream() {
    return restream;
  }

  /**
   * Gets count of channels to which stream is restreamed.
   *
   * @return count of channels to which stream is restreamed
   */
  public int isRestreamChannelsCount() {
    return restreamChannelsCount;
  }

  /**
   * Gets whether given stream has products linked or not.
   *
   * @return whether given stream has products linked or not
   */
  public boolean isProductsLinked() {
    return productsLinked;
  }

  /**
   * Gets number of products linked to a broadcast.
   *
   * @return number of products linked to a broadcast
   */
  public int getProductsCount() {
    return productsCount;
  }

  /**
   * Gets number of moderators added to a broadcast.
   *
   * @return number of moderators added to a broadcast
   */
  public int getModeratorsCount() {
    return moderatorsCount;
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

  public boolean isSelfHosted() {
    return selfHosted;
  }
}