package io.isometrik.gs.builder.subscription;

/**
 * Query builder class for creating the request for add subscription for fetching stream start and
 * stop event s for a user.
 */
public class AddSubscriptionQuery {

  private final Boolean streamStartChannel;
  private final String userToken;
  private AddSubscriptionQuery(Builder builder) {
    this.streamStartChannel = builder.streamStartChannel;
    this.userToken = builder.userToken;
  }

  /**
   * The Builder class for the AddSubscriptionQuery.
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
     * @param streamStartChannel the type of stream presence event for which to subscribe for-stream
     * start or stream stop event
     * @return the AddSubscriptionQuery.Builder{@link Builder} instance
     * @see Builder
     */
    public Builder setStreamStartChannel(Boolean streamStartChannel) {
      this.streamStartChannel = streamStartChannel;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the token of the user for whom to add the subscription for stream presence events
     * @return the AddSubscriptionQuery.Builder{@link Builder} instance
     * @see Builder
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build add subscription query.
     *
     * @return the AddSubscriptionQuery{@link AddSubscriptionQuery} instance
     * @see AddSubscriptionQuery
     */
    public AddSubscriptionQuery build() {
      return new AddSubscriptionQuery(this);
    }
  }

  /**
   * Gets stream start event.
   *
   * @return the type of stream presence event for which to subscribe for-stream start or stream stop event
   */
  public Boolean getStreamStartChannel() {
    return streamStartChannel;
  }


  /**
   * Gets user token.
   *
   * @return the token of the user for whom to add the subscription for stream presence events
   */
  public String getUserToken() {
    return userToken;
  }
}
