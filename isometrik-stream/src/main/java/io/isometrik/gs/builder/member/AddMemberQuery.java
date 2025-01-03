package io.isometrik.gs.builder.member;

/**
 * Query builder class for creating the request for adding a member to a stream group.
 */
public class AddMemberQuery {

  private final String streamId;
  private final String memberId;
  private final String userToken;

  private AddMemberQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.memberId = builder.memberId;
    this.userToken = builder.userToken;
  }

  /**
   * The Builder class for the AddMemberQuery.
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
     * @param streamId the id of the stream group, in which to add member
     * @return the AddMemberQuery.Builder{@link Builder}
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
     * @param userToken the token of the member adding other member to the stream group
     * @return the AddMemberQuery.Builder{@link Builder}
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
     * @param memberId the id of the member to be added to the stream group
     * @return the AddMemberQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setMemberId(String memberId) {
      this.memberId = memberId;
      return this;
    }

    /**
     * Build add member query.
     *
     * @return the AddMemberQuery{@link AddMemberQuery} instance
     * @see AddMemberQuery
     */
    public AddMemberQuery build() {
      return new AddMemberQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group, in which to add member
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets member id.
   *
   * @return the id of the member to be added to the stream group
   */
  public String getMemberId() {
    return memberId;
  }

  /**
   * Gets user token.
   *
   * @return the token of the member adding other member to the stream group
   */
  public String getUserToken() {
    return userToken;
  }
}
