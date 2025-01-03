package io.isometrik.gs.builder.member;

/**
 * Query builder class for creating the request for fetching the details of a member by uid in
 * stream group.
 */
public class FetchMemberDetailsQuery {
  private final Integer uid;
  private final String streamId;
  private final String memberId;
  private final String userToken;

  private FetchMemberDetailsQuery(Builder builder) {
    this.uid = builder.uid;
    this.streamId = builder.streamId;
    this.userToken = builder.userToken;
    this.memberId = builder.memberId;
  }

  /**
   * The Builder class for the FetchMemberDetailsByUidQuery.
   */
  public static class Builder {
    private Integer uid;
    private String streamId;
    private String memberId;
    private String userToken;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    public Builder setMemberId(String memberId) {
      this.memberId = memberId;
      return this;
    }

    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Sets user uid.
     *
     * @param uid the uid of the user whose details are to be fetched
     * @return the FetchMemberDetailsByUidQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setUserUid(Integer uid) {
      this.uid = uid;
      return this;
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream whose member details are to be fetched
     * @return the FetchMemberDetailsByUidQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Build fetch user details query.
     *
     * @return the FetchMemberDetailsByUidQuery{@link FetchMemberDetailsQuery} instance
     * @see FetchMemberDetailsQuery
     */
    public FetchMemberDetailsQuery build() {
      return new FetchMemberDetailsQuery(this);
    }
  }

  /**
   * Gets user uid.
   *
   * @return the uid of the user whose details are to be fetched
   */
  public Integer getUserUid() {
    return uid;
  }

  /**
   * Gets streamId.
   *
   * @return the id of the stream whose member details are to be fetched
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets user token.
   *
   * @return the user token
   */
  public String getUserToken() {
    return userToken;
  }

  public String getMemberId() {
    return memberId;
  }
}
