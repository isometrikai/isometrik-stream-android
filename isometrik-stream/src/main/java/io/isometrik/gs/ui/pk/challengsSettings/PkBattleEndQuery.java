package io.isometrik.gs.ui.pk.challengsSettings;

/**
 * Query builder class for creating the request for fetching the list of users.
 */
public class PkBattleEndQuery {
  private final String  action;
  private final String userToken;
  private final String inviteId;
  private final boolean intentToStop;


  private PkBattleEndQuery(Builder builder) {
    this.action = builder.action;
    this.userToken = builder.userToken;
    this.inviteId = builder.inviteId;
    this.intentToStop = builder.intentToStop;
  }

  /**
   * The builder class for building fetch users query.
   */
  public static class Builder {
    private String  action;
    private String userToken;
    private String inviteId;
    private boolean intentToStop;

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

    public Builder setIntentToStop(boolean intentToStop) {
      this.intentToStop = intentToStop;
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
    public PkBattleEndQuery build() {
      return new PkBattleEndQuery(this);
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


  public String getInviteId() {
    return inviteId;
  }

  public String getAction() {
    return action;
  }

  public boolean isIntentToStop() {
    return intentToStop;
  }
}
