package io.isometrik.gs.builder.member;

/**
 * Query builder class for creating the request for leaving a stream group be a member.
 */
public class LeaveMemberQuery {

  private final String streamId;
  private final String userToken;

  private LeaveMemberQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.userToken = builder.userToken;
  }

  /**
   * The Builder class for the LeaveMemberQuery.
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
     * @param streamId the id of the stream group to be left by a member
     * @return the LeaveMemberQuery.Builder{@link Builder}
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
     * @param userToken the token of the member leaving the stream group
     * @return the LeaveMemberQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build leave member query.
     *
     * @return the LeaveMemberQuery{@link LeaveMemberQuery} instance
     * @see LeaveMemberQuery
     */
    public LeaveMemberQuery build() {
      return new LeaveMemberQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group to be left by a member
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets user token.
   *
   * @return the token of the member leaving the stream group
   */
  public String getUserToken() {
    return userToken;
  }
}
