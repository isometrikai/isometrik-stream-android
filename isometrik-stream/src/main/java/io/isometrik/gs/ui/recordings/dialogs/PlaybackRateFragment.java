package io.isometrik.gs.ui.recordings.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.gs.ui.databinding.IsmPlaybackRateSheetBinding;
import io.isometrik.gs.ui.utils.RecyclerItemClickListener;
import java.util.ArrayList;

/**
 * Playback rate bottomsheet dialog fragment to allow update of media playback rate.
 */
public class PlaybackRateFragment extends BottomSheetDialogFragment {

  public static final String TAG = "PlaybackRateFragment";

  private final ArrayList<PlaybackOptionsItem> playbackRateOptions = new ArrayList<>();
  private Activity activity;

  private PlaybackOptionUpdateCallback playbackOptionUpdateCallback;

  private IsmPlaybackRateSheetBinding ismPlaybackRateSheetBinding;

  public PlaybackRateFragment() {
    // Required empty public constructor
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (ismPlaybackRateSheetBinding == null) {
      ismPlaybackRateSheetBinding = IsmPlaybackRateSheetBinding.inflate(inflater, container, false);
    } else {

      if (ismPlaybackRateSheetBinding.getRoot().getParent() != null) {
        ((ViewGroup) ismPlaybackRateSheetBinding.getRoot().getParent()).removeView(
            ismPlaybackRateSheetBinding.getRoot());
      }
    }
    ismPlaybackRateSheetBinding.rvRateOptions.setLayoutManager(new LinearLayoutManager(activity));
    ismPlaybackRateSheetBinding.rvRateOptions.setAdapter(
        new PlaybackOptionsAdapter(activity, playbackRateOptions));
    ismPlaybackRateSheetBinding.rvRateOptions.addOnItemTouchListener(
        new RecyclerItemClickListener(activity, ismPlaybackRateSheetBinding.rvRateOptions,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, final int position) {

                if (position >= 0) {
                  if (playbackOptionUpdateCallback != null) {
                    playbackOptionUpdateCallback.changePlaybackRate(
                        playbackRateOptions.get(position).getOption());
                    dismissDialog();
                  }
                }
              }

              @Override
              public void onItemLongClick(View view, final int position) {

              }
            }));

    ismPlaybackRateSheetBinding.tvClose.setOnClickListener(view -> dismissDialog());
    return ismPlaybackRateSheetBinding.getRoot();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    activity = null;
  }

  public void updateParameters(ArrayList<PlaybackOptionsItem> playbackRateOptions,
      PlaybackOptionUpdateCallback playbackOptionUpdateCallback) {
    this.playbackRateOptions.clear();
    this.playbackRateOptions.addAll(playbackRateOptions);
    this.playbackOptionUpdateCallback = playbackOptionUpdateCallback;
  }

  public void dismissDialog() {
    try {
      dismiss();
    } catch (Exception ignore) {

    }
  }
}