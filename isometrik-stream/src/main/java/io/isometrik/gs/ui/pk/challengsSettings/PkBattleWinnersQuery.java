package io.isometrik.gs.ui.pk.challengsSettings;

/**
 * Query builder class for creating the request for fetching the list of users.
 */
public class PkBattleWinnersQuery {
  private final String userToken;
  private final String pkId;


  private PkBattleWinnersQuery(Builder builder) {
    this.userToken = builder.userToken;
    this.pkId = builder.pkId;
  }

  /**
   * The builder class for building fetch users query.
   */
  public static class Builder {
    private String userToken;
    private String pkId;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }


    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }


    public Builder setPkId(String pkId) {
      this.pkId = pkId;
      return this;
    }

    /**
     * Build fetch users query.
     *
     * @return the fetch users query
     */
    public PkBattleWinnersQuery build() {
      return new PkBattleWinnersQuery(this);
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


  public String getPkId() {
    return pkId;
  }


}
