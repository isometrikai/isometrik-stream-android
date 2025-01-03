package io.isometrik.gs.builder.restream;

/**
 * Query builder class for creating the request for fetching restream channels for a user.
 */
public class FetchRestreamChannelsQuery {

  private final String userToken;

  private FetchRestreamChannelsQuery(Builder builder) {

    this.userToken = builder.userToken;
  }

  public static class Builder {
    private String userToken;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token for which to fetch restream channels
     * @return the FetchRestreamChannelsQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build fetch restream channels query.
     *
     * @return the FetchRestreamChannelsQuery{@link FetchRestreamChannelsQuery}
     * instance
     * @see FetchRestreamChannelsQuery
     */
    public FetchRestreamChannelsQuery build() {
      return new FetchRestreamChannelsQuery(this);
    }
  }

  /**
   * Gets user token.
   *
   * @return the token of the user for which to fetch restream channels
   */
  public String getUserToken() {
    return userToken;
  }
}
