package io.isometrik.gs.builder.member;

/**
 * Query builder class for creating the request for fetching the members in a stream group
 */
public class FetchMembersQuery {

  private final String streamId;
  private final Integer skip;
  private final Integer limit;
  private final String userToken;
  private final String searchTag;

  private FetchMembersQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.skip = builder.skip;
    this.limit = builder.limit;
    this.userToken = builder.userToken;
    this.searchTag = builder.searchTag;
  }

  /**
   * The Builder class for the FetchMembersQuery.
   */
  public static class Builder {
    private String streamId;
    private Integer skip;
    private Integer limit;
    private String userToken;
    private String searchTag;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    public Builder setSkip(Integer skip) {
      this.skip = skip;
      return this;
    }

    public Builder setLimit(Integer limit) {
      this.limit = limit;
      return this;
    }

    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    public Builder setSearchTag(String searchTag) {
      this.searchTag = searchTag;
      return this;
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream group, whose members are to be fetched
     * @return the FetchMembersQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Build fetch members query.
     *
     * @return the FetchMembersQuery{@link FetchMembersQuery} instance
     * @see FetchMembersQuery
     */
    public FetchMembersQuery build() {
      return new FetchMembersQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group, whose members are to be fetched
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

  /**
   * Gets skip.
   *
   * @return the skip
   */
  public Integer getSkip() {
    return skip;
  }

  /**
   * Gets limit.
   *
   * @return the limit
   */
  public Integer getLimit() {
    return limit;
  }

  /**
   * Gets search tag.
   *
   * @return the search tag
   */
  public String getSearchTag() {
    return searchTag;
  }
}
