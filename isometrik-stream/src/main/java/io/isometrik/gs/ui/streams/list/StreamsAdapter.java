package io.isometrik.gs.ui.streams.list;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmLivestreamsItemBinding;
import io.isometrik.gs.ui.scrollable.webrtc.MultiliveStreamsActivity;
import io.isometrik.gs.ui.utils.GlideApp;
import io.isometrik.gs.ui.utils.PlaceholderUtils;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * The type Streams adapter.
 */
public class StreamsAdapter extends RecyclerView.Adapter {

  private final Context mContext;
  private final ArrayList<LiveStreamsModel> streams;
  private final String userId;
  private final int cornerRadiusInPixels;

  private final StreamsFragment streamsFragment;

  /**
   * Instantiates a new Streams adapter.
   *
   * @param mContext the m context
   * @param streams the streams
   */
  StreamsAdapter(Context mContext, ArrayList<LiveStreamsModel> streams,
      StreamsFragment streamsFragment) {
    this.mContext = mContext;
    this.streams = streams;
    this.streamsFragment = streamsFragment;
    userId = IsometrikStreamSdk.getInstance().getUserSession().getUserId();
    cornerRadiusInPixels = (int) (Resources.getSystem().getDisplayMetrics().density * 13);
  }

  @Override
  public int getItemCount() {
    return streams.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

    return new StreamsViewHolder(
        IsmLivestreamsItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup,
            false));
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    StreamsViewHolder holder = (StreamsViewHolder) viewHolder;

