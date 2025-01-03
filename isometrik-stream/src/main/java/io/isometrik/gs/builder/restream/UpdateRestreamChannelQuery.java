package io.isometrik.gs.builder.restream;

/**
 * Query builder class for creating the request for updating a restream channel for a user.
 */
public class UpdateRestreamChannelQuery {

  private final String channelId;
  private final String channelName;
  private final Integer channelType;
  private final String ingestUrl;
  private final Boolean enabled;
  private final String userToken;

  private UpdateRestreamChannelQuery(Builder builder) {
    this.channelId = builder.channelId;
    this.channelName = builder.channelName;
    this.channelType = builder.channelType;
    this.ingestUrl = builder.ingestUrl;
    this.enabled = builder.enabled;
    this.userToken = builder.userToken;
  }

  public static class Builder {
    private String channelId;
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
     * @param channelName the new name of the restream channel to be updated
     * @return the
     * UpdateRestreamChannelQuery.Builder{@link
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
     * @param channelType the new type of the restream channel to be updated
     * @return the
     * UpdateRestreamChannelQuery.Builder{@link
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
     * @param ingestUrl the new ingest url of the restream channel to be updated
     * @return the
     * UpdateRestreamChannelQuery.Builder{@link
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
     * @param enabled whether to enable or disable the restream channel to be updated
     * @return the
     * UpdateRestreamChannelQuery.Builder{@link
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
     * @param userToken the user token for which to update restream channel
     * @return the
     * UpdateRestreamChannelQuery.Builder{@link
     * Builder}
     * instance
     * @see Builder
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Sets channelId.
     *
     * @param channelId the id of the restream channel to be updated
     * @return the
     * UpdateRestreamChannelQuery.Builder{@link
     * Builder}
     * instance
     * @see Builder
     */
    public Builder setChannelId(String channelId) {
      this.channelId = channelId;
      return this;
    }

    /**
     * Build update restream channel query.
     *
     * @return the
     * UpdateRestreamChannelQuery{@link
     * UpdateRestreamChannelQuery}
     * instance
     * @see UpdateRestreamChannelQuery
     */
    public UpdateRestreamChannelQuery build() {
      return new UpdateRestreamChannelQuery(this);
    }
  }

  /**
   * Returns the new name of the restream channel to be updated.
   *
   * @return channelName the new name of the restream channel to be updated
   */
  public String getChannelName() {
    return channelName;
  }

  /**
   * Returns the new type of the restream channel to be updated.
   *
   * @return channelType the new type of the restream channel to be updated
   */
  public Integer getChannelType() {
    return channelType;
  }

  /**
   * Returns the new ingest url of the restream channel to be updated.
   *
   * @return ingestUrl the new ingest url of the restream channel to be updated
   */
  public String getIngestUrl() {
    return ingestUrl;
  }

  /**
   * Returns whether to enable or disable the restream channel to be updated.
   *
   * @return enabled whether to enable or disable the restream channel to be updated
   */
  public Boolean getEnabled() {
    return enabled;
  }

  /**
   * Returns the id of the restream channel to be updated.
   *
   * @return channelId the id of the restream channel to be updated
   */
  public String getChannelId() {
    return channelId;
  }

  /**
   * Gets user token.
   *
   * @return the token of the user for whom to update restream channel
   */
  public String getUserToken() {
    return userToken;
  }
}
