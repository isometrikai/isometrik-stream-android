package io.isometrik.gs.response.restream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.restream.DeleteRestreamChannelQuery;

/**
 * The class to parse delete restream channel result of deleting a restreaming channel for a user
 * query
 * DeleteRestreamChannelQuery{@link DeleteRestreamChannelQuery}.
 *
 * @see DeleteRestreamChannelQuery
 */
public class DeleteRestreamChannelResult implements Serializable {
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