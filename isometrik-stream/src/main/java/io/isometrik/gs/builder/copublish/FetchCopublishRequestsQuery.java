package io.isometrik.gs.builder.copublish;

/**
 * Query builder class for fetching copublish requests in a stream group.
 */
public class FetchCopublishRequestsQuery {

  private final String streamId;
  private final Integer skip;
  private final Integer limit;
  private final String userToken;
  private final String searchTag;

  private FetchCopublishRequestsQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.skip = builder.skip;
    this.limit = builder.limit;
    this.userToken = builder.userToken;
    this.searchTag = builder.searchTag;
  }

  /**
   * The Builder class for the FetchCopublishRequestsQuery.
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

    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    public Builder setSearchTag(String searchTag) {
      this.searchTag = searchTag;
      return this;
    }

    /**
     * Sets limit.
     *
     * @param limit the number of copublish requests to be fetched
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setLimit(int limit) {
      this.limit = limit;
      return this;
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream for which to fetch copublish requests
     * @return the Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Build fetch copublish requests query.
     *
     * @return the FetchCopublishRequestsQuery{@link FetchCopublishRequestsQuery}
     * instance
     * @see FetchCopublishRequestsQuery
     */
    public FetchCopublishRequestsQuery build() {
      return new FetchCopublishRequestsQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream for which to fetch copublish requests
   */
  public String getStreamId() {
    return streamId;
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
   * Gets user token.
   *
   * @return the user token
   */
  public String getUserToken() {
    return userToken;
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
