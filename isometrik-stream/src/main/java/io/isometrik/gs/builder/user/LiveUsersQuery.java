package io.isometrik.gs.builder.user;

import io.isometrik.gs.builder.moderator.FetchModeratorsQuery;

/**
 * Query builder class for creating the request for fetching the list of users.
 */
public class LiveUsersQuery {
  private final Integer skip;
  private final Integer limit;
  private final String searchTag;
  private final String userToken;


  private LiveUsersQuery(Builder builder) {
    this.skip = builder.skip;
    this.limit = builder.limit;
    this.searchTag = builder.searchTag;
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building fetch users query.
   */
  public static class Builder {
    private Integer skip;
    private Integer limit;
    private String searchTag;
    private String userToken;

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
    public LiveUsersQuery build() {
      return new LiveUsersQuery(this);
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
}
