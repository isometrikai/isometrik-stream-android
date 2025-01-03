package io.isometrik.gs.response.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.message.FetchMessagesCountQuery;

/**
 * The class to parse fetch messages count result of fetching the count of messages in a stream
 * group query
 * FetchMessagesCountQuery{@link FetchMessagesCountQuery}.
 *
 * @see FetchMessagesCountQuery
 */
public class FetchMessagesCountResult implements Serializable {

  @SerializedName("messagesCount")
  @Expose
  private int messagesCount;

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

  public int getMessagesCount() {
    return messagesCount;
  }
}
