package io.isometrik.gs.ui.members.select;

import java.util.ArrayList;

/**
 * The interface select members contract for the multi live containing interfaces Presenter and View
 * to be implemented
 * by the
 * MultiLiveSelectMembersPresenter{@link MultiLiveSelectMembersPresenter} and
 * MultiLiveSelectMembersActivity{@link MultiLiveSelectMembersActivity} respectively.
 *
 * @see MultiLiveSelectMembersPresenter
 * @see MultiLiveSelectMembersActivity
 */
public interface MultiLiveSelectMembersContract {

    /**
     * The interface SelectMembersContract.Presenter to be implemented by SelectMembersPresenter{@link
     * MultiLiveSelectMembersPresenter}
     *
     * @see MultiLiveSelectMembersPresenter
     */
    interface Presenter {

        /**
         * Request users data.
         *
         * @param offset          the offset
         * @param refreshRequest  the refresh request
         * @param isSearchRequest whether fetch users request is from the search or not
         * @param searchTag       the user's name to be searched
         */
        void requestUsersData(int offset, boolean refreshRequest, boolean isSearchRequest,
                              String searchTag);

        /**
         * Request users data on scroll.
         *
         * @param firstVisibleItemPosition the first visible item position
         * @param visibleItemCount         the visible item count
         * @param totalItemCount           the total item count
         */
        void requestUsersDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
                                      int totalItemCount);

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

    /**
     * The interface MultiLiveSelectMembersContract.View to be implemented by
     * MultiLiveSelectMembersActivity{@link
     * MultiLiveSelectMembersActivity}
     *
     * @see MultiLiveSelectMembersActivity
     */
    interface View {

        /**
         * On users data received.
         *
         * @param users           the users
         * @param latestUsers     the latest users
         * @param isSearchRequest whether fetch users request is from the search or not
         */
        void onUsersDataReceived(ArrayList<MultiLiveSelectMembersModel> users, boolean latestUsers,
                                 boolean isSearchRequest);

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

        /**
         * On error.
         *
         * @param errorMessage the error message to be shown in the toast for details of the failed
         *                     operation
         */
        void onError(String errorMessage);

        /**
         * Callback when user requested restreaming but hasnot added or enabled any of restream
         * channels.
         */
        void noRestreamChannelsFound();
    }
}
