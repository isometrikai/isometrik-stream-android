package io.isometrik.gs.builder.member;

/**
 * Query builder class for creating the request for removing a member from a stream group.
 */
public class RemoveMemberQuery {

  private final String streamId;
  private final String memberId;
  private final String userToken;

  private RemoveMemberQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.memberId = builder.memberId;
    this.userToken = builder.userToken;
  }

  /**
   * The Builder class for the RemoveMemberQuery.
   */
  public static class Builder {
    private String streamId;
    private String memberId;
    private String userToken;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream group from which to remove a member
     * @return the RemoveMemberQuery.Builder{@link Builder}
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
     * @param userToken the token of the member who is removing another member from a stream group
     * @return the RemoveMemberQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Sets member id.
     *
     * @param memberId the id of the member being removed from a stream group
     * @return the RemoveMemberQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setMemberId(String memberId) {
      this.memberId = memberId;
      return this;
    }

    /**
     * Build remove member query.
     *
     * @return the RemoveMemberQuery{@link RemoveMemberQuery} instance
     * @see RemoveMemberQuery
     */
    public RemoveMemberQuery build() {
      return new RemoveMemberQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group from which to remove a member
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets member id.
   *
   * @return the id of the member being removed from a stream group
   */
  public String getMemberId() {
    return memberId;
  }

  /**
   * Gets user token.
   *
   * @return the token of the member who is removing another member from a stream group
   */
  public String getUserToken() {
    return userToken;
  }
}
