package io.isometrik.gs.ui.restream.list;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.restream.DeleteRestreamChannelQuery;
import io.isometrik.gs.builder.restream.FetchRestreamChannelsQuery;
import io.isometrik.gs.builder.restream.UpdateAllRestreamChannelsQuery;
import io.isometrik.gs.builder.restream.UpdateRestreamChannelQuery;
import io.isometrik.gs.response.restream.FetchRestreamChannelsResult;
import java.util.ArrayList;

/**
 * The FetchChannelsContract presenter fetches and deletes a restreaming channel for a user.
 * It implements FetchChannelsContract.Presenter{@link FetchChannelsContract.Presenter}
 *
 * @see FetchChannelsContract.Presenter
 */
public class FetchChannelsPresenter implements FetchChannelsContract.Presenter {
  FetchChannelsPresenter() {

  }

  private FetchChannelsContract.View fetchChannelsView;
  private final Isometrik isometrik = IsometrikStreamSdk.getInstance().getIsometrik();

  @Override
  public void attachView(FetchChannelsContract.View fetchChannelsView) {
    this.fetchChannelsView = fetchChannelsView;
  }

  @Override
  public void detachView() {
    this.fetchChannelsView = null;
  }

  @Override
  public void fetchRestreamChannels() {
    isometrik.getRemoteUseCases()
        .getRestreamingUseCases()
        .fetchRestreamChannels(new FetchRestreamChannelsQuery.Builder().setUserToken(
            IsometrikStreamSdk.getInstance().getUserSession().getUserToken()).build(), (var1, var2) -> {

          if (var1 != null) {

            ArrayList<ChannelsModel> channelsModels = new ArrayList<>();

            ArrayList<FetchRestreamChannelsResult.RestreamChannel> channels =
                var1.getRestreamChannels();
            int size = channels.size();

            for (int i = 0; i < size; i++) {

              channelsModels.add(new ChannelsModel(channels.get(i)));
            }

            if (fetchChannelsView != null) {
              fetchChannelsView.onRestreamChannelsFetched(channelsModels);
            }
          } else if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {
            if (fetchChannelsView != null) {
              fetchChannelsView.onRestreamChannelsFetched(new ArrayList<>());
            }
          } else {
            if (fetchChannelsView != null) {
              fetchChannelsView.onError(var2.getErrorMessage());
            }
          }
        });
  }

  @Override
  public void deleteRestreamChannel(String channelId) {
    isometrik.getRemoteUseCases()
        .getRestreamingUseCases()
        .deleteRestreamChannel(new DeleteRestreamChannelQuery.Builder().setChannelId(channelId)
            .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
            .build(), (var1, var2) -> {

          if (var1 != null) {
            if (fetchChannelsView != null) {
              fetchChannelsView.onRestreamChannelDeletedSuccessfully(channelId);
            }
          } else {
            if (fetchChannelsView != null) {
              fetchChannelsView.onError(var2.getErrorMessage());
            }
          }
        });
  }

  @Override
  public void toggleAllRestreamChannels(boolean enable) {
    isometrik.getRemoteUseCases()
        .getRestreamingUseCases()
        .updateAllRestreamChannels(new UpdateAllRestreamChannelsQuery.Builder().setEnabled(enable)
            .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
            .build(), (var1, var2) -> {

          if (var1 != null) {
            if (fetchChannelsView != null) {
              fetchChannelsView.onRestreamChannelsToggledSuccessfully(enable);
            }
          } else {
            if (fetchChannelsView != null) {
              fetchChannelsView.onFailedToToggleRestreamChannels(var2.getErrorMessage());
            }
          }
        });
  }

  @Override
  public void toggleRestreamChannel(String channelId, boolean enable) {
    isometrik.getRemoteUseCases()
        .getRestreamingUseCases()
        .updateRestreamChannel(new UpdateRestreamChannelQuery.Builder().setEnabled(enable)
            .setChannelId(channelId)
            .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
            .build(), (var1, var2) -> {

          if (var1 != null) {
            if (fetchChannelsView != null) {
              fetchChannelsView.onRestreamChannelToggledSuccessfully(channelId);
            }
          } else {
            if (fetchChannelsView != null) {
              fetchChannelsView.onFailedToToggleRestreamChannel(channelId, var2.getErrorMessage());
            }
          }
        });
  }
}
