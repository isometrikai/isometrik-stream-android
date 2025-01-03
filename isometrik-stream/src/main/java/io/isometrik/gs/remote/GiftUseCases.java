package io.isometrik.gs.remote;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.gift.GiftByCategoryQuery;
import io.isometrik.gs.builder.gift.GiftCategoriesQuery;
import io.isometrik.gs.builder.gift.GiftSendQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.ui.gifts.giftCategories.GiftsByCategory;
import io.isometrik.gs.ui.gifts.giftCategories.GiftsSend;
import io.isometrik.gs.ui.gifts.response.GiftCategoriesResponse;
import io.isometrik.gs.ui.gifts.giftCategories.GiftCategories;
import io.isometrik.gs.ui.gifts.response.GiftSentResponse;
import io.isometrik.gs.ui.gifts.response.GiftsByCategoryResponse;

/**
 * Classes containing use cases for various users operations, allowing ui sdk to communicate with
 * the remote backend using respective model classes.
 */
public class GiftUseCases {

  /**
   * Model classes for user
   */
  private final GiftCategories giftCategories;
  private final GiftsByCategory giftsByCategory;
  private final GiftsSend giftsSend;

  private final IMConfiguration configuration;
  private final RetrofitManager retrofitManager;
  private final BaseResponse baseResponse;
  private final Gson gson;

  public GiftUseCases(IMConfiguration imConfiguration, RetrofitManager retrofitManager,
                      BaseResponse baseResponse, Gson gson) {
    configuration = imConfiguration;
    this.retrofitManager = retrofitManager;
    this.gson = gson;
    this.baseResponse = baseResponse;

    giftCategories = new GiftCategories();
    giftsByCategory = new GiftsByCategory();
    giftsSend = new GiftsSend();

  }

  /**
   * Fetch gift categories.
   *
   * @param giftCategoriesQuery the fetch users query
   * @param completionHandler the completion handler
   */
  public void fetchGiftCategories(@NotNull GiftCategoriesQuery giftCategoriesQuery,
                         @NotNull CompletionHandler<GiftCategoriesResponse> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(false);
    if (isometrikError == null) {
      giftCategories.validateParams(giftCategoriesQuery, completionHandler, retrofitManager, configuration,
              baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }



  /**
   * Fetch gift by category.
   *
   * @param giftByCategoryQuery the fetch user details query
   * @param completionHandler the completion handler
   */
  public void fetchGiftByCategory(@NotNull GiftByCategoryQuery giftByCategoryQuery,
      @NotNull CompletionHandler<GiftsByCategoryResponse> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      giftsByCategory.validateParams(giftByCategoryQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * send gift to streamer.
   *
   * @param giftSendQuery the fetch user details query
   * @param completionHandler the completion handler
   */
  public void sendGiftToStreamer(@NotNull GiftSendQuery giftSendQuery,
                                  @NotNull CompletionHandler<GiftSentResponse> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      giftsSend.validateParams(giftSendQuery, completionHandler, retrofitManager,
              configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

}
