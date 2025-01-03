package io.isometrik.gs.builder.stream;

/**
 * Query builder class for creating the request for updating publishing status of a member in a
 * stream group.
 */
public class UpdateStreamPublishingStatusQuery {

  private final String streamId;
  private final Boolean startPublish;
  private final String userToken;

  private UpdateStreamPublishingStatusQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.startPublish = builder.startPublish;
    this.userToken = builder.userToken;
  }

  /**
   * The Builder class for the UpdateStreamPublishingStatusQuery.
   */
  public static class Builder {
    private String streamId;
    private Boolean startPublish;
    private String userToken;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream group in which to update the publishing status of a user
     * @return the
     * UpdateStreamPublishingStatusQuery.Builder{@link
     * Builder}
     * instance
     * @see Builder
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Sets start publish.
     *
     * @param startPublish the status, publishing started or stopped in a publishing group by a user
     * @return the
     * UpdateStreamPublishingStatusQuery.Builder{@link
     * Builder}
     * instance
     * @see Builder
     */
    public Builder setStartPublish(Boolean startPublish) {
      this.startPublish = startPublish;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the token of the user whose publishing status is updated in a stream group
     * @return the
     * UpdateStreamPublishingStatusQuery.Builder{@link
     * Builder}
     * instance
     * @see Builder
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build update stream publishing status query.
     *
     * @return the
     * UpdateStreamPublishingStatusQuery{@link
     * UpdateStreamPublishingStatusQuery}
     * instance
     * @see UpdateStreamPublishingStatusQuery
     */
    public UpdateStreamPublishingStatusQuery build() {
      return new UpdateStreamPublishingStatusQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group in which to update the publishing status of a user
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Is start publish boolean.
   *
   * @return the status, publishing started or stopped in a publishing group by a user
   */
  public Boolean isStartPublish() {
    return startPublish;
  }

  /**
   * Gets user token.
   *
   * @return the token of the user whose publishing status is updated in a stream group
   */
  public String getUserToken() {
    return userToken;
  }
}
