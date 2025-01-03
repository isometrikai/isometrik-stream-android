package io.isometrik.gs.builder.subscription;

/**
 * Query builder class for creating the request for removing subscription for stream start or stop
 * events for a user.
 */
public class RemoveSubscriptionQuery {

  private final Boolean streamStartChannel;
  private final String userToken;

  private RemoveSubscriptionQuery(Builder builder) {
    this.streamStartChannel = builder.streamStartChannel;
    this.userToken = builder.userToken;
  }

  /**
   * The Builder class for the RemoveSubscriptionQuery.
   */
  public static class Builder {
    private Boolean streamStartChannel;
    private String userToken;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets stream start channel.
     *
     * @param streamStartChannel the type of presence event to unsubscribe from-stream start or
     * stream stop event
     * @return the
     * RemoveSubscriptionQuery.Builder{@link
     * Builder} instance
     * @see Builder
     */
    public Builder setStreamStartChannel(Boolean streamStartChannel) {
      this.streamStartChannel = streamStartChannel;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the token of the user for whom to remove the subscription for stream
     * presence
     * events
     * @return the
     * RemoveSubscriptionQuery.Builder{@link
     * Builder} instance
     * @see Builder
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build remove subscription query.
     *
     * @return the
     * RemoveSubscriptionQuery{@link RemoveSubscriptionQuery}
     * instance
     * @see RemoveSubscriptionQuery
     */
    public RemoveSubscriptionQuery build() {
      return new RemoveSubscriptionQuery(this);
    }
  }

  /**
   * Gets stream start event.
   *
   * @return the type of presence event to unsubscribe from-stream start or stream stop event
   */
  public Boolean getStreamStartChannel() {
    return streamStartChannel;
  }

  /**
   * Gets user token.
   *
   * @return the token of the user for whom to remove the subscription for stream presence events
   */
  public String getUserToken() {
    return userToken;
  }
}
