package io.isometrik.gs.ui.restream.list;

import io.isometrik.gs.ui.utils.BasePresenter;
import java.util.ArrayList;

/**
 * The interface FetchChannels contract containing interfaces Presenter and View to be
 * implemented
 * by the
 * FetchChannelsPresenter{@link FetchChannelsPresenter} and
 * FetchChannelsFragment{@link FetchChannelsFragment} respectively.
 *
 * @see FetchChannelsPresenter
 * @see FetchChannelsFragment
 */
public interface FetchChannelsContract {
  /**
   * The interface FetchChannelsContract.Presenter to be implemented by
   * FetchChannelsPresenter{@link
   * FetchChannelsPresenter}
   *
   * @see FetchChannelsPresenter
   */
  interface Presenter extends BasePresenter<FetchChannelsContract.View> {

    void fetchRestreamChannels();

    void deleteRestreamChannel(String channelId);

    void toggleRestreamChannel(String channelId, boolean enable);

    void toggleAllRestreamChannels(boolean enable);
  }

  /**
   * The interface FetchChannelsContract.View to be implemented by
   * FetchChannelsFragment{@link
   * FetchChannelsFragment}
   *
   * @see FetchChannelsFragment
   */
  interface View {

    void onFailedToToggleRestreamChannels(String errorMessage);

    void onRestreamChannelsToggledSuccessfully(boolean enable);

    void onRestreamChannelDeletedSuccessfully(String channelId);

    void onRestreamChannelToggledSuccessfully(String channelId);

    void onFailedToToggleRestreamChannel(String channelId, String errorMessage);

    void onRestreamChannelsFetched(ArrayList<ChannelsModel> restreamChannels);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);
  }
}
