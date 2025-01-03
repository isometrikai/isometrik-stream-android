package io.isometrik.gs.response.restream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.restream.UpdateAllRestreamChannelsQuery;

/**
 * The class to parse update all restream channels result of updating all restreaming channels for a
 * user
 * query
 * UpdateAllRestreamChannelsQuery{@link UpdateAllRestreamChannelsQuery}.
 *
 * @see UpdateAllRestreamChannelsQuery
 */
public class UpdateAllRestreamChannelsResult implements Serializable {
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