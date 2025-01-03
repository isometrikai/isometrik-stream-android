package io.isometrik.gs.ui.restream.list;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.gs.ui.databinding.IsmRestreamChannelsItemBinding;
import io.isometrik.gs.ui.utils.ColorsUtil;
import io.isometrik.gs.ui.utils.TextDrawable;
import java.util.ArrayList;

public class ChannelsAdapter extends RecyclerView.Adapter {
  private final Context mContext;
  private final ArrayList<ChannelsModel> channels;
  private final boolean isSelectedUser;
  private final FetchChannelsFragment fetchChannelsFragment;
  private final int textSize;

  /**
   * Instantiates a new Channels adapter.
   *
   * @param mContext the m context
   * @param channels the list of channels
   * @param isSelectedUser whether given user is selected or created user,as selected users are not
   * allowed to update/delete channel
   * @param fetchChannelsFragment instance of FetchChannelsFragment to allow edit/delete request
   * callback
   */
  ChannelsAdapter(Context mContext, ArrayList<ChannelsModel> channels, boolean isSelectedUser,
      FetchChannelsFragment fetchChannelsFragment) {
    this.mContext = mContext;
    this.channels = channels;
    this.isSelectedUser = isSelectedUser;
    //this.isSelectedUser=false;
    this.fetchChannelsFragment = fetchChannelsFragment;
    textSize = (int) (mContext.getResources().getDisplayMetrics().density * 24);
  }

  @Override
  public int getItemCount() {
    return channels.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    return new ChannelsViewHolder(
        IsmRestreamChannelsItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
            viewGroup, false));
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    ChannelsViewHolder holder = (ChannelsViewHolder) viewHolder;

    try {
      ChannelsModel channel = channels.get(position);
      if (channel != null) {
        holder.ismRestreamChannelsItemBinding.swToggleChannel.setOnCheckedChangeListener(null);
        holder.ismRestreamChannelsItemBinding.swToggleChannel.setChecked(channel.isEnabled());
        holder.ismRestreamChannelsItemBinding.swToggleChannel.setEnabled(!isSelectedUser);
//        if (isSelectedUser) {
////          holder.ismRestreamChannelsItemBinding.rlAdmin.setVisibility(View.GONE);
//        } else {
          holder.ismRestreamChannelsItemBinding.rlAdmin.setVisibility(View.VISIBLE);
          holder.ismRestreamChannelsItemBinding.ivEdit.setOnClickListener(
              view -> fetchChannelsFragment.editChannel(channel));
          holder.ismRestreamChannelsItemBinding.ivDelete.setOnClickListener(
              view -> fetchChannelsFragment.deleteChannel(channel.getChannelId()));

          holder.ismRestreamChannelsItemBinding.swToggleChannel.setOnCheckedChangeListener(
              (compoundButton, b) -> fetchChannelsFragment.toggleRestreamChannel(
                  channel.getChannelId(), b));
//        }
        holder.ismRestreamChannelsItemBinding.ivChannelType.setImageDrawable(
            ContextCompat.getDrawable(mContext, channel.getChannelTypeDrawable()));

        holder.ismRestreamChannelsItemBinding.tvChannelName.setText(channel.getChannelName());
        holder.ismRestreamChannelsItemBinding.tvChannelEndpoint.setText(channel.getIngestUrl());

        try {
          holder.ismRestreamChannelsItemBinding.ivChannelImage.setImageDrawable(
              TextDrawable.builder()

                  .beginConfig()
                  .textColor(Color.WHITE)
                  .useFont(Typeface.DEFAULT)
                  .fontSize(textSize)
                  .bold()
                  .toUpperCase()
                  .endConfig()
                  .buildRound((channel.getChannelName().trim()).charAt(0) + "",
                      Color.parseColor(ColorsUtil.getColorCode(position % 19))));
        } catch (Exception ignore) {
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * The type Channels view holder.
   */
  static class ChannelsViewHolder extends RecyclerView.ViewHolder {

    private final IsmRestreamChannelsItemBinding ismRestreamChannelsItemBinding;

    public ChannelsViewHolder(final IsmRestreamChannelsItemBinding ismRestreamChannelsItemBinding) {
      super(ismRestreamChannelsItemBinding.getRoot());
      this.ismRestreamChannelsItemBinding = ismRestreamChannelsItemBinding;
    }
  }
}
