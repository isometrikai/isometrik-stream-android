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
import io.isometrik.gs.ui.databinding.IsmPlaybackQualitySheetBinding;
import io.isometrik.gs.ui.utils.RecyclerItemClickListener;
import java.util.ArrayList;

/**
 * Playback quality bottomsheet dialog fragment to allow update of media playback quality.
 */

public class PlaybackQualityFragment extends BottomSheetDialogFragment {

  public static final String TAG = "PlaybackQualityFragment";

  private final ArrayList<PlaybackOptionsItem> playbackQualityOptions = new ArrayList<>();
  private Activity activity;

  private PlaybackOptionUpdateCallback playbackOptionUpdateCallback;

  private IsmPlaybackQualitySheetBinding ismPlaybackQualitySheetBinding;

  public PlaybackQualityFragment() {
    // Required empty public constructor
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (ismPlaybackQualitySheetBinding == null) {
      ismPlaybackQualitySheetBinding =
          IsmPlaybackQualitySheetBinding.inflate(inflater, container, false);
    } else {

      if (ismPlaybackQualitySheetBinding.getRoot().getParent() != null) {
        ((ViewGroup) ismPlaybackQualitySheetBinding.getRoot().getParent()).removeView(
            ismPlaybackQualitySheetBinding.getRoot());
      }
    }
    ismPlaybackQualitySheetBinding.rvQualityOptions.setLayoutManager(
        new LinearLayoutManager(activity));
    ismPlaybackQualitySheetBinding.rvQualityOptions.setAdapter(
        new PlaybackOptionsAdapter(activity, playbackQualityOptions));
    ismPlaybackQualitySheetBinding.rvQualityOptions.addOnItemTouchListener(
        new RecyclerItemClickListener(activity, ismPlaybackQualitySheetBinding.rvQualityOptions,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, final int position) {

                if (position >= 0) {
                  if (playbackOptionUpdateCallback != null) {
                    playbackOptionUpdateCallback.changePlaybackQuality(
                        playbackQualityOptions.get(position).getOption());
                    dismissDialog();
                  }
                }
              }

              @Override
              public void onItemLongClick(View view, final int position) {

              }
            }));

    ismPlaybackQualitySheetBinding.tvClose.setOnClickListener(view -> dismissDialog());

    return ismPlaybackQualitySheetBinding.getRoot();
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

  public void updateParameters(ArrayList<PlaybackOptionsItem> playbackQualityOptions,
      PlaybackOptionUpdateCallback playbackOptionUpdateCallback) {
    this.playbackQualityOptions.clear();
    this.playbackQualityOptions.addAll(playbackQualityOptions);
    this.playbackOptionUpdateCallback = playbackOptionUpdateCallback;
  }

  public void dismissDialog() {
    try {
      dismiss();
    } catch (Exception ignore) {

    }
  }
}
