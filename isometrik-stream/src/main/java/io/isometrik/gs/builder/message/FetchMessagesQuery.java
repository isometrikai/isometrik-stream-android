package io.isometrik.gs.builder.message;

import java.util.List;

/**
 * Query builder class for creating the request for fetching messages sent in a stream group.
 */
public class FetchMessagesQuery {

  private final String streamId;
  private final String userToken;
  private final Integer sort;
  private final Integer limit;
  private final Integer skip;
  private final Long lastMessageTimestamp;
  private final List<String> messageIds;
  private final List<Integer> messageTypes;
  private final List<String> customTypes;
  private final List<String> senderIds;
  private final String searchTag;
  private final Boolean senderIdsExclusive;

  private FetchMessagesQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.userToken = builder.userToken;
    this.sort = builder.sort;
    this.limit = builder.limit;
    this.skip = builder.skip;
    this.lastMessageTimestamp = builder.lastMessageTimestamp;
    this.messageIds = builder.messageIds;
    this.messageTypes = builder.messageTypes;
    this.customTypes = builder.customTypes;
    this.senderIds = builder.senderIds;
    this.searchTag = builder.searchTag;
    this.senderIdsExclusive = builder.senderIdsExclusive;
  }

  /**
   * The Builder class for the FetchMessagesQuery.
   */
  public static class Builder {
    private String streamId;
    private String userToken;
    private Integer sort;
    private Integer limit;
    private Integer skip;
    private Long lastMessageTimestamp;
    private List<String> messageIds;
    private List<Integer> messageTypes;
    private List<String> customTypes;
    private List<String> senderIds;
    private String searchTag;
    private Boolean senderIdsExclusive;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    public Builder setSort(Integer sort) {
      this.sort = sort;
      return this;
    }

    public Builder setSkip(Integer skip) {
      this.skip = skip;
      return this;
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

    public Builder setSearchTag(String searchTag) {
      this.searchTag = searchTag;
      return this;
    }

    public Builder setSenderIdsExclusive(Boolean senderIdsExclusive) {
      this.senderIdsExclusive = senderIdsExclusive;
      return this;
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream group whose messages are to be fetched
     * @return the
     * FetchMessagesQuery.Builder{@link Builder}
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
     * @param userToken the token of the user fetching messages in a stream group
     * @return the
     * FetchMessagesQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Sets limit.
     *
     * @param limit the number of messages to be fetched in a stream group
     * @return the
     * FetchMessagesQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setLimit(Integer limit) {
      this.limit = limit;
      return this;
    }

    /**
     * Build fetch messages query.
     *
     * @return the FetchMessagesQuery{@link FetchMessagesQuery} instance
     * @see FetchMessagesQuery
     */
    public FetchMessagesQuery build() {
      return new FetchMessagesQuery(this);
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
   * Gets limit.
   *
   * @return the number of messages to be fetched in a stream group
   */
  public Integer getLimit() {
    return limit;
  }

  /**
   * Gets user token.
   *
   * @return the token of the user fetching messages in a stream group
   */
  public String getUserToken() {
    return userToken;
  }

  public Integer getSort() {
    return sort;
  }

  public Integer getSkip() {
    return skip;
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

  public String getSearchTag() {
    return searchTag;
  }

  public Boolean getSenderIdsExclusive() {
    return senderIdsExclusive;
  }
}
