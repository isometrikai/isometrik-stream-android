package io.isometrik.gs.builder.user;

/**
 * Query builder class for creating the request for fetching the list of users.
 */
public class FetchUsersQuery {
  private final Integer skip;
  private final Integer limit;
  private final String searchTag;

  private FetchUsersQuery(Builder builder) {
    this.skip = builder.skip;
    this.limit = builder.limit;
    this.searchTag = builder.searchTag;
  }

  /**
   * The builder class for building fetch users query.
   */
  public static class Builder {
    private Integer skip;
    private Integer limit;
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
    public FetchUsersQuery build() {
      return new FetchUsersQuery(this);
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
}
