package io.isometrik.gs.services;

import java.util.Map;

import io.isometrik.gs.response.copublish.AcceptCopublishRequestResult;
import io.isometrik.gs.response.copublish.AddCopublishRequestResult;
import io.isometrik.gs.response.copublish.DeleteCopublishRequestResult;
import io.isometrik.gs.response.copublish.DenyCopublishRequestResult;
import io.isometrik.gs.response.copublish.FetchCopublishRequestStatusResult;
import io.isometrik.gs.response.copublish.FetchCopublishRequestsResult;
import io.isometrik.gs.response.copublish.SwitchProfileResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * The interface copublish service to request or delete copublish, accept or deny a user's
 * copublish request or fetch all copublish requests or fetch status of a copublish request in a
 * stream group.
 */
public interface CopublishService {

  /**
   * Add copublish request call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call<AddCopublishRequestResult>
   * @see AddCopublishRequestResult
   */
  @POST("/streaming/v2/copublish/request")
  Call<AddCopublishRequestResult> addCopublishRequest(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Accept copublish request call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call<AcceptCopublishRequestResult>
   * @see AcceptCopublishRequestResult
   */
  @POST("/streaming/v2/copublish/accept")
  Call<AcceptCopublishRequestResult> acceptCopublishRequest(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Deny copublish request call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call<DenyCopublishRequestResult>
   * @see DenyCopublishRequestResult
   */
  @POST("/streaming/v2/copublish/deny")
  Call<DenyCopublishRequestResult> denyCopublishRequest(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Switch profile call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call<SwitchProfileResult>
   * @see SwitchProfileResult
   */
  @POST("/streaming/v2/switchprofile")
  Call<SwitchProfileResult> switchProfile(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Delete copublish request call.
   *
   * @param headers the headers
   * @return Call<DeleteCopublishRequestResult>
   * @see DeleteCopublishRequestResult
   */
  @DELETE("/streaming/v2/copublish/request")
  Call<DeleteCopublishRequestResult> deleteCopublishRequest(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch copublish request status call.
   *
   * @param headers the headers
   * @return the Call<FetchCopublishRequestStatusResult>
   * @see FetchCopublishRequestStatusResult
   */
  @GET("/streaming/v2/copublish/status")
  Call<FetchCopublishRequestStatusResult> fetchCopublishRequestStatus(
      @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch copublish requests call.
   *
   * @param headers the headers
   * @return the Call<FetchCopublishRequestsResult>
   * @see FetchCopublishRequestsResult
   */
  @GET("/streaming/v2/copublish/requests")
  Call<FetchCopublishRequestsResult> fetchCopublishRequests(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);
}
