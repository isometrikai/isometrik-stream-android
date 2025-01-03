package io.isometrik.gs.builder.ecommerce.products;

import org.json.JSONObject;

/**
 * Query builder class for creating the request to update product.
 */
public class UpdateProductQuery {
  private final String productId;
  private final String productName;
  private final JSONObject metadata;
  private UpdateProductQuery(Builder builder) {
    this.productId = builder.productId;
    this.productName = builder.productName;
    this.metadata = builder.metadata;
  }

  /**
   * The Builder class for the UpdateProductQuery.
   */
  public static class Builder {
    private String productId;
    private String productName;
    private JSONObject metadata;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets product name.
     *
     * @param productName the new name of the product to be updated
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setProductName(String productName) {
      this.productName = productName;
      return this;
    }

    /**
     * Sets metadata.
     *
     * @param metadata the new custom data of the product to be updated
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setMetadata(JSONObject metadata) {
      this.metadata = metadata;
      return this;
    }

    /**
     * Sets product id.
     *
     * @param productId the id of the product whose details are to be updated
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setProductId(String productId) {
      this.productId = productId;
      return this;
    }

    /**
     * Build update product query.
     *
     * @return the UpdateProductQuery{@link UpdateProductQuery} instance
     * @see UpdateProductQuery
     */
    public UpdateProductQuery build() {
      return new UpdateProductQuery(this);
    }
  }

  /**
   * Gets product id.
   *
   * @return the id of the product whose details are to be updated
   */
  public String getProductId() {
    return productId;
  }

  /**
   * Gets product name.
   *
   * @return the new name of the product to be updated
   */
  public String getProductName() {
    return productName;
  }

  /**
   * Gets metadata.
   *
   * @return the new custom data of the product to be updated
   */
  public JSONObject getMetadata() {
    return metadata;
  }


}
