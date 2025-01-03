package io.isometrik.gs.services;

import java.util.Map;

import io.isometrik.gs.response.subscription.AddSubscriptionResult;
import io.isometrik.gs.response.subscription.RemoveSubscriptionResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.HeaderMap;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;

/**
 * The interface subscription service to add or remove subscriptions for reciving stream presence
 * events on stream start or stop.
 */
public interface SubscriptionService {

  /**
   * Add subscription for stream start or stop event call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<AddSubscriptionResult>
   * @see AddSubscriptionResult
   */
  @PUT("/gs/v2/subscription")
  Call<AddSubscriptionResult> addSubscription(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Remove subscription for stream start or stop event call.
   *
   * @param headers the headers
   * @return the Call<RemoveSubscriptionResult>
   * @see RemoveSubscriptionResult
   */
  @DELETE("/gs/v2/subscription")
  Call<RemoveSubscriptionResult> removeSubscription(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);
}
