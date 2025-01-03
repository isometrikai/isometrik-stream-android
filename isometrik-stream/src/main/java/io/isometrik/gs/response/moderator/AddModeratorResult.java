package io.isometrik.gs.response.moderator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.moderator.AddModeratorQuery;

/**
 * The class to parse add moderator result of adding a moderator to a stream group query
 * AddModeratorQuery{@link AddModeratorQuery}.
 *
 * @see AddModeratorQuery
 */
public class AddModeratorResult implements Serializable {

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
