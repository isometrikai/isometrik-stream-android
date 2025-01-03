package io.isometrik.gs.remote;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.stream.FetchStreamsQuery;
import io.isometrik.gs.builder.stream.StartStreamQuery;
import io.isometrik.gs.builder.stream.StopStreamQuery;
import io.isometrik.gs.builder.stream.UpdateStreamDetailsQuery;
import io.isometrik.gs.builder.stream.UpdateStreamPublishingStatusQuery;
import io.isometrik.gs.builder.stream.UpdateUserPublishingStatusQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.models.stream.FetchStreams;
import io.isometrik.gs.models.stream.StartStream;
import io.isometrik.gs.models.stream.StopStream;
import io.isometrik.gs.models.stream.UpdateStreamDetails;
import io.isometrik.gs.models.stream.UpdateStreamPublishStatus;
import io.isometrik.gs.models.stream.UpdateUserPublishStatus;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.stream.FetchStreamsResult;
import io.isometrik.gs.response.stream.StartStreamResult;
import io.isometrik.gs.response.stream.StopStreamResult;
import io.isometrik.gs.response.stream.UpdateStreamDetailsResult;
import io.isometrik.gs.response.stream.UpdateStreamPublishingStatusResult;
import io.isometrik.gs.response.stream.UpdateUserPublishingStatusResult;

/**
 * Classes containing use cases for various streams operations, allowing ui sdk to communicate with
 * the remote backend using respective model classes.
 */
public class StreamsUseCases {

  /**
   * Model classes for streams
   */
  private final FetchStreams fetchStreams;
  private final StartStream startStream;
  private final StopStream stopStream;
  private final UpdateStreamPublishStatus updateStreamPublishStatus;
  private final UpdateUserPublishStatus updateUserPublishStatus;

  private final UpdateStreamDetails updateStreamDetails;

  private final IMConfiguration configuration;
  private final RetrofitManager retrofitManager;
  private final BaseResponse baseResponse;
  private final Gson gson;

  public StreamsUseCases(IMConfiguration imConfiguration, RetrofitManager retrofitManager,
      BaseResponse baseResponse, Gson gson) {
    configuration = imConfiguration;
    this.retrofitManager = retrofitManager;
    this.gson = gson;
    this.baseResponse = baseResponse;

    fetchStreams = new FetchStreams();
    startStream = new StartStream();
    stopStream = new StopStream();
    updateStreamPublishStatus = new UpdateStreamPublishStatus();
    updateUserPublishStatus = new UpdateUserPublishStatus();
    updateStreamDetails = new UpdateStreamDetails();
  }

  /**
   * Start stream.
   *
   * @param startStreamQuery the start stream query
   * @param completionHandler the completion handler
   */
  public void startStream(@NotNull StartStreamQuery startStreamQuery,
      @NotNull CompletionHandler<StartStreamResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      startStream.validateParams(startStreamQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Stop stream.
   *
   * @param stopStreamQuery the stop stream query
   * @param completionHandler the completion handler
   */
  public void stopStream(@NotNull StopStreamQuery stopStreamQuery,
      @NotNull CompletionHandler<StopStreamResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      stopStream.validateParams(stopStreamQuery, completionHandler, retrofitManager, configuration,
          baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Update stream publishing status.
   *
   * @param updatePublishingStatusQuery the update publishing status query
   * @param completionHandler the completion handler
   */
  public void updateStreamPublishingStatus(
      @NotNull UpdateStreamPublishingStatusQuery updatePublishingStatusQuery,
      @NotNull CompletionHandler<UpdateStreamPublishingStatusResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      updateStreamPublishStatus.validateParams(updatePublishingStatusQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Update user publishing status.
   *
   * @param updateUserPublishingStatusQuery the update user publishing status query
   * @param completionHandler the completion handler
   */
  public void updateUserPublishingStatus(
      @NotNull UpdateUserPublishingStatusQuery updateUserPublishingStatusQuery,
      @NotNull CompletionHandler<UpdateUserPublishingStatusResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      updateUserPublishStatus.validateParams(updateUserPublishingStatusQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Update stream details.
   *
   * @param updateStreamDetailsQuery the update stream details query
   * @param completionHandler the completion handler
   */
  public void updateStreamDetails(@NotNull UpdateStreamDetailsQuery updateStreamDetailsQuery,
      @NotNull CompletionHandler<UpdateStreamDetailsResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      updateStreamDetails.validateParams(updateStreamDetailsQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch streams.
   *
   * @param fetchStreamsQuery the fetch streams query
   * @param completionHandler the completion handler
   */
  public void fetchStreams(@NotNull FetchStreamsQuery fetchStreamsQuery,
      @NotNull CompletionHandler<FetchStreamsResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchStreams.validateParams(fetchStreamsQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }
}
