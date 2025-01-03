package io.isometrik.gs.response.message.utils;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import org.json.JSONObject;

public class StreamMessage {
  @SerializedName("deviceId")
  @Expose
  private String deviceId;
  @SerializedName("repliesCount")
  @Expose
  private int repliesCount;
  @SerializedName("senderName")
  @Expose
  private String senderName;
  @SerializedName("senderProfileImageUrl")
  @Expose
  private String senderProfileImageUrl;
  @SerializedName("senderId")
  @Expose
  private String senderId;
  @SerializedName("senderIdentifier")
  @Expose
  private String senderIdentifier;
  @SerializedName("body")
  @Expose
  private String body;
  @SerializedName("messageId")
  @Expose
  private String messageId;
  @SerializedName("messageType")
  @Expose
  private Integer messageType;
  @SerializedName("sentAt")
  @Expose
  private long sentAt;
  @SerializedName("searchableTags")
  @Expose
  private ArrayList<String> searchableTags;
  @SerializedName("metaData")
  @Expose
  private Object metaData;
  @SerializedName("customType")
  @Expose
  private String customType;

  public String getDeviceId() {
    return deviceId;
  }

  public int getRepliesCount() {
    return repliesCount;
  }

  /**
   * Gets sender name.
   *
   * @return the name of the user who sent message
   */
  public String getSenderName() {
    return senderName;
  }

  /**
   * Gets sender image.
   *
   * @return the image of the user who sent message
   */
  public String getSenderProfileImageUrl() {
    return senderProfileImageUrl;
  }

  /**
   * Gets sender id.
   *
   * @return the id of the user who sent message
   */
  public String getSenderId() {
    return senderId;
  }

  /**
   * Gets sender identifier.
   *
   * @return the identifier of the user who sent message
   */
  public String getSenderIdentifier() {
    return senderIdentifier;
  }

  /**
   * Gets message body.
   *
   * @return the message body
   */
  public String getBody() {
    return body;
  }

  /**
   * Gets message id.
   *
   * @return the id of message
   */
  public String getMessageId() {
    return messageId;
  }

  /**
   * Gets message type.
   *
   * @return the type of message sent
   */
  public Integer getMessageType() {
    return messageType;
  }

  /**
   * Gets sent at.
   *
   * @return the time at which message was sent
   */
  public long getSentAt() {
    return sentAt;
  }

  public ArrayList<String> getSearchableTags() {
    return searchableTags;
  }

  public JSONObject getMetaData() {

    try {
      return new JSONObject(new Gson().toJson(metaData));
    } catch (Exception ignore) {
      return new JSONObject();
    }
  }

  public String getCustomType() {
    return customType;
  }
}
