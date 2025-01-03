package io.isometrik.gs.builder.message;

/**
 * Query builder class for creating the request for deleting messages sent, from a stream group.
 */
public class DeleteMessageQuery {

  private final String streamId;
  private final String messageId;
  private final String userToken;

  private DeleteMessageQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.messageId = builder.messageId;
    this.userToken = builder.userToken;
  }

  /**
   * The Builder class for the DeleteMessageQuery.
   */
  public static class Builder {
    private String streamId;
    private String messageId;
    private String userToken;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream group from which to delete a message
     * @return the
     * DeleteMessageQuery.Builder{@link Builder}
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
     * @param userToken the token of the moderator who is deleting the message
     * @return the
     * DeleteMessageQuery.Builder{@link Builder}
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
     * @param messageId the id of the message to be removed
     * @return the
     * DeleteMessageQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setMessageId(String messageId) {
      this.messageId = messageId;
      return this;
    }

    /**
     * Build delete message query.
     *
     * @return the DeleteMessageQuery{@link DeleteMessageQuery} instance
     * @see DeleteMessageQuery
     */
    public DeleteMessageQuery build() {
      return new DeleteMessageQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group from which to delete a message
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets message id.
   *
   * @return the id of the message to be removed
   */
  public String getMessageId() {
    return messageId;
  }

  /**
   * Gets user token.
   *
   * @return the token of the moderator who is deleting the message
   */
  public String getUserToken() {
    return userToken;
  }
}