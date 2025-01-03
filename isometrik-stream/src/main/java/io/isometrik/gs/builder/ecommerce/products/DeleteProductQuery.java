package io.isometrik.gs.builder.ecommerce.products;

/**
 * Query builder class for creating the request to delete product.
 */
public class DeleteProductQuery {
  private final String productId;
  private DeleteProductQuery(Builder builder) {
    this.productId = builder.productId;
  }

  /**
   * The Builder class for the DeleteProductQuery.
   */
  public static class Builder {
    private  String productId;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }
    /**
     * Sets product id.
     *
     * @param productId the id of the product to be deleted
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setProductId(String productId) {
      this.productId = productId;
      return this;
    }

    /**
     * Build delete product query.
     *
     * @return the DeleteProductQuery{@link DeleteProductQuery} instance
     * @see DeleteProductQuery
     */
    public DeleteProductQuery build() {
      return new DeleteProductQuery(this);
    }
  }

  /**
   * Gets product id.
   *
   * @return the id of the product to be deleted
   */
  public String getProductId() {
    return productId;
  }

}
