package io.isometrik.gs.builder.ecommerce.checkout;

/**
 * Query builder class for creating the request to verify checkout.
 */
public class VerifyCheckoutQuery {
  private final String checkoutSecret;
  private final Integer skip;
  private final Integer limit;

  private VerifyCheckoutQuery(Builder builder) {
    this.checkoutSecret = builder.checkoutSecret;
    this.skip = builder.skip;
    this.limit = builder.limit;
  }

  /**
   * The Builder class for the VerifyCheckoutQuery.
   */
  public static class Builder {
    private String checkoutSecret;
    private Integer skip;
    private Integer limit;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    public Builder setSkip(Integer skip) {
      this.skip = skip;
      return this;
    }

    public Builder setLimit(Integer limit) {
      this.limit = limit;
      return this;
    }

    /**
     * Sets checkout secret.
     *
     * @param checkoutSecret the checkout secret to be used for verifying checkout request and
     * fetching cart products
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setCheckoutSecret(String checkoutSecret) {
      this.checkoutSecret = checkoutSecret;
      return this;
    }

    /**
     * Build verify checkout query.
     *
     * @return the VerifyCheckoutQuery{@link VerifyCheckoutQuery} instance
     * @see VerifyCheckoutQuery
     */
    public VerifyCheckoutQuery build() {
      return new VerifyCheckoutQuery(this);
    }
  }

  /**
   * Gets checkout secret.
   *
   * @return the checkout secret to be used for verifying checkout request and
   * fetching cart products
   */
  public String getCheckoutSecret() {
    return checkoutSecret;
  }

  public Integer getSkip() {
    return skip;
  }

  public Integer getLimit() {
    return limit;
  }
}
