package io.isometrik.gs.builder.stream;

import org.json.JSONObject;

import java.util.List;

/**
 * The query builder for updating stream details request.
 */
public class UpdateStreamDetailsQuery {
  private final String streamId;
  private final String userToken;
  private final List<String> searchableTags;
  private final JSONObject metaData;
  private final String customType;

  private UpdateStreamDetailsQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.userToken = builder.userToken;
    this.searchableTags = builder.searchableTags;
    this.metaData = builder.metaData;
    this.customType = builder.customType;
  }

  /**
   * The builder class for building update stream details query.
   */
  public static class Builder {
    private String streamId;
    private String userToken;
    private List<String> searchableTags;
    private JSONObject metaData;
    private String customType;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets stream id.
     *
     * @param streamId the stream id
     * @return the stream id
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Sets searchable tags.
     *
     * @param searchableTags the searchable tags
     * @return the searchable tags
     */
    public Builder setSearchableTags(List<String> searchableTags) {
      this.searchableTags = searchableTags;
      return this;
    }

    /**
     * Sets meta data.
     *
     * @param metaData the meta data
     * @return the meta data
     */
    public Builder setMetaData(JSONObject metaData) {
      this.metaData = metaData;
      return this;
    }

    /**
     * Sets custom type.
     *
     * @param customType the custom type
     * @return the custom type
     */
    public Builder setCustomType(String customType) {
      this.customType = customType;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build update stream details query.
     *
     * @return the update stream details query
     */
    public UpdateStreamDetailsQuery build() {
      return new UpdateStreamDetailsQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the stream id
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets user token.
   *
   * @return the user token
   */
  public String getUserToken() {
    return userToken;
  }

  /**
   * Gets searchable tags.
   *
   * @return the searchable tags
   */
  public List<String> getSearchableTags() {
    return searchableTags;
  }

  /**
   * Gets meta data.
   *
   * @return the meta data
   */
  public JSONObject getMetaData() {
    return metaData;
  }

  /**
   * Gets custom type.
   *
   * @return the custom type
   */
  public String getCustomType() {
    return customType;
  }
}
