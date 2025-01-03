//package io.isometrik.gs.response.stream;
//
//import com.google.gson.Gson;
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//
//import org.json.JSONObject;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//
//import io.isometrik.gs.builder.stream.FetchStreamsQuery;
//
///**
// * The class to parse fetch streams result of fetching the stream groups list query
// * FetchStreamsQuery{@link FetchStreamsQuery}.
// *
// * @see FetchStreamsQuery
// */
//public class FetchStreamsResult implements Serializable {
//
//    @SerializedName("streams")
//    @Expose
//    private ArrayList<Stream> streams;
//
//    @SerializedName("msg")
//    @Expose
//    private String message;
//
//    /**
//     * The class containing details of the stream group.
//     */
//    public static class Stream {
//        @SerializedName("rtmpIngest")
//        @Expose
//        private boolean rtmpIngest;
//        @SerializedName("persistRtmpIngestEndpoint")
//        @Expose
//        private boolean persistRtmpIngestEndpoint;
//        @SerializedName("searchableTags")
//        @Expose
//        private ArrayList<String> searchableTags;
//        @SerializedName("customType")
//        @Expose
//        private String customType;
//        @SerializedName("metaData")
//        @Expose
//        private Object metaData;
//        @SerializedName("streamId")
//        @Expose
//        private String streamId;
//        @SerializedName("streamImage")
//        @Expose
//        private String streamImage;
//        @SerializedName("streamDescription")
//        @Expose
//        private String streamDescription;
//
//        @SerializedName("membersCount")
//        @Expose
//        private int membersCount;
//        @SerializedName("viewersCount")
//        @Expose
//        private int viewersCount;
//        @SerializedName("membersPublishingCount")
//        @Expose
//        private int publishersCount;
//
//        @SerializedName("isPublic")
//        @Expose
//        private boolean isPublic;
//
//        @SerializedName("audioOnly")
//        @Expose
//        private boolean audioOnly;
//
//        @SerializedName("multiLive")
//        @Expose
//        private boolean multiLive;
//
//        @SerializedName("hdBroadcast")
//        @Expose
//        private boolean hdBroadcast;
//        @SerializedName("lowLatencyMode")
//        @Expose
//        private boolean lowLatencyMode;
//        @SerializedName("restream")
//        @Expose
//        private boolean restream;
//
//        @SerializedName("restreamChannelsCount")
//        @Expose
//        private int restreamChannelsCount;
//
//        @SerializedName("members")
//        @Expose
//        private ArrayList<String> memberIds;
//
//        @SerializedName("createdBy")
//        @Expose
//        private String initiatorId;
//
//        @SerializedName("initiatorName")
//        @Expose
//        private String initiatorName;
//
//        @SerializedName("initiatorIdentifier")
//        @Expose
//        private String initiatorIdentifier;
//
//        @SerializedName("initiatorImage")
//        @Expose
//        private String initiatorImage;
//
//        @SerializedName("startTime")
//        @Expose
//        private long startTime;
//
//        @SerializedName("productsLinked")
//        @Expose
//        private boolean productsLinked;
//
//        @SerializedName("productsCount")
//        @Expose
//        private int productsCount;
//        @SerializedName("featuringProduct")
//        @Expose
//        private String featuringProduct;
//
//        @SerializedName("enableRecording")
//        @Expose
//        private boolean enableRecording;
//        @SerializedName("selfHosted")
//        @Expose
//        private boolean selfHosted;
//        @SerializedName("moderatorsCount")
//        @Expose
//        private int moderatorsCount;
//        @SerializedName("canPublish")
//        @Expose
//        private boolean canPublish;
//
//        public boolean isRtmpIngest() {
//            return rtmpIngest;
//        }
//
//        public boolean isPersistRtmpIngestEndpoint() {
//            return persistRtmpIngestEndpoint;
//        }
//
//        /**
//         * Gets stream id.
//         *
//         * @return the id of the stream group
//         */
//        public String getStreamId() {
//            return streamId;
//        }
//
//        /**
//         * Gets stream image.
//         *
//         * @return the image of the stream group
//         */
//        public String getStreamImage() {
//            return streamImage;
//        }
//
//        /**
//         * Gets stream description.
//         *
//         * @return the description of the stream group
//         */
//        public String getStreamDescription() {
//            return streamDescription;
//        }
//
//        /**
//         * Gets members count.
//         *
//         * @return the number of members
//         */
//        public int getMembersCount() {
//            return membersCount;
//        }
//
//        /**
//         * Gets viewers count.
//         *
//         * @return the number of viewers
//         */
//        public int getViewersCount() {
//            return viewersCount;
//        }
//
//        /**
//         * Gets publishers count.
//         *
//         * @return the count of publishers currently publishing
//         */
//        public int getPublishersCount() {
//            return publishersCount;
//        }
//
//        /**
//         * Gets start time.
//         *
//         * @return the time at which stream group was created
//         */
//        public long getStartTime() {
//            return startTime;
//        }
//
//        /**
//         * Gets member ids.
//         *
//         * @return the ids of the stream group members
//         */
//        public ArrayList<String> getMemberIds() {
//            return memberIds;
//        }
//
//        /**
//         * Gets initiator id.
//         *
//         * @return the id of the stream group creator
//         */
//        public String getInitiatorId() {
//            return initiatorId;
//        }
//
//        /**
//         * Gets initiator name.
//         *
//         * @return the name of the stream group creator
//         */
//        public String getInitiatorName() {
//            return initiatorName;
//        }
//
//        /**
//         * Gets initiator identifier.
//         *
//         * @return the identifier of the stream group creator
//         */
//        public String getInitiatorIdentifier() {
//            return initiatorIdentifier;
//        }
//
//        /**
//         * Gets initiator image.
//         *
//         * @return the image of the stream group creator
//         */
//        public String getInitiatorImage() {
//            return initiatorImage;
//        }
//
//        /**
//         * Gets whether given stream is public.
//         *
//         * @return whether given stream is public
//         */
//        public boolean isPublic() {
//            return isPublic;
//        }
//
//        /**
//         * Gets whether given stream is audioOnly.
//         *
//         * @return whether given stream is audioOnly
//         */
//        public boolean isAudioOnly() {
//            return audioOnly;
//        }
//
//        /**
//         * Gets whether given stream is multiLive.
//         *
//         * @return whether given stream is multiLive
//         */
//        public boolean isMultiLive() {
//            return multiLive;
//        }
//
//        /**
//         * Gets whether given stream is hd or not.
//         *
//         * @return whether given stream is hd or not
//         */
//        public boolean isHdBroadcast() {
//            return hdBroadcast;
//        }
//
//        /**
//         * Gets whether given stream is restreamed or not.
//         *
//         * @return whether given stream is restreamed or not
//         */
//        public boolean isRestream() {
//            return restream;
//        }
//
//        /**
//         * Gets number of restream channels count.
//         *
//         * @return the number of restream channels count
//         */
//        public int getRestreamChannelsCount() {
//            return restreamChannelsCount;
//        }
//
//        /**
//         * Gets whether given stream has products linked or not.
//         *
//         * @return whether given stream has products linked or not
//         */
//        public boolean isProductsLinked() {
//            return productsLinked;
//        }
//
//        /**
//         * Gets number of products linked to a broadcast.
//         *
//         * @return number of products linked to a broadcast
//         */
//        public int getProductsCount() {
//            return productsCount;
//        }
//
//        /**
//         * Gets number of moderators added to a broadcast.
//         *
//         * @return number of moderators added to a broadcast
//         */
//        public int getModeratorsCount() {
//            return moderatorsCount;
//        }
//
//        public ArrayList<String> getSearchableTags() {
//            return searchableTags;
//        }
//
//        public String getCustomType() {
//            return customType;
//        }
//
//        public JSONObject getMetaData() {
//
//            try {
//                return new JSONObject(new Gson().toJson(metaData));
//            } catch (Exception ignore) {
//                return new JSONObject();
//            }
//        }
//
//        public boolean isLowLatencyMode() {
//            return lowLatencyMode;
//        }
//
//        public String getFeaturingProduct() {
//            return featuringProduct;
//        }
//
//        public boolean isEnableRecording() {
//            return enableRecording;
//        }
//
//        public boolean isSelfHosted() {
//            return selfHosted;
//        }
//
//        public boolean isCanPublish() {
//            return canPublish;
//        }
//    }
//
//    /**
//     * Gets message.
//     *
//     * @return the response message received
//     */
//    public String getMessage() {
//        return message;
//    }
//
//    /**
//     * Gets streams.
//     *
//     * @return the list of stream groups
//     * @see Stream
//     */
//    public ArrayList<Stream> getStreams() {
//        return streams;
//    }
//
//    public void setStreams(ArrayList<Stream> streams) {
//        this.streams = streams;
//    }
//}
