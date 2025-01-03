package io.isometrik.gs.remote;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.copublish.AcceptCopublishRequestQuery;
import io.isometrik.gs.builder.copublish.AddCopublishRequestQuery;
import io.isometrik.gs.builder.copublish.DeleteCopublishRequestQuery;
import io.isometrik.gs.builder.copublish.DenyCopublishRequestQuery;
import io.isometrik.gs.builder.copublish.FetchCopublishRequestStatusQuery;
import io.isometrik.gs.builder.copublish.FetchCopublishRequestsQuery;
import io.isometrik.gs.builder.copublish.SwitchProfileQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.models.copublish.AcceptCopublishRequest;
import io.isometrik.gs.models.copublish.AddCopublishRequest;
import io.isometrik.gs.models.copublish.DeleteCopublishRequest;
import io.isometrik.gs.models.copublish.DenyCopublishRequest;
import io.isometrik.gs.models.copublish.FetchCopublishRequestStatus;
import io.isometrik.gs.models.copublish.FetchCopublishRequests;
import io.isometrik.gs.models.copublish.SwitchProfile;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.copublish.AcceptCopublishRequestResult;
import io.isometrik.gs.response.copublish.AddCopublishRequestResult;
import io.isometrik.gs.response.copublish.DeleteCopublishRequestResult;
import io.isometrik.gs.response.copublish.DenyCopublishRequestResult;
import io.isometrik.gs.response.copublish.FetchCopublishRequestStatusResult;
import io.isometrik.gs.response.copublish.FetchCopublishRequestsResult;
import io.isometrik.gs.response.copublish.SwitchProfileResult;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.IsometrikError;

/**
 * Classes containing use cases for various copublish operations, allowing ui sdk to communicate
 * with the remote backend using respective model classes.
 */
public class CopublishUseCases {
  /**
   * Model classes for copublish
   */
  private final AcceptCopublishRequest acceptCopublishRequest;
  private final AddCopublishRequest addCopublishRequest;
  private final DenyCopublishRequest denyCopublishRequest;
  private final SwitchProfile switchProfile;
  private final DeleteCopublishRequest deleteCopublishRequest;
  private final FetchCopublishRequests fetchCopublishRequests;
  private final FetchCopublishRequestStatus fetchCopublishRequestStatus;

  private final IMConfiguration configuration;
  private final RetrofitManager retrofitManager;
  private final BaseResponse baseResponse;
  private final Gson gson;

  public CopublishUseCases(IMConfiguration imConfiguration, RetrofitManager retrofitManager,
      BaseResponse baseResponse, Gson gson) {
    configuration = imConfiguration;
    this.retrofitManager = retrofitManager;
    this.gson = gson;
    this.baseResponse = baseResponse;

    acceptCopublishRequest = new AcceptCopublishRequest();
    addCopublishRequest = new AddCopublishRequest();
    denyCopublishRequest = new DenyCopublishRequest();
    switchProfile = new SwitchProfile();
    deleteCopublishRequest = new DeleteCopublishRequest();
    fetchCopublishRequests = new FetchCopublishRequests();
    fetchCopublishRequestStatus = new FetchCopublishRequestStatus();
  }

  /**
   * Accept copublish request.
   *
   * @param acceptCopublishRequestQuery the accept copublish request query
   * @param completionHandler the completion handler
   */
  public void acceptCopublishRequest(
      @NotNull AcceptCopublishRequestQuery acceptCopublishRequestQuery,
      @NotNull CompletionHandler<AcceptCopublishRequestResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      acceptCopublishRequest.validateParams(acceptCopublishRequestQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Add copublish request.
   *
   * @param addCopublishRequestQuery the add copublish request query
   * @param completionHandler the completion handler
   */
  public void addCopublishRequest(@NotNull AddCopublishRequestQuery addCopublishRequestQuery,
      @NotNull CompletionHandler<AddCopublishRequestResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      addCopublishRequest.validateParams(addCopublishRequestQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Deny copublish request.
   *
   * @param denyCopublishRequestQuery the deny copublish request query
   * @param completionHandler the completion handler
   */
  public void denyCopublishRequest(@NotNull DenyCopublishRequestQuery denyCopublishRequestQuery,
      @NotNull CompletionHandler<DenyCopublishRequestResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      denyCopublishRequest.validateParams(denyCopublishRequestQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Switch profile from viewer to a broadcaster.
   *
   * @param switchProfileQuery the switch profile query
   * @param completionHandler the completion handler
   */
  public void switchProfile(@NotNull SwitchProfileQuery switchProfileQuery,
      @NotNull CompletionHandler<SwitchProfileResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      switchProfile.validateParams(switchProfileQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Delete copublish request from a stream group.
   *
   * @param deleteCopublishRequestQuery the delete copublish request query
   * @param completionHandler the completion handler
   */
  public void deleteCopublishRequest(
      @NotNull DeleteCopublishRequestQuery deleteCopublishRequestQuery,
      @NotNull CompletionHandler<DeleteCopublishRequestResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      deleteCopublishRequest.validateParams(deleteCopublishRequestQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch copublish requests in a stream group.
   *
   * @param fetchCopublishRequestsQuery the fetch copublish requests query
   * @param completionHandler the completion handler
   */
  public void fetchCopublishRequests(
      @NotNull FetchCopublishRequestsQuery fetchCopublishRequestsQuery,
      @NotNull CompletionHandler<FetchCopublishRequestsResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchCopublishRequests.validateParams(fetchCopublishRequestsQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch copublish request status in a stream group.
   *
   * @param fetchCopublishRequestStatusQuery the fetch copublish request status query
   * @param completionHandler the completion handler
   */
  public void fetchCopublishRequestStatus(
      @NotNull FetchCopublishRequestStatusQuery fetchCopublishRequestStatusQuery,
      @NotNull CompletionHandler<FetchCopublishRequestStatusResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchCopublishRequestStatus.validateParams(fetchCopublishRequestStatusQuery,
          completionHandler, retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }
}
