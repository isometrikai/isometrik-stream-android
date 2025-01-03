package io.isometrik.gs.builder.copublish;

/**
 * Query builder class for switching profile from a viewer to a broadcaster in a stream group.
 */
public class SwitchProfileQuery {

  private final String streamId;
  private final String userToken;

  private SwitchProfileQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.userToken = builder.userToken;
  }

  /**
   * The Builder class for the SwitchProfileQuery.
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
     * @param streamId the id of the stream for which to switch profile from a viewer to a
     * broadcaster
     * @return the
     * SwitchProfileQuery.Builder{@link
     * Builder}
     * instance
     * @see Builder
     */
    public Builder setStreamId(
        String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the token of the user switching profile from a viewer to a broadcaster in a
     * stream
     * group
     * @return the
     * SwitchProfileQuery.Builder{@link
     * Builder}
     * instance
     * @see Builder
     */
    public Builder setUserToken(
        String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build add switch profile query.
     *
     * @return the SwitchProfileQuery{@link SwitchProfileQuery}
     * instance
     * @see SwitchProfileQuery
     */
    public SwitchProfileQuery build() {
      return new SwitchProfileQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream for which to switch profile from a viewer to a broadcaster
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets user token.
   *
   * @return the token of the user switching profile from a viewer to a broadcaster in a stream group
   */
  public String getUserToken() {
    return userToken;
  }
}