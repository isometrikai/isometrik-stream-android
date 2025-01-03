package io.isometrik.gs.response.restream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.restream.AddRestreamChannelQuery;

/**
 * The class to parse add restream channel result of adding a restreaming channel for a user
 * query AddRestreamChannelQuery
 * Add{@link AddRestreamChannelQuery}.
 *
 * @see AddRestreamChannelQuery
 */
public class AddRestreamChannelResult implements Serializable {
  @SerializedName("msg")
  @Expose
  private String message;
  @SerializedName("channelId")
  @Expose
  private String channelId;

  /**
   * Gets channelId.
   *
   * @return channelId the id of the newly created channel
   */
  public String getChannelId() {
    return channelId;
  }

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }
}