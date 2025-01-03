package io.isometrik.gs.ui.messages.replies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmRemovedMessageItemBinding;
import io.isometrik.gs.ui.databinding.IsmTextMessageItemBinding;
import io.isometrik.gs.ui.utils.GlideApp;
import io.isometrik.gs.ui.utils.PlaceholderUtils;
import java.util.ArrayList;

/**
 * The type Messages adapter.
 */
public class MessageRepliesAdapter extends RecyclerView.Adapter {

  private final Context mContext;
  private final ArrayList<MessageRepliesModel> messages;

  private final int NORMAL_MESSAGE = 0;
  private final int REMOVED_MESSAGE = 3;

  private final MessageRepliesFragment messageRepliesFragment;

  /**
   * Instantiates a new Message replies adapter.
   *
   * @param mContext the m context
   * @param messages the messages
   * @param messageRepliesFragment instance of fragment for remove message callback
   */
  public MessageRepliesAdapter(Context mContext, ArrayList<MessageRepliesModel> messages,
      MessageRepliesFragment messageRepliesFragment) {
    this.mContext = mContext;
    this.messages = messages;
    this.messageRepliesFragment = messageRepliesFragment;
  }

  @Override
  public int getItemViewType(int position) {

    if (messages.get(position).getMessageItemType() == 3) {
      return REMOVED_MESSAGE;
    }
    return NORMAL_MESSAGE;
  }

  @Override
  public int getItemCount() {
    return messages.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    if (viewType == REMOVED_MESSAGE) {
      return new MessageRepliesViewHolder(
          IsmRemovedMessageItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
              viewGroup, false));
    }
    return new MessageRepliesViewHolder(
        IsmTextMessageItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup,
            false));
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    if (viewHolder.getItemViewType() == REMOVED_MESSAGE) {
      configureViewHolderRemoveMessage((MessageRepliesViewHolder) viewHolder, position);
    } else {
      configureViewHolderNormalMessage((MessageRepliesViewHolder) viewHolder, position);
    }
  }

  private void configureViewHolderRemoveMessage(MessageRepliesViewHolder viewHolder,
      final int position) {

    try {
      MessageRepliesModel message = messages.get(position);
      if (message != null) {
        viewHolder.ismRemovedMessageItemBinding.tvMessageTime.setText(message.getMessageTime());
        viewHolder.ismRemovedMessageItemBinding.tvMessage.setText(message.getMessage());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void configureViewHolderNormalMessage(MessageRepliesViewHolder viewHolder,
      final int position) {

    try {
      MessageRepliesModel message = messages.get(position);
      if (message != null) {

        viewHolder.ismTextMessageItemBinding.tvMessage.setText(message.getMessage());
        viewHolder.ismTextMessageItemBinding.tvUserName.setText(message.getSenderName());
        viewHolder.ismTextMessageItemBinding.tvMessageTime.setText(message.getMessageTime());
        viewHolder.ismTextMessageItemBinding.ivReply.setVisibility(View.GONE);
        viewHolder.ismTextMessageItemBinding.tvViewReplies.setVisibility(View.GONE);

        if (message.isCanRemoveMessage()) {

          viewHolder.ismTextMessageItemBinding.ivDeleteMessage.setVisibility(View.VISIBLE);

          viewHolder.ismTextMessageItemBinding.ivDeleteMessage.setOnClickListener(v -> {
            messageRepliesFragment.removeMessageReply(message.getMessageId(),
                message.getTimestamp());
          });
        } else {
          viewHolder.ismTextMessageItemBinding.ivDeleteMessage.setVisibility(View.GONE);
        }
        if (message.isReceivedMessage()) {
          viewHolder.ismTextMessageItemBinding.ivDeliveryStatus.setVisibility(View.INVISIBLE);
        } else {

          viewHolder.ismTextMessageItemBinding.ivDeliveryStatus.setVisibility(View.VISIBLE);
          if (message.isDelivered()) {

            viewHolder.ismTextMessageItemBinding.ivDeliveryStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_ic_done));
          } else {
            viewHolder.ismTextMessageItemBinding.ivDeliveryStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_ic_access_time));
          }
        }
        if (PlaceholderUtils.isValidImageUrl(message.getSenderImage())) {

          try {
            GlideApp.with(mContext)
                .load(message.getSenderImage())
                .placeholder(R.drawable.ism_default_profile_image)
                .transform(new CircleCrop())
                .into(viewHolder.ismTextMessageItemBinding.ivProfilePic);
          } catch (IllegalArgumentException | NullPointerException ignore) {

          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getSenderName(),
              viewHolder.ismTextMessageItemBinding.ivProfilePic, position, 20);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * The type Messages view holder.
   */
  static class MessageRepliesViewHolder extends RecyclerView.ViewHolder {

    private IsmRemovedMessageItemBinding ismRemovedMessageItemBinding;
    private IsmTextMessageItemBinding ismTextMessageItemBinding;

    public MessageRepliesViewHolder(
        final IsmRemovedMessageItemBinding ismRemovedMessageItemBinding) {
      super(ismRemovedMessageItemBinding.getRoot());
      this.ismRemovedMessageItemBinding = ismRemovedMessageItemBinding;
    }

    public MessageRepliesViewHolder(final IsmTextMessageItemBinding ismTextMessageItemBinding) {
      super(ismTextMessageItemBinding.getRoot());
      this.ismTextMessageItemBinding = ismTextMessageItemBinding;
    }
  }
}