package io.isometrik.gs.builder.ecommerce.session;

/**
 * Query builder class for creating the request to fetch product details and existence of product in
 * cart.
 */
public class FetchCartProductDetailsQuery {
  private final String productId;
  private final String streamId;
  private final String userToken;

  private FetchCartProductDetailsQuery(Builder builder) {
    this.productId = builder.productId;
    this.streamId = builder.streamId;
    this.userToken = builder.userToken;
  }

  /**
   * The Builder class for the FetchProductDetailsQuery.
   */
  public static class Builder {
    private String productId;
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
     * @param userToken the token of the user who is fetching linked product's details to a stream
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
     * @param streamId the id of the stream, whose linked product's details are to be fetched
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Sets product id.
     *
     * @param productId the id of the product whose details are to be fetched
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setProductId(String productId) {
      this.productId = productId;
      return this;
    }

    /**
     * Build fetch product details query.
     *
     * @return the FetchProductDetailsQuery{@link FetchCartProductDetailsQuery} instance
     * @see FetchCartProductDetailsQuery
     */
    public FetchCartProductDetailsQuery build() {
      return new FetchCartProductDetailsQuery(this);
    }
  }

  /**
   * Gets product id.
   *
   * @return the id of the product whose details are to be fetched
   */
  public String getProductId() {
    return productId;
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream, whose linked product's details are to be fetched
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets user token.
   *
   * @return the token of the user who is fetching linked product's details in a stream
   */
  public String getUserToken() {
    return userToken;
  }
}
