package io.isometrik.gs.response.moderator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.moderator.RemoveModeratorQuery;

/**
 * The class to parse remove moderator result of removing a moderator from the stream group query
 * RemoveModeratorQuery{@link RemoveModeratorQuery}.
 *
 * @see RemoveModeratorQuery
 */
public class RemoveModeratorResult implements Serializable {
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
