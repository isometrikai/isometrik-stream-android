package io.isometrik.gs.builder.wallet;

/**
 * Query builder class for creating the request for fetching wallet balance.
 */
public class WalletBalanceQuery {
  private final String  currency;
  private final String userToken;


  private WalletBalanceQuery(Builder builder) {
    this.currency = builder.currency;
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building fetch users query.
   */
  public static class Builder {
    private String  currency;
    private String userToken;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }


    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    public Builder setCurrency(String currency) {
      this.currency = currency;
      return this;
    }


    /**
     * Build fetch users query.
     *
     * @return the fetch users query
     */
    public WalletBalanceQuery build() {
      return new WalletBalanceQuery(this);
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


  public String getCurrency() {
    return currency;
  }

}
