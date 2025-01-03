package io.isometrik.gs.builder.copublish;

/**
 * Query builder class for deleting a copublish request in a stream group.
 */
public class DeleteCopublishRequestQuery {
  private final String streamId;
  private final String userToken;

  private DeleteCopublishRequestQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.userToken = builder.userToken;
  }

  /**
   * The Builder class for the DeleteCopublishRequestQuery.
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
     * Sets stream id.
     *
     * @param streamId the id of the stream for which to delete copublish request
     * @return the
     * Builder{@link
     * Builder}
     * instance
     * @see Builder
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the token of the user whose copublish request is to be deleted for the
     * stream
     * group
     * @return the
     * Builder{@link
     * Builder}
     * instance
     * @see Builder
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build delete copublish request query.
     *
     * @return the
     * DeleteCopublishRequestQuery{@link
     * DeleteCopublishRequestQuery}
     * instance
     * @see DeleteCopublishRequestQuery
     */
    public DeleteCopublishRequestQuery build() {
      return new DeleteCopublishRequestQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream for which to delete copublish request
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets user token.
   *
   * @return the token of the user whose copublish request is to be deleted for the stream group
   */
  public String getUserToken() {
    return userToken;
  }
}