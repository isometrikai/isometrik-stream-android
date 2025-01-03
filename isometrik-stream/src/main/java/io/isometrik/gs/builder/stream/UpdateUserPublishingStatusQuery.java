package io.isometrik.gs.builder.stream;

/**
 * Query builder class for creating the request for updating publishing status of a user for all of
 * the stream groups.
 */
public class UpdateUserPublishingStatusQuery {

  private final String userToken;

  private UpdateUserPublishingStatusQuery(Builder builder) {

    this.userToken = builder.userToken;
  }

  /**
   * The Builder class for the UpdateUserPublishingStatusQuery.
   */
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
     * @param userToken the token of the user whose publishing status is to be updated on app
     * restart
     * @return the
     * UpdateUserPublishingStatusQuery.Builder{@link
     * Builder}
     * instance
     * @see Builder
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build update user publishing status query.
     *
     * @return the
     * UpdateUserPublishingStatusQuery{@link
     * UpdateUserPublishingStatusQuery}
     * instance
     * @see UpdateUserPublishingStatusQuery
     */
    public UpdateUserPublishingStatusQuery build() {
      return new UpdateUserPublishingStatusQuery(this);
    }
  }

  /**
   * Gets user token.
   *
   * @return the token of the user whose publishing status is to be updated on app restart
   */
  public String getUserToken() {
    return userToken;
  }
}
