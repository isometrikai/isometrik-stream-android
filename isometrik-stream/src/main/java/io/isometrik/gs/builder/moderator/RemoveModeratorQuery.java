package io.isometrik.gs.builder.moderator;

/**
 * Query builder class for creating the request for removing a moderator from a stream group.
 */
public class RemoveModeratorQuery {

  private final String streamId;
  private final String moderatorId;
  private final String userToken;

  private RemoveModeratorQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.moderatorId = builder.moderatorId;
    this.userToken = builder.userToken;
  }

  /**
   * The Builder class for the RemoveModeratorQuery.
   */
  public static class Builder {
    private String streamId;
    private String userToken;
    private String moderatorId;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream group from which to remove a moderator
     * @return the RemoveModeratorQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the token of the user who is removing a moderator from a stream group
     * @return the RemoveModeratorQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Sets moderator id.
     *
     * @param moderatorId the id of the moderator being removed from a stream group
     * @return the RemoveModeratorQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setModeratorId(String moderatorId) {
      this.moderatorId = moderatorId;
      return this;
    }

    /**
     * Build remove moderator query.
     *
     * @return the RemoveModeratorQuery{@link RemoveModeratorQuery} instance
     * @see RemoveModeratorQuery
     */
    public RemoveModeratorQuery build() {
      return new RemoveModeratorQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group from which to remove a moderator
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets moderator id.
   *
   * @return the id of the moderator being removed from a stream group
   */
  public String getModeratorId() {
    return moderatorId;
  }

  /**
   * Gets user token.
   *
   * @return the token of the user who is removing a moderator from a stream group
   */
  public String getUserToken() {
    return userToken;
  }
}
