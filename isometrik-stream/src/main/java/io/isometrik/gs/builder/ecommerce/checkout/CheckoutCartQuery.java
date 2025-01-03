package io.isometrik.gs.builder.ecommerce.checkout;

/**
 * Query builder class for creating the request to checkout cart.
 */
public class CheckoutCartQuery {

  private final String streamId;
  private final String userToken;

  private CheckoutCartQuery(Builder builder) {

    this.streamId = builder.streamId;
    this.userToken = builder.userToken;
  }

  /**
   * The Builder class for the CheckoutCartQuery.
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
     * @param userToken the token of the user who is checking out products added to cart in a stream
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
     * @param streamId the id of the stream in which products were added to cart
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Build checkout cart query.
     *
     * @return the CheckoutCartQuery{@link CheckoutCartQuery} instance
     * @see CheckoutCartQuery
     */
    public CheckoutCartQuery build() {
      return new CheckoutCartQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream in which products were added to cart
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets user token.
   *
   * @return the token of the user who is checking out products added to cart in a stream
   */
  public String getUserToken() {
    return userToken;
  }
}
