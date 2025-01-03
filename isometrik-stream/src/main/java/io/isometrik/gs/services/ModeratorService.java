package io.isometrik.gs.services;

import java.util.Map;

import io.isometrik.gs.response.moderator.AddModeratorResult;
import io.isometrik.gs.response.moderator.FetchModeratorsResult;
import io.isometrik.gs.response.moderator.LeaveModeratorResult;
import io.isometrik.gs.response.moderator.RemoveModeratorResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * The interface moderator service to add,remove moderator,fetch moderators in stream group and
 * leave a
 * stream group by a moderator.
 */
public interface ModeratorService {

  /**
   * Add moderator to a stream group call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<AddModeratorResult>
   * @see AddModeratorResult
   */
  @POST("/streaming/v2/moderator")
  Call<AddModeratorResult> addModerator(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Remove moderator from a stream group call.
   *
   * @param headers the headers
   * @return the Call<RemoveModeratorResult>
   * @see RemoveModeratorResult
   */
  @DELETE("/streaming/v2/moderator")
  Call<RemoveModeratorResult> removeModerator(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch moderators in a stream group call.
   *
   * @param headers the headers
   * @return the Call<FetchModeratorsResult>
   * @see FetchModeratorsResult
   */
  @GET("/streaming/v2/moderators")
  Call<FetchModeratorsResult> fetchModerators(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Leave stream group by a moderator call.
   *
   * @param headers the headers
   * @return Call<LeaveModeratorResult>
   * @see LeaveModeratorResult
   */
  @DELETE("/streaming/v2/moderator/leave")
  Call<LeaveModeratorResult> leaveModerator(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);
}
