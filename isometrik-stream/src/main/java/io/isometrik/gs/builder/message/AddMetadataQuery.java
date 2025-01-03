package io.isometrik.gs.builder.message;

import org.json.JSONObject;

/**
 * Query builder class for creating the request for adding metadata message in a stream group
 */
public class AddMetadataQuery {
  private final String streamId;
  private final JSONObject metadata;
  private final String userToken;

  private AddMetadataQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.metadata = builder.metadata;
    this.userToken = builder.userToken;
  }

  /**
   * The Builder class for the AddMetadataQuery.
   */
  public static class Builder {
    private String streamId;
    private JSONObject metadata;
    private String userToken;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream group into which to add metadata
     * @return the AddMetadataQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Sets token of user adding metadata.
     *
     * @param userToken the token of the user adding the metadata in the stream group
     * @return the AddMetadataQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Sets metadata to be added to stream group.
     *
     * @param metadata the metadata to be added to the stream group
     * @return the AddMetadataQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setMetadata(JSONObject metadata) {
      this.metadata = metadata;
      return this;
    }

    /**
     * Build add metadata query.
     *
     * @return the AddMetadataQuery{@link AddMetadataQuery} instance
     * @see AddMetadataQuery
     */
    public AddMetadataQuery build() {
      return new AddMetadataQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group into which to add metadata
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets metadata to be added to stream group.
   *
   * @return the metadata being added to the stream group
   */
  public JSONObject getMetadata() {
    return metadata;
  }

  /**
   * Gets user token.
   *
   * @return the token of the user adding metadata message in the stream group
   */
  public String getUserToken() {
    return userToken;
  }
}
