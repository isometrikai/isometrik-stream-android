package io.isometrik.gs.response.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

import io.isometrik.gs.builder.message.SendMessageReplyQuery;

/**
 * The class to parse send message reply result of sending a message reply in a stream group query
 * SendMessageReplyQuery{@link SendMessageReplyQuery}.
 *
 * @see SendMessageReplyQuery
 */
public class SendMessageReplyResult implements Serializable {

  @SerializedName("messageId")
  @Expose
  private String messageId;

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
   * Gets message id.
   *
   * @return the id of the message sent
   */
  public String getMessageId() {
    return messageId;
  }
}
