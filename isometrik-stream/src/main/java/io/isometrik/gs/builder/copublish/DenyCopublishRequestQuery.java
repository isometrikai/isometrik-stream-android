package io.isometrik.gs.builder.copublish;

/**
 * Query builder class for denying a copublish request in a stream group.
 */
public class DenyCopublishRequestQuery {

  private final String streamId;
  private final String requestByUserId;
  private final String userToken;

  private DenyCopublishRequestQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.requestByUserId = builder.requestByUserId;
    this.userToken = builder.userToken;
  }

  /**
   * The Builder class for the DenyCopublishRequestQuery.
   */
  public static class Builder {
    private String streamId;
    private String requestByUserId;
    private String userToken;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream for which to deny a copublish request
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
     * Sets request by user id.
     *
     * @param requestByUserId the id of the user whose copublish request is to be denied
     * @return the
     * Builder{@link
     * Builder}
     * instance
     * @see Builder
     */
    public Builder setUserId(String requestByUserId) {
      this.requestByUserId = requestByUserId;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the token of the initiator of the stream group who is denying the copublish
     * request
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
     * Build deny copublish request query.
     *
     * @return the
     * DenyCopublishRequestQuery{@link DenyCopublishRequestQuery}
     * instance
     * @see DenyCopublishRequestQuery
     */
    public DenyCopublishRequestQuery build() {
      return new DenyCopublishRequestQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream for which to deny a copublish request
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets request by user id.
   *
   * @return the id of the user whose copublish request is to be denied
   */
  public String getRequestByUserId() {
    return requestByUserId;
  }

  /**
   * Gets user token.
   *
   * @return the token of the initiator of the stream group who is denying the copublish request
   */
  public String getUserToken() {
    return userToken;
  }
}