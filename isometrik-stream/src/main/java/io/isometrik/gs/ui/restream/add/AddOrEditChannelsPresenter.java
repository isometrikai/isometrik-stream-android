package io.isometrik.gs.ui.restream.add;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.restream.AddRestreamChannelQuery;
import io.isometrik.gs.builder.restream.UpdateRestreamChannelQuery;

/**
 * The AddOrEditChannels presenter adds or updates a restreaming channel for a user.
 * It implements AddOrEditChannelsContract.Presenter{@link AddOrEditChannelsContract.Presenter}
 *
 * @see AddOrEditChannelsContract.Presenter
 */
public class AddOrEditChannelsPresenter implements AddOrEditChannelsContract.Presenter {
  AddOrEditChannelsPresenter() {

  }

  private AddOrEditChannelsContract.View addOrEditChannelsView;
  private final Isometrik isometrik = IsometrikStreamSdk.getInstance().getIsometrik();

  @Override
  public void attachView(AddOrEditChannelsContract.View addOrEditChannelsView) {
    this.addOrEditChannelsView = addOrEditChannelsView;
  }

  @Override
  public void detachView() {
    this.addOrEditChannelsView = null;
  }

  @Override
  public void addChannel(String channelName, String rtmpUrl, String streamKey, boolean enable,
      int channelType) {
    isometrik.getRemoteUseCases()
        .getRestreamingUseCases()
        .addRestreamChannel(new AddRestreamChannelQuery.Builder().setEnabled(enable)
            .setChannelName(channelName)
            .setChannelType(channelType)
            .setIngestUrl(rtmpUrl + "/" + streamKey)
            .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
            .build(), (var1, var2) -> {

          if (var1 != null) {
            if (addOrEditChannelsView != null) {
              addOrEditChannelsView.onChannelCreatedSuccessfully(var1.getChannelId());
            }
          } else {
            if (addOrEditChannelsView != null) {
              addOrEditChannelsView.onError(var2.getErrorMessage());
            }
          }
        });
  }

  @Override
  public void updateChannel(String channelId, String channelName, String rtmpUrl, String streamKey,
      boolean enable, int channelType) {
    isometrik.getRemoteUseCases()
        .getRestreamingUseCases()
        .updateRestreamChannel(new UpdateRestreamChannelQuery.Builder().setEnabled(enable)
            .setChannelName(channelName)
            .setChannelType(channelType)
            .setIngestUrl(rtmpUrl + "/" + streamKey)
            .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
            .setChannelId(channelId)
            .build(), (var1, var2) -> {

          if (var1 != null) {
            if (addOrEditChannelsView != null) {
              addOrEditChannelsView.onChannelEditedSuccessfully();
            }
          } else {
            if (addOrEditChannelsView != null) {
              addOrEditChannelsView.onError(var2.getErrorMessage());
            }
          }
        });
  }

  @Override
  public void validateChannelDetails(String channelName, String rtmpUrl, String streamKey) {
    if (addOrEditChannelsView != null) {
      if (channelName.isEmpty()) {
        addOrEditChannelsView.channelDetailsValidationResult(false,
            IsometrikStreamSdk.getInstance().getContext().getString(R.string.ism_empty_channel_name));
      } else if (rtmpUrl.isEmpty()) {
        addOrEditChannelsView.channelDetailsValidationResult(false,
            IsometrikStreamSdk.getInstance().getContext().getString(R.string.ism_empty_rtmp_url));
      } else if (streamKey.isEmpty()) {
        addOrEditChannelsView.channelDetailsValidationResult(false,
            IsometrikStreamSdk.getInstance().getContext().getString(R.string.ism_empty_stream_key));
      } else {
        addOrEditChannelsView.channelDetailsValidationResult(true, null);
      }
    }
  }
}
