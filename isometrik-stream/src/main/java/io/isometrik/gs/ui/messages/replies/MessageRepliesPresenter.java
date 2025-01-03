package io.isometrik.gs.ui.messages.replies;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.utils.Constants;
import io.isometrik.gs.ui.utils.DateUtil;
import io.isometrik.gs.ui.utils.MessageTypeEnum;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.message.DeleteMessageReplyQuery;
import io.isometrik.gs.builder.message.FetchMessageRepliesQuery;
import io.isometrik.gs.builder.message.SendMessageReplyQuery;
import io.isometrik.gs.callbacks.MessageEventCallback;
import io.isometrik.gs.events.message.MessageAddEvent;
import io.isometrik.gs.events.message.MessageRemoveEvent;
import io.isometrik.gs.events.message.MessageReplyAddEvent;
import io.isometrik.gs.events.message.MessageReplyRemoveEvent;
import io.isometrik.gs.response.message.FetchMessageRepliesResult;
import java.util.ArrayList;
import java.util.Collections;
import org.jetbrains.annotations.NotNull;

/**
 * The message replies presenter to fetch the replies to a message, send a new reply or remove
 * existing reply.
 * It implements MessageRepliesContract.Presenter{@link MessageRepliesContract.Presenter}
 *
 * @see MessageRepliesContract.Presenter
 */
public class MessageRepliesPresenter implements MessageRepliesContract.Presenter {

  /**
   * Instantiates a new featured products presenter.
   */
  MessageRepliesPresenter() {

  }

  private MessageRepliesContract.View messageRepliesView;
  private final Isometrik isometrik = IsometrikStreamSdk.getInstance().getIsometrik();

  private boolean isLastPage;
  private boolean isLoading;
  private String streamId, parentMessageId;

  private boolean isModerator;
  private boolean isSearchRequest;
  private String searchTag;
  private final int PAGE_SIZE = Constants.MESSAGES_PAGE_SIZE;

  private int offset;

  @Override
  public void initialize(String streamId, String parentMessageId, boolean isModerator) {
    this.streamId = streamId;
    this.parentMessageId = parentMessageId;
    this.isModerator = isModerator;
  }

  @Override
  public void attachView(MessageRepliesContract.View messageRepliesView) {
    this.messageRepliesView = messageRepliesView;
  }

  @Override
  public void detachView() {
    this.messageRepliesView = null;
  }

  @Override
  public void fetchMessageReplies(int offset, boolean refreshRequest, boolean isSearchRequest,
      String searchTag) {

    isLoading = true;

    if (refreshRequest) {
      this.offset = 0;
      isLastPage = false;
    }
    this.isSearchRequest = isSearchRequest;
    this.searchTag = isSearchRequest ? searchTag : null;

    FetchMessageRepliesQuery.Builder fetchMessageRepliesQuery =
        new FetchMessageRepliesQuery.Builder().setUserToken(
                IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
            .setStreamId(streamId)
            .setParentMessageId(parentMessageId)
            .setLimit(PAGE_SIZE)
            .setSkip(offset);
    if (isSearchRequest && searchTag != null) {
      fetchMessageRepliesQuery.setSearchTag(searchTag);
    }
    isometrik.getRemoteUseCases()
        .getMessagesUseCases()
        .fetchMessageReplies(fetchMessageRepliesQuery.build(), (var1, var2) -> {

          if (var1 != null) {

            ArrayList<MessageRepliesModel> messagesModels = new ArrayList<>();

            ArrayList<FetchMessageRepliesResult.StreamMessageReply> messages =
                var1.getStreamMessageReplies();
            int size = messages.size();
            if (size < PAGE_SIZE) {

              isLastPage = true;
            }
            for (int i = 0; i < size; i++) {
              messagesModels.add(0, new MessageRepliesModel(messages.get(i), isModerator,
                  IsometrikStreamSdk.getInstance().getUserSession().getUserId()));
            }
            if (messageRepliesView != null) {
              messageRepliesView.onMessageRepliesDataReceived(messagesModels, refreshRequest);
            }
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {

              if (refreshRequest) {
                //No message replies found
                if (messageRepliesView != null) {
                  messageRepliesView.onMessageRepliesDataReceived(new ArrayList<>(), true);
                }
              } else {
                isLastPage = true;
              }
            } else {
              if (messageRepliesView != null) {
                messageRepliesView.onError(var2.getErrorMessage());
              }
            }
          }
          isLoading = false;
        });
  }

  @Override
  public void fetchMessageRepliesOnScroll() {
    if (!isLoading && !isLastPage) {

      offset++;
      fetchMessageReplies(offset * PAGE_SIZE, false, isSearchRequest, searchTag);
    }
  }

  @Override
  public void removeMessageReply(String messageId, long messageSentAt) {
    isometrik.getRemoteUseCases()
        .getMessagesUseCases()
        .deleteMessageReply(new DeleteMessageReplyQuery.Builder().setStreamId(streamId)
            .setParentMessageId(parentMessageId)
            .setMessageId(messageId)
            .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
            .build(), (var1, var2) -> {
          if (var1 != null) {
            if (messageRepliesView != null) {
              messageRepliesView.onMessageRemovedEvent(messageId, IsometrikStreamSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_message_removed,
                      IsometrikStreamSdk.getInstance().getUserSession().getUserName(),
                      DateUtil.getDate(var1.getDeletedAt())));
            }
          } else {
            if (messageRepliesView != null) {
              messageRepliesView.onError(var2.getErrorMessage());
            }
          }
        });
  }

