package io.isometrik.gs.services;

import java.util.Map;

import io.isometrik.gs.response.viewer.AddViewerResult;
import io.isometrik.gs.response.viewer.FetchViewersCountResult;
import io.isometrik.gs.response.viewer.FetchViewersResult;
import io.isometrik.gs.response.viewer.LeaveViewerResult;
import io.isometrik.gs.response.viewer.RemoveViewerResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * The interface viewer service to join,leave a stream group as viewer,remove a viewer, fetch
 * viewers and viewers count in a stream group.
 */
public interface ViewerService {

  /**
   * Join stream group as viewer call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<AddViewerResult>
   * @see AddViewerResult
   */
  @POST("/streaming/v2/viewer")
  Call<AddViewerResult> addViewer(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Leave as viewer from a stream group call.
   *
   * @param headers the headers
   * @return the Call<LeaveViewerResult>
   * @see LeaveViewerResult
   */
  @DELETE("/streaming/v2/viewer/leave")
  Call<LeaveViewerResult> leaveViewer(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Remove a viewer from a stream group call.
   *
   * @param headers the headers
   * @return the Call<RemoveViewerResult>
   * @see RemoveViewerResult
   */
  @DELETE("/streaming/v2/viewer")
  Call<RemoveViewerResult> removeViewer(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch viewers in a stream group call.
   *
   * @param headers the headers
   * @return the Call<FetchViewersResult>
   * @see FetchViewersResult
   */
  @GET("/streaming/v2/viewers")
  Call<FetchViewersResult> fetchViewers(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch viewers count in a stream group call.
   *
   * @param headers the headers
   * @return the Call<FetchViewersCountResult>
   * @see FetchViewersCountResult
   */
  @GET("/streaming/v2/viewers/count")
  Call<FetchViewersCountResult> fetchViewersCount(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);
}
