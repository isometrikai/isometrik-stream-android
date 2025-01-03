package io.isometrik.gs.ui.settings;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmBottomsheetRemoteuserSettingsBinding;
import io.isometrik.gs.ui.settings.callbacks.SettingsActionCallback;
import org.jetbrains.annotations.NotNull;

/**
 * Bottomsheet dialog fragment to remote user's audio/video mute state and to toggle it in a
 * broadcast.Host can kick out a member.
 */
public class RemoteUserSettings extends BottomSheetDialogFragment {

  public static final String TAG = "RemoteUserSettings";

  private boolean remoteVideoMuted, remoteAudioMuted, isModerator, audioOnly,
      isModeratorAllowedToRemoveRemoteUser, selfHosted;

  private String userName, streamId, userId;
  private int uid;

  private SettingsActionCallback actionCallback;

  private IsmBottomsheetRemoteuserSettingsBinding ismBottomsheetRemoteuserSettingsBinding;

  public RemoteUserSettings() {
    // Required empty public constructor
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (ismBottomsheetRemoteuserSettingsBinding == null) {
      ismBottomsheetRemoteuserSettingsBinding =
          IsmBottomsheetRemoteuserSettingsBinding.inflate(inflater, container, false);
    } else {

      if (ismBottomsheetRemoteuserSettingsBinding.getRoot().getParent() != null) {
        ((ViewGroup) ismBottomsheetRemoteuserSettingsBinding.getRoot().getParent()).removeView(
            ismBottomsheetRemoteuserSettingsBinding.getRoot());
      }
    }

    ismBottomsheetRemoteuserSettingsBinding.rlKickOut.setVisibility(
        (isModerator && isModeratorAllowedToRemoveRemoteUser) ? View.VISIBLE : View.GONE);

    //For allowing switch from one stream to another, which by default will have remote audio/video unmuted
    ismBottomsheetRemoteuserSettingsBinding.ivRemoteAudio.setSelected(remoteAudioMuted);
    if (remoteAudioMuted) {
      ismBottomsheetRemoteuserSettingsBinding.tvRemoteAudio.setText(
          getString(R.string.ism_mute_unmute_audio, getString(R.string.ism_unmute), userName));
    } else {
      ismBottomsheetRemoteuserSettingsBinding.tvRemoteAudio.setText(
          getString(R.string.ism_mute_unmute_audio, getString(R.string.ism_mute), userName));
    }

    ismBottomsheetRemoteuserSettingsBinding.ivRemoteVideo.setSelected(remoteVideoMuted);

    if (remoteVideoMuted) {
      ismBottomsheetRemoteuserSettingsBinding.tvRemoteVideo.setText(
          getString(R.string.ism_mute_unmute_video, getString(R.string.ism_unmute), userName));
    } else {
      ismBottomsheetRemoteuserSettingsBinding.tvRemoteVideo.setText(
          getString(R.string.ism_mute_unmute_video, getString(R.string.ism_mute), userName));
    }

    if (audioOnly) {
      ismBottomsheetRemoteuserSettingsBinding.rlRemoteVideo.setVisibility(View.GONE);
    } else {
      if (selfHosted) {
        ismBottomsheetRemoteuserSettingsBinding.rlRemoteVideo.setVisibility(View.GONE);
      } else {
        ismBottomsheetRemoteuserSettingsBinding.rlRemoteVideo.setVisibility(View.VISIBLE);
      }
    }

    if (selfHosted) {
      ismBottomsheetRemoteuserSettingsBinding.rlRemoteAudio.setVisibility(View.GONE);
    }

    ismBottomsheetRemoteuserSettingsBinding.rlRemoteVideo.setOnClickListener(view -> {
      remoteVideoMuted = !remoteVideoMuted;
      ismBottomsheetRemoteuserSettingsBinding.ivRemoteVideo.setSelected(remoteVideoMuted);

      if (remoteVideoMuted) {
        ismBottomsheetRemoteuserSettingsBinding.tvRemoteVideo.setText(
            getString(R.string.ism_mute_unmute_video, getString(R.string.ism_unmute), userName));
      } else {
        ismBottomsheetRemoteuserSettingsBinding.tvRemoteVideo.setText(
            getString(R.string.ism_mute_unmute_video, getString(R.string.ism_mute), userName));
      }

//      IsometrikUiSdk.getInstance()
//          .getIsometrik()
//          .getMultiBroadcastOperations()
//          .muteRemoteVideo(uid, remoteVideoMuted);

      if (actionCallback != null) {
        actionCallback.onRemoteUserMediaSettingsUpdated(streamId, userId, uid, userName, false,
            remoteVideoMuted);
      }
    });

    ismBottomsheetRemoteuserSettingsBinding.rlKickOut.setOnClickListener(view -> {
      if (actionCallback != null) actionCallback.removeMemberRequested(streamId, userId);
    });

    ismBottomsheetRemoteuserSettingsBinding.rlRemoteAudio.setOnClickListener(view -> {
      remoteAudioMuted = !remoteAudioMuted;
      ismBottomsheetRemoteuserSettingsBinding.ivRemoteAudio.setSelected(remoteAudioMuted);

      if (remoteAudioMuted) {
        ismBottomsheetRemoteuserSettingsBinding.tvRemoteAudio.setText(
            getString(R.string.ism_mute_unmute_audio, getString(R.string.ism_unmute), userName));
      } else {
        ismBottomsheetRemoteuserSettingsBinding.tvRemoteAudio.setText(
            getString(R.string.ism_mute_unmute_audio, getString(R.string.ism_mute), userName));
      }

//      IsometrikUiSdk.getInstance()
//          .getIsometrik()
//          .getMultiBroadcastOperations()
//          .muteRemoteAudio(uid, remoteAudioMuted);
      if (actionCallback != null) {
        actionCallback.onRemoteUserMediaSettingsUpdated(streamId, userId, uid, userName, true,
            remoteAudioMuted);
      }
    });

    if (selfHosted
        && ismBottomsheetRemoteuserSettingsBinding.rlKickOut.getVisibility() == View.GONE) {
      ismBottomsheetRemoteuserSettingsBinding.rlNoActionsAvailable.setVisibility(View.VISIBLE);
    } else {
      ismBottomsheetRemoteuserSettingsBinding.rlNoActionsAvailable.setVisibility(View.GONE);
    }

    return ismBottomsheetRemoteuserSettingsBinding.getRoot();
  }

  @Override
  public void onAttach(@NotNull Context context) {
    super.onAttach(context);

    if (context instanceof SettingsActionCallback) {
      actionCallback = (SettingsActionCallback) context;
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    actionCallback = null;
  }

  public void updateParameters(String streamId, boolean isModerator, String userId, int uid,
      String userName, boolean remoteAudioMuted, boolean remoteVideoMuted, boolean audioOnly,
      String initiatorId, boolean selfHosted) {
    this.streamId = streamId;
    this.isModerator = isModerator;
    this.remoteAudioMuted = remoteAudioMuted;
    this.remoteVideoMuted = remoteVideoMuted;
    this.uid = uid;
    this.userName = userName;
    this.userId = userId;
    this.audioOnly = audioOnly;
    this.selfHosted = selfHosted;
    try {
      isModeratorAllowedToRemoveRemoteUser = !initiatorId.equals(userId);
    } catch (Exception ignore) {
      isModeratorAllowedToRemoveRemoteUser = true;
    }
  }
}