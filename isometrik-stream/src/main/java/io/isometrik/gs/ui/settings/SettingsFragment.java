package io.isometrik.gs.ui.settings;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmBottomsheetSettingsBinding;
import io.isometrik.gs.ui.settings.callbacks.SettingsActionCallback;
import io.isometrik.gs.rtcengine.rtc.webrtc.MultiLiveBroadcastOperations;

import org.jetbrains.annotations.NotNull;

/**
 * Bottomsheet dialog fragment to update settings in a broadcast to toggle local/remote audio/video
 * state,visibility of chat messages,network stats and control buttons.Members can also leave a
 * broadcast.
 */
public class SettingsFragment extends BottomSheetDialogFragment {

    public static final String TAG = "SettingsFragment";

    private boolean isBroadcaster, remoteVideoMuted, remoteAudioMuted, localVideoMuted,
            localAudioMuted,
            isAdmin, audioOnly, isPkInitiator, isPkBattle;
    private int userCount;
    private String streamId;
    private String pkInviteId = "";

    private int localStreamerUid = -1;
    private SettingsActionCallback actionCallback;

    private IsmBottomsheetSettingsBinding ismBottomsheetSettingsBinding;

    private MultiLiveBroadcastOperations multiLiveBroadcastOperations;


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (ismBottomsheetSettingsBinding == null) {
            ismBottomsheetSettingsBinding =
                    IsmBottomsheetSettingsBinding.inflate(inflater, container, false);
        } else {

            if (ismBottomsheetSettingsBinding.getRoot().getParent() != null) {
                ((ViewGroup) ismBottomsheetSettingsBinding.getRoot().getParent()).removeView(
                        ismBottomsheetSettingsBinding.getRoot());
            }
        }


        if (isBroadcaster) {
            if (isAdmin) {
                ismBottomsheetSettingsBinding.rlLeave.setVisibility(View.GONE);
            } else {
                ismBottomsheetSettingsBinding.rlLeave.setVisibility(View.VISIBLE);
            }
            ismBottomsheetSettingsBinding.rlLocalAudio.setVisibility(View.VISIBLE);
            if (audioOnly) {
                ismBottomsheetSettingsBinding.rlLocalVideo.setVisibility(View.GONE);
            } else {
                ismBottomsheetSettingsBinding.rlLocalVideo.setVisibility(View.VISIBLE);
            }
        } else {
            ismBottomsheetSettingsBinding.rlLeave.setVisibility(View.GONE);
            ismBottomsheetSettingsBinding.rlLocalAudio.setVisibility(View.GONE);
            ismBottomsheetSettingsBinding.rlLocalVideo.setVisibility(View.GONE);
        }


        if (!localAudioMuted) {
            ismBottomsheetSettingsBinding.ivLocalAudio.setSelected(false);
            ismBottomsheetSettingsBinding.tvLocalAudio.setText(
                    getString(R.string.ism_mute_local_audio));
        }


        ismBottomsheetSettingsBinding.rlLocalVideo.setOnClickListener(view -> {
            localVideoMuted = !localVideoMuted;
            ismBottomsheetSettingsBinding.ivLocalVideo.setSelected(localVideoMuted);

            if (localVideoMuted) {
                ismBottomsheetSettingsBinding.tvLocalVideo.setText(
                        getString(R.string.ism_unmute_local_video));
            } else {
                ismBottomsheetSettingsBinding.tvLocalVideo.setText(
                        getString(R.string.ism_mute_local_video));
            }

            multiLiveBroadcastOperations
                    .muteLocalVideo(localVideoMuted);


        });
        ismBottomsheetSettingsBinding.rlLocalAudio.setOnClickListener(view -> {
            localAudioMuted = !localAudioMuted;
            Log.e("Click Audio ", "==> " + localAudioMuted);
            ismBottomsheetSettingsBinding.ivLocalAudio.setSelected(localAudioMuted);

            if (localAudioMuted) {
                ismBottomsheetSettingsBinding.tvLocalAudio.setText(
                        getString(R.string.ism_unmute_local_audio));
            } else {
                ismBottomsheetSettingsBinding.tvLocalAudio.setText(
                        getString(R.string.ism_mute_local_audio));
            }

            multiLiveBroadcastOperations.muteLocalAudio(localAudioMuted);

        });


        ismBottomsheetSettingsBinding.rlLeave.setOnClickListener(view -> {
            if (actionCallback != null) actionCallback.leaveBroadcastRequested();
        });


        ismBottomsheetSettingsBinding.rlModerator.setOnClickListener(view -> {
            if (actionCallback != null) actionCallback.fetchModerators(streamId);
        });


        return ismBottomsheetSettingsBinding.getRoot();
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

    public void updateParameters(boolean isAdmin, boolean isBroadcaster, String streamId,
                                 boolean audioOnly, SettingsActionCallback actionCallback,
                                 int localStreamerUid, String pkInviteId, boolean isPkBattle) {

        this.isAdmin = isAdmin;
        this.isBroadcaster = isBroadcaster;
        this.audioOnly = audioOnly;
        this.actionCallback = actionCallback;
        this.localStreamerUid = localStreamerUid;
        this.isPkBattle = isPkBattle;

        try {
            if (!streamId.equals(this.streamId)) {

                remoteAudioMuted = false;
                remoteVideoMuted = false;
            }
            this.streamId = streamId;

            Log.e("muted param", "===> this.pkInviteId " + this.pkInviteId + "=====pkInviteId " + pkInviteId);
            if (!this.pkInviteId.equalsIgnoreCase(pkInviteId)) {
                Log.e("muted param", "===> reset");

                remoteAudioMuted = false;
                remoteVideoMuted = false;
                if (!isPkInitiator) {
                    localAudioMuted = false;
                }
            }
            this.pkInviteId = pkInviteId;

        } catch (Exception ignore) {

        }
    }

    public void updateParameters(boolean isPkInitiator, int userCount) {
        this.isPkInitiator = isPkInitiator;
        this.userCount = userCount;
    }

    public void streamSwitched() {
        //to handle case when stream has been switched and than same stream selected back,leading to incorrect state of remote audio/video text and image as streamId was not changed
        this.streamId = "";
    }

    public boolean isLocalAudioMuted() {
        return localAudioMuted;
    }

    public boolean isLocalVideoMuted() {
        return localVideoMuted;
    }

    public boolean isRemoteAudioMuted() {
        return remoteAudioMuted;
    }

    public boolean isRemoteVideoMuted() {
        return remoteVideoMuted;
    }

    public void updateMultiLiveBroadcastOperator(MultiLiveBroadcastOperations multiLiveBroadcastOperations) {
        this.multiLiveBroadcastOperations = multiLiveBroadcastOperations;

    }
}