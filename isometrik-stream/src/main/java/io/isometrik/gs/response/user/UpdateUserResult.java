package io.isometrik.gs.response.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.user.UpdateUserQuery;

/**
 * The class to parse update user result of updating the user details query UpdateUserQuery{@link
 * UpdateUserQuery}.
 *
 * @see UpdateUserQuery
 */
public class UpdateUserResult implements Serializable {
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
