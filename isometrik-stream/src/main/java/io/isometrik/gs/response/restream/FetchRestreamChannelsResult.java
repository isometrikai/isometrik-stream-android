package io.isometrik.gs.response.restream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import io.isometrik.gs.builder.restream.FetchRestreamChannelsQuery;

/**
 * The class to parse fetch restream channels result of fetching the restream channels query
 * FetchRestreamChannelsQuery{@link FetchRestreamChannelsQuery}.
 *
 * @see FetchRestreamChannelsQuery
 */
public class FetchRestreamChannelsResult implements Serializable {

  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("restreamChannels")
  @Expose
  private ArrayList<RestreamChannel> restreamChannels;

  /**
   * The class containing details of the restream channel.
   */
  public static class RestreamChannel {

    @SerializedName("ingestUrl")
    @Expose
    private String ingestUrl;
    @SerializedName("channelName")
    @Expose
    private String channelName;
    @SerializedName("channelId")
    @Expose
    private String channelId;

    @SerializedName("enabled")
    @Expose
    private boolean enabled;
    @SerializedName("channelType")
    @Expose
    private int channelType;

    /**
     * Gets ingestUrl
     *
     * @return Ingest url(only rtmp and rtmps supported) of the restream channel
     */
    public String getIngestUrl() {
      return ingestUrl;
    }

    /**
     * Gets channelName
     *
     * @return Name of the restream channel
     */
    public String getChannelName() {
      return channelName;
    }

    /**
     * Gets channelId
     *
     * @return Id of the restream channel
     */
    public String getChannelId() {
      return channelId;
    }

    /**
     * Gets enabled
     *
     * @return Whether the restream channel is enabled or not
     */
    public boolean isEnabled() {
      return enabled;
    }

    /**
     * Gets channelType
     *
     * @return Type of the restream channel. Will be between 0-5 where 0-facebook, 1-youtube,
     * 2-twitch, 3-twitter, 4-linkedin, 5-custom
     */
    public int getChannelType() {
      return channelType;
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
   * Gets restream channels for the user.
   *
   * @return the list of restream channels for the user
   */
  public ArrayList<RestreamChannel> getRestreamChannels() {
    return restreamChannels;
  }
}
