package io.isometrik.gs.ui.restream.add;

import io.isometrik.gs.ui.utils.BasePresenter;

/**
 * The interface AddOrEditChannels contract containing interfaces Presenter and View to be
 * implemented
 * by the
 * AddOrEditChannelsPresenter{@link AddOrEditChannelsPresenter} and
 * AddOrEditChannelsFragment{@link AddOrEditChannelsFragment} respectively.
 *
 * @see AddOrEditChannelsPresenter
 * @see AddOrEditChannelsFragment
 */
public interface AddOrEditChannelsContract {

  /**
   * The interface AddOrEditChannelsContract.Presenter to be implemented by
   * AddOrEditChannelsPresenter{@link
   * AddOrEditChannelsPresenter}
   *
   * @see AddOrEditChannelsPresenter
   */
  interface Presenter extends BasePresenter<AddOrEditChannelsContract.View> {

    void addChannel(String channelName, String rtmpUrl, String streamKey, boolean enable,
        int channelType);

    void updateChannel(String channelId, String channelName, String rtmpUrl, String streamKey,
        boolean enable, int channelType);

    void validateChannelDetails(String channelName, String rtmpUrl, String streamKey);
  }

  /**
   * The interface AddOrEditChannelsContract.View to be implemented by
   * AddOrEditChannelsFragment{@link
   * AddOrEditChannelsFragment}
   *
   * @see AddOrEditChannelsFragment
   */
  interface View {

    void channelDetailsValidationResult(boolean valid, String error);

    void onChannelCreatedSuccessfully(String channelId);

    void onChannelEditedSuccessfully();

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);
  }
}
