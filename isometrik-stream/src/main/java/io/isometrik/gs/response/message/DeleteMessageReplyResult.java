package io.isometrik.gs.response.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.message.DeleteMessageReplyQuery;

/**
 * The class to parse delete message reply result of deleting a message reply from the stream group
 * query DeleteMessageReplyQuery{@link DeleteMessageReplyQuery}.
 *
 * @see DeleteMessageReplyQuery
 */
public class DeleteMessageReplyResult implements Serializable {
  @SerializedName("msg")
  @Expose
  private String message;
  @SerializedName("deletedAt")
  @Expose
  private long deletedAt;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets deleted at.
   *
   * @return the time at which message was deleted
   */
  public long getDeletedAt() {
    return deletedAt;
  }
}
