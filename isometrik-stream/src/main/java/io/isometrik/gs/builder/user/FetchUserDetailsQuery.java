package io.isometrik.gs.builder.user;

/**
 * Query builder class for creating the request for fetching the details of a user.
 */
public class FetchUserDetailsQuery {
  private final String userToken;

  private FetchUserDetailsQuery(Builder builder) {
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building fetch user details query.
   */
  public static class Builder {
    private String userToken;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build fetch user details query.
     *
     * @return the fetch user details query
     */
    public FetchUserDetailsQuery build() {
      return new FetchUserDetailsQuery(this);
    }
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
