package io.isometrik.gs.response.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.user.CreateUserQuery;

/**
 * The class to parse add user result of creating a new user query AddUserQuery{@link
 * CreateUserQuery}.
 *
 * @see CreateUserQuery
 */
public class CreateUserResult implements Serializable {

  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("userToken")
  @Expose
  private String userToken;

  @SerializedName("userId")
  @Expose
  private String userId;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets user token.
   *
   * @return the user token
   */
  public String getUserToken() {
    return userToken;
  }

  /**
   * Gets user id.
   *
   * @return the user id
   */
  public String getUserId() {
    return userId;
  }
}