  @Override
  public void sendMessageReply(String messageReply, long localMessageId) {

    SendMessageReplyQuery.Builder sendMessageReplyQueryBuilder =
        new SendMessageReplyQuery.Builder().setBody(messageReply)
            .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
            .setStreamId(streamId)
            .setSearchableTags(new ArrayList<>(Collections.singletonList(messageReply)))
            .setParentMessageId(parentMessageId)
            .setDeviceId(IsometrikStreamSdk.getInstance().getUserSession().getDeviceId())
            .setMessageType(MessageTypeEnum.NormalMessage.getValue());

    isometrik.getRemoteUseCases()
        .getMessagesUseCases()
        .sendMessageReply(sendMessageReplyQueryBuilder.build(), (var1, var2) -> {
          if (var1 != null) {

            if (messageRepliesView != null) {
              messageRepliesView.onMessageDelivered(var1.getMessageId(),
                  String.valueOf(localMessageId));
            }
          } else {
            if (messageRepliesView != null) {
              messageRepliesView.onError(var2.getErrorMessage());
            }
          }
        });
  }

  /**
   * @see MessageEventCallback
   */
  private final MessageEventCallback messageEventCallback = new MessageEventCallback() {
    @Override
    public void messageAdded(@NotNull Isometrik isometrik,
        @NotNull MessageAddEvent messageAddEvent) {
      //TODO Nothing

    }

    @Override
    public void messageRemoved(@NotNull Isometrik isometrik,
        @NotNull MessageRemoveEvent messageRemoveEvent) {
      //TODO Nothing

    }

    @Override
    public void messageReplyAdded(@NotNull Isometrik isometrik,
        @NotNull MessageReplyAddEvent messageReplyAddEvent) {
      if (messageReplyAddEvent.getStreamId().equals(streamId)
          && messageReplyAddEvent.getParentMessageId().equals(parentMessageId)) {
        if (!messageReplyAddEvent.getDeviceId()
            .equals(IsometrikStreamSdk.getInstance().getUserSession().getDeviceId())) {

          if (messageReplyAddEvent.getMessageType() == MessageTypeEnum.NormalMessage.getValue()) {

            if (messageRepliesView != null) {
              messageRepliesView.onTextMessageReceived(
                  new MessageRepliesModel(messageReplyAddEvent, isModerator));
            }
          }
        }
      }
    }

    @Override
    public void messageReplyRemoved(@NotNull Isometrik isometrik,
        @NotNull MessageReplyRemoveEvent messageReplyRemoveEvent) {
      if (messageReplyRemoveEvent.getStreamId().equals(streamId)
          && messageReplyRemoveEvent.getParentMessageId().equals(parentMessageId)) {

        if (messageRepliesView != null) {
          messageRepliesView.onMessageRemovedEvent(messageReplyRemoveEvent.getMessageId(),
              IsometrikStreamSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_message_removed,
                      messageReplyRemoveEvent.getInitiatorName(),
                      DateUtil.getDate(messageReplyRemoveEvent.getTimestamp())));
        }
      }
    }
  };

  /**
   * {@link MessageRepliesContract.Presenter#registerStreamMessagesEventListener()}
   */
  @Override
  public void registerStreamMessagesEventListener() {
    isometrik.getRealtimeEventsListenerManager().addMessageEventListener(messageEventCallback);
  }

  /**
   * {@link MessageRepliesContract.Presenter#unregisterStreamMessagesEventListener()}
   */
  @Override
  public void unregisterStreamMessagesEventListener() {
    isometrik.getRealtimeEventsListenerManager().removeMessageEventListener(messageEventCallback);
  }
}
