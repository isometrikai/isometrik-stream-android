package io.isometrik.gs.builder.ecommerce.session;

/**
 * Query builder class for creating the request to fetch linked products to a streaming session.
 */
public class FetchLinkedProductsQuery {

  private final Integer skip;
  private final Integer limit;
  private final String streamId;
  private final String userToken;

  private FetchLinkedProductsQuery(Builder builder) {
    this.skip = builder.skip;
    this.limit = builder.limit;
    this.streamId = builder.streamId;
    this.userToken = builder.userToken;
  }

  /**
   * The Builder class for the FetchLinkedProductsQuery.
   */
  public static class Builder {
    private Integer skip;
    private Integer limit;
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
     * @param userToken the token of the user who is fetching linked products to a stream
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
     * @param streamId the id of the stream, whose linked products are to be fetched
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Sets skip.
     *
     * @param skip the number of products to be skipped before fetching products linked to stream
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setSkip(Integer skip) {
      this.skip = skip;
      return this;
    }

    /**
     * Sets limit.
     *
     * @param limit the number of products linked to stream to be fetched
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setLimit(Integer limit) {
      this.limit = limit;
      return this;
    }

    /**
     * Build fetch linked products query.
     *
     * @return the FetchLinkedProductsQuery{@link FetchLinkedProductsQuery} instance
     * @see FetchLinkedProductsQuery
     */
    public FetchLinkedProductsQuery build() {
      return new FetchLinkedProductsQuery(this);
    }
  }

  /**
   * Gets skip.
   *
   * @return the number of products to be skipped before fetching products linked to stream
   */
  public Integer getSkip() {
    return skip;
  }

  /**
   * Gets limit.
   *
   * @return the number of products linked to stream to be fetched
   */
  public Integer getLimit() {
    return limit;
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream, whose linked products are to be fetched
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets user token.
   *
   * @return the token of the user who is fetching linked products to a stream
   */
  public String getUserToken() {
    return userToken;
  }
}
