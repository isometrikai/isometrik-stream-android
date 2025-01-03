package io.isometrik.gs.builder.viewer;

/**
 * Query builder class for creating the request for leaving as viewer by a user for a stream group.
 */
public class LeaveViewerQuery {

  private final String streamId;
  private final String userToken;

  private LeaveViewerQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.userToken = builder.userToken;
  }

  /**
   * The Builder class for the LeaveViewerQuery.
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
     * @param streamId the id of stream group to be left by a viewer
     * @return the LeaveViewerQuery.Builder{@link Builder}
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
     * @param userToken the token of the user who is leaving a stream group as viewer
     * @return the LeaveViewerQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build leave viewer query.
     *
     * @return the LeaveViewerQuery{@link LeaveViewerQuery} instance
     * @see LeaveViewerQuery
     */
    public LeaveViewerQuery build() {
      return new LeaveViewerQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of stream group to be left by a viewer
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets user token.
   *
   * @return the token of the user who is leaving a stream group as viewer
   */
  public String getUserToken() {
    return userToken;
  }
}
