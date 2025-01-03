package io.isometrik.gs.response.restream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.restream.UpdateRestreamChannelQuery;

/**
 * The class to parse update restream channel result of updating a restreaming channel for a user
 * query
 * UpdateRestreamChannelQuery{@link UpdateRestreamChannelQuery}.
 *
 * @see UpdateRestreamChannelQuery
 */
public class UpdateRestreamChannelResult implements Serializable {
  @SerializedName("msg")
  @Expose
  private String message;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }
}