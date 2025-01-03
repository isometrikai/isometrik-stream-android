package io.isometrik.gs.builder.restream;

/**
 * Query builder class for creating the request for updating all restream channels for a user.
 */
public class UpdateAllRestreamChannelsQuery {

  private final Boolean enabled;
  private final String userToken;

  private UpdateAllRestreamChannelsQuery(Builder builder) {
    this.enabled = builder.enabled;
    this.userToken = builder.userToken;
  }

  public static class Builder {

    private String userToken;
    private Boolean enabled;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets enabled.
     *
     * @param enabled whether to enable or disable all the restream channels
     * @return the UpdateAllRestreamChannelsQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setEnabled(Boolean enabled) {
      this.enabled = enabled;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token for which to update all restream channels
     * @return the UpdateAllRestreamChannelsQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build update restream channel query.
     *
     * @return the UpdateAllRestreamChannelsQuery{@link UpdateAllRestreamChannelsQuery}
     * instance
     * @see UpdateAllRestreamChannelsQuery
     */
    public UpdateAllRestreamChannelsQuery build() {
      return new UpdateAllRestreamChannelsQuery(this);
    }
  }

  /**
   * Returns whether to enable or disable all the restream channels.
   *
   * @return enabled whether to enable or disable all the restream channels
   */
  public Boolean getEnabled() {
    return enabled;
  }

  /**
   * Gets user token.
   *
   * @return the token of the user for whom to update all restream channels
   */
  public String getUserToken() {
    return userToken;
  }
}
