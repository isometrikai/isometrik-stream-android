package io.isometrik.gs.builder.ecommerce.cart;

/**
 * Query builder class for creating the request to remove product from cart.
 */
public class RemoveProductFromCartQuery {
  private final String streamId;
  private final String productId;
  private final String userToken;
  private RemoveProductFromCartQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.productId = builder.productId;
    this.userToken = builder.userToken;
  }

  /**
   * The Builder class for the RemoveProductFromCartQuery.
   */
  public static class Builder {
    private String streamId;
    private String productId;
    private String userToken;
    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets user token.
     *
     * @param userToken the token of the user for which to remove product from cart
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
     * @param streamId the id of the stream in which to remove product from cart
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
     * @param productId the id of the product to be removed from cart
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setProductId(String productId) {
      this.productId = productId;
      return this;
    }

    /**
     * Build remove product from cart query.
     *
     * @return the RemoveProductFromCartQuery{@link RemoveProductFromCartQuery} instance
     * @see RemoveProductFromCartQuery
     */
    public RemoveProductFromCartQuery build() {
      return new RemoveProductFromCartQuery(this);
    }
  }


  /**
   * Gets stream id.
   *
   * @return the id of the stream in which to remove product from cart
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets product id.
   *
   * @return the id of the product to be removed from cart
   */
  public String getProductId() {
    return productId;
  }
  /**
   * Gets user token.
   *
   * @return the token of the user for which to remove product from cart
   */
  public String getUserToken() {
    return userToken;
  }
}
