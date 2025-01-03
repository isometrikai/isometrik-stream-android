package io.isometrik.gs.builder.viewer;

/**
 * Query builder class for creating the request for adding a viewer to a stream group.
 */
public class AddViewerQuery {

  private final String streamId;
  private final String userToken;

  private AddViewerQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.userToken = builder.userToken;
  }

  /**
   * The Builder class for the AddViewerQuery.
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
     * @param streamId the id of the stream to be joined as viewer
     * @return the AddViewerQuery.Builder{@link Builder}
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
     * @param userToken the token of the user joining the stream group as viewer
     * @return the AddViewerQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build add viewer query.
     *
     * @return the AddViewerQuery{@link AddViewerQuery} instance
     * @see AddViewerQuery
     */
    public AddViewerQuery build() {
      return new AddViewerQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream to be joined as viewer
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets user token.
   *
   * @return the token of the user joining the stream group as viewer
   */
  public String getUserToken() {
    return userToken;
  }
}
