package io.isometrik.gs.builder.copublish;

/**
 * Query builder class for fetching status of a copublish request in a stream group.
 */
public class FetchCopublishRequestStatusQuery {
  private final String streamId;
  private final String userToken;

  private FetchCopublishRequestStatusQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.userToken = builder.userToken;
  }

  /**
   * The Builder class for the FetchCopublishRequestStatusQuery.
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
     * @param streamId the id of the stream for which to fetch status of a copublish request
     * @return the
     * Builder{@link
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
     * @param userToken the token of the user whose copublish request's status is to be fetched for
     * the
     * stream group
     * group
     * @return the
     * Builder{@link
     * Builder}
     * instance
     * @see Builder
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build fetch status of the copublish request query.
     *
     * @return the
     * FetchCopublishRequestStatusQuery{@link
     * FetchCopublishRequestStatusQuery}
     * instance
     * @see FetchCopublishRequestStatusQuery
     */
    public FetchCopublishRequestStatusQuery build() {
      return new FetchCopublishRequestStatusQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream for which to fetch status of a copublish request
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets user token.
   *
   * @return the token of the user whose copublish request's status is to be fetched for the stream group
   */
  public String getUserToken() {
    return userToken;
  }
}
