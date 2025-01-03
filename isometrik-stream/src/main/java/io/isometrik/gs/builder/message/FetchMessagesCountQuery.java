package io.isometrik.gs.builder.message;

import java.util.List;

/**
 * Query builder class for creating the request for fetching messages count in a stream group.
 */
public class FetchMessagesCountQuery {

  private final String streamId;
  private final String userToken;
  private final Long lastMessageTimestamp;
  private final List<String> messageIds;
  private final List<Integer> messageTypes;
  private final List<String> customTypes;
  private final List<String> senderIds;
  private final Boolean senderIdsExclusive;

  private FetchMessagesCountQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.userToken = builder.userToken;
    this.lastMessageTimestamp = builder.lastMessageTimestamp;
    this.messageIds = builder.messageIds;
    this.messageTypes = builder.messageTypes;
    this.customTypes = builder.customTypes;
    this.senderIds = builder.senderIds;

    this.senderIdsExclusive = builder.senderIdsExclusive;
  }

  /**
   * The Builder class for the FetchMessagesCountQuery.
   */
  public static class Builder {
    private String streamId;
    private String userToken;

    private Long lastMessageTimestamp;
    private List<String> messageIds;
    private List<Integer> messageTypes;
    private List<String> customTypes;
    private List<String> senderIds;

    private Boolean senderIdsExclusive;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    public Builder setLastMessageTimestamp(Long lastMessageTimestamp) {
      this.lastMessageTimestamp = lastMessageTimestamp;
      return this;
    }

    public Builder setMessageIds(List<String> messageIds) {
      this.messageIds = messageIds;
      return this;
    }

    public Builder setMessageTypes(List<Integer> messageTypes) {
      this.messageTypes = messageTypes;
      return this;
    }

    public Builder setCustomTypes(List<String> customTypes) {
      this.customTypes = customTypes;
      return this;
    }

    public Builder setSenderIds(List<String> senderIds) {
      this.senderIds = senderIds;
      return this;
    }

    public Builder setSenderIdsExclusive(Boolean senderIdsExclusive) {
      this.senderIdsExclusive = senderIdsExclusive;
      return this;
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream group whose messages count are to be fetched
     * @return the
     * FetchMessagesCountQuery.Builder{@link Builder}
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
     * @param userToken the token of the user fetching messages count in a stream group
     * @return the
     * FetchMessagesCountQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build fetch messages count query.
     *
     * @return the FetchMessagesCountQuery{@link FetchMessagesCountQuery} instance
     * @see FetchMessagesCountQuery
     */
    public FetchMessagesCountQuery build() {
      return new FetchMessagesCountQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group whose messages are to be fetched
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets user token.
   *
   * @return the token of the user fetching messages count in a stream group
   */
  public String getUserToken() {
    return userToken;
  }

  public Long getLastMessageTimestamp() {
    return lastMessageTimestamp;
  }

  public List<String> getMessageIds() {
    return messageIds;
  }

  public List<Integer> getMessageTypes() {
    return messageTypes;
  }

  public List<String> getCustomTypes() {
    return customTypes;
  }

  public List<String> getSenderIds() {
    return senderIds;
  }

  public Boolean getSenderIdsExclusive() {
    return senderIdsExclusive;
  }
}
