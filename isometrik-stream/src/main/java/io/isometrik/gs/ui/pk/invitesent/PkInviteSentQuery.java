package io.isometrik.gs.ui.pk.invitesent;

/**
 * Query builder class for creating the request for fetching the list of users.
 */
public class PkInviteSentQuery {
  private final Integer skip;
  private final Integer limit;
  private final String searchTag;
  private final String userToken;
  private final String userId;
  private final String senderStreamId;
  private final String receiverStreamId;


  private PkInviteSentQuery(Builder builder) {
    this.skip = builder.skip;
    this.limit = builder.limit;
    this.searchTag = builder.searchTag;
    this.userToken = builder.userToken;
    this.userId = builder.userId;
    this.senderStreamId = builder.senderStreamId;
    this.receiverStreamId = builder.receiverStreamId;
  }

  /**
   * The builder class for building fetch users query.
   */
  public static class Builder {
    private Integer skip;
    private Integer limit;
    private String searchTag;
    private String userToken;
    private String userId;
    private String senderStreamId;
    private String receiverStreamId;

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
    public Builder setUserId(String userId) {
      this.userId = userId;
      return this;
    }
    public Builder setSenderStreamId(String senderStreamId) {
      this.senderStreamId = senderStreamId;
      return this;
    }

    public Builder setReceiverStreamId(String receiverStreamId) {
      this.receiverStreamId = receiverStreamId;
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
    public PkInviteSentQuery build() {
      return new PkInviteSentQuery(this);
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

  public String getUserId() {
    return userId;
  }

  public String getSenderStreamId(){
    return senderStreamId;
  }
  public String getReceiverStreamId(){
    return receiverStreamId;
  }
}