    try {
      LiveStreamsModel stream = streams.get(position);
      holder.ismLivestreamsItemBinding.tvNoOfMembers.setText(
          String.valueOf(stream.membersCount));
      holder.ismLivestreamsItemBinding.tvNoOfViewers.setText(
          String.valueOf(stream.viewersCount));
      holder.ismLivestreamsItemBinding.tvNoOfPublishers.setText(
          String.valueOf(stream.publishersCount));
      holder.ismLivestreamsItemBinding.tvStreamDescription.setText(stream.streamDescription);
      holder.ismLivestreamsItemBinding.tvStreamDuration.setText(stream.duration);
      holder.ismLivestreamsItemBinding.tvInitiatorName.setText(stream.initiatorName);

      if (stream.isGivenUserCanPublish) {

        holder.ismLivestreamsItemBinding.btJoin.setOnClickListener(v -> {
          Intent intent = new Intent(mContext, MultiliveStreamsActivity.class);
          intent.putExtra("streamId", stream.streamId);
          intent.putExtra("streamDescription", stream.streamDescription);
          intent.putExtra("streamImage", stream.streamImage);
          intent.putExtra("startTime", stream.startTime);
          intent.putExtra("membersCount", stream.membersCount);
          intent.putExtra("viewersCount", stream.viewersCount);
          intent.putExtra("publishersCount", stream.publishersCount);
          intent.putExtra("joinRequest", true);
          intent.putExtra("isAdmin", stream.initiatorId.equals(userId));
          intent.putExtra("isPkChallenge", stream.isPkChallenge());
          intent.putExtra("pkId", stream.getPkId());
          intent.putExtra("initiatorName", stream.initiatorName);
          intent.putExtra("publishRequired", true);
          intent.putExtra("initiatorId", stream.initiatorId);
          intent.putExtra("initiatorIdentifier", stream.initiatorIdentifier);
          intent.putExtra("initiatorImage", stream.initiatorImage);
          intent.putExtra("isPublic", stream.isPublic);
          intent.putExtra("audioOnly", stream.isAudioOnly());
          intent.putExtra("isBroadcaster", true);
          intent.putExtra("multiLive", stream.isMultiLive);
          intent.putExtra("hdBroadcast", stream.isHdBroadcast());
          intent.putExtra("restream", stream.isRestream());
          intent.putExtra("productsLinked", stream.isProductsLinked());
          intent.putExtra("productsCount", stream.productsCount);
          intent.putExtra("streamDescription", stream.streamDescription);
          intent.putExtra("selfHosted", stream.isSelfHosted());
          //mContext.startActivity(intent);

          streamsFragment.startBroadcast(intent);
        });

        holder.ismLivestreamsItemBinding.ivStreamImage.setOnClickListener(v -> {

          Intent  intent = new Intent(mContext, MultiliveStreamsActivity.class);

          intent.putExtra("streamId", stream.streamId);
          intent.putExtra("streamDescription", stream.streamDescription);
          intent.putExtra("streamImage", stream.streamImage);
          intent.putExtra("startTime", stream.startTime);
          intent.putExtra("membersCount", stream.membersCount);
          intent.putExtra("viewersCount", stream.viewersCount);
          intent.putExtra("publishersCount", stream.publishersCount);
          intent.putExtra("isAdmin", stream.initiatorId.equals(userId));
          intent.putExtra("isPkChallenge", stream.isPkChallenge());
          intent.putExtra("pkId", stream.getPkId());
          intent.putExtra("initiatorName", stream.initiatorName);
          intent.putExtra("initiatorId", stream.initiatorId);
          intent.putExtra("initiatorIdentifier", stream.initiatorIdentifier);
          intent.putExtra("initiatorImage", stream.initiatorImage);
          intent.putExtra("isPublic", stream.isPublic);
          intent.putExtra("audioOnly", stream.isAudioOnly());
          intent.putExtra("isBroadcaster", false);
          intent.putExtra("streamPosition", position);
          intent.putExtra("streams", (Serializable) streams);
          intent.putExtra("multiLive", stream.isMultiLive);
          intent.putExtra("hdBroadcast", stream.isHdBroadcast());
          intent.putExtra("restream", stream.isRestream());
          intent.putExtra("productsLinked", stream.isProductsLinked());
          intent.putExtra("productsCount", stream.productsCount);
          intent.putExtra("streamDescription", stream.streamDescription);
          intent.putExtra("selfHosted", stream.isSelfHosted());
          intent.putExtra("secondUserStreamUserId", stream.getSecondUserStreamUserId());
          intent.putExtra("secondUserStreamId", stream.getSecondUserStreamId());
          mContext.startActivity(intent);
        });

        holder.ismLivestreamsItemBinding.btJoin.setVisibility(View.VISIBLE);
      } else {
        holder.ismLivestreamsItemBinding.btJoin.setVisibility(View.GONE);

        holder.ismLivestreamsItemBinding.ivStreamImage.setOnClickListener(v -> {
          Intent intent = new Intent(mContext, MultiliveStreamsActivity.class);

          intent.putExtra("streamId", stream.streamId);
          intent.putExtra("streamDescription", stream.streamDescription);
          intent.putExtra("streamImage", stream.streamImage);
          intent.putExtra("startTime", stream.startTime);
          intent.putExtra("membersCount", stream.membersCount);
          intent.putExtra("viewersCount", stream.viewersCount);
          intent.putExtra("publishersCount", stream.publishersCount);
          intent.putExtra("isAdmin", stream.initiatorId.equals(userId));
          intent.putExtra("isPkChallenge", stream.isPkChallenge());
          intent.putExtra("pkId", stream.getPkId());
          intent.putExtra("initiatorName", stream.initiatorName);
          intent.putExtra("initiatorId", stream.initiatorId);
          intent.putExtra("initiatorIdentifier", stream.initiatorIdentifier);
          intent.putExtra("initiatorImage", stream.initiatorImage);
          intent.putExtra("isPublic", stream.isPublic);
          intent.putExtra("audioOnly", stream.isAudioOnly());
          intent.putExtra("isBroadcaster", false);
          intent.putExtra("streamPosition", position);
          intent.putExtra("streams", (Serializable) streams);
          intent.putExtra("multiLive", stream.isMultiLive);
          intent.putExtra("hdBroadcast", stream.isHdBroadcast());
          intent.putExtra("restream", stream.isRestream());
          intent.putExtra("productsLinked", stream.isProductsLinked());
          intent.putExtra("productsCount", stream.productsCount);
          intent.putExtra("streamDescription", stream.streamDescription);
          intent.putExtra("selfHosted", stream.isSelfHosted());
          mContext.startActivity(intent);
        });
      }

      try {
        GlideApp.with(mContext)
            .load(stream.streamImage)
            .transform(new CenterCrop(), new RoundedCorners(cornerRadiusInPixels))
            .into(holder.ismLivestreamsItemBinding.ivStreamImage);
      } catch (IllegalArgumentException | NullPointerException ignore) {

      }
      if (PlaceholderUtils.isValidImageUrl(stream.initiatorImage)) {

        try {
          GlideApp.with(mContext)
              .load(stream.initiatorImage)
              .placeholder(R.drawable.ism_default_profile_image)
              .transform(new CircleCrop())
              .into(holder.ismLivestreamsItemBinding.ivInitiatorImage);
        } catch (IllegalArgumentException | NullPointerException ignore) {

        }
      } else {
        PlaceholderUtils.setTextRoundDrawable(mContext, stream.initiatorName,
            holder.ismLivestreamsItemBinding.ivInitiatorImage, position, 12);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * The type Streams view holder.
   */
  static class StreamsViewHolder extends RecyclerView.ViewHolder {
    private final IsmLivestreamsItemBinding ismLivestreamsItemBinding;

    public StreamsViewHolder(final IsmLivestreamsItemBinding ismLivestreamsItemBinding) {
      super(ismLivestreamsItemBinding.getRoot());
      this.ismLivestreamsItemBinding = ismLivestreamsItemBinding;
    }
  }
}