package io.isometrik.gs.builder.restream;

/**
 * Query builder class for creating the request for deleting a restream channel for a user.
 */
public class DeleteRestreamChannelQuery {

  private final String channelId;
  private final String userToken;

  private DeleteRestreamChannelQuery(Builder builder) {
    this.channelId = builder.channelId;
    this.userToken = builder.userToken;
  }

  public static class Builder {
    private String channelId;
    private String userToken;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token for which to delete restream channel
     * @return the
     * DeleteRestreamChannelQuery.Builder{@link
     * Builder}
     * instance
     * @see Builder
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Sets channelId.
     *
     * @param channelId the id of the restream channel to be deleted
     * @return the
     * DeleteRestreamChannelQuery.Builder{@link
     * Builder}
     * instance
     * @see Builder
     */
    public Builder setChannelId(String channelId) {
      this.channelId = channelId;
      return this;
    }

    /**
     * Build delete restream channel query.
     *
     * @return the
     * DeleteRestreamChannelQuery{@link
     * DeleteRestreamChannelQuery}
     * instance
     * @see DeleteRestreamChannelQuery
     */
    public DeleteRestreamChannelQuery build() {
      return new DeleteRestreamChannelQuery(this);
    }
  }

  /**
   * Returns the id of the restream channel to be deleted.
   *
   * @return channelId the id of the restream channel to be deleted
   */
  public String getChannelId() {
    return channelId;
  }

  /**
   * Gets user token.
   *
   * @return the token of the user for which to delete restream channel
   */
  public String getUserToken() {
    return userToken;
  }
}
