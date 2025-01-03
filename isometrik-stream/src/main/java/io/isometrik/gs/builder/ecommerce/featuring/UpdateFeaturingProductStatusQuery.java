package io.isometrik.gs.builder.ecommerce.featuring;

/**
 * Query builder class for creating the request to update featuring product status.
 */
public class UpdateFeaturingProductStatusQuery {
  private final String productId;
  private final Boolean startFeaturing;
  private final String streamId;
  private final String userToken;

  private UpdateFeaturingProductStatusQuery(Builder builder) {
    this.productId = builder.productId;
    this.startFeaturing = builder.startFeaturing;
    this.streamId = builder.streamId;
    this.userToken = builder.userToken;
  }

  /**
   * The Builder class for the UpdateFeaturingProductStatusQuery.
   */
  public static class Builder {
    private String productId;
    private Boolean startFeaturing;
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
     * @param userToken the token of the user who is updating featuring product status in a stream
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
     * @param streamId the id of the stream, whose featuring product status is to be updated
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Sets start featuring.
     *
     * @param startFeaturing whether to start or stop featuring a product in broadcast
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setStartFeaturing(Boolean startFeaturing) {
      this.startFeaturing = startFeaturing;
      return this;
    }

    /**
     * Sets product id.
     *
     * @param productId the id of the product whose featuring status is being updated
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setProductId(String productId) {
      this.productId = productId;
      return this;
    }

    /**
     * Build update featuring product status query.
     *
     * @return the UpdateFeaturingProductStatusQuery{@link UpdateFeaturingProductStatusQuery} instance
     * @see UpdateFeaturingProductStatusQuery
     */
    public UpdateFeaturingProductStatusQuery build() {
      return new UpdateFeaturingProductStatusQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream, whose featuring product status is to be updated
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets product id.
   *
   * @return the id of the product whose featuring status is being updated
   */
  public String getProductId() {
    return productId;
  }

  /**
   * Gets start featuring.
   *
   * @return whether to start or stop featuring a product in broadcast
   */
  public Boolean getStartFeaturing() {
    return startFeaturing;
  }

  /**
   * Gets user token.
   *
   * @return the token of the user who is updating featuring product status in a stream
   */
  public String getUserToken() {
    return userToken;
  }
}
