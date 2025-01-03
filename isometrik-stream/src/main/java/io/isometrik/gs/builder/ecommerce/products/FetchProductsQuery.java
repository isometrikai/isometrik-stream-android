package io.isometrik.gs.builder.ecommerce.products;

/**
 * Query builder class for creating the request to fetch products.
 */
public class FetchProductsQuery {

  private final Integer skip;
  private final Integer limit;

  private FetchProductsQuery(Builder builder) {
    this.skip = builder.skip;
    this.limit = builder.limit;
  }

  /**
   * The Builder class for the FetchProductsQuery.
   */
  public static class Builder {
    private Integer skip;
    private Integer limit;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets skip.
     *
     * @param skip the number of products to be skipped before fetching products
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
     * @param limit the number of products to be fetched
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setLimit(Integer limit) {
      this.limit = limit;
      return this;
    }

    /**
     * Build fetch products query.
     *
     * @return the FetchProductsQuery{@link FetchProductsQuery} instance
     * @see FetchProductsQuery
     */
    public FetchProductsQuery build() {
      return new FetchProductsQuery(this);
    }
  }

  /**
   * Gets skip.
   *
   * @return the number of products to be skipped before fetching products
   */
  public Integer getSkip() {
    return skip;
  }

  /**
   * Gets limit.
   *
   * @return the number of products to be fetched
   */
  public Integer getLimit() {
    return limit;
  }
}
