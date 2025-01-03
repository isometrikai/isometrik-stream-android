package io.isometrik.gs.builder.message;

import org.json.JSONObject;

import java.util.List;

/**
 * Query builder class for creating the request for sending message reply in a stream group
 */
public class SendMessageReplyQuery {

  private final String streamId;
  private final String body;
  private final String userToken;
  private final String deviceId;
  private final List<String> searchableTags;
  private final JSONObject metaData;
  private final String customType;
  private final Integer messageType;
  private final String parentMessageId;

  private SendMessageReplyQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.body = builder.body;
    this.userToken = builder.userToken;
    this.messageType = builder.messageType;
    this.deviceId = builder.deviceId;
    this.customType = builder.customType;
    this.metaData = builder.metaData;
    this.searchableTags = builder.searchableTags;
    this.parentMessageId = builder.parentMessageId;
  }

  /**
   * The Builder class for the SendMessageReplyQuery.
   */
  public static class Builder {
    private String streamId;
    private String body;
    private String userToken;
    private String deviceId;
    private List<String> searchableTags;
    private JSONObject metaData;
    private String customType;
    private Integer messageType;
    private String parentMessageId;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream group into which message reply is to be sent
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    public Builder setDeviceId(String deviceId) {
      this.deviceId = deviceId;
      return this;
    }

    public Builder setSearchableTags(List<String> searchableTags) {
      this.searchableTags = searchableTags;
      return this;
    }

    public Builder setMetaData(JSONObject metaData) {
      this.metaData = metaData;
      return this;
    }

    public Builder setCustomType(String customType) {
      this.customType = customType;
      return this;
    }

    /**
     * Sets sender token.
     *
     * @param userToken the token of the user sending the message reply in the stream group
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Sets message reply body.
     *
     * @param body the message reply body that is being sent in the stream group
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setBody(String body) {
      this.body = body;
      return this;
    }

    /**
     * Sets message reply type.
     *
     * @param messageType the type of the message reply being sent in the stream group
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setMessageType(Integer messageType) {
      this.messageType = messageType;
      return this;
    }

    /**
     * Sets parent message id.
     *
     * @param parentMessageId the id of the parent message for which to send reply
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setParentMessageId(String parentMessageId) {
      this.parentMessageId = parentMessageId;
      return this;
    }

    /**
     * Build send message reply query.
     *
     * @return the SendMessageReplyQuery{@link SendMessageReplyQuery} instance
     * @see SendMessageReplyQuery
     */
    public SendMessageReplyQuery build() {
      return new SendMessageReplyQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group into which message reply is to be sent
   */
  public String getStreamId() {
    return streamId;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public List<String> getSearchableTags() {
    return searchableTags;
  }

  public JSONObject getMetaData() {
    return metaData;
  }

  public String getCustomType() {
    return customType;
  }

  /**
   * Gets message reply body.
   *
   * @return the message reply body that is being sent in the stream group
   */
  public String getBody() {
    return body;
  }

  /**
   * Gets message reply type.
   *
   * @return the type of the message reply being sent in the stream group
   */
  public Integer getMessageType() {
    return messageType;
  }

  /**
   * Gets parent message id.
   *
   * @return the id of the parent message for which to send reply
   */
  public String getParentMessageId() {
    return parentMessageId;
  }

  /**
   * Gets user token.
   *
   * @return the token of the user sending the message reply in the stream group
   */
  public String getUserToken() {
    return userToken;
  }
}
