package io.isometrik.gs.builder.moderator;

/**
 * Query builder class for creating the request for leaving a stream group be a moderator.
 */
public class LeaveModeratorQuery {

  private final String streamId;
  private final String userToken;

  private LeaveModeratorQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.userToken = builder.userToken;
  }

  /**
   * The Builder class for the LeaveModeratorQuery.
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
     * @param streamId the id of the stream group to be left by a moderator
     * @return the LeaveModeratorQuery.Builder{@link Builder}
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
     * @param userToken the token of the moderator leaving the stream group
     * @return the LeaveModeratorQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build leave moderator query.
     *
     * @return the LeaveModeratorQuery{@link LeaveModeratorQuery} instance
     * @see LeaveModeratorQuery
     */
    public LeaveModeratorQuery build() {
      return new LeaveModeratorQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group to be left by a moderator
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets user token.
   *
   * @return the token of the moderator leaving the stream group
   */
  public String getUserToken() {
    return userToken;
  }
}
