package io.isometrik.gs.ui.scrollable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import io.isometrik.gs.ui.databinding.IsmScrollableStreamsItemBinding;
import io.isometrik.gs.ui.streams.list.LiveStreamsModel;
import io.isometrik.gs.ui.utils.GlideApp;
import io.isometrik.gs.ui.utils.blur.BlurTransformation;
import java.util.ArrayList;

/**
 * The type scrollable streams adapter.
 */
public class ScrollableStreamsAdapter extends RecyclerView.Adapter {

  private final Context mContext;
  private final ArrayList<LiveStreamsModel> streams;

  /**
   * Instantiates a new Streams adapter.
   *
   * @param mContext the m context
   * @param streams the streams
   */
  public ScrollableStreamsAdapter(Context mContext, ArrayList<LiveStreamsModel> streams) {
    this.mContext = mContext;
    this.streams = streams;
  }

  @Override
  public int getItemCount() {
    return streams.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

    return new StreamsViewHolder(
        IsmScrollableStreamsItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
            viewGroup, false));
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int postion) {

    StreamsViewHolder holder = (StreamsViewHolder) viewHolder;

    try {
      LiveStreamsModel stream = streams.get(postion);
      holder.ismScrollableStreamsItemBinding.ivStreamImage.setVisibility(View.VISIBLE);
      try {

        //Stream image url
        GlideApp.with(mContext)
            .load(stream.streamImage)
            .transform(new CenterCrop())
            .transform(new BlurTransformation())
            .into(holder.ismScrollableStreamsItemBinding.ivStreamImage);
      } catch (IllegalArgumentException | NullPointerException ignore) {

      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * The type Streams view holder.
   */
  public static class StreamsViewHolder extends RecyclerView.ViewHolder {

    public final IsmScrollableStreamsItemBinding ismScrollableStreamsItemBinding;

    public StreamsViewHolder(
        final IsmScrollableStreamsItemBinding ismScrollableStreamsItemBinding) {
      super(ismScrollableStreamsItemBinding.getRoot());
      this.ismScrollableStreamsItemBinding = ismScrollableStreamsItemBinding;
    }
  }
}