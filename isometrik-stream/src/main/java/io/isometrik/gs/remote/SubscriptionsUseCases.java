package io.isometrik.gs.remote;

import com.google.gson.Gson;
import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.subscription.AddSubscriptionQuery;
import io.isometrik.gs.builder.subscription.RemoveSubscriptionQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.models.subscription.AddSubscription;
import io.isometrik.gs.models.subscription.RemoveSubscription;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.subscription.AddSubscriptionResult;
import io.isometrik.gs.response.subscription.RemoveSubscriptionResult;
import org.jetbrains.annotations.NotNull;

/**
 * Classes containing use cases for various subscriptions operations, allowing ui sdk to communicate
 * with
 * the remote backend using respective model classes.
 */
public class SubscriptionsUseCases {

  /**
   * Model classes for subscription
   */
  private final AddSubscription addSubscription;
  private final RemoveSubscription removeSubscription;

  private final IMConfiguration configuration;
  private final RetrofitManager retrofitManager;
  private final BaseResponse baseResponse;
  private final Gson gson;

  public SubscriptionsUseCases(IMConfiguration imConfiguration, RetrofitManager retrofitManager,
      BaseResponse baseResponse, Gson gson) {
    configuration = imConfiguration;
    this.retrofitManager = retrofitManager;
    this.gson = gson;
    this.baseResponse = baseResponse;

    addSubscription = new AddSubscription();
    removeSubscription = new RemoveSubscription();
  }

  /**
   * Add subscription.
   *
   * @param addSubscriptionQuery the add subscription query
   * @param completionHandler the completion handler
   */
  public void addSubscription(@NotNull AddSubscriptionQuery addSubscriptionQuery,
      @NotNull CompletionHandler<AddSubscriptionResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      addSubscription.validateParams(addSubscriptionQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Remove subscription.
   *
   * @param removeSubscriptionQuery the remove subscription query
   * @param completionHandler the completion handler
   */
  public void removeSubscription(@NotNull RemoveSubscriptionQuery removeSubscriptionQuery,
      @NotNull CompletionHandler<RemoveSubscriptionResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      removeSubscription.validateParams(removeSubscriptionQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }
}
