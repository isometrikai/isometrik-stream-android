package io.isometrik.gs.ui.messages;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmGiftMessageItemBinding;
import io.isometrik.gs.ui.databinding.IsmLikeMessageItemBinding;
import io.isometrik.gs.ui.databinding.IsmPresenceMessageItemBinding;
import io.isometrik.gs.ui.databinding.IsmRemovedMessageItemBinding;
import io.isometrik.gs.ui.databinding.IsmRequestAcceptedMessageItemBinding;
import io.isometrik.gs.ui.databinding.IsmRequestActionMessageItemBinding;
import io.isometrik.gs.ui.databinding.IsmRequestMessageItemBinding;
import io.isometrik.gs.ui.databinding.IsmTextMessageItemBinding;
import io.isometrik.gs.ui.scrollable.webrtc.MultiliveStreamsActivity;
import io.isometrik.gs.ui.utils.GlideApp;
import io.isometrik.gs.ui.utils.PlaceholderUtils;
import java.util.ArrayList;

/**
 * The type Messages adapter.
 */
public class MessagesAdapter extends RecyclerView.Adapter {

  private final Context mContext;
  private final ArrayList<MessagesModel> messages;

  private final int NORMAL_MESSAGE = 0;
  private final int LIKE_MESSAGE = 1;
  public static final int GIFT_MESSAGE = 2;
  private final int REMOVED_MESSAGE = 3;
  private final int PRESENCE_MESSAGE = 4;
  private final int COPUBLISH_REQUEST_MESSAGE = 5;
  private final int COPUBLISH_ACCEPTED_MESSAGE = 6;
  private final int COPUBLISH_ACTION_MESSAGE = 7;
  private boolean onlyForGift = false;

  /**
   * Instantiates a new Messages adapter.
   *
   * @param mContext    the m context
   * @param messages    the messages
   * @param onlyForGift only for gift
   */
  public MessagesAdapter(Context mContext, ArrayList<MessagesModel> messages, boolean onlyForGift) {
    this.mContext = mContext;
    this.messages = messages;
    this.onlyForGift = onlyForGift;
  }

  @Override
  public int getItemViewType(int position) {

    switch (messages.get(position).getMessageItemType()) {

      case 2:
        return GIFT_MESSAGE;
      case 3:
        return LIKE_MESSAGE;
      case 4:
        return REMOVED_MESSAGE;
      case 5:
        return PRESENCE_MESSAGE;
      case 6:
        return COPUBLISH_REQUEST_MESSAGE;
      case 7:
        return COPUBLISH_ACCEPTED_MESSAGE;
      case 8:
        return COPUBLISH_ACTION_MESSAGE;
      default:
        return NORMAL_MESSAGE;
    }
  }

