package io.isometrik.gs.ui.live;

import java.io.File;
import java.util.ArrayList;

import io.isometrik.gs.ui.members.select.MultiLiveSelectMembersModel;

public interface GoLiveContract {

    interface Presenter {
        /**
         * Request image upload.
         *
         * @param imagePath the image path
         */
        void requestImageUpload(String imagePath);

        /**
         * Delete image.
         *
         * @param file the file
         */
        void deleteImage(File file);

        /**
         * Start broadcast.
         *
         * @param streamDescription         the stream description
         * @param streamImageUrl            the stream image url
         * @param isPublic                  whether stream type is public or not
         * @param audioOnly                 whether stream type is audioOnly or not
         * @param multiLive                 whether stream type is multiLive or not
         * @param enableRecording           whether to record stream or not
         * @param hdBroadcast               whether stream is hd or not
         * @param restream                  whether to restream given broadcast or not
         * @param lowLatencyMode            whether stream is in low latency mode or not
         * @param productsLinked            whether products are linked or not to given broadcast
         * @param productIds                list of products linked to a broadcast
         * @param selfHosted                whether stream is self hosted or not
         * @param rtmpIngest                whether stream has rtmp ingest or not
         * @param persistRtmpIngestEndpoint whether to persist rtmp endpoint or not
         */
        void startBroadcast(String streamDescription, String streamImageUrl, boolean isPublic, boolean audioOnly, boolean multiLive, boolean enableRecording, boolean hdBroadcast, boolean lowLatencyMode, boolean restream, boolean productsLinked, ArrayList<String> productIds, boolean selfHosted, boolean rtmpIngest, boolean persistRtmpIngestEndpoint);

        /**
         * Start broadcast.
         *
         * @param streamDescription         the stream description
         * @param streamImageUrl            the stream image url
         * @param members                   the members
         * @param isPublic                  whether stream type is public or not
         * @param audioOnly                 whether stream type is audioOnly or not
         * @param multiLive                 whether stream type is multiLive or not
         * @param enableRecording           whether to record stream or not
         * @param hdBroadcast               whether stream is hd or not
         * @param restream                  whether to restream given broadcast or not
         * @param lowLatencyMode            whether stream is in low latency mode or not
         * @param productsLinked            whether products are linked or not to given broadcast
         * @param productIds                list of products linked to a broadcast
         * @param selfHosted                whether stream is self hosted or not
         * @param rtmpIngest                whether stream has rtmp ingest or not
         * @param persistRtmpIngestEndpoint whether to persist rtmp endpoint or not
         */
        void startBroadcast(String streamDescription, String streamImageUrl,
                            ArrayList<MultiLiveSelectMembersModel> members, boolean isPublic, boolean audioOnly,
                            boolean multiLive, boolean enableRecording, boolean hdBroadcast, boolean lowLatencyMode,
                            boolean restream, boolean productsLinked, ArrayList<String> productIds, boolean selfHosted, boolean rtmpIngest, boolean persistRtmpIngestEndpoint);


    }

    interface View {
        /**
         * On broadcast started.
         *
         * @param streamId          the stream id
         * @param streamDescription the stream description
         * @param streamImageUrl    the stream image url
         * @param memberIds         the member ids
         * @param startTime         the start time
         * @param userId            the user id
         * @param audioOnly         whether stream type is audioOnly or not
         * @param ingestEndpoint    the ingest endpoint for the single streams
         * @param streamKey         the stream key for the single streams
         * @param hdBroadcast       whether stream is hd or not
         * @param restream          whether to restream given broadcast or not
         * @param rtcToken          the rtc token for the multilive broadcast
         * @param restreamEndpoints the list of restream endpoints
         * @param productsLinked    whether products are linked or not to given broadcast
         * @param productsCount     number of products linked to the broadcast
         * @param rtmpIngestUrl     RTMP ingest url
         */
        void onBroadcastStarted(String streamId, String streamDescription, String streamImageUrl, ArrayList<String> memberIds,
                                long startTime, String userId, boolean audioOnly, String ingestEndpoint, String streamKey,
                                boolean hdBroadcast, boolean restream, String rtcToken, ArrayList<String> restreamEndpoints,
                                boolean productsLinked, int productsCount, String rtmpIngestUrl);

        /**
         * On error.
         *
         * @param errorMessage the error message to be shown in the toast for details of the failed
         *                     operation
         */
        void onError(String errorMessage);

        /**
         * On image upload result.
         *
         * @param url the url
         */
        void onImageUploadResult(String url);

        /**
         * On image upload error.
         *
         * @param errorMessage the error message containing details of the error encountered while
         *                     trying to upload image
         */
        void onImageUploadError(String errorMessage);

        /**
         * Callback when user requested restreaming but hasnot added or enabled any of restream
         * channels.
         */

        /**
         * On broadcast started.
         *
         * @param streamId          the stream id
         * @param streamDescription the stream description
         * @param streamImageUrl    the stream image url
         * @param memberIds         the member ids
         * @param startTime         the start time
         * @param userId            the user id
         * @param ingestEndpoint    the ingest endpoint for the single streams
         * @param streamKey         the stream key for the single streams
         * @param hdBroadcast       whether stream is hd or not
         * @param restream          whether to restream given broadcast or not
         * @param rtcToken          the rtc token for the multilive broadcast
         * @param restreamEndpoints the list of restream endpoints
         * @param productsLinked    whether products are linked or not to given broadcast
         * @param productsCount     number of products linked to the broadcast
         * @param rtmpIngestUrl     RTMP ingest url
         */
        void onBroadcastStarted(String streamId, String streamDescription, String streamImageUrl,
                                ArrayList<String> memberIds, long startTime, String userId, String ingestEndpoint,
                                String streamKey, boolean hdBroadcast, boolean restream, String rtcToken,
                                ArrayList<String> restreamEndpoints, boolean productsLinked, int productsCount, String rtmpIngestUrl);

        void noRestreamChannelsFound();
    }
}
