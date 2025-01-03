package io.isometrik.gs.builder.message;

import org.json.JSONObject;

import java.util.List;

/**
 * Query builder class for creating the request for sending message in a stream group
 */
public class SendMessageQuery {

  private final String streamId;
  private final String body;
  private final String userToken;
  private final String deviceId;
  private final List<String> searchableTags;
  private final JSONObject metaData;
  private final String customType;
  private final Integer messageType;

  private SendMessageQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.body = builder.body;
    this.userToken = builder.userToken;
    this.messageType = builder.messageType;
    this.deviceId = builder.deviceId;
    this.customType = builder.customType;
    this.metaData = builder.metaData;
    this.searchableTags = builder.searchableTags;
  }

  /**
   * The Builder class for the SendMessageQuery.
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

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
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
     * Sets stream id.
     *
     * @param streamId the id of the stream group into which message is to be sent
     * @return the SendMessageQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the token of the user sending the message in the stream group
     * @return the SendMessageQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Sets message.
     *
     * @param body the message body that is being sent in the stream group
     * @return the SendMessageQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setBody(String body) {
      this.body = body;
      return this;
    }

    /**
     * Sets message type.
     *
     * @param messageType the type of the message being sent in the stream group
     * @return the SendMessageQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setMessageType(Integer messageType) {
      this.messageType = messageType;
      return this;
    }

    /**
     * Build send message query.
     *
     * @return the SendMessageQuery{@link SendMessageQuery} instance
     * @see SendMessageQuery
     */
    public SendMessageQuery build() {
      return new SendMessageQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group into which message is to be sent
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets message body.
   *
   * @return the message body that is being sent in the stream group
   */
  public String getBody() {
    return body;
  }

  /**
   * Gets message type.
   *
   * @return the type of the message being sent in the stream group
   */
  public Integer getMessageType() {
    return messageType;
  }

  /**
   * Gets user token.
   *
   * @return the token of the user sending the message in the stream group
   */
  public String getUserToken() {
    return userToken;
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
}
