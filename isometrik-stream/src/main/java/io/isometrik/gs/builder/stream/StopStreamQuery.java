package io.isometrik.gs.builder.stream;

/**
 * Query builder class for creating the request for stopping a stream.
 */
public class StopStreamQuery {

  private final String streamId;
  private final String userId;
  private final String userToken;

  private StopStreamQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.userToken = builder.userToken;
    this.userId = builder.userId;
  }

  /**
   * The Builder class for the StopStreamQuery.
   */
  public static class Builder {
    private String streamId;
    private String userToken;
    private String userId;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream group to end
     * @return the StopStreamQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }
    public Builder setUserId(String userId) {
      this.userId = userId;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the token of the stream group creator, who is ending the stream group
     * @return the StopStreamQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build stop stream query.
     *
     * @return the StopStreamQuery{@link StopStreamQuery} instance
     * @see StopStreamQuery
     */
    public StopStreamQuery build() {
      return new StopStreamQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group to end
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets user token.
   *
   * @return the token of the stream group creator, who is ending the stream group
   */
  public String getUserToken() {
    return userToken;
  }

  public String getUserId() {
    return userId;
  }

}
