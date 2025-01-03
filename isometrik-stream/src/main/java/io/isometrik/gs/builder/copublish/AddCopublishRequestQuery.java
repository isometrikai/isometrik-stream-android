package io.isometrik.gs.builder.copublish;

/**
 * Query builder class for adding a copublish request in a stream group.
 */
public class AddCopublishRequestQuery {

  private final String streamId;
  private final String userToken;

  private AddCopublishRequestQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.userToken = builder.userToken;
  }

  /**
   * The Builder class for the AddCopublishRequestQuery.
   */
  public static class Builder {
    private String streamId;
    private String userToken;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream for which to request copublish
     * @return the
     * Builder{@link
     * Builder}
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
     * @param userToken the token of the user requesting the copublish in the stream group
     * @return the
     * Builder{@link
     * Builder}
     * instance
     * @see Builder
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build add copublish request query.
     *
     * @return the
     * AddCopublishRequestQuery{@link AddCopublishRequestQuery}
     * instance
     * @see AddCopublishRequestQuery
     */
    public AddCopublishRequestQuery build() {
      return new AddCopublishRequestQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream for which to request copublish
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets user token.
   *
   * @return the token of the user requesting the copublish in the stream group
   */
  public String getUserToken() {
    return userToken;
  }
}