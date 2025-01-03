package io.isometrik.gs.services;

import java.util.Map;

import io.isometrik.gs.response.stream.FetchStreamsResult;
import io.isometrik.gs.response.stream.StartStreamResult;
import io.isometrik.gs.response.stream.StopStreamResult;
import io.isometrik.gs.response.stream.UpdateStreamDetailsResult;
import io.isometrik.gs.response.stream.UpdateStreamPublishingStatusResult;
import io.isometrik.gs.response.stream.UpdateUserPublishingStatusResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;

/**
 * The interface stream service to start or stop streaming, fetch stream groups and update user's
 * publishing status in a stream group or in all streams of which he is a viewer or was publishing
 * in.
 */
public interface StreamService {

  /**
   * Start streaming call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<StartStreamResult>
   * @see StartStreamResult
   */
  @POST("/live/v1/stream")
  Call<StartStreamResult> startStream(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Stop streaming call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<StopStreamResult>
   * @see StopStreamResult
   */
  @PATCH("/live/v1/stream")
  Call<StopStreamResult> stopStream(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Update publishing status of a member in a stream group call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<UpdateStreamPublishingStatusResult>
   * @see UpdateStreamPublishingStatusResult
   */
  @PUT("/streaming/v2/publish")
  Call<UpdateStreamPublishingStatusResult> updatePublishingStatus(
      @HeaderMap Map<String, String> headers, @Body Map<String, Object> bodyParams);

  /**
   * Update user's publishing status in a stream group call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<UpdateUserPublishingStatusResult>
   * @see UpdateUserPublishingStatusResult
   */
  @PUT("/streaming/v2/publish/user")
  Call<UpdateUserPublishingStatusResult> updateUserPublishingStatus(
      @HeaderMap Map<String, String> headers, @Body Map<String, Object> bodyParams);

  /**
   * Fetch streams call.
   *
   * @param headers the headers
   * @return the Call<FetchStreamsResult>
   * @see FetchStreamsResult
   */
  @GET("/live/v1/streams")
  Call<FetchStreamsResult> fetchStreams(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Update stream details.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<UpdateStreamDetailsResult>
   * @see UpdateStreamDetailsResult
   */
  @PATCH("/live/v1/stream/details")
  Call<UpdateStreamDetailsResult> updateStreamDetails(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);
}
