package io.isometrik.gs.builder.ecommerce.cart;

/**
 * Query builder class for creating the request to add product to cart.
 */
public class AddProductToCartQuery {

  private final String streamId;
  private final String productId;
  private final Integer quantity;
  private final String userToken;

  private AddProductToCartQuery(Builder builder) {

    this.streamId = builder.streamId;
    this.productId = builder.productId;
    this.quantity = builder.quantity;
    this.userToken = builder.userToken;
  }

  /**
   * The Builder class for the AddProductToCartQuery.
   */
  public static class Builder {

    private String streamId;
    private String productId;
    private Integer quantity;
    private String userToken;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets user token.
     *
     * @param userToken the token of the user for whom to add product to cart
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
     * @param streamId the id of the stream in which to add product to cart
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
     * @param productId the id of the product to be added to cart
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setProductId(String productId) {
      this.productId = productId;
      return this;
    }

    /**
     * Sets quantity.
     *
     * @param quantity the units of the product to be added product to cart
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setQuantity(Integer quantity) {
      this.quantity = quantity;
      return this;
    }

    /**
     * Build add product to cart query.
     *
     * @return the AddProductToCartQuery{@link AddProductToCartQuery} instance
     * @see AddProductToCartQuery
     */
    public AddProductToCartQuery build() {
      return new AddProductToCartQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream in which to add product to cart
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets product id.
   *
   * @return the id of the product to be added to cart
   */
  public String getProductId() {
    return productId;
  }

  /**
   * Gets quantity.
   *
   * @return the units of the product to be added product to cart
   */
  public Integer getQuantity() {
    return quantity;
  }

  /**
   * Gets user token.
   *
   * @return the token of the user for whom to add product to cart
   */
  public String getUserToken() {
    return userToken;
  }
}
