package io.isometrik.gs.response.moderator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.moderator.LeaveModeratorQuery;

/**
 * The class to parse leave moderator result of a moderator leaving the stream group query
 * LeaveModeratorQuery{@link LeaveModeratorQuery}.
 *
 * @see LeaveModeratorQuery
 */
public class LeaveModeratorResult implements Serializable {

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
