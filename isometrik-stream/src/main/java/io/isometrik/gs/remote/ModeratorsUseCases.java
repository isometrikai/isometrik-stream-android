package io.isometrik.gs.remote;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.moderator.AddModeratorQuery;
import io.isometrik.gs.builder.moderator.FetchModeratorsQuery;
import io.isometrik.gs.builder.moderator.LeaveModeratorQuery;
import io.isometrik.gs.builder.moderator.RemoveModeratorQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.models.moderator.AddModerator;
import io.isometrik.gs.models.moderator.FetchModerators;
import io.isometrik.gs.models.moderator.LeaveModerator;
import io.isometrik.gs.models.moderator.RemoveModerator;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.moderator.AddModeratorResult;
import io.isometrik.gs.response.moderator.FetchModeratorsResult;
import io.isometrik.gs.response.moderator.LeaveModeratorResult;
import io.isometrik.gs.response.moderator.RemoveModeratorResult;

/**
 * Classes containing use cases for various moderators operations, allowing ui sdk to communicate
 * with the remote backend using respective model classes.
 */
public class ModeratorsUseCases {
  /**
   * Model classes for moderators
   */
  private final AddModerator addModerator;
  private final FetchModerators fetchModerators;
  private final LeaveModerator leaveModerator;
  private final RemoveModerator removeModerator;

  private final IMConfiguration configuration;
  private final RetrofitManager retrofitManager;
  private final BaseResponse baseResponse;
  private final Gson gson;

  public ModeratorsUseCases(IMConfiguration imConfiguration, RetrofitManager retrofitManager,
      BaseResponse baseResponse, Gson gson) {
    configuration = imConfiguration;
    this.retrofitManager = retrofitManager;
    this.gson = gson;
    this.baseResponse = baseResponse;

    addModerator = new AddModerator();
    fetchModerators = new FetchModerators();
    leaveModerator = new LeaveModerator();
    removeModerator = new RemoveModerator();
  }

  /**
   * Add moderator.
   *
   * @param addModeratorQuery the add moderator query
   * @param completionHandler the completion handler
   */
  public void addModerator(@NotNull AddModeratorQuery addModeratorQuery,
      @NotNull CompletionHandler<AddModeratorResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      addModerator.validateParams(addModeratorQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch moderators.
   *
   * @param fetchModeratorsQuery the fetch moderators query
   * @param completionHandler the completion handler
   */
  public void fetchModerators(@NotNull FetchModeratorsQuery fetchModeratorsQuery,
      @NotNull CompletionHandler<FetchModeratorsResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchModerators.validateParams(fetchModeratorsQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Leave moderator.
   *
   * @param leaveModeratorQuery the leave moderator query
   * @param completionHandler the completion handler
   */
  public void leaveModerator(@NotNull LeaveModeratorQuery leaveModeratorQuery,
      @NotNull CompletionHandler<LeaveModeratorResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      leaveModerator.validateParams(leaveModeratorQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Remove moderator.
   *
   * @param removeModeratorQuery the remove moderator query
   * @param completionHandler the completion handler
   */
  public void removeModerator(@NotNull RemoveModeratorQuery removeModeratorQuery,
      @NotNull CompletionHandler<RemoveModeratorResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      removeModerator.validateParams(removeModeratorQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }
}
