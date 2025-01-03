package io.isometrik.gs.ui.pk.challengsSettings;

/**
 * Query builder class for creating the request for fetching the list of users.
 */
public class PkStreamStatQuery {
  private final Integer skip;
  private final Integer limit;
  private final String searchTag;
  private final String userToken;
  private final String streamId;


  private PkStreamStatQuery(Builder builder) {
    this.skip = builder.skip;
    this.limit = builder.limit;
    this.searchTag = builder.searchTag;
    this.userToken = builder.userToken;
    this.streamId = builder.streamId;
  }

  /**
   * The builder class for building fetch users query.
   */
  public static class Builder {
    private Integer skip;
    private Integer limit;
    private String searchTag;
    private String userToken;
    private String streamId;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    public Builder setSkip(Integer skip) {
      this.skip = skip;
      return this;
    }

    /**
     * Sets count.
     *
     * @param limit the count
     * @return the count
     */
    public Builder setLimit(Integer limit) {
      this.limit = limit;
      return this;
    }

    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }


    /**
     * Sets search tag.
     *
     * @param searchTag the search tag
     * @return the search tag
     */
    public Builder setSearchTag(String searchTag) {
      this.searchTag = searchTag;
      return this;
    }

    /**
     * Build fetch users query.
     *
     * @return the fetch users query
     */
    public PkStreamStatQuery build() {
      return new PkStreamStatQuery(this);
    }
  }

  public Integer getSkip() {
    return skip;
  }

  /**
   * Gets count.
   *
   * @return the count
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

  /**
   * Gets user token.
   *
   * @return the user token
   */
  public String getUserToken() {
    return userToken;
  }

  public String getStreamId() {
    return streamId;
  }

}
