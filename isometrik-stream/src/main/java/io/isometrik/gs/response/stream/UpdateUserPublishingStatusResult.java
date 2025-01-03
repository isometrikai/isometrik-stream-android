package io.isometrik.gs.response.stream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.stream.UpdateUserPublishingStatusQuery;

/**
 * The class to parse update user publishing status result of updating the publishing status of a
 * user in any of the stream groups where user was a viewer or was publishing in query
 * UpdateUserPublishingStatusQuery{@link UpdateUserPublishingStatusQuery}.
 *
 * @see UpdateUserPublishingStatusQuery
 */
public class UpdateUserPublishingStatusResult implements Serializable {
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