package io.isometrik.gs.builder.ecommerce.products;

/**
 * Query builder class for creating the request to fetch product details.
 */
public class FetchProductDetailsQuery {
  private final String productId;

  private FetchProductDetailsQuery(Builder builder) {
    this.productId = builder.productId;
  }

  /**
   * The Builder class for the FetchProductDetailsQuery.
   */
  public static class Builder {
    private String productId;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
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
     * @return the FetchProductDetailsQuery{@link FetchProductDetailsQuery} instance
     * @see FetchProductDetailsQuery
     */
    public FetchProductDetailsQuery build() {
      return new FetchProductDetailsQuery(this);
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

}
