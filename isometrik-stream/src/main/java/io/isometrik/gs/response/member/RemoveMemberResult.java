package io.isometrik.gs.response.member;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.member.RemoveMemberQuery;

/**
 * The class to parse remove member result of removing a member from the stream group query
 * RemoveMemberQuery{@link RemoveMemberQuery}.
 *
 * @see RemoveMemberQuery
 */
public class RemoveMemberResult implements Serializable {
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
