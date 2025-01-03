package io.isometrik.gs.builder.wallet;

/**
 * Query builder class for creating the request for fetching transaction.
 */
public class TransactionsQuery {
  private final String  currency;
  private final String userToken;
  private final String txnType;
  private final Boolean txnSpecific;


  private TransactionsQuery(Builder builder) {
    this.currency = builder.currency;
    this.userToken = builder.userToken;
    this.txnType = builder.txnType;
    this.txnSpecific = builder.txnSpecific;
  }

  /**
   * The builder class for building fetch users query.
   */
  public static class Builder {
    private String  currency;
    private String userToken;
    private String txnType;
    private Boolean txnSpecific;

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

    public Builder setTxnType(String txnType) {
      this.txnType = txnType;
      return this;
    }

    public Builder setTxnSpecific(Boolean txnSpecific) {
      this.txnSpecific = txnSpecific;
      return this;
    }


    /**
     * Build fetch users query.
     *
     * @return the fetch users query
     */
    public TransactionsQuery build() {
      return new TransactionsQuery(this);
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

  public String getTxnType(){
    return txnType;
  }

  public Boolean getTxnSpecific(){
    return txnSpecific;
  }

}
