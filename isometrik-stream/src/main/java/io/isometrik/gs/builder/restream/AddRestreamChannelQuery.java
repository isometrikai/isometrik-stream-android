package io.isometrik.gs.builder.restream;

/**
 * Query builder class for creating the request for adding a restream channel for a user.
 */
public class AddRestreamChannelQuery {

  private final String channelName;
  private final Integer channelType;
  private final String ingestUrl;
  private final Boolean enabled;
  private final String userToken;

  private AddRestreamChannelQuery(Builder builder) {

    this.channelName = builder.channelName;
    this.channelType = builder.channelType;
    this.ingestUrl = builder.ingestUrl;
    this.enabled = builder.enabled;
    this.userToken = builder.userToken;
  }

  public static class Builder {
    private String userToken;
    private String channelName;
    private Integer channelType;
    private String ingestUrl;
    private Boolean enabled;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets channelName.
     *
     * @param channelName the name of the restream channel to be created
     * @return the
     * AddRestreamChannelQuery.Builder{@link
     * Builder}
     * instance
     * @see Builder
     */
    public Builder setChannelName(String channelName) {
      this.channelName = channelName;
      return this;
    }

    /**
     * Sets channelType.
     *
     * @param channelType the type of the restream channel to be created
     * @return the
     * AddRestreamChannelQuery.Builder{@link
     * Builder}
     * instance
     * @see Builder
     */
    public Builder setChannelType(Integer channelType) {
      this.channelType = channelType;
      return this;
    }

    /**
     * Sets ingestUrl.
     *
     * @param ingestUrl the ingest url of the restream channel to be created
     * @return the
     * AddRestreamChannelQuery.Builder{@link
     * Builder}
     * instance
     * @see Builder
     */
    public Builder setIngestUrl(String ingestUrl) {
      this.ingestUrl = ingestUrl;
      return this;
    }

    /**
     * Sets enabled.
     *
     * @param enabled whether to enable or disable the restream channel to be created
     * @return the
     * AddRestreamChannelQuery.Builder{@link
     * Builder}
     * instance
     * @see Builder
     */
    public Builder setEnabled(Boolean enabled) {
      this.enabled = enabled;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token for which to create restream channel
     * @return the
     * AddRestreamChannelQuery.Builder{@link
     * Builder}
     * instance
     * @see Builder
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build add restream channel query.
     *
     * @return the AddRestreamChannelQuery{@link AddRestreamChannelQuery}
     * instance
     * @see AddRestreamChannelQuery
     */
    public AddRestreamChannelQuery build() {
      return new AddRestreamChannelQuery(this);
    }
  }

  /**
   * Returns the name of the restream channel to be created.
   *
   * @return channelName the name of the restream channel to be created
   */
  public String getChannelName() {
    return channelName;
  }

  /**
   * Returns the type of the restream channel to be created.
   *
   * @return channelType the type of the restream channel to be created
   */
  public Integer getChannelType() {
    return channelType;
  }

  /**
   * Returns the ingest url of the restream channel to be created.
   *
   * @return ingestUrl the ingest url of the restream channel to be created
   */
  public String getIngestUrl() {
    return ingestUrl;
  }

  /**
   * Returns whether to enable or disable the restream channel to be created.
   *
   * @return enabled whether to enable or disable the restream channel to be created
   */
  public Boolean getEnabled() {
    return enabled;
  }

  /**
   * Gets user token.
   *
   * @return the token of the user for which to create restream channel
   */
  public String getUserToken() {
    return userToken;
  }
}
