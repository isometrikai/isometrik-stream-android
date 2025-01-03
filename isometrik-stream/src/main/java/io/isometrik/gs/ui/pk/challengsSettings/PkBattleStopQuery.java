package io.isometrik.gs.ui.pk.challengsSettings;

/**
 * Query builder class for creating the request for fetching the list of users.
 */
public class PkBattleStopQuery {
  private final String  action;
  private final String userToken;
  private final String pkId;


  private PkBattleStopQuery(Builder builder) {
    this.action = builder.action;
    this.userToken = builder.userToken;
    this.pkId = builder.pkId;
  }

  /**
   * The builder class for building fetch users query.
   */
  public static class Builder {
    private String  action;
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

    public Builder setAction(String action) {
      this.action = action;
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
    public PkBattleStopQuery build() {
      return new PkBattleStopQuery(this);
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

  public String getAction() {
    return action;
  }

}
