package io.isometrik.gs.ui.pk.challengsSettings;

/**
 * Query builder class for creating the request for fetching the list of users.
 */
public class PkBattleStartQuery {
  private final int battleTimeInMin;
  private final String userToken;
  private final String inviteId;


  private PkBattleStartQuery(Builder builder) {
    this.battleTimeInMin = builder.battleTimeInMin;
    this.userToken = builder.userToken;
    this.inviteId = builder.inviteId;
  }

  /**
   * The builder class for building fetch users query.
   */
  public static class Builder {
    private int battleTimeInMin;
    private String userToken;
    private String inviteId;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }


    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    public Builder setBattleTimeInMin(int battleTimeInMin) {
      this.battleTimeInMin = battleTimeInMin;
      return this;
    }

    public Builder setInviteId(String inviteId) {
      this.inviteId = inviteId;
      return this;
    }

    /**
     * Build fetch users query.
     *
     * @return the fetch users query
     */
    public PkBattleStartQuery build() {
      return new PkBattleStartQuery(this);
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


  public int getBattleTimeInMin() {
    return battleTimeInMin;
  }

  public String getInviteId() {
    return inviteId;
  }
}
