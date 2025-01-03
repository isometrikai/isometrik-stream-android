package io.isometrik.gs.ui.messages.replies;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmBottomsheetMessageRepliesBinding;
import io.isometrik.gs.ui.utils.AlertProgress;
import io.isometrik.gs.ui.utils.KeyboardUtil;
import io.isometrik.gs.ui.utils.MessageTypeEnum;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

public class MessageRepliesFragment extends BottomSheetDialogFragment
    implements MessageRepliesContract.View {

  public static final String TAG = "MessageRepliesBottomSheetFragment";

  private IsmBottomsheetMessageRepliesBinding ismBottomsheetMessageRepliesBinding;

  /**
   * Instantiates a new message replies fragment.
   */
  public MessageRepliesFragment() {
    // Required empty public constructor
  }

  private String streamId, parentMessageId, parentMessageText;
  private boolean allowReplyingToMessage, isModerator;

  private AlertProgress alertProgress;
  private AlertDialog alertDialog;
  private Activity activity;

  private ArrayList<MessageRepliesModel> messageReplies;
  private MessageRepliesAdapter messageRepliesAdapter;
  private LinearLayoutManager messageRepliesLayoutManager;
  private MessageReplyCallback messageReplyCallback;
  private MessageRepliesContract.Presenter messageRepliesPresenter;

  @SuppressLint("ClickableViewAccessibility")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (ismBottomsheetMessageRepliesBinding == null) {
      ismBottomsheetMessageRepliesBinding =
          IsmBottomsheetMessageRepliesBinding.inflate(inflater, container, false);
    } else {

      if (ismBottomsheetMessageRepliesBinding.getRoot().getParent() != null) {
        ((ViewGroup) ismBottomsheetMessageRepliesBinding.getRoot().getParent()).removeView(
            ismBottomsheetMessageRepliesBinding.getRoot());
      }
    }
    alertProgress = new AlertProgress();
    updateShimmerVisibility(true);
    messageRepliesPresenter.initialize(streamId, parentMessageId, isModerator);

    messageRepliesPresenter.registerStreamMessagesEventListener();

    ismBottomsheetMessageRepliesBinding.tvParentMessage.setText(parentMessageText);

    if (allowReplyingToMessage) {
      ismBottomsheetMessageRepliesBinding.etReplyToMessage.setText("");
      ismBottomsheetMessageRepliesBinding.ivReply.setVisibility(View.VISIBLE);

      ismBottomsheetMessageRepliesBinding.ivReplyToMessage.setOnClickListener(v -> {
        if (ismBottomsheetMessageRepliesBinding.etReplyToMessage.getText() != null
            && !ismBottomsheetMessageRepliesBinding.etReplyToMessage.getText()
            .toString()
            .trim()
            .isEmpty()) {
          String textMessage =
              ismBottomsheetMessageRepliesBinding.etReplyToMessage.getText().toString().trim();
          long localMessageId = System.currentTimeMillis();

          if (messageReplies.size() == 0) {
            ismBottomsheetMessageRepliesBinding.tvNoReplies.setVisibility(View.GONE);
            ismBottomsheetMessageRepliesBinding.rvMessageReplies.setVisibility(View.VISIBLE);
          }

          messageReplies.add(new MessageRepliesModel(textMessage, String.valueOf(localMessageId),
              MessageTypeEnum.NormalMessage.getValue(), localMessageId, isModerator,
              IsometrikStreamSdk.getInstance().getUserSession()));
          messageRepliesAdapter.notifyItemInserted(messageReplies.size() - 1);

          scrollToLastMessage();
          messageRepliesPresenter.sendMessageReply(textMessage, localMessageId);
          ismBottomsheetMessageRepliesBinding.etReplyToMessage.setText("");
          KeyboardUtil.hideKeyboard(activity);
        }
      });
    } else {
      ismBottomsheetMessageRepliesBinding.ivReply.setVisibility(View.GONE);
    }

    messageReplies = new ArrayList<>();
    messageRepliesLayoutManager = new LinearLayoutManager(activity);
    ismBottomsheetMessageRepliesBinding.rvMessageReplies.setLayoutManager(
        messageRepliesLayoutManager);
    messageRepliesAdapter = new MessageRepliesAdapter(activity, messageReplies, this);
    ismBottomsheetMessageRepliesBinding.rvMessageReplies.addOnScrollListener(
        recyclerViewOnScrollListener);
    ismBottomsheetMessageRepliesBinding.rvMessageReplies.setAdapter(messageRepliesAdapter);

    ismBottomsheetMessageRepliesBinding.tvNoReplies.setVisibility(View.GONE);
    fetchMessageReplies(false, null);
    ismBottomsheetMessageRepliesBinding.refresh.setOnRefreshListener(
        () -> fetchMessageReplies(false, null));
    ismBottomsheetMessageRepliesBinding.ivReply.setOnClickListener(v -> {
      if (ismBottomsheetMessageRepliesBinding.rlReplyToMessage.getVisibility() == View.VISIBLE) {
        ismBottomsheetMessageRepliesBinding.rlReplyToMessage.setVisibility(View.GONE);
      } else {
        ismBottomsheetMessageRepliesBinding.rlReplyToMessage.setVisibility(View.VISIBLE);
      }
    });

    ismBottomsheetMessageRepliesBinding.etSearch.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
          fetchMessageReplies(true, s.toString());
        } else {

          fetchMessageReplies(false, null);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });
    //To allow scroll on message replies recyclerview
    ismBottomsheetMessageRepliesBinding.rvMessageReplies.setOnTouchListener((v, event) -> {
      v.getParent().requestDisallowInterceptTouchEvent(true);
      v.onTouchEvent(event);
      return true;
    });
    //KeyboardUtil.hideKeyboard(activity,ismBottomsheetMessageRepliesBinding.etSearch);
    //KeyboardUtil.hideKeyboard(activity,ismBottomsheetMessageRepliesBinding.etReplyToMessage);
    return ismBottomsheetMessageRepliesBinding.getRoot();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
    setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);

    messageRepliesPresenter = new MessageRepliesPresenter();
    messageRepliesPresenter.attachView(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    messageRepliesPresenter.detachView();
    activity = null;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    Dialog dialog = super.onCreateDialog(savedInstanceState);
    try {
      dialog.setOnShowListener(dialog1 -> new Handler().postDelayed(() -> {
        BottomSheetDialog d = (BottomSheetDialog) dialog1;
        FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        @SuppressWarnings("rawtypes")
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
      }, 0));
    } catch (Exception ignore) {
    }

    return dialog;
  }

  @Override
  public void onDismiss(@NonNull DialogInterface dialog) {
    if (allowReplyingToMessage) {
      if (activity != null) {
        activity.getWindow()
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
      }
    }
    super.onDismiss(dialog);
  }

  @Override
  public void onCancel(@NotNull DialogInterface dialog) {
    super.onCancel(dialog);
    messageRepliesPresenter.unregisterStreamMessagesEventListener();
  }

  /**
   * {@link MessageRepliesContract.View#onError(String)}
   */
  @Override
  public void onError(String errorMessage) {
    updateShimmerVisibility(false);
    hideProgressDialog();
    if (ismBottomsheetMessageRepliesBinding.refresh.isRefreshing()) {
      ismBottomsheetMessageRepliesBinding.refresh.setRefreshing(false);
    }
    if (activity != null) {
      activity.runOnUiThread(() -> {
        if (errorMessage != null) {
          Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(activity, getString(R.string.ism_error), Toast.LENGTH_SHORT).show();
        }
      });
    }
  }

  /**
   * The Recycler view on scroll listener.
   */
  public RecyclerView.OnScrollListener recyclerViewOnScrollListener =
      new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);

          if (dy != 0 && messageRepliesLayoutManager.findFirstVisibleItemPosition() == 0) {
            messageRepliesPresenter.fetchMessageRepliesOnScroll();
          }
        }
      };

  /**
   * Remove message reply from a stream group.
   *
   * @param messageId the id of the message reply to be removed from the stream group
   * @param timestamp the timestamp at which message reply to be removed was sent
   */
  public void removeMessageReply(String messageId, long timestamp) {
    if (messageId.length() == 24) {
      showProgressDialog(getString(R.string.ism_removing_message_reply));
      messageRepliesPresenter.removeMessageReply(messageId, timestamp);
    } else {
      if (activity != null) {
        Toast.makeText(activity, getString(R.string.ism_delete_unsent_message_reply),
            Toast.LENGTH_SHORT).show();
      }
    }
  }

  /**
   * {@link MessageRepliesContract.View#onMessageRepliesDataReceived(ArrayList, boolean)}
   */
  @Override
  public void onMessageRepliesDataReceived(ArrayList<MessageRepliesModel> messageRepliesModels,
      boolean latestMessageReplies) {
    if (activity != null) {
      activity.runOnUiThread(() -> {

        if (latestMessageReplies) {
          messageReplies.clear();
        }
        messageReplies.addAll(0, messageRepliesModels);

        if (messageReplies.size() > 0) {
          ismBottomsheetMessageRepliesBinding.tvNoReplies.setVisibility(View.GONE);
          ismBottomsheetMessageRepliesBinding.rvMessageReplies.setVisibility(View.VISIBLE);
          messageRepliesAdapter.notifyDataSetChanged();
        } else {
          ismBottomsheetMessageRepliesBinding.tvNoReplies.setVisibility(View.VISIBLE);
          ismBottomsheetMessageRepliesBinding.rvMessageReplies.setVisibility(View.GONE);
        }

        if (latestMessageReplies) scrollToLastMessage();

        updateShimmerVisibility(false);
        if (ismBottomsheetMessageRepliesBinding.refresh.isRefreshing()) {
          ismBottomsheetMessageRepliesBinding.refresh.setRefreshing(false);
        }
      });
    }
  }

  /**
   * {@link MessageRepliesContract.View#onMessageDelivered(String,
   * String)}
   */
  @Override
  public void onMessageDelivered(String messageId, String temporaryMessageId) {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        int size = messageReplies.size();
        for (int i = size - 1; i >= 0; i--) {
          if (messageReplies.get(i).getMessageItemType() == MessageTypeEnum.NormalMessage.getValue()
              && messageReplies.get(i).getMessageId().equals(temporaryMessageId)) {

            MessageRepliesModel messagesModel = messageReplies.get(i);
            messagesModel.setDelivered(true);
            messagesModel.setMessageId(messageId);
            messageReplies.set(i, messagesModel);
            messageRepliesAdapter.notifyItemChanged(i);
            break;
          }
        }
        messageReplyCallback.onMessageReplyAdded(parentMessageId);
      });
    }
  }

  @Override
  public void onMessageRemovedEvent(String messageId, String message) {
    if (activity != null) {

      activity.runOnUiThread(() -> {
        int size = messageReplies.size();
        for (int i = size - 1; i >= 0; i--) {
          if (messageReplies.get(i).getMessageItemType() == MessageTypeEnum.NormalMessage.getValue()
              && messageReplies.get(i).getMessageId().equals(messageId)) {

            MessageRepliesModel messagesModel = messageReplies.get(i);
            messagesModel.setMessageItemType(MessageTypeEnum.RemovedMessage.getValue());
            messagesModel.setMessage(message);
            messageReplies.set(i, messagesModel);
            messageRepliesAdapter.notifyItemChanged(i);
            break;
          }
        }
      });
    }
    hideProgressDialog();
  }

  /**
   * {@link MessageRepliesContract.View#onTextMessageReceived(MessageRepliesModel)}
   */
  @Override
  public void onTextMessageReceived(MessageRepliesModel messagesModel) {

    if (activity != null) {
      activity.runOnUiThread(() -> {
        messageReplies.add(messagesModel);
        int size = messageReplies.size();

        if (size == 1) {
          ismBottomsheetMessageRepliesBinding.tvNoReplies.setVisibility(View.GONE);
          ismBottomsheetMessageRepliesBinding.rvMessageReplies.setVisibility(View.VISIBLE);
        }
        messageRepliesAdapter.notifyItemInserted(size - 1);
      });
    }
    scrollToLastMessage();
  }

  private void showProgressDialog(String message) {

    if (activity != null) {
      alertDialog = alertProgress.getProgressDialog(activity, message);
      if (!activity.isFinishing() && !activity.isDestroyed()) alertDialog.show();
    }
  }

  private void hideProgressDialog() {

    if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
  }

  /**
   * Scrolls message list to last message on receipt of new text or presence message to show latest
   * message received.
   */
  @SuppressWarnings("TryWithIdenticalCatches")
  private void scrollToLastMessage() {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        try {

          new Handler().postDelayed(() -> messageRepliesLayoutManager.scrollToPositionWithOffset(
              messageRepliesAdapter.getItemCount() - 1, 0), 500);
        } catch (IndexOutOfBoundsException e) {
          e.printStackTrace();
        } catch (NullPointerException e) {
          e.printStackTrace();
        }
      });
    }
  }

  private void updateShimmerVisibility(boolean visible) {
    if (visible) {
      ismBottomsheetMessageRepliesBinding.shimmerFrameLayout.startShimmer();
      ismBottomsheetMessageRepliesBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
    } else {
      if (ismBottomsheetMessageRepliesBinding.shimmerFrameLayout.getVisibility() == View.VISIBLE) {
        ismBottomsheetMessageRepliesBinding.shimmerFrameLayout.setVisibility(View.GONE);
        ismBottomsheetMessageRepliesBinding.shimmerFrameLayout.stopShimmer();
      }
    }
  }

  private void fetchMessageReplies(boolean isSearchRequest, String searchTag) {
    try {
      messageRepliesPresenter.fetchMessageReplies(0, true, isSearchRequest, searchTag);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void updateParameters(String streamId, String parentMessageId, String parentMessageText,
      long parentMessageSentAt, boolean allowReplyingToMessage, boolean isModerator,
      MessageReplyCallback messageReplyCallback) {
    this.streamId = streamId;
    this.parentMessageId = parentMessageId;
    this.parentMessageText = parentMessageText;
    this.allowReplyingToMessage = allowReplyingToMessage;
    this.isModerator = isModerator;
    this.messageReplyCallback = messageReplyCallback;
  }
}