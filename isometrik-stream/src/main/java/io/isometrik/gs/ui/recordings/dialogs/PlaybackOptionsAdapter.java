package io.isometrik.gs.ui.recordings.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmPlaybackOptionsListItemBinding;
import java.util.ArrayList;

public class PlaybackOptionsAdapter extends RecyclerView.Adapter {

  private final Context mContext;
  private final ArrayList<PlaybackOptionsItem> playbackOptionsItems;

  /**
   * Instantiates a new options adapter.
   *
   * @param mContext the context used for changing text color for selected item
   * @param playbackOptionsItems the list of playback options
   */
  public PlaybackOptionsAdapter(Context mContext,
      ArrayList<PlaybackOptionsItem> playbackOptionsItems) {
    this.mContext = mContext;
    this.playbackOptionsItems = playbackOptionsItems;
  }

  @Override
  public int getItemCount() {
    return playbackOptionsItems.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

    return new PlaybackOptionsViewHolder(
        IsmPlaybackOptionsListItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
            viewGroup, false));
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int postion) {

    PlaybackOptionsViewHolder holder = (PlaybackOptionsViewHolder) viewHolder;

    try {
      PlaybackOptionsItem playbackOptionsItem = playbackOptionsItems.get(postion);
      holder.ismPlaybackOptionsListItemBinding.tvOptions.setText(playbackOptionsItem.getOption());

      if (playbackOptionsItem.isSelected()) {
        holder.ismPlaybackOptionsListItemBinding.tvOptions.setTextColor(
            ContextCompat.getColor(mContext, R.color.ism_playback_options_selected_blue));
      } else {
        holder.ismPlaybackOptionsListItemBinding.tvOptions.setTextColor(
            ContextCompat.getColor(mContext, R.color.ism_playback_options_unselected_gray));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * The type playback options view holder.
   */
  public static class PlaybackOptionsViewHolder extends RecyclerView.ViewHolder {

    private final IsmPlaybackOptionsListItemBinding ismPlaybackOptionsListItemBinding;

    public PlaybackOptionsViewHolder(
        final IsmPlaybackOptionsListItemBinding ismPlaybackOptionsListItemBinding) {
      super(ismPlaybackOptionsListItemBinding.getRoot());
      this.ismPlaybackOptionsListItemBinding = ismPlaybackOptionsListItemBinding;
    }
  }
}