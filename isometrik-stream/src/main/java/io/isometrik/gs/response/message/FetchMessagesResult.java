package io.isometrik.gs.response.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import io.isometrik.gs.builder.message.FetchMessagesQuery;
import io.isometrik.gs.response.message.utils.StreamMessage;

/**
 * The class to parse fetch messages result of fetching the list of messages in a stream group query
 * FetchMessagesQuery{@link FetchMessagesQuery}.
 *
 * @see FetchMessagesQuery
 */
public class FetchMessagesResult implements Serializable {

  @SerializedName("messages")
  @Expose
  private ArrayList<StreamMessage> streamMessages;

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

  /**
   * Gets stream messages.
   *
   * @return the list of messages in the stream group
   * @see StreamMessage
   */
  public ArrayList<StreamMessage> getStreamMessages() {
    return streamMessages;
  }
}
