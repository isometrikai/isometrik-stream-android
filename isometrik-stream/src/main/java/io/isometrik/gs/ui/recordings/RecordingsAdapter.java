package io.isometrik.gs.ui.recordings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import io.isometrik.gs.ui.databinding.IsmRecordingItemBinding;
import io.isometrik.gs.ui.utils.GlideApp;
import io.isometrik.gs.ui.utils.blur.BlurTransformation;
import io.isometrik.gs.response.recording.FetchRecordingsResult;
import java.util.ArrayList;

public class RecordingsAdapter extends RecyclerView.Adapter {

  private final Context mContext;
  private final ArrayList<FetchRecordingsResult.Recording> recordings;

  /**
   * Instantiates a new recordings adapter.
   *
   * @param mContext the context used for loading images
   * @param recordings the recordings
   */
  public RecordingsAdapter(Context mContext,
      ArrayList<FetchRecordingsResult.Recording> recordings) {
    this.mContext = mContext;
    this.recordings = recordings;
  }

  @Override
  public int getItemCount() {
    return recordings.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

    return new RecordingsViewHolder(
        IsmRecordingItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup,
            false));
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int postion) {

    RecordingsViewHolder holder = (RecordingsViewHolder) viewHolder;

    try {
      FetchRecordingsResult.Recording recording = recordings.get(postion);
      holder.ismRecordingItemBinding.ivStreamImage.setVisibility(View.VISIBLE);

      holder.ismRecordingItemBinding.ivAudioOnly.setVisibility(
          recording.isAudioOnly() ? View.VISIBLE : View.GONE);
      try {

        //Recording image url
        GlideApp.with(mContext)
            .load(recording.getStreamImage())
            .transform(new CenterCrop())
            .transform(new BlurTransformation())
            .into(holder.ismRecordingItemBinding.ivStreamImage);
      } catch (IllegalArgumentException | NullPointerException ignore) {

      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * The type Recordings view holder.
   */
  public static class RecordingsViewHolder extends RecyclerView.ViewHolder {

    public final IsmRecordingItemBinding ismRecordingItemBinding;

    public RecordingsViewHolder(final IsmRecordingItemBinding ismRecordingItemBinding) {
      super(ismRecordingItemBinding.getRoot());
      this.ismRecordingItemBinding = ismRecordingItemBinding;
    }
  }
}