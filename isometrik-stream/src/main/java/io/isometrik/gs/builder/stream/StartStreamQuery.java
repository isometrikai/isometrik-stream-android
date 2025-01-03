package io.isometrik.gs.builder.stream;

import org.json.JSONObject;

import java.util.List;

/**
 * Query builder class for creating the request for starting a stream.
 */
public class StartStreamQuery {
    private final String customType;
    private final List<String> searchableTags;
    private final JSONObject metaData;
    private final Boolean selfHosted;
    private final String streamDescription;
    private final String streamImage;
    private final List<String> members;
    private final Boolean isPublic;
    private final Boolean audioOnly;
    private final Boolean multiLive;
    private final Boolean enableRecording;
    private final Boolean lowLatencyMode;
    private final Boolean hdBroadcast;
    private final Boolean restream;
    private final Boolean productsLinked;
    private final List<String> productIds;
    private final String userToken;
    private final Boolean rtmpIngest;
    private final Boolean persistRtmpIngestEndpoint;
    private final String userId;

    private StartStreamQuery(Builder builder) {
        this.streamDescription = builder.streamDescription;
        this.streamImage = builder.streamImage;
        this.members = builder.members;
        this.isPublic = builder.isPublic;
        this.audioOnly = builder.audioOnly;
        this.multiLive = builder.multiLive;
        this.enableRecording = builder.enableRecording;
        this.lowLatencyMode = builder.lowLatencyMode;
        this.hdBroadcast = builder.hdBroadcast;
        this.restream = builder.restream;
        this.productsLinked = builder.productsLinked;
        this.productIds = builder.productIds;
        this.userToken = builder.userToken;
        this.customType = builder.customType;
        this.metaData = builder.metaData;
        this.searchableTags = builder.searchableTags;
        this.selfHosted = builder.selfHosted;
        this.rtmpIngest = builder.rtmpIngest;
        this.persistRtmpIngestEndpoint = builder.persistRtmpIngestEndpoint;
        this.userId = builder.userId;
    }

    /**
     * The Builder class for the StartStreamQuery.
     */
    public static class Builder {
        private String customType;
        private List<String> searchableTags;
        private JSONObject metaData;
        private Boolean selfHosted;
        private String streamDescription;
        private String streamImage;
        private String userToken;
        private List<String> members;
        private Boolean isPublic;
        private Boolean audioOnly;
        private Boolean multiLive;
        private Boolean enableRecording;
        private Boolean lowLatencyMode;
        private Boolean hdBroadcast;
        private Boolean restream;
        private Boolean productsLinked;
        private List<String> productIds;
        private Boolean rtmpIngest;
        private Boolean persistRtmpIngestEndpoint;
        private String userId;


        /**
         * Instantiates a new Builder.
         */
        public Builder() {
        }

        public Builder setCustomType(String customType) {
            this.customType = customType;
            return this;
        }

        public Builder setSearchableTags(List<String> searchableTags) {
            this.searchableTags = searchableTags;
            return this;
        }

        public Builder setMetaData(JSONObject metaData) {
            this.metaData = metaData;
            return this;
        }

        public Builder setSelfHosted(Boolean selfHosted) {
            this.selfHosted = selfHosted;
            return this;
        }

        /**
         * Sets stream description.
         *
         * @param streamDescription the description of stream group
         * @return the StartStreamQuery.Builder{@link Builder}
         * instance
         * @see Builder
         */
        public Builder setStreamDescription(String streamDescription) {
            this.streamDescription = streamDescription;
            return this;
        }

        /**
         * Sets stream image.
         *
         * @param streamImage the image of the stream group
         * @return the StartStreamQuery.Builder{@link Builder}
         * instance
         * @see Builder
         */
        public Builder setStreamImage(String streamImage) {
            this.streamImage = streamImage;
            return this;
        }

        /**
         * Sets user token.
         *
         * @param userToken the token of the stream group creator
         * @return the StartStreamQuery.Builder{@link Builder}
         * instance
         * @see Builder
         */
        public Builder setUserToken(String userToken) {
            this.userToken = userToken;
            return this;
        }

        /**
         * Sets isPublic.
         *
         * @param isPublic whether stream group is public or private
         * @return the StartStreamQuery.Builder{@link Builder}
         * instance
         * @see Builder
         */
        public Builder setPublic(Boolean isPublic) {
            this.isPublic = isPublic;
            return this;
        }

        /**
         * Sets audioOnly.
         *
         * @param audioOnly whether stream group is audioOnly or not
         * @return the StartStreamQuery.Builder{@link Builder}
         * instance
         * @see Builder
         */
        public Builder setAudioOnly(Boolean audioOnly) {
            this.audioOnly = audioOnly;
            return this;
        }

        /**
         * Sets multiLive.
         *
         * @param multiLive whether stream group is multiLive or not
         * @return the StartStreamQuery.Builder{@link Builder}
         * instance
         * @see Builder
         */
        public Builder setMultiLive(Boolean multiLive) {
            this.multiLive = multiLive;
            return this;
        }

        /**
         * Sets enableRecording.
         *
         * @param enableRecording whether to enable recording for the stream group or not
         * @return the StartStreamQuery.Builder{@link Builder}
         * instance
         * @see Builder
         */
        public Builder setEnableRecording(Boolean enableRecording) {
            this.enableRecording = enableRecording;
            return this;
        }

        /**
         * Sets multiLive.
         *
         * @param lowLatencyMode whether the stream group is low latency or not
         * @return the StartStreamQuery.Builder{@link Builder}
         * instance
         * @see Builder
         */
        public Builder setLowLatencyMode(Boolean lowLatencyMode) {
            this.lowLatencyMode = lowLatencyMode;
            return this;
        }

        /**
         * Sets hdBroadcast.
         *
         * @param hdBroadcast whether the stream group to be created is hd or not
         * @return the StartStreamQuery.Builder{@link Builder}
         * instance
         * @see Builder
         */
        public Builder setHdBroadcast(Boolean hdBroadcast) {
            this.hdBroadcast = hdBroadcast;
            return this;
        }

        /**
         * Sets restream.
         *
         * @param restream whether the stream group to be created is to be restreamed or not
         * @return the StartStreamQuery.Builder{@link Builder}
         * instance
         * @see Builder
         */
        public Builder setRestream(Boolean restream) {
            this.restream = restream;
            return this;
        }

        /**
         * Sets members.
         *
         * @param members the list of members added to the stream group at time of creation
         * @return the StartStreamQuery.Builder{@link Builder}
         * instance
         * @see Builder
         */
        public Builder setMembers(List<String> members) {
            this.members = members;
            return this;
        }

        /**
         * Sets product ids.
         *
         * @param productIds the list of products linked to the stream group at time of creation
         * @return the StartStreamQuery.Builder{@link Builder}
         * instance
         * @see Builder
         */
        public Builder setProductIds(List<String> productIds) {
            this.productIds = productIds;
            return this;
        }

        /**
         * Sets products linked.
         *
         * @param productsLinked whether the stream group to be created has products linked or not
         * @return the StartStreamQuery.Builder{@link Builder}
         * instance
         * @see Builder
         */
        public Builder setProductsLinked(Boolean productsLinked) {
            this.productsLinked = productsLinked;
            return this;
        }


        public Builder setRtmpIngest(Boolean rtmpIngest) {
            this.rtmpIngest = rtmpIngest;
            return this;
        }

        public Builder setPersistRtmpIngestEndpoint(Boolean persistRtmpIngestEndpoint) {
            this.persistRtmpIngestEndpoint = persistRtmpIngestEndpoint;
            return this;
        }

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        /**
         * Build start stream query.
         *
         * @return the StartStreamQuery{@link StartStreamQuery} instance
         * @see StartStreamQuery
         */
        public StartStreamQuery build() {
            return new StartStreamQuery(this);
        }
    }

    /**
     * Gets stream description.
     *
     * @return the description of stream group
     */
    public String getStreamDescription() {
        return streamDescription;
    }

    /**
     * Gets stream image.
     *
     * @return the image of the stream group
     */
    public String getStreamImage() {
        return streamImage;
    }

    /**
     * Gets members.
     *
     * @return the list of members added to the stream group at time of creation
     */
    public List<String> getMembers() {
        return members;
    }

    /**
     * Gets is public.
     *
     * @return whether the stream group to be created is public or private
     */
    public Boolean isPublic() {
        return isPublic;
    }

    /**
     * Gets audio only.
     *
     * @return whether the stream group to be created is audioOnly or not
     */
    public Boolean getAudioOnly() {
        return audioOnly;
    }

    /**
     * Gets multi live.
     *
     * @return whether the stream group to be created is multiLive or not
     */
    public Boolean getMultiLive() {
        return multiLive;
    }

    /**
     * Gets value of enable recording.
     *
     * @return whether to enable recording for the stream group to be created or not
     */
    public Boolean getEnableRecording() {
        return enableRecording;
    }

    /**
     * Gets value of low latency mode.
     *
     * @return whether the stream group to be created is low latency or not
     */
    public Boolean getLowLatencyMode() {
        return lowLatencyMode;
    }

    /**
     * Gets value of hd broadcast.
     *
     * @return whether the stream group to be created is hd or not
     */
    public Boolean getHdBroadcast() {
        return hdBroadcast;
    }

    /**
     * Gets value of restream.
     *
     * @return whether the stream group to be created is to be restreamed or not
     */
    public Boolean getRestream() {
        return restream;
    }

    /**
     * Gets value of products linked.
     *
     * @return whether the stream group to be created has products linked or not
     */
    public Boolean getProductsLinked() {
        return productsLinked;
    }

    /**
     * Gets product ids.
     *
     * @return the list of product ids linked to the stream group at time of creation
     */
    public List<String> getProductIds() {
        return productIds;
    }

    /**
     * Gets user token.
     *
     * @return the token of the stream group creator
     */
    public String getUserToken() {
        return userToken;
    }

    public String getCustomType() {
        return customType;
    }

    public List<String> getSearchableTags() {
        return searchableTags;
    }

    public JSONObject getMetaData() {
        return metaData;
    }

    public Boolean getSelfHosted() {
        return selfHosted;
    }

    public Boolean getRtmpIngest() {
        return rtmpIngest;
    }

    public Boolean getPersistRtmpIngestEndpoint() {
        return persistRtmpIngestEndpoint;
    }

    public String getUserId() {
        return userId;
    }


}
