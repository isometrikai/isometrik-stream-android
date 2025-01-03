package io.isometrik.gs.remote;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.recording.FetchRecordingsQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.models.recording.FetchRecordings;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.recording.FetchRecordingsResult;

/**
 * Classes containing use cases for various recordings operations, allowing ui sdk to communicate
 * with the remote backend using respective model classes.
 */
public class RecordingsUseCases {

  /**
   * Model classes for recordings
   */
  private final FetchRecordings fetchRecordings;

  private final IMConfiguration configuration;
  private final RetrofitManager retrofitManager;
  private final BaseResponse baseResponse;
  private final Gson gson;

  public RecordingsUseCases(IMConfiguration imConfiguration, RetrofitManager retrofitManager,
      BaseResponse baseResponse, Gson gson) {
    configuration = imConfiguration;
    this.retrofitManager = retrofitManager;
    this.gson = gson;
    this.baseResponse = baseResponse;

    fetchRecordings = new FetchRecordings();
  }

  /**
   * Fetch user's stream recordings.
   *
   * @param fetchRecordingsQuery the fetch recordings query
   * @param completionHandler the completion handler
   */
  public void fetchRecordings(@NotNull FetchRecordingsQuery fetchRecordingsQuery,
      @NotNull CompletionHandler<FetchRecordingsResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchRecordings.validateParams(fetchRecordingsQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }
}
