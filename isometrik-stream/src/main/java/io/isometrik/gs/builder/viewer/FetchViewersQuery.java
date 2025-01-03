package io.isometrik.gs.builder.viewer;

/**
 * Query builder class for creating the request for fetching the list of viewers in a stream group.
 */
public class FetchViewersQuery {

  private final String streamId;
  private final String userToken;
  private final Integer limit;
  private final Integer skip;
  private final Integer sort;
  private final String searchTag;

  private FetchViewersQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.limit = builder.limit;
    this.userToken = builder.userToken;
    this.skip = builder.skip;
    this.sort = builder.sort;
    this.searchTag = builder.searchTag;
  }

  /**
   * The Builder class for the FetchViewersQuery.
   */
  public static class Builder {
    private String streamId;
    private String userToken;
    private Integer limit;
    private Integer skip;
    private Integer sort;
    private String searchTag;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets count.
     *
     * @param limit the number of viewers in a stream group to be fetched
     * @return the FetchViewersQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setLimit(Integer limit) {
      this.limit = limit;
      return this;
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream group, whose viewers are to be fetched
     * @return the FetchViewersQuery.Builder{@link Builder}
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
     * @param userToken the token of the user who is fetching viewers in a stream group
     * @return the FetchViewersQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    public Builder setSkip(Integer skip) {
      this.skip = skip;
      return this;
    }

    public Builder setSort(Integer sort) {
      this.sort = sort;
      return this;
    }

    public Builder setSearchTag(String searchTag) {
      this.searchTag = searchTag;
      return this;
    }

    /**
     * Build fetch viewers query.
     *
     * @return the FetchViewersQuery{@link FetchViewersQuery} instance
     * @see FetchViewersQuery
     */
    public FetchViewersQuery build() {
      return new FetchViewersQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group, whose viewers are to be fetched
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets limit.
   *
   * @return the number of viewers in a stream group to be fetched
   */
  public Integer getLimit() {
    return limit;
  }

  /**
   * Gets user token.
   *
   * @return the token of the user who is fetching viewers in a stream group
   */
  public String getUserToken() {
    return userToken;
  }

  public Integer getSkip() {
    return skip;
  }

  public Integer getSort() {
    return sort;
  }

  public String getSearchTag() {
    return searchTag;
  }
}
