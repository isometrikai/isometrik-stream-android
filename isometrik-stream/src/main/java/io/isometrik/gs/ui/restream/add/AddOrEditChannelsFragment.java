package io.isometrik.gs.ui.restream.add;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmBottomsheetRestreamAddEditChannelBinding;
import io.isometrik.gs.ui.utils.AlertProgress;
import io.isometrik.gs.enums.RestreamChannelType;

public class AddOrEditChannelsFragment extends BottomSheetDialogFragment
    implements AddOrEditChannelsContract.View {

  public static final String TAG = "AddOrEditChannelsBottomSheetFragment";

  private AddOrEditChannelsContract.Presenter addOrEditChannelsPresenter;

  private AlertProgress alertProgress;
  private AlertDialog alertDialog;

  private Activity activity;
  private boolean addChannelRequested;
  private String channelId;
  private int channelType;
  private String channelName, ingestUrl, learnMoreUrl;
  private boolean enabled;

  private IsmBottomsheetRestreamAddEditChannelBinding ismBottomsheetRestreamAddEditChannelBinding;

  public AddOrEditChannelsFragment() {
    // Required empty public constructor
  }

  @SuppressLint("ClickableViewAccessibility")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (ismBottomsheetRestreamAddEditChannelBinding == null) {
      ismBottomsheetRestreamAddEditChannelBinding =
          IsmBottomsheetRestreamAddEditChannelBinding.inflate(inflater, container, false);
    } else {

      if (ismBottomsheetRestreamAddEditChannelBinding.getRoot().getParent() != null) {
        ((ViewGroup) ismBottomsheetRestreamAddEditChannelBinding.getRoot().getParent()).removeView(
            ismBottomsheetRestreamAddEditChannelBinding.getRoot());
      }
    }
    alertProgress = new AlertProgress();
    if (addChannelRequested) {

      ismBottomsheetRestreamAddEditChannelBinding.tvTitle.setText(
          getString(R.string.ism_select_channel_type));

      ismBottomsheetRestreamAddEditChannelBinding.etChannelName.getText().clear();
      ismBottomsheetRestreamAddEditChannelBinding.etRtmpUrl.getText().clear();
      ismBottomsheetRestreamAddEditChannelBinding.etStreamKey.getText().clear();
      ismBottomsheetRestreamAddEditChannelBinding.swEnableChannel.setChecked(false);
    } else {
      ismBottomsheetRestreamAddEditChannelBinding.tvTitle.setText(
          getString(R.string.ism_update_channel_type));
      ismBottomsheetRestreamAddEditChannelBinding.etChannelName.setText(channelName);
      ismBottomsheetRestreamAddEditChannelBinding.swEnableChannel.setChecked(enabled);
      try {
        ismBottomsheetRestreamAddEditChannelBinding.etStreamKey.setText(
            ingestUrl.substring(ingestUrl.lastIndexOf("/") + 1));
        ismBottomsheetRestreamAddEditChannelBinding.etRtmpUrl.setText(
            ingestUrl.substring(0, ingestUrl.lastIndexOf("/")));
      } catch (Exception ignore) {
      }
    }

    ismBottomsheetRestreamAddEditChannelBinding.btNext.setText(getString(R.string.ism_next));
    ismBottomsheetRestreamAddEditChannelBinding.rlChannelDetails.setVisibility(View.GONE);
    ismBottomsheetRestreamAddEditChannelBinding.flChannelType.setVisibility(View.VISIBLE);
    ismBottomsheetRestreamAddEditChannelBinding.ibClose.setImageDrawable(
        ContextCompat.getDrawable(activity, R.drawable.ism_ic_close));

    updateSelectedChannelTypeVisibility(channelType);
    ismBottomsheetRestreamAddEditChannelBinding.tvLearnMore.setMovementMethod(
        LinkMovementMethod.getInstance());

    ismBottomsheetRestreamAddEditChannelBinding.btNext.setOnClickListener(view -> {
      if (ismBottomsheetRestreamAddEditChannelBinding.flChannelType.getVisibility()
          == View.VISIBLE) {

        if (channelType == -1) {
          Toast.makeText(activity, getString(R.string.ism_invalid_channel_type), Toast.LENGTH_SHORT)
              .show();
        } else {
          ismBottomsheetRestreamAddEditChannelBinding.flChannelType.setVisibility(View.GONE);
          ismBottomsheetRestreamAddEditChannelBinding.rlChannelDetails.setVisibility(View.VISIBLE);

          ismBottomsheetRestreamAddEditChannelBinding.ibClose.setImageDrawable(
              ContextCompat.getDrawable(activity, R.drawable.ism_ic_arrow_back));

          if (addChannelRequested) {
            ismBottomsheetRestreamAddEditChannelBinding.tvTitle.setText(
                getString(R.string.ism_add_channel_details));
            ismBottomsheetRestreamAddEditChannelBinding.btNext.setText(
                getString(R.string.ism_create));
          } else {
            ismBottomsheetRestreamAddEditChannelBinding.tvTitle.setText(
                getString(R.string.ism_update_channel_details));
            ismBottomsheetRestreamAddEditChannelBinding.btNext.setText(
                getString(R.string.ism_update));
          }
        }
      } else {
        addOrEditChannelsPresenter.validateChannelDetails(
            ismBottomsheetRestreamAddEditChannelBinding.etChannelName.getText().toString(),
            ismBottomsheetRestreamAddEditChannelBinding.etRtmpUrl.getText().toString(),
            ismBottomsheetRestreamAddEditChannelBinding.etStreamKey.getText().toString());
      }
    });

    ismBottomsheetRestreamAddEditChannelBinding.ibClose.setOnClickListener(view -> {
      if (ismBottomsheetRestreamAddEditChannelBinding.flChannelType.getVisibility()
          == View.VISIBLE) {

        dismissDialog();
      } else {
        ismBottomsheetRestreamAddEditChannelBinding.rlChannelDetails.setVisibility(View.GONE);
        ismBottomsheetRestreamAddEditChannelBinding.flChannelType.setVisibility(View.VISIBLE);
        ismBottomsheetRestreamAddEditChannelBinding.ibClose.setImageDrawable(
            ContextCompat.getDrawable(activity, R.drawable.ism_ic_close));
        ismBottomsheetRestreamAddEditChannelBinding.btNext.setText(getString(R.string.ism_next));
        if (addChannelRequested) {
          ismBottomsheetRestreamAddEditChannelBinding.tvTitle.setText(
              getString(R.string.ism_select_channel_type));
        } else {
          ismBottomsheetRestreamAddEditChannelBinding.tvTitle.setText(
              getString(R.string.ism_update_channel_type));
        }
      }
    });

    ismBottomsheetRestreamAddEditChannelBinding.tvLearnMore.setOnClickListener(view -> {
      Intent browserIntent = new Intent(Intent.ACTION_VIEW);
      browserIntent.setData(Uri.parse(learnMoreUrl));
      startActivity(browserIntent);
    });

    ismBottomsheetRestreamAddEditChannelBinding.cvFacebook.setOnClickListener(view -> {
      channelType = RestreamChannelType.Facebook.getValue();
      updateSelectedChannelTypeVisibility(channelType);
    });

    ismBottomsheetRestreamAddEditChannelBinding.cvYoutube.setOnClickListener(view -> {
      channelType = RestreamChannelType.Youtube.getValue();
      updateSelectedChannelTypeVisibility(channelType);
    });

    ismBottomsheetRestreamAddEditChannelBinding.cvTwitch.setOnClickListener(view -> {
      channelType = RestreamChannelType.Twitch.getValue();
      updateSelectedChannelTypeVisibility(channelType);
    });

    ismBottomsheetRestreamAddEditChannelBinding.cvTwitter.setOnClickListener(view -> {
      channelType = RestreamChannelType.Twitter.getValue();
      updateSelectedChannelTypeVisibility(channelType);
    });

    ismBottomsheetRestreamAddEditChannelBinding.cvLinkedIn.setOnClickListener(view -> {
      channelType = RestreamChannelType.LinkedIn.getValue();
      updateSelectedChannelTypeVisibility(channelType);
    });

    ismBottomsheetRestreamAddEditChannelBinding.cvCustomRtmp.setOnClickListener(view -> {
      channelType = RestreamChannelType.CustomRtmp.getValue();
      updateSelectedChannelTypeVisibility(channelType);
    });

    return ismBottomsheetRestreamAddEditChannelBinding.getRoot();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
    addOrEditChannelsPresenter = new AddOrEditChannelsPresenter();
    addOrEditChannelsPresenter.attachView(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    addOrEditChannelsPresenter.detachView();
    activity = null;
  }

  /**
   * {@link AddOrEditChannelsContract.View#onError(String)}
   */
  @Override
  public void onError(String errorMessage) {
    hideProgressDialog();
    if (activity != null) {
      activity.runOnUiThread(() -> {
        if (errorMessage != null) {
          Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(activity, getString(R.string.ism_error), Toast.LENGTH_SHORT).show();
        }
      });
    }
  }

  @Override
  public void onChannelCreatedSuccessfully(String channelId) {
    hideProgressDialog();
    dismissDialog();
  }

  @Override
  public void onChannelEditedSuccessfully() {
    hideProgressDialog();
    dismissDialog();
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

  private void dismissDialog() {
    try {
      dismiss();
    } catch (Exception ignore) {
    }
  }

  public void updateParams(boolean addChannelRequested, String channelId, String channelName,
      String ingestUrl, boolean enabled, int channelType) {
    this.addChannelRequested = addChannelRequested;
    this.channelType = channelType;
    if (!addChannelRequested) {
      this.channelId = channelId;
      this.ingestUrl = ingestUrl;
      this.enabled = enabled;
      this.channelName = channelName;
    }
  }

  @Override
  public void channelDetailsValidationResult(boolean valid, String error) {
    if (valid) {
      if (addChannelRequested) {
        showProgressDialog(getString(R.string.ism_adding_channel));
        addOrEditChannelsPresenter.addChannel(
            ismBottomsheetRestreamAddEditChannelBinding.etChannelName.getText().toString(),
            ismBottomsheetRestreamAddEditChannelBinding.etRtmpUrl.getText().toString(),
            ismBottomsheetRestreamAddEditChannelBinding.etStreamKey.getText().toString(),
            ismBottomsheetRestreamAddEditChannelBinding.swEnableChannel.isChecked(), channelType);
      } else {
        showProgressDialog(getString(R.string.ism_updating_channel));
        addOrEditChannelsPresenter.updateChannel(channelId,
            ismBottomsheetRestreamAddEditChannelBinding.etChannelName.getText().toString(),
            ismBottomsheetRestreamAddEditChannelBinding.etRtmpUrl.getText().toString(),
            ismBottomsheetRestreamAddEditChannelBinding.etStreamKey.getText().toString(),
            ismBottomsheetRestreamAddEditChannelBinding.swEnableChannel.isChecked(), channelType);
      }
    } else {
      Toast.makeText(activity, error, Toast.LENGTH_SHORT).show();
    }
  }

  private void updateSelectedChannelTypeVisibility(int channelType) {
    if (channelType == RestreamChannelType.Facebook.getValue()) {
      ismBottomsheetRestreamAddEditChannelBinding.ivFacebookSelected.setVisibility(View.VISIBLE);
      ismBottomsheetRestreamAddEditChannelBinding.ivYoutubeSelected.setVisibility(View.GONE);
      ismBottomsheetRestreamAddEditChannelBinding.ivTwitchSelected.setVisibility(View.GONE);
      ismBottomsheetRestreamAddEditChannelBinding.ivTwitterSelected.setVisibility(View.GONE);
      ismBottomsheetRestreamAddEditChannelBinding.ivLinkedInSelected.setVisibility(View.GONE);
      ismBottomsheetRestreamAddEditChannelBinding.ivCustomRtmpSelected.setVisibility(View.GONE);
      learnMoreUrl = getString(R.string.ism_facebook_learn_more_link);
    } else if (channelType == RestreamChannelType.Youtube.getValue()) {
      ismBottomsheetRestreamAddEditChannelBinding.ivFacebookSelected.setVisibility(View.GONE);
      ismBottomsheetRestreamAddEditChannelBinding.ivYoutubeSelected.setVisibility(View.VISIBLE);
      ismBottomsheetRestreamAddEditChannelBinding.ivTwitchSelected.setVisibility(View.GONE);
      ismBottomsheetRestreamAddEditChannelBinding.ivTwitterSelected.setVisibility(View.GONE);
      ismBottomsheetRestreamAddEditChannelBinding.ivLinkedInSelected.setVisibility(View.GONE);
      ismBottomsheetRestreamAddEditChannelBinding.ivCustomRtmpSelected.setVisibility(View.GONE);
      learnMoreUrl = getString(R.string.ism_youtube_learn_more_link);
    } else if (channelType == RestreamChannelType.Twitch.getValue()) {
      ismBottomsheetRestreamAddEditChannelBinding.ivFacebookSelected.setVisibility(View.GONE);
      ismBottomsheetRestreamAddEditChannelBinding.ivYoutubeSelected.setVisibility(View.GONE);
      ismBottomsheetRestreamAddEditChannelBinding.ivTwitchSelected.setVisibility(View.VISIBLE);
      ismBottomsheetRestreamAddEditChannelBinding.ivTwitterSelected.setVisibility(View.GONE);
      ismBottomsheetRestreamAddEditChannelBinding.ivLinkedInSelected.setVisibility(View.GONE);
      ismBottomsheetRestreamAddEditChannelBinding.ivCustomRtmpSelected.setVisibility(View.GONE);
      learnMoreUrl = getString(R.string.ism_twitch_learn_more_link);
    } else if (channelType == RestreamChannelType.Twitter.getValue()) {
      ismBottomsheetRestreamAddEditChannelBinding.ivFacebookSelected.setVisibility(View.GONE);
      ismBottomsheetRestreamAddEditChannelBinding.ivYoutubeSelected.setVisibility(View.GONE);
      ismBottomsheetRestreamAddEditChannelBinding.ivTwitchSelected.setVisibility(View.GONE);
      ismBottomsheetRestreamAddEditChannelBinding.ivTwitterSelected.setVisibility(View.VISIBLE);
      ismBottomsheetRestreamAddEditChannelBinding.ivLinkedInSelected.setVisibility(View.GONE);
      ismBottomsheetRestreamAddEditChannelBinding.ivCustomRtmpSelected.setVisibility(View.GONE);
      learnMoreUrl = getString(R.string.ism_twitter_learn_more_link);
    } else if (channelType == RestreamChannelType.LinkedIn.getValue()) {
      ismBottomsheetRestreamAddEditChannelBinding.ivFacebookSelected.setVisibility(View.GONE);
      ismBottomsheetRestreamAddEditChannelBinding.ivYoutubeSelected.setVisibility(View.GONE);
      ismBottomsheetRestreamAddEditChannelBinding.ivTwitchSelected.setVisibility(View.GONE);
      ismBottomsheetRestreamAddEditChannelBinding.ivTwitterSelected.setVisibility(View.GONE);
      ismBottomsheetRestreamAddEditChannelBinding.ivLinkedInSelected.setVisibility(View.VISIBLE);
      ismBottomsheetRestreamAddEditChannelBinding.ivCustomRtmpSelected.setVisibility(View.GONE);
      learnMoreUrl = getString(R.string.ism_linkedin_learn_more_link);
    } else if (channelType == RestreamChannelType.CustomRtmp.getValue()) {
      ismBottomsheetRestreamAddEditChannelBinding.ivFacebookSelected.setVisibility(View.GONE);
      ismBottomsheetRestreamAddEditChannelBinding.ivYoutubeSelected.setVisibility(View.GONE);
      ismBottomsheetRestreamAddEditChannelBinding.ivTwitchSelected.setVisibility(View.GONE);
      ismBottomsheetRestreamAddEditChannelBinding.ivTwitterSelected.setVisibility(View.GONE);
      ismBottomsheetRestreamAddEditChannelBinding.ivLinkedInSelected.setVisibility(View.GONE);
      ismBottomsheetRestreamAddEditChannelBinding.ivCustomRtmpSelected.setVisibility(View.VISIBLE);
      learnMoreUrl = getString(R.string.ism_custom_rtmp_learn_more_link);
    } else {
      ismBottomsheetRestreamAddEditChannelBinding.ivFacebookSelected.setVisibility(View.GONE);
      ismBottomsheetRestreamAddEditChannelBinding.ivYoutubeSelected.setVisibility(View.GONE);
      ismBottomsheetRestreamAddEditChannelBinding.ivTwitchSelected.setVisibility(View.GONE);
      ismBottomsheetRestreamAddEditChannelBinding.ivTwitterSelected.setVisibility(View.GONE);
      ismBottomsheetRestreamAddEditChannelBinding.ivLinkedInSelected.setVisibility(View.GONE);
      ismBottomsheetRestreamAddEditChannelBinding.ivCustomRtmpSelected.setVisibility(View.GONE);
      learnMoreUrl = getString(R.string.ism_custom_rtmp_learn_more_link);
    }
  }

  @Override
  public void onDismiss(@NonNull DialogInterface dialog) {
    try {
      hideProgressDialog();
    } catch (Exception ignore) {
    }
    super.onDismiss(dialog);
  }
}
