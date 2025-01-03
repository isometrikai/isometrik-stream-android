package io.isometrik.gs.builder.message;

/**
 * Query builder class for creating the request for deleting message reply, from a stream group.
 */
public class DeleteMessageReplyQuery {

  private final String streamId;
  private final String messageId;
  private final String parentMessageId;
  private final String userToken;

  private DeleteMessageReplyQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.messageId = builder.messageId;
    this.parentMessageId = builder.parentMessageId;
    this.userToken = builder.userToken;
  }

  /**
   * The Builder class for the DeleteMessageReplyQuery.
   */
  public static class Builder {
    private String streamId;
    private String messageId;
    private String parentMessageId;
    private String userToken;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream group from which to delete a message reply
     * @return the DeleteMessageReplyQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Sets moderator token.
     *
     * @param userToken the token of the moderator who is deleting the message reply
     * @return the DeleteMessageReplyQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Sets message id.
     *
     * @param messageId the id of the message reply to be removed
     * @return the DeleteMessageReplyQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setMessageId(String messageId) {
      this.messageId = messageId;
      return this;
    }

    /**
     * Sets parent message id.
     *
     * @param parentMessageId the id of the parent message whose reply is to be removed
     * @return the DeleteMessageReplyQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setParentMessageId(String parentMessageId) {
      this.parentMessageId = parentMessageId;
      return this;
    }

    /**
     * Build delete message reply query.
     *
     * @return the DeleteMessageReplyQuery{@link DeleteMessageReplyQuery} instance
     * @see DeleteMessageReplyQuery
     */
    public DeleteMessageReplyQuery build() {
      return new DeleteMessageReplyQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group from which to delete a message reply
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets message id.
   *
   * @return the id of the message reply to be removed
   */
  public String getMessageId() {
    return messageId;
  }

  /**
   * Gets parent message id.
   *
   * @return the id of the parent message whose reply is to be removed
   */
  public String getParentMessageId() {
    return parentMessageId;
  }

  /**
   * Gets user token.
   *
   * @return the token of the moderator who is deleting the message reply
   */
  public String getUserToken() {
    return userToken;
  }
}