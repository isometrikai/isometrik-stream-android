package io.isometrik.gs.remote;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.viewer.AddViewerQuery;
import io.isometrik.gs.builder.viewer.FetchViewersCountQuery;
import io.isometrik.gs.builder.viewer.FetchViewersQuery;
import io.isometrik.gs.builder.viewer.LeaveViewerQuery;
import io.isometrik.gs.builder.viewer.RemoveViewerQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.models.viewer.AddViewer;
import io.isometrik.gs.models.viewer.FetchViewers;
import io.isometrik.gs.models.viewer.FetchViewersCount;
import io.isometrik.gs.models.viewer.LeaveViewer;
import io.isometrik.gs.models.viewer.RemoveViewer;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.viewer.AddViewerResult;
import io.isometrik.gs.response.viewer.FetchViewersCountResult;
import io.isometrik.gs.response.viewer.FetchViewersResult;
import io.isometrik.gs.response.viewer.LeaveViewerResult;
import io.isometrik.gs.response.viewer.RemoveViewerResult;

/**
 * Classes containing use cases for various viewers operations, allowing ui sdk to communicate with
 * the remote backend using respective model classes.
 */
public class ViewersUseCases {

  /**
   * Model classes for viewer
   */
  private final AddViewer addViewer;
  private final FetchViewers fetchViewers;
  private final FetchViewersCount fetchViewersCount;
  private final LeaveViewer leaveViewer;
  private final RemoveViewer removeViewer;

  private final IMConfiguration configuration;
  private final RetrofitManager retrofitManager;
  private final BaseResponse baseResponse;
  private final Gson gson;

  public ViewersUseCases(IMConfiguration imConfiguration, RetrofitManager retrofitManager,
      BaseResponse baseResponse, Gson gson) {

    configuration = imConfiguration;
    this.retrofitManager = retrofitManager;
    this.gson = gson;
    this.baseResponse = baseResponse;

    addViewer = new AddViewer();
    fetchViewers = new FetchViewers();
    leaveViewer = new LeaveViewer();
    removeViewer = new RemoveViewer();
    fetchViewersCount = new FetchViewersCount();
  }

  /**
   * Add viewer.
   *
   * @param addViewerQuery the add viewer query
   * @param completionHandler the completion handler
   */
  public void addViewer(@NotNull AddViewerQuery addViewerQuery,
      @NotNull CompletionHandler<AddViewerResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      addViewer.validateParams(addViewerQuery, completionHandler, retrofitManager, configuration,
          baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch viewers.
   *
   * @param fetchViewersQuery the fetch viewers query
   * @param completionHandler the completion handler
   */
  public void fetchViewers(@NotNull FetchViewersQuery fetchViewersQuery,
      @NotNull CompletionHandler<FetchViewersResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchViewers.validateParams(fetchViewersQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch viewers count.
   *
   * @param fetchViewersCountQuery the fetch viewers count query
   * @param completionHandler the completion handler
   */
  public void fetchViewersCount(@NotNull FetchViewersCountQuery fetchViewersCountQuery,
      @NotNull CompletionHandler<FetchViewersCountResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchViewersCount.validateParams(fetchViewersCountQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Leave viewer.
   *
   * @param leaveViewerQuery the leave viewer query
   * @param completionHandler the completion handler
   */
  public void leaveViewer(@NotNull LeaveViewerQuery leaveViewerQuery,
      @NotNull CompletionHandler<LeaveViewerResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      leaveViewer.validateParams(leaveViewerQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Remove viewer.
   *
   * @param removeViewerQuery the remove viewer query
   * @param completionHandler the completion handler
   */
  public void removeViewer(@NotNull RemoveViewerQuery removeViewerQuery,
      @NotNull CompletionHandler<RemoveViewerResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      removeViewer.validateParams(removeViewerQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }
}
