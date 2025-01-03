package io.isometrik.gs.builder.ecommerce.products;

import org.json.JSONObject;

/**
 * Query builder class for creating the request to add product.
 */
public class AddProductQuery {

  private final String externalProductId;
  private final String productName;
  private final Integer count;
  private final JSONObject metadata;

  private AddProductQuery(Builder builder) {
    this.externalProductId = builder.externalProductId;
    this.productName = builder.productName;
    this.count = builder.count;
    this.metadata = builder.metadata;
  }

  /**
   * The Builder class for the AddProductQuery.
   */
  public static class Builder {
    private String externalProductId;
    private String productName;
    private Integer count;
    private JSONObject metadata;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets external product id.
     *
     * @param externalProductId the external id of the product to be added
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setExternalProductId(String externalProductId) {
      this.externalProductId = externalProductId;
      return this;
    }

    /**
     * Sets product name.
     *
     * @param productName the name of the product to be added
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setProductName(String productName) {
      this.productName = productName;
      return this;
    }

    /**
     * Sets count.
     *
     * @param count the quantity of the product to be added
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setCount(Integer count) {
      this.count = count;
      return this;
    }

    /**
     * Sets metadata.
     *
     * @param metadata the custom data of the product to be added
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setMetadata(JSONObject metadata) {
      this.metadata = metadata;
      return this;
    }

    /**
     * Build add product query.
     *
     * @return the AddProductQuery{@link AddProductQuery} instance
     * @see AddProductQuery
     */
    public AddProductQuery build() {
      return new AddProductQuery(this);
    }
  }

  /**
   * Gets external product id.
   *
   * @return the external id of the product to be added
   */
  public String getExternalProductId() {
    return externalProductId;
  }

  /**
   * Gets product name.
   *
   * @return the name of the product to be added
   */
  public String getProductName() {
    return productName;
  }

  /**
   * Gets count.
   *
   * @return the quantity of the product to be added
   */
  public Integer getCount() {
    return count;
  }

  /**
   * Gets metadata.
   *
   * @return the custom data of the product to be added
   */
  public JSONObject getMetadata() {
    return metadata;
  }
}
