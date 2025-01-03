package io.isometrik.gs.ui.restream.list;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmBottomsheetRestreamFetchChannelsBinding;
import io.isometrik.gs.ui.restream.RestreamChannelsActionCallback;
import io.isometrik.gs.ui.utils.AlertProgress;
import java.util.ArrayList;

public class FetchChannelsFragment extends BottomSheetDialogFragment
    implements FetchChannelsContract.View {

  public static final String TAG = "FetchChannelsBottomSheetFragment";

  private FetchChannelsContract.Presenter fetchChannelsPresenter;
  private RestreamChannelsActionCallback restreamChannelsActionCallback;

  private final ArrayList<ChannelsModel> channels = new ArrayList<>();
  private ChannelsAdapter channelsAdapter;

  private AlertProgress alertProgress;
  private AlertDialog alertDialog;

  private Activity activity;

  private IsmBottomsheetRestreamFetchChannelsBinding ismBottomsheetRestreamFetchChannelsBinding;

  @SuppressLint("ClickableViewAccessibility")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (ismBottomsheetRestreamFetchChannelsBinding == null) {
      ismBottomsheetRestreamFetchChannelsBinding =
          IsmBottomsheetRestreamFetchChannelsBinding.inflate(inflater, container, false);
    } else {

      if (ismBottomsheetRestreamFetchChannelsBinding.getRoot().getParent() != null) {
        ((ViewGroup) ismBottomsheetRestreamFetchChannelsBinding.getRoot().getParent()).removeView(
            ismBottomsheetRestreamFetchChannelsBinding.getRoot());
      }
    }
    alertProgress = new AlertProgress();

    ismBottomsheetRestreamFetchChannelsBinding.swToggleChannels.setOnCheckedChangeListener(
        checkedChangeListener);

//    if (IsometrikUiSdk.getInstance().getUserSession().getUserSelected()) {
//      ismBottomsheetRestreamFetchChannelsBinding.rlToggleChannels.setVisibility(View.GONE);
//      ismBottomsheetRestreamFetchChannelsBinding.btAdd.setVisibility(View.GONE);
//    } else {
      ismBottomsheetRestreamFetchChannelsBinding.rlToggleChannels.setVisibility(View.VISIBLE);
      ismBottomsheetRestreamFetchChannelsBinding.btAdd.setVisibility(View.VISIBLE);
//    }

    ismBottomsheetRestreamFetchChannelsBinding.rvChannels.setLayoutManager(
        new LinearLayoutManager(activity));
    channelsAdapter = new ChannelsAdapter(activity, channels,
        IsometrikStreamSdk.getInstance().getUserSession().getUserSelected(), this);
    ismBottomsheetRestreamFetchChannelsBinding.rvChannels.setAdapter(channelsAdapter);
    showProgressDialog(getString(R.string.ism_fetching_channels));
    fetchChannelsPresenter.fetchRestreamChannels();

    ismBottomsheetRestreamFetchChannelsBinding.btAdd.setOnClickListener(view -> {
      if (restreamChannelsActionCallback != null) restreamChannelsActionCallback.addChannel();
    });

    ismBottomsheetRestreamFetchChannelsBinding.ibClose.setOnClickListener(view -> {
      try {
        dismiss();
      } catch (Exception ignore) {
      }
    });

    return ismBottomsheetRestreamFetchChannelsBinding.getRoot();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
    fetchChannelsPresenter = new FetchChannelsPresenter();
    fetchChannelsPresenter.attachView(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    fetchChannelsPresenter.detachView();
    activity = null;
  }

  /**
   * {@link FetchChannelsContract.View#onError(String)}
   */
  @Override
  public void onError(String errorMessage) {
    hideProgressDialog();
    if (activity != null) {
      activity.runOnUiThread(() -> {
        //hideProgressDialog();
        if (errorMessage != null) {
          Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(activity, getString(R.string.ism_error), Toast.LENGTH_SHORT).show();
        }
      });
    }
  }

  @Override
  public void onFailedToToggleRestreamChannels(String errorMessage) {

    if (activity != null) {
      activity.runOnUiThread(() -> {

        hideProgressDialog();
        if (errorMessage != null) {
          Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(activity, getString(R.string.ism_error), Toast.LENGTH_SHORT).show();
        }
        ismBottomsheetRestreamFetchChannelsBinding.swToggleChannels.setOnCheckedChangeListener(
            null);
        //Toggle the state,incase update channels fails
        ismBottomsheetRestreamFetchChannelsBinding.swToggleChannels.setChecked(
            !ismBottomsheetRestreamFetchChannelsBinding.swToggleChannels.isChecked());
        ismBottomsheetRestreamFetchChannelsBinding.swToggleChannels.setOnCheckedChangeListener(
            checkedChangeListener);
      });
    }
  }

  @Override
  public void onRestreamChannelsToggledSuccessfully(boolean enable) {
    if (activity != null) {
      activity.runOnUiThread(() -> {

        int size = channels.size();
        if (size > 0) {

          for (int i = 0; i < channels.size(); i++) {
            if (channels.get(i).isEnabled() != enable) {

              ChannelsModel channelsModel = channels.get(i);
              channelsModel.setEnabled(enable);
              channels.set(i, channelsModel);
              channelsAdapter.notifyItemChanged(i);
            }
          }
        }

        hideProgressDialog();
      });
    }
  }

  @Override
  public void onRestreamChannelDeletedSuccessfully(String channelId) {
    if (activity != null) {
      activity.runOnUiThread(() -> {

        int size = channels.size();
        if (size > 0) {

          for (int i = 0; i < channels.size(); i++) {
            if (channels.get(i).getChannelId().equals(channelId)) {
              channels.remove(i);
              channelsAdapter.notifyItemRemoved(i);
              break;
            }
          }
        }

        hideProgressDialog();
      });
    }
  }

  @Override
  public void onRestreamChannelToggledSuccessfully(String channelId) {
    if (activity != null) {
      activity.runOnUiThread(() -> {

        int size = channels.size();
        if (size > 0) {

          for (int i = 0; i < channels.size(); i++) {
            if (channels.get(i).getChannelId().equals(channelId)) {
              ChannelsModel channelsModel = channels.get(i);
              channelsModel.setEnabled(!channelsModel.isEnabled());
              channels.set(i, channelsModel);
              channelsAdapter.notifyItemChanged(i);
              break;
            }
          }
        }

        hideProgressDialog();
      });
    }
  }

  @Override
  public void onFailedToToggleRestreamChannel(String channelId, String errorMessage) {
    if (activity != null) {
      activity.runOnUiThread(() -> {

        int size = channels.size();
        if (size > 0) {

          for (int i = 0; i < channels.size(); i++) {
            if (channels.get(i).getChannelId().equals(channelId)) {
              //To restore state of toggle button
              channelsAdapter.notifyItemChanged(i);
              break;
            }
          }
        }

        hideProgressDialog();
        if (errorMessage != null) {
          Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(activity, getString(R.string.ism_error), Toast.LENGTH_SHORT).show();
        }
      });
    }
  }

  @Override
  public void onRestreamChannelsFetched(ArrayList<ChannelsModel> restreamChannels) {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        channels.clear();
        channels.addAll(restreamChannels);
        int size = restreamChannels.size();
        if (size > 0) {
          ismBottomsheetRestreamFetchChannelsBinding.tvNoChannels.setVisibility(View.GONE);
          ismBottomsheetRestreamFetchChannelsBinding.rvChannels.setVisibility(View.VISIBLE);
          channelsAdapter.notifyDataSetChanged();
        } else {
          ismBottomsheetRestreamFetchChannelsBinding.tvNoChannels.setVisibility(View.VISIBLE);
          ismBottomsheetRestreamFetchChannelsBinding.rvChannels.setVisibility(View.GONE);
        }

        hideProgressDialog();
      });
    }
  }

  public void deleteChannel(String channelId) {
    showProgressDialog(getString(R.string.ism_deleting_channel));
    fetchChannelsPresenter.deleteRestreamChannel(channelId);
  }

  public void toggleRestreamChannel(String channelId, boolean enable) {
    showProgressDialog(getString(R.string.ism_updating_channel));
    fetchChannelsPresenter.toggleRestreamChannel(channelId, enable);
  }

  public void editChannel(ChannelsModel channel) {
    if (restreamChannelsActionCallback != null) {
      restreamChannelsActionCallback.editChannel(channel.getChannelId(), channel.getChannelName(),
          channel.getIngestUrl(), channel.isEnabled(), channel.getChannelType());
    }
  }

  public void updateParams(RestreamChannelsActionCallback restreamChannelsActionCallback) {
    this.restreamChannelsActionCallback = restreamChannelsActionCallback;
  }

  private void showProgressDialog(String message) {

    if (activity != null) {
      alertDialog = alertProgress.getProgressDialog(activity, message);
      if (!activity.isFinishing() && !activity.isDestroyed()) alertDialog.show();
    }
  }

  private void hideProgressDialog() {

    if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
  }

  private final CompoundButton.OnCheckedChangeListener checkedChangeListener =
      new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
          showProgressDialog(getString(R.string.ism_updating_channels));
          fetchChannelsPresenter.toggleAllRestreamChannels(b);
        }
      };

  @Override
  public void onDismiss(@NonNull DialogInterface dialog) {
    try {
      hideProgressDialog();
    } catch (Exception ignore) {
    }
    super.onDismiss(dialog);
  }
}
