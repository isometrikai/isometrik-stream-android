package io.isometrik.gs.services;

import java.util.Map;

import io.isometrik.gs.response.restream.AddRestreamChannelResult;
import io.isometrik.gs.response.restream.DeleteRestreamChannelResult;
import io.isometrik.gs.response.restream.FetchRestreamChannelsResult;
import io.isometrik.gs.response.restream.UpdateAllRestreamChannelsResult;
import io.isometrik.gs.response.restream.UpdateRestreamChannelResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface RestreamService {

  /**
   * Add restream channel.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<AddRestreamChannelResult>
   * @see AddRestreamChannelResult
   */
  @POST("/streaming/v2/restream/channel")
  Call<AddRestreamChannelResult> addRestreamChannel(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Update restream channel.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<UpdateRestreamChannelResult>
   * @see UpdateRestreamChannelResult
   */
  @PATCH("/streaming/v2/restream/channel")
  Call<UpdateRestreamChannelResult> updateRestreamChannel(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Delete restream channel.
   *
   * @param headers the headers
   * @return the Call<DeleteRestreamChannelResult>
   * @see DeleteRestreamChannelResult
   */
  @DELETE("/streaming/v2/restream/channel")
  Call<DeleteRestreamChannelResult> deleteRestreamChannel(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch restream channels.
   *
   * @param headers the headers
   * @return the Call<FetchRestreamChannelsResult>
   * @see FetchRestreamChannelsResult
   */
  @GET("/streaming/v2/restream/channel")
  Call<FetchRestreamChannelsResult> fetchRestreamChannels(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Update(enable or disable) all restream channels.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<UpdateAllRestreamChannelsResult>
   * @see UpdateAllRestreamChannelsResult
   */
  @PATCH("/streaming/v2/restream/channel/all")
  Call<UpdateAllRestreamChannelsResult> updateAllRestreamChannels(
      @HeaderMap Map<String, String> headers, @Body Map<String, Object> bodyParams);
}
