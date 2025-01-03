package io.isometrik.gs.builder.moderator;

/**
 * Query builder class for creating the request for adding a moderator to a stream group.
 */
public class AddModeratorQuery {

  private final String streamId;
  private final String moderatorId;
  private final String userToken;

  private AddModeratorQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.moderatorId = builder.moderatorId;
    this.userToken = builder.userToken;
  }

  /**
   * The Builder class for the AddModeratorQuery.
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
     * @param streamId the id of the stream group, in which to add moderator
     * @return the AddModeratorQuery.Builder{@link Builder}
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
     * @param userToken the token of the user who is adding a moderator to the stream group
     * @return the AddModeratorQuery.Builder{@link Builder}
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
     * @param moderatorId the id of the moderator to be added to the stream group
     * @return the AddModeratorQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setModeratorId(String moderatorId) {
      this.moderatorId = moderatorId;
      return this;
    }

    /**
     * Build add moderator query.
     *
     * @return the AddModeratorQuery{@link AddModeratorQuery} instance
     * @see AddModeratorQuery
     */
    public AddModeratorQuery build() {
      return new AddModeratorQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group, in which to add moderator
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets moderator id.
   *
   * @return the id of the moderator to be added to the stream group
   */
  public String getModeratorId() {
    return moderatorId;
  }

  /**
   * Gets user token.
   *
   * @return the token of the user adding a moderator to the stream group
   */
  public String getUserToken() {
    return userToken;
  }
}
