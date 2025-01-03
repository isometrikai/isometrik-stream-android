package io.isometrik.gs.builder.viewer;

/**
 * Query builder class for creating the request for removing a viewer from a stream group.
 */
public class RemoveViewerQuery {

  private final String streamId;
  private final String viewerId;
  private final String userToken;

  private RemoveViewerQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.viewerId = builder.viewerId;
    this.userToken = builder.userToken;
  }

  /**
   * The Builder class for the RemoveViewerQuery.
   */
  public static class Builder {
    private String streamId;
    private String userToken;
    private String viewerId;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream group,from which to remove the viewer
     * @return the RemoveViewerQuery.Builder{@link Builder}
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
     * @param userToken the token of the user who is removing a viewer from the stream group
     * @return the RemoveViewerQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Sets viewer id.
     *
     * @param viewerId the id of the viewer who is being removed from a stream group
     * @return the RemoveViewerQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setViewerId(String viewerId) {
      this.viewerId = viewerId;
      return this;
    }

    /**
     * Build remove viewer query.
     *
     * @return the RemoveViewerQuery{@link RemoveViewerQuery} instance
     * @see RemoveViewerQuery
     */
    public RemoveViewerQuery build() {
      return new RemoveViewerQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group,from which to remove the viewer
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets viewer id.
   *
   * @return the id of the viewer who is being removed from a stream group
   */
  public String getViewerId() {
    return viewerId;
  }

  /**
   * Gets user token.
   *
   * @return the token of the user removing a viewer from the stream group
   */
  public String getUserToken() {
    return userToken;
  }
}
