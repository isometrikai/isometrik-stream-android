package io.isometrik.gs.response.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.user.DeleteUserQuery;

/**
 * The class to parse delete user result of deleting a user query DeleteUserQuery{@link
 * DeleteUserQuery}.
 *
 * @see DeleteUserQuery
 */
public class DeleteUserResult implements Serializable {
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
