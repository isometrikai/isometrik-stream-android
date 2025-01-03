package io.isometrik.gs.builder.ecommerce.featuring;

/**
 * Query builder class for creating the request to fetch featuring product in a streaming session.
 */
public class FetchFeaturingProductQuery {

  private final String streamId;
  private final String userToken;
  private FetchFeaturingProductQuery(Builder builder) {

    this.streamId = builder.streamId;
    this.userToken = builder.userToken;
  }

  /**
   * The Builder class for the FetchFeaturingProductQuery.
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
     * Sets user token.
     *
     * @param userToken the token of the user who is fetching featuring product in a stream
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }


    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream, whose featuring product is to be fetched
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Build fetch featuring product query.
     *
     * @return the FetchFeaturingProductQuery{@link FetchFeaturingProductQuery} instance
     * @see FetchFeaturingProductQuery
     */
    public FetchFeaturingProductQuery build() {
      return new FetchFeaturingProductQuery(this);
    }
  }



  /**
   * Gets stream id.
   *
   * @return the id of the stream, whose featuring product is to be fetched
   */
  public String getStreamId() {
    return streamId;
  }
  /**
   * Gets user token.
   *
   * @return the token of the user who is fetching featuring product in a stream
   */
  public String getUserToken() {
    return userToken;
  }
}