  @Override
  public int getItemCount() {
    return messages.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    switch (viewType) {

      case LIKE_MESSAGE:

        return new MessagesViewHolder(IsmLikeMessageItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
      case GIFT_MESSAGE:

        return new MessagesViewHolder(IsmGiftMessageItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
      case REMOVED_MESSAGE:

        return new MessagesViewHolder(IsmRemovedMessageItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
      case PRESENCE_MESSAGE:

        return new MessagesViewHolder(IsmPresenceMessageItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
      case COPUBLISH_REQUEST_MESSAGE:

        return new MessagesViewHolder(IsmRequestMessageItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
      case COPUBLISH_ACCEPTED_MESSAGE:

        return new MessagesViewHolder(IsmRequestAcceptedMessageItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
      case COPUBLISH_ACTION_MESSAGE:

        return new MessagesViewHolder(IsmRequestActionMessageItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
      default:

        return new MessagesViewHolder(IsmTextMessageItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    switch (viewHolder.getItemViewType()) {

      case LIKE_MESSAGE:

        configureViewHolderLikeMessage((MessagesViewHolder) viewHolder, position);
        break;

      case GIFT_MESSAGE:

        configureViewHolderGiftMessage((MessagesViewHolder) viewHolder, position);
        break;

      case REMOVED_MESSAGE:

        configureViewHolderRemoveMessage((MessagesViewHolder) viewHolder, position);
        break;

      case PRESENCE_MESSAGE:

        configureViewHolderPresenceMessage((MessagesViewHolder) viewHolder, position);
        break;

      case COPUBLISH_REQUEST_MESSAGE:

        configureViewHolderCopublishRequestedMessage((MessagesViewHolder) viewHolder, position);
        break;

      case COPUBLISH_ACCEPTED_MESSAGE:

        configureViewHolderCopublishAcceptedMessage((MessagesViewHolder) viewHolder, position);
        break;

      case COPUBLISH_ACTION_MESSAGE:

        configureViewHolderCopublishActionMessage((MessagesViewHolder) viewHolder, position);
        break;

      default:

        configureViewHolderNormalMessage((MessagesViewHolder) viewHolder, position);
        break;
    }
  }

  private void configureViewHolderPresenceMessage(MessagesViewHolder viewHolder, final int position) {

    try {
      MessagesModel message = messages.get(position);
      if (message != null) {
        viewHolder.ismPresenceMessageItemBinding.tvMessage.setText(message.getMessage());
        viewHolder.ismPresenceMessageItemBinding.tvMessageTime.setText(message.getMessageTime());

        if (message.getMessage().contains("kicked out")) {
          viewHolder.ismPresenceMessageItemBinding.ivProfilePic.setImageResource(R.drawable.ic_kickedout_msg);
        } else if (message.getMessage().contains("left the audience")) {
          viewHolder.ismPresenceMessageItemBinding.ivProfilePic.setImageResource(R.drawable.ic_left_msg);
        } else {
          viewHolder.ismPresenceMessageItemBinding.ivProfilePic.setImageResource(R.drawable.ic_joined_msg);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void configureViewHolderRemoveMessage(MessagesViewHolder viewHolder, final int position) {

    try {
      MessagesModel message = messages.get(position);
      if (message != null) {
        viewHolder.ismRemovedMessageItemBinding.tvMessageTime.setText(message.getMessageTime());
        viewHolder.ismRemovedMessageItemBinding.tvMessage.setText(message.getMessage());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void configureViewHolderNormalMessage(MessagesViewHolder viewHolder, final int position) {

    try {
      MessagesModel message = messages.get(position);
      if (message != null) {
//                createMessage(message.getSenderName() +" : ",message.getMessage(),viewHolder.ismTextMessageItemBinding.tvMessage);

        viewHolder.ismTextMessageItemBinding.tvMessage.setText(message.getMessage());
        viewHolder.ismTextMessageItemBinding.tvUserName.setText(message.getSenderName());
        viewHolder.ismTextMessageItemBinding.tvMessageTime.setText(message.getMessageTime());

        if (message.canReply()) {
          viewHolder.ismTextMessageItemBinding.ivReply.setVisibility(View.VISIBLE);
          viewHolder.ismTextMessageItemBinding.ivReply.setOnClickListener(v -> {
            if (mContext instanceof MultiliveStreamsActivity) {
              ((MultiliveStreamsActivity) mContext).fetchMessageReplies(message.getMessageId(), message.getTimestamp(), true, message.getMessage());
            }
          });
        } else {
          viewHolder.ismTextMessageItemBinding.ivReply.setVisibility(View.GONE);
        }

        viewHolder.ismTextMessageItemBinding.ivReply.setVisibility(View.GONE);

        if (message.hasReplies() && message.canReply()) {
          viewHolder.ismTextMessageItemBinding.tvViewReplies.setVisibility(View.VISIBLE);
          viewHolder.ismTextMessageItemBinding.tvViewReplies.setOnClickListener(v -> {
            if (mContext instanceof MultiliveStreamsActivity) {

              ((MultiliveStreamsActivity) mContext).fetchMessageReplies(message.getMessageId(), message.getTimestamp(), false, message.getMessage());
            }
          });
        } else {
          viewHolder.ismTextMessageItemBinding.tvViewReplies.setVisibility(View.GONE);
        }

        if (message.isCanRemoveMessage()) {

          viewHolder.ismTextMessageItemBinding.ivDeleteMessage.setVisibility(View.VISIBLE);

          viewHolder.ismTextMessageItemBinding.ivDeleteMessage.setOnClickListener(v -> {

            if (mContext instanceof MultiliveStreamsActivity) {

              ((MultiliveStreamsActivity) mContext).removeMessage(message.getMessageId(), message.getTimestamp());
            }
          });
        } else {
          viewHolder.ismTextMessageItemBinding.ivDeleteMessage.setVisibility(View.GONE);
        }
//        if (message.isReceivedMessage()) {
//          viewHolder.ismTextMessageItemBinding.ivDeliveryStatus.setVisibility(View.INVISIBLE);
//        } else {
//
//          viewHolder.ismTextMessageItemBinding.ivDeliveryStatus.setVisibility(View.VISIBLE);
//          if (message.isDelivered()) {
//
//            viewHolder.ismTextMessageItemBinding.ivDeliveryStatus.setImageDrawable(
//                ContextCompat.getDrawable(mContext, R.drawable.ism_ic_done));
//          } else {
//            viewHolder.ismTextMessageItemBinding.ivDeliveryStatus.setImageDrawable(
//                ContextCompat.getDrawable(mContext, R.drawable.ism_ic_access_time));
//          }
//        }
        if (PlaceholderUtils.isValidImageUrl(message.getSenderImage())) {

          try {
            Glide.with(mContext).load(message.getSenderImage()).placeholder(R.drawable.ism_default_profile_image).transform(new CircleCrop()).into(viewHolder.ismTextMessageItemBinding.ivProfilePic);
          } catch (IllegalArgumentException | NullPointerException ignore) {

          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getSenderName(), viewHolder.ismTextMessageItemBinding.ivProfilePic, position, 8);
        }


        viewHolder.ismTextMessageItemBinding.rlParent.setOnClickListener(view -> {
          if (mContext instanceof MultiliveStreamsActivity) {
            ((MultiliveStreamsActivity) mContext).remoteUserActionClicked(message.getSenderId(), message.getSenderName(), message.getSenderIdentifier(), message.getSenderImage());
          }
        });

      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  private void configureViewHolderLikeMessage(MessagesViewHolder viewHolder, final int position) {

    try {
      MessagesModel message = messages.get(position);
      if (message != null) {

        viewHolder.ismLikeMessageItemBinding.tvUserName.setText(message.getSenderName());
        viewHolder.ismLikeMessageItemBinding.tvMessageTime.setText(message.getMessageTime());

//        if (message.isReceivedMessage()) {
//          viewHolder.ismLikeMessageItemBinding.ivDeliveryStatus.setVisibility(View.INVISIBLE);
//        } else {
//
//          viewHolder.ismLikeMessageItemBinding.ivDeliveryStatus.setVisibility(View.VISIBLE);
//          if (message.isDelivered()) {
//
//            viewHolder.ismLikeMessageItemBinding.ivDeliveryStatus.setImageDrawable(
//                ContextCompat.getDrawable(mContext, R.drawable.ism_ic_done));
//          } else {
//            viewHolder.ismLikeMessageItemBinding.ivDeliveryStatus.setImageDrawable(
//                ContextCompat.getDrawable(mContext, R.drawable.ism_ic_access_time));
//          }
//        }
        if (PlaceholderUtils.isValidImageUrl(message.getSenderImage())) {
          try {
            Glide.with(mContext).load(message.getSenderImage()).placeholder(R.drawable.ism_default_profile_image).transform(new CircleCrop()).into(viewHolder.ismLikeMessageItemBinding.ivProfilePic);
          } catch (IllegalArgumentException | NullPointerException ignore) {

          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getSenderName(), viewHolder.ismLikeMessageItemBinding.ivProfilePic, position, 8);
        }
        try {
          Glide.with(mContext).load(message.getMessage())
                  //              .crossFade()
                  .diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(new DrawableImageViewTarget(viewHolder.ismLikeMessageItemBinding.ivLike));
          //GlideApp.with(mContext)
          //    .load(message.getMessage())
          //    .into(viewHolder.ismLikeMessageItemBinding.ivLike);
        } catch (IllegalArgumentException | NullPointerException ignore) {

        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @SuppressLint("SetTextI18n")
  private void configureViewHolderGiftMessage(MessagesViewHolder viewHolder, final int position) {

    try {
      MessagesModel message = messages.get(position);
      if (message != null) {
        if (onlyForGift) {
          if (PlaceholderUtils.isValidImageUrl(message.getSenderImage())) {

            try {
              Glide.with(mContext).load(message.getSenderImage()).placeholder(R.drawable.ism_default_profile_image).transform(new CircleCrop()).into(viewHolder.ismGiftMessageItemBinding.ivProfilePic);
            } catch (IllegalArgumentException | NullPointerException ignore) {

            }
          } else {
            PlaceholderUtils.setTextRoundDrawable(mContext, message.getSenderName(), viewHolder.ismGiftMessageItemBinding.ivProfilePic, position, 8);
          }
          viewHolder.ismGiftMessageItemBinding.tvUserName.setText(message.getSenderName());

          viewHolder.ismGiftMessageItemBinding.tvCoinValue.setText(mContext.getString(R.string.coins) + " " + message.getCoinValue());
          viewHolder.ismGiftMessageItemBinding.ivGift.setVisibility(View.GONE);
          viewHolder.ismGiftMessageItemBinding.ivGiftSmall.setVisibility(View.VISIBLE);

          try {
            Glide.with(mContext).load(message.getMessage()).transition(withCrossFade()).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(new DrawableImageViewTarget(viewHolder.ismGiftMessageItemBinding.ivGiftSmall, true));
            //GlideApp.with(mContext)
            //    .load(message.getMessage())
            //    .into(viewHolder.ismGiftMessageItemBinding.ivGift);
          } catch (IllegalArgumentException | NullPointerException ignore) {

          } catch (Exception e) {
            e.printStackTrace();
          }

        } else {

          viewHolder.ismGiftMessageItemBinding.tvUserName.setText(message.getSenderName());
          viewHolder.ismGiftMessageItemBinding.tvMessageTime.setText(message.getMessageTime());

          if (message.getReceiverName() != null && !message.getReceiverName().isEmpty()) {
            viewHolder.ismGiftMessageItemBinding.tvCoinValue.setText(message.getGiftName() + " : " + message.getCoinValue() + ", sent from " + message.getSenderName() + " \nto " + message.getReceiverName());
          } else {
            viewHolder.ismGiftMessageItemBinding.tvCoinValue.setText(message.getGiftName() + " : " + message.getCoinValue() + ", sent from " + message.getSenderName());
          }


          viewHolder.ismGiftMessageItemBinding.ivGift.setVisibility(View.VISIBLE);
          viewHolder.ismGiftMessageItemBinding.ivGiftSmall.setVisibility(View.GONE);


//        if (message.isReceivedMessage()) {
//          viewHolder.ismGiftMessageItemBinding.ivDeliveryStatus.setVisibility(View.INVISIBLE);
//        } else {
//
//          viewHolder.ismGiftMessageItemBinding.ivDeliveryStatus.setVisibility(View.VISIBLE);
//          if (message.isDelivered()) {
//
//            viewHolder.ismGiftMessageItemBinding.ivDeliveryStatus.setImageDrawable(
//                ContextCompat.getDrawable(mContext, R.drawable.ism_ic_done));
//          } else {
//            viewHolder.ismGiftMessageItemBinding.ivDeliveryStatus.setImageDrawable(
//                ContextCompat.getDrawable(mContext, R.drawable.ism_ic_access_time));
//          }
//        }
          if (PlaceholderUtils.isValidImageUrl(message.getSenderImage())) {

            try {
              Glide.with(mContext).load(message.getSenderImage()).placeholder(R.drawable.ism_default_profile_image).transform(new CircleCrop()).into(viewHolder.ismGiftMessageItemBinding.ivProfilePic);
            } catch (IllegalArgumentException | NullPointerException ignore) {

            }
          } else {
            PlaceholderUtils.setTextRoundDrawable(mContext, message.getSenderName(), viewHolder.ismGiftMessageItemBinding.ivProfilePic, position, 8);
          }
          try {
            Glide.with(mContext).load(message.getMessage()).transition(withCrossFade()).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(new DrawableImageViewTarget(viewHolder.ismGiftMessageItemBinding.ivGift, true));
            //GlideApp.with(mContext)
            //    .load(message.getMessage())
            //    .into(viewHolder.ismGiftMessageItemBinding.ivGift);
          } catch (IllegalArgumentException | NullPointerException ignore) {

          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    } catch (Exception e) {
    }
  }

  private void configureViewHolderCopublishRequestedMessage(MessagesViewHolder viewHolder, final int position) {

    try {
      MessagesModel message = messages.get(position);
      if (message != null) {
//                createMessage(message.getSenderName(),message.getMessage(),viewHolder.ismRequestMessageItemBinding.tvMessage);

        viewHolder.ismRequestMessageItemBinding.tvMessage.setText(message.getMessage());
        viewHolder.ismRequestMessageItemBinding.tvUserName.setText(message.getSenderName());
        viewHolder.ismRequestMessageItemBinding.tvMessageTime.setText(message.getMessageTime());
        if (message.isInitiator()) {
          viewHolder.ismRequestMessageItemBinding.rlAccept.setVisibility(View.VISIBLE);
          viewHolder.ismRequestMessageItemBinding.rlDecline.setVisibility(View.VISIBLE);
          viewHolder.ismRequestMessageItemBinding.rlAccept.setOnClickListener(v -> {
            if (mContext instanceof MultiliveStreamsActivity) {
              ((MultiliveStreamsActivity) mContext).acceptCopublishRequest(message.getSenderId());
            }
          });

          viewHolder.ismRequestMessageItemBinding.rlDecline.setOnClickListener(v -> {
            if (mContext instanceof MultiliveStreamsActivity) {
              ((MultiliveStreamsActivity) mContext).declineCopublishRequest(message.getSenderId());
            }
          });
        } else {
          viewHolder.ismRequestMessageItemBinding.rlAccept.setVisibility(View.GONE);
          viewHolder.ismRequestMessageItemBinding.rlDecline.setVisibility(View.GONE);
        }
        if (PlaceholderUtils.isValidImageUrl(message.getSenderImage())) {

          try {
            Glide.with(mContext).load(message.getSenderImage()).placeholder(R.drawable.ism_default_profile_image).transform(new CircleCrop()).into(viewHolder.ismRequestMessageItemBinding.ivProfilePic);
          } catch (IllegalArgumentException | NullPointerException ignore) {

          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getSenderName(), viewHolder.ismRequestMessageItemBinding.ivProfilePic, position, 8);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void configureViewHolderCopublishAcceptedMessage(MessagesViewHolder viewHolder, final int position) {

    try {
      MessagesModel message = messages.get(position);
      if (message != null) {
//                createMessage(message.getSenderName(),message.getMessage(),viewHolder.ismRequestAcceptedMessageItemBinding.tvMessage);

        viewHolder.ismRequestAcceptedMessageItemBinding.tvMessage.setText(message.getMessage());
        viewHolder.ismRequestAcceptedMessageItemBinding.tvUserName.setText(message.getSenderName());
        viewHolder.ismRequestAcceptedMessageItemBinding.tvMessageTime.setText(message.getMessageTime());
        if (message.isCanJoin()) {
          viewHolder.ismRequestAcceptedMessageItemBinding.rlJoin.setVisibility(View.VISIBLE);
          viewHolder.ismRequestAcceptedMessageItemBinding.rlJoin.setOnClickListener(v -> {
            if (mContext instanceof MultiliveStreamsActivity) {
              ((MultiliveStreamsActivity) mContext).switchProfile();
            }
          });
        } else {
          viewHolder.ismRequestAcceptedMessageItemBinding.rlJoin.setVisibility(View.GONE);
        }
        if (PlaceholderUtils.isValidImageUrl(message.getSenderImage())) {

          try {
            Glide.with(mContext).load(message.getSenderImage()).placeholder(R.drawable.ism_default_profile_image).transform(new CircleCrop()).into(viewHolder.ismRequestAcceptedMessageItemBinding.ivProfilePic);
          } catch (IllegalArgumentException | NullPointerException ignore) {

          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getSenderName(), viewHolder.ismRequestAcceptedMessageItemBinding.ivProfilePic, position, 8);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void configureViewHolderCopublishActionMessage(MessagesViewHolder viewHolder, final int position) {

    try {
      MessagesModel message = messages.get(position);
      if (message != null) {
//                createMessage(message.getSenderName(),message.getMessage(),viewHolder.ismRequestActionMessageItemBinding.tvMessage);

        viewHolder.ismRequestActionMessageItemBinding.tvMessage.setText(message.getMessage());
        viewHolder.ismRequestActionMessageItemBinding.tvUserName.setText(message.getSenderName());
        viewHolder.ismRequestActionMessageItemBinding.tvMessageTime.setText(message.getMessageTime());
        if (PlaceholderUtils.isValidImageUrl(message.getSenderImage())) {
          try {
            Glide.with(mContext).load(message.getSenderImage()).placeholder(R.drawable.ism_default_profile_image).transform(new CircleCrop()).into(viewHolder.ismRequestActionMessageItemBinding.ivProfilePic);
          } catch (IllegalArgumentException | NullPointerException ignore) {

          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getSenderName(), viewHolder.ismRequestActionMessageItemBinding.ivProfilePic, position, 8);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void createMessage(String userName, String message, TextView textView) {
    String sourceString = "<b>" + userName + "</b> " + message;
    textView.setText(Html.fromHtml(sourceString, Html.FROM_HTML_MODE_LEGACY));
  }

  /**
   * The type Messages view holder.
   */
  static class MessagesViewHolder extends RecyclerView.ViewHolder {

    private IsmLikeMessageItemBinding ismLikeMessageItemBinding;
    private IsmGiftMessageItemBinding ismGiftMessageItemBinding;
    private IsmRemovedMessageItemBinding ismRemovedMessageItemBinding;
    private IsmPresenceMessageItemBinding ismPresenceMessageItemBinding;
    private IsmRequestMessageItemBinding ismRequestMessageItemBinding;
    private IsmRequestAcceptedMessageItemBinding ismRequestAcceptedMessageItemBinding;
    private IsmRequestActionMessageItemBinding ismRequestActionMessageItemBinding;
    private IsmTextMessageItemBinding ismTextMessageItemBinding;

    public MessagesViewHolder(final IsmLikeMessageItemBinding ismLikeMessageItemBinding) {
      super(ismLikeMessageItemBinding.getRoot());
      this.ismLikeMessageItemBinding = ismLikeMessageItemBinding;
    }

    public MessagesViewHolder(final IsmGiftMessageItemBinding ismGiftMessageItemBinding) {
      super(ismGiftMessageItemBinding.getRoot());
      this.ismGiftMessageItemBinding = ismGiftMessageItemBinding;
    }

    public MessagesViewHolder(final IsmRemovedMessageItemBinding ismRemovedMessageItemBinding) {
      super(ismRemovedMessageItemBinding.getRoot());
      this.ismRemovedMessageItemBinding = ismRemovedMessageItemBinding;
    }

    public MessagesViewHolder(final IsmPresenceMessageItemBinding ismPresenceMessageItemBinding) {
      super(ismPresenceMessageItemBinding.getRoot());
      this.ismPresenceMessageItemBinding = ismPresenceMessageItemBinding;
    }

    public MessagesViewHolder(final IsmRequestMessageItemBinding ismRequestMessageItemBinding) {
      super(ismRequestMessageItemBinding.getRoot());
      this.ismRequestMessageItemBinding = ismRequestMessageItemBinding;
    }

    public MessagesViewHolder(final IsmRequestAcceptedMessageItemBinding ismRequestAcceptedMessageItemBinding) {
      super(ismRequestAcceptedMessageItemBinding.getRoot());
      this.ismRequestAcceptedMessageItemBinding = ismRequestAcceptedMessageItemBinding;
    }

    public MessagesViewHolder(final IsmRequestActionMessageItemBinding ismRequestActionMessageItemBinding) {
      super(ismRequestActionMessageItemBinding.getRoot());
      this.ismRequestActionMessageItemBinding = ismRequestActionMessageItemBinding;
    }


    public MessagesViewHolder(final IsmTextMessageItemBinding ismTextMessageItemBinding) {
      super(ismTextMessageItemBinding.getRoot());
      this.ismTextMessageItemBinding = ismTextMessageItemBinding;
    }
  }
}