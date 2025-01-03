package io.isometrik.gs.builder.stream;

import java.util.List;

/**
 * Query builder class for creating the request for fetching the streams.
 */
public class FetchStreamsQuery {
  private final Boolean isPublic;
  private final Boolean audioOnly;
  private final Boolean multiLive;
  private final Boolean recorded;
  private final Boolean lowLatencyMode;
  private final Boolean hdBroadcast;
  private final Boolean restream;
  private final Boolean productsLinked;
  private final Boolean canPublish;
  private final String userToken;
  private final Integer sort;
  private final Integer limit;
  private final Integer skip;
  private final Boolean includeMembers;
  private final List<String> membersIncluded;
  private final List<String> membersExactly;
  private final List<String> streamIds;
  private final String customType;
  private final Integer membersSkip;
  private final Integer membersLimit;
  private final String searchTag;

  private FetchStreamsQuery(Builder builder) {

    this.isPublic = builder.isPublic;
    this.audioOnly = builder.audioOnly;
    this.multiLive = builder.multiLive;
    this.recorded = builder.recorded;
    this.lowLatencyMode = builder.lowLatencyMode;
    this.hdBroadcast = builder.hdBroadcast;
    this.restream = builder.restream;
    this.productsLinked = builder.productsLinked;
    this.canPublish = builder.canPublish;

    this.userToken = builder.userToken;
    this.sort = builder.sort;
    this.limit = builder.limit;
    this.skip = builder.skip;
    this.includeMembers = builder.includeMembers;
    this.membersIncluded = builder.membersIncluded;
    this.membersExactly = builder.membersExactly;
    this.streamIds = builder.streamIds;
    this.customType = builder.customType;
    this.membersSkip = builder.membersSkip;
    this.membersLimit = builder.membersLimit;
    this.searchTag = builder.searchTag;
  }

  /**
   * The Builder class for the FetchStreamsQuery.
   */
  public static class Builder {
    private Boolean isPublic;
    private Boolean audioOnly;
    private Boolean multiLive;
    private Boolean recorded;
    private Boolean lowLatencyMode;
    private Boolean hdBroadcast;
    private Boolean restream;
    private Boolean productsLinked;
    private Boolean canPublish;
    private String userToken;
    private Integer sort;
    private Integer limit;
    private Integer skip;
    private Boolean includeMembers;
    private List<String> membersIncluded;
    private List<String> membersExactly;
    private List<String> streamIds;
    private String customType;
    private Integer membersSkip;
    private Integer membersLimit;
    private String searchTag;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    public Builder setPublic(Boolean aPublic) {
      isPublic = aPublic;
      return this;
    }

    public Builder setAudioOnly(Boolean audioOnly) {
      this.audioOnly = audioOnly;
      return this;
    }

    public Builder setMultiLive(Boolean multiLive) {
      this.multiLive = multiLive;
      return this;
    }

    public Builder setRecorded(Boolean recorded) {
      this.recorded = recorded;
      return this;
    }

    public Builder setLowLatencyMode(Boolean lowLatencyMode) {
      this.lowLatencyMode = lowLatencyMode;
      return this;
    }

    public Builder setHdBroadcast(Boolean hdBroadcast) {
      this.hdBroadcast = hdBroadcast;
      return this;
    }

    public Builder setRestream(Boolean restream) {
      this.restream = restream;
      return this;
    }

    public Builder setProductsLinked(Boolean productsLinked) {
      this.productsLinked = productsLinked;
      return this;
    }

    public Builder setCanPublish(Boolean canPublish) {
      this.canPublish = canPublish;
      return this;
    }

    public Builder setSort(Integer sort) {
      this.sort = sort;
      return this;
    }

    public Builder setSkip(Integer skip) {
      this.skip = skip;
      return this;
    }

    public Builder setIncludeMembers(Boolean includeMembers) {
      this.includeMembers = includeMembers;
      return this;
    }

    public Builder setMembersIncluded(List<String> membersIncluded) {
      this.membersIncluded = membersIncluded;
      return this;
    }

    public Builder setMembersExactly(List<String> membersExactly) {
      this.membersExactly = membersExactly;
      return this;
    }

    public Builder setStreamIds(List<String> streamIds) {
      this.streamIds = streamIds;
      return this;
    }

    public Builder setCustomType(String customType) {
      this.customType = customType;
      return this;
    }

    public Builder setMembersSkip(Integer membersSkip) {
      this.membersSkip = membersSkip;
      return this;
    }

    public Builder setMembersLimit(Integer membersLimit) {
      this.membersLimit = membersLimit;
      return this;
    }

    public Builder setSearchTag(String searchTag) {
      this.searchTag = searchTag;
      return this;
    }

    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Sets limit.
     *
     * @param limit the number of streams to be fetched
     * @return the FetchStreamsQuery.Builder{@link Builder}
     * instance
     * @see Builder
     */
    public Builder setLimit(Integer limit) {
      this.limit = limit;
      return this;
    }

    /**
     * Build fetch streams query.
     *
     * @return the FetchStreamsQuery{@link FetchStreamsQuery} instance
     * @see FetchStreamsQuery
     */
    public FetchStreamsQuery build() {
      return new FetchStreamsQuery(this);
    }
  }

  /**
   * Gets limit.
   *
   * @return the number of streams to be fetched
   */
  public Integer getLimit() {
    return limit;
  }

  /**
   * Gets user token.
   *
   * @return the user token
   */
  public String getUserToken() {
    return userToken;
  }

  public Integer getSort() {
    return sort;
  }

  public Integer getSkip() {
    return skip;
  }

  public Boolean getIncludeMembers() {
    return includeMembers;
  }

  public List<String> getMembersIncluded() {
    return membersIncluded;
  }

  public List<String> getMembersExactly() {
    return membersExactly;
  }

  public List<String> getStreamIds() {
    return streamIds;
  }

  public String getCustomType() {
    return customType;
  }

  public Integer getMembersSkip() {
    return membersSkip;
  }

  public Integer getMembersLimit() {
    return membersLimit;
  }

  public String getSearchTag() {
    return searchTag;
  }

  public Boolean getAudioOnly() {
    return audioOnly;
  }

  public Boolean getMultiLive() {
    return multiLive;
  }

  public Boolean getRecorded() {
    return recorded;
  }

  public Boolean getLowLatencyMode() {
    return lowLatencyMode;
  }

  public Boolean getHdBroadcast() {
    return hdBroadcast;
  }

  public Boolean getRestream() {
    return restream;
  }

  public Boolean getProductsLinked() {
    return productsLinked;
  }

  public Boolean getCanPublish() {
    return canPublish;
  }

  public Boolean getPublic() {
    return isPublic;
  }
}
