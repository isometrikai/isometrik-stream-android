package io.isometrik.gs.builder.ecommerce.products;

/**
 * Query builder class for creating the request to update product inventory.
 */
public class UpdateProductInventoryQuery {
  private final String productId;
  private final Integer count;
  private UpdateProductInventoryQuery(Builder builder) {
    this.productId = builder.productId;
    this.count = builder.count;
  }

  /**
   * The Builder class for the UpdateProductInventoryQuery.
   */
  public static class Builder {
    private String productId;
    private Integer count;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets count.
     *
     * @param count the new inventory of the product to be updated
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setCount(Integer count) {
      this.count = count;
      return this;
    }

    /**
     * Sets product id.
     *
     * @param productId the id of the product whose inventory is to be updated
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setProductId(String productId) {
      this.productId = productId;
      return this;
    }

    /**
     * Build update product inventory query.
     *
     * @return the UpdateProductInventoryQuery{@link UpdateProductInventoryQuery} instance
     * @see UpdateProductInventoryQuery
     */
    public UpdateProductInventoryQuery build() {
      return new UpdateProductInventoryQuery(this);
    }
  }

  /**
   * Gets product id.
   *
   * @return the id of the product whose inventory is to be updated
   */
  public String getProductId() {
    return productId;
  }

  /**
   * Gets count.
   *
   * @return the new inventory of the product to be updated
   */
  public Integer getCount() {
    return count;
  }


}
