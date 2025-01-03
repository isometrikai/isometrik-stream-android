package io.isometrik.gs.remote;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.restream.AddRestreamChannelQuery;
import io.isometrik.gs.builder.restream.DeleteRestreamChannelQuery;
import io.isometrik.gs.builder.restream.FetchRestreamChannelsQuery;
import io.isometrik.gs.builder.restream.UpdateAllRestreamChannelsQuery;
import io.isometrik.gs.builder.restream.UpdateRestreamChannelQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.models.restream.AddRestreamChannel;
import io.isometrik.gs.models.restream.DeleteRestreamChannel;
import io.isometrik.gs.models.restream.FetchRestreamChannels;
import io.isometrik.gs.models.restream.UpdateAllRestreamChannels;
import io.isometrik.gs.models.restream.UpdateRestreamChannel;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.restream.AddRestreamChannelResult;
import io.isometrik.gs.response.restream.DeleteRestreamChannelResult;
import io.isometrik.gs.response.restream.FetchRestreamChannelsResult;
import io.isometrik.gs.response.restream.UpdateAllRestreamChannelsResult;
import io.isometrik.gs.response.restream.UpdateRestreamChannelResult;

/**
 * Classes containing use cases for various restreaming operations, allowing ui sdk to communicate
 * with the remote backend using respective model classes.
 */
public class RestreamingUseCases {

  /**
   * Model classes for restreaming
   */
  private final AddRestreamChannel addRestreamChannel;
  private final UpdateRestreamChannel updateRestreamChannel;
  private final DeleteRestreamChannel deleteRestreamChannel;
  private final UpdateAllRestreamChannels updateAllRestreamChannels;
  private final FetchRestreamChannels fetchRestreamChannels;

  private final IMConfiguration configuration;
  private final RetrofitManager retrofitManager;
  private final BaseResponse baseResponse;
  private final Gson gson;

  public RestreamingUseCases(IMConfiguration imConfiguration, RetrofitManager retrofitManager,
      BaseResponse baseResponse, Gson gson) {
    configuration = imConfiguration;
    this.retrofitManager = retrofitManager;
    this.gson = gson;
    this.baseResponse = baseResponse;

    addRestreamChannel = new AddRestreamChannel();
    updateRestreamChannel = new UpdateRestreamChannel();
    deleteRestreamChannel = new DeleteRestreamChannel();
    updateAllRestreamChannels = new UpdateAllRestreamChannels();
    fetchRestreamChannels = new FetchRestreamChannels();
  }

  /**
   * Adds restream channel for a user.
   *
   * @param addRestreamChannelQuery the add restream channel query
   * @param completionHandler the completion handler
   */
  public void addRestreamChannel(@NotNull AddRestreamChannelQuery addRestreamChannelQuery,
      @NotNull CompletionHandler<AddRestreamChannelResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      addRestreamChannel.validateParams(addRestreamChannelQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Deletes restream channel for a user.
   *
   * @param deleteRestreamChannelQuery the delete restream channel query
   * @param completionHandler the completion handler
   */
  public void deleteRestreamChannel(@NotNull DeleteRestreamChannelQuery deleteRestreamChannelQuery,
      @NotNull CompletionHandler<DeleteRestreamChannelResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      deleteRestreamChannel.validateParams(deleteRestreamChannelQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Updates restream channel for a user.
   *
   * @param updateRestreamChannelQuery the update restream channel query
   * @param completionHandler the completion handler
   */
  public void updateRestreamChannel(@NotNull UpdateRestreamChannelQuery updateRestreamChannelQuery,
      @NotNull CompletionHandler<UpdateRestreamChannelResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      updateRestreamChannel.validateParams(updateRestreamChannelQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Updates all restream channels for a user.
   *
   * @param updateAllRestreamChannelsQuery the update all restream channels query
   * @param completionHandler the completion handler
   */
  public void updateAllRestreamChannels(
      @NotNull UpdateAllRestreamChannelsQuery updateAllRestreamChannelsQuery,
      @NotNull CompletionHandler<UpdateAllRestreamChannelsResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      updateAllRestreamChannels.validateParams(updateAllRestreamChannelsQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Gets all restream channels for a user.
   *
   * @param fetchRestreamChannelsQuery the fetch restream channels query
   * @param completionHandler the completion handler
   */
  public void fetchRestreamChannels(@NotNull FetchRestreamChannelsQuery fetchRestreamChannelsQuery,
      @NotNull CompletionHandler<FetchRestreamChannelsResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchRestreamChannels.validateParams(fetchRestreamChannelsQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }
}
