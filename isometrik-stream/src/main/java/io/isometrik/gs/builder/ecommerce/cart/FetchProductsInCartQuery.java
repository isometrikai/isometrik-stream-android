package io.isometrik.gs.builder.ecommerce.cart;

/**
 * Query builder class for creating the request to fetch products in cart.
 */
public class FetchProductsInCartQuery {

  private final Integer skip;
  private final Integer limit;
  private final String streamId;
  private final String userToken;

  private FetchProductsInCartQuery(Builder builder) {
    this.skip = builder.skip;
    this.limit = builder.limit;
    this.streamId = builder.streamId;
    this.userToken = builder.userToken;
  }

  /**
   * The Builder class for the FetchProductsInCartQuery.
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
     * @param userToken the token of the user whose products in cart for a stream to be fetched
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
     * @param streamId the id of the stream for which products in cart for a user to be fetched
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
     * @param skip the number of products to be skipped before fetching products in cart
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
     * @param limit the number of products in cart to be fetched
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setLimit(Integer limit) {
      this.limit = limit;
      return this;
    }

    /**
     * Build fetch products in cart query.
     *
     * @return the FetchProductsInCartQuery{@link FetchProductsInCartQuery} instance
     * @see FetchProductsInCartQuery
     */
    public FetchProductsInCartQuery build() {
      return new FetchProductsInCartQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream for which products in cart for a user to be fetched
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets skip.
   *
   * @return the number of products to be skipped before fetching products in cart
   */
  public Integer getSkip() {
    return skip;
  }

  /**
   * Gets limit.
   *
   * @return the number of products in cart to be fetched
   */
  public Integer getLimit() {
    return limit;
  }

  /**
   * Gets user token.
   *
   * @return the token of the user whose products in cart for a stream to be fetched
   */
  public String getUserToken() {
    return userToken;
  }
}
