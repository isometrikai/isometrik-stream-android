package io.isometrik.gs.services;

import java.util.Map;

import io.isometrik.gs.ui.gifts.response.GiftCategoriesResponse;
import io.isometrik.gs.ui.gifts.response.GiftSentResponse;
import io.isometrik.gs.ui.gifts.response.GiftsByCategoryResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * The interface GiftService to fetch gift categories and send gift.
 */
public interface GiftService {

    /**
     * Fetch gift categories call.
     *
     * @param headers the headers
     * @return the Call<FetchRecordingsResult>
     * @see GiftCategoriesResponse
     */
    @GET("/v1/app/giftGroup")
    Call<GiftCategoriesResponse> fetchGiftCategories(@HeaderMap Map<String, String> headers,
                                                     @QueryMap Map<String, Object> queryParams);

    @GET("/v1/app/virtualGifts")
    Call<GiftsByCategoryResponse> fetchGiftByCategory(@HeaderMap Map<String, String> headers,
                                                      @QueryMap Map<String, Object> queryParams);

    @POST("/live/v4/giftTransfer")
    Call<GiftSentResponse> sendGiftToStreamer(@HeaderMap Map<String, String> headers,
                                              @Body Map<String, Object> bodyParams);
}
