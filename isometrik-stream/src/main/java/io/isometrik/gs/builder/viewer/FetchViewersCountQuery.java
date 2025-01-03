package io.isometrik.gs.builder.viewer;

/**
 * Query builder class for creating the request for fetching the viewers count in a stream group.
 */
public class FetchViewersCountQuery {

  private final String streamId;
  private final String userToken;

  private FetchViewersCountQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.userToken = builder.userToken;
  }

  /**
   * The Builder class for the FetchViewersCountQuery.
   */
  public static class Builder {
    private String streamId;
    private String userToken;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream group, whose viewers count is to be fetched
     * @return the
     * FetchViewersCountQuery.Builder{@link
     * Builder}
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
     * @param userToken the token of the user who is fetching viewers count
     * @return the
     * FetchViewersCountQuery.Builder{@link
     * Builder}
     * instance
     * @see Builder
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build fetch viewers count query.
     *
     * @return the FetchViewersCountQuery{@link FetchViewersCountQuery}
     * instance
     * @see FetchViewersCountQuery
     */
    public FetchViewersCountQuery build() {
      return new FetchViewersCountQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group, whose viewers count is to be fetched
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets user token.
   *
   * @return the token of the user who is fetching viewers count
   */
  public String getUserToken() {
    return userToken;
  }
}
