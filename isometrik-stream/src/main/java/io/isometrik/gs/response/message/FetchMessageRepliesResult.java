package io.isometrik.gs.response.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import io.isometrik.gs.builder.message.FetchMessageRepliesQuery;
import io.isometrik.gs.response.message.utils.StreamMessage;

/**
 * The class to parse fetch message replies result of fetching the list of message replies in a
 * stream group query
 * FetchMessageRepliesQuery{@link FetchMessageRepliesQuery}.
 *
 * @see FetchMessageRepliesQuery
 */
public class FetchMessageRepliesResult implements Serializable {

  @SerializedName("messages")
  @Expose
  private ArrayList<StreamMessageReply> streamMessageReplies;

  @SerializedName("msg")
  @Expose
  private String message;

  /**
   * The class containing details of the message reply in the stream group.
   */
  public static class StreamMessageReply extends StreamMessage {

    @SerializedName("parentMessageId")
    @Expose
    private String parentMessageId;
    @SerializedName("parentMessageSenderId")
    @Expose
    private String parentMessageSenderId;

    public String getParentMessageId() {
      return parentMessageId;
    }

    public String getParentMessageSenderId() {
      return parentMessageSenderId;
    }
  }

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets stream message replies.
   *
   * @return the list of message replies in the stream group
   * @see StreamMessageReply
   */
  public ArrayList<StreamMessageReply> getStreamMessageReplies() {
    return streamMessageReplies;
  }
}
