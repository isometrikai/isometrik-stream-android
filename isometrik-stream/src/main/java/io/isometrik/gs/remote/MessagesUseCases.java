package io.isometrik.gs.remote;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.message.AddMetadataQuery;
import io.isometrik.gs.builder.message.DeleteMessageQuery;
import io.isometrik.gs.builder.message.DeleteMessageReplyQuery;
import io.isometrik.gs.builder.message.FetchMessageRepliesQuery;
import io.isometrik.gs.builder.message.FetchMessagesCountQuery;
import io.isometrik.gs.builder.message.FetchMessagesQuery;
import io.isometrik.gs.builder.message.SendMessageQuery;
import io.isometrik.gs.builder.message.SendMessageReplyQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.models.message.AddMessage;
import io.isometrik.gs.models.message.AddMessageReply;
import io.isometrik.gs.models.message.AddMetadata;
import io.isometrik.gs.models.message.DeleteMessage;
import io.isometrik.gs.models.message.DeleteMessageReply;
import io.isometrik.gs.models.message.FetchMessageReplies;
import io.isometrik.gs.models.message.FetchMessages;
import io.isometrik.gs.models.message.FetchMessagesCount;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.message.AddMetadataResult;
import io.isometrik.gs.response.message.DeleteMessageReplyResult;
import io.isometrik.gs.response.message.DeleteMessageResult;
import io.isometrik.gs.response.message.FetchMessageRepliesResult;
import io.isometrik.gs.response.message.FetchMessagesCountResult;
import io.isometrik.gs.response.message.FetchMessagesResult;
import io.isometrik.gs.response.message.SendMessageReplyResult;
import io.isometrik.gs.response.message.SendMessageResult;

/**
 * Classes containing use cases for various messages operations, allowing ui sdk to communicate with
 * the remote backend using respective model classes.
 */
public class MessagesUseCases {
  /**
   * Model classes for messages
   */
  private final AddMessage addMessage;
  private final DeleteMessage deleteMessage;
  private final FetchMessages fetchMessages;

  private final FetchMessagesCount fetchMessagesCount;
  private final AddMetadata addMetadata;
  private final AddMessageReply addMessageReply;
  private final DeleteMessageReply deleteMessageReply;
  private final FetchMessageReplies fetchMessageReplies;

  private final IMConfiguration configuration;
  private final RetrofitManager retrofitManager;
  private final BaseResponse baseResponse;
  private final Gson gson;

  public MessagesUseCases(IMConfiguration imConfiguration, RetrofitManager retrofitManager,
      BaseResponse baseResponse, Gson gson) {
    configuration = imConfiguration;
    this.retrofitManager = retrofitManager;
    this.gson = gson;
    this.baseResponse = baseResponse;

    addMessage = new AddMessage();
    addMetadata = new AddMetadata();
    deleteMessage = new DeleteMessage();
    fetchMessages = new FetchMessages();
    addMessageReply = new AddMessageReply();
    deleteMessageReply = new DeleteMessageReply();
    fetchMessageReplies = new FetchMessageReplies();
    fetchMessagesCount = new FetchMessagesCount();
  }

  /**
   * Send message.
   *
   * @param sendMessageQuery the send message query
   * @param completionHandler the completion handler
   */
  public void sendMessage(@NotNull SendMessageQuery sendMessageQuery,
      @NotNull CompletionHandler<SendMessageResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      addMessage.validateParams(sendMessageQuery, completionHandler, retrofitManager, configuration,
          baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Delete message.
   *
   * @param deleteMessageQuery the delete message query
   * @param completionHandler the completion handler
   */
  public void deleteMessage(@NotNull DeleteMessageQuery deleteMessageQuery,
      @NotNull CompletionHandler<DeleteMessageResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      deleteMessage.validateParams(deleteMessageQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch messages.
   *
   * @param fetchMessagesQuery the fetch messages query
   * @param completionHandler the completion handler
   */
  public void fetchMessages(@NotNull FetchMessagesQuery fetchMessagesQuery,
      @NotNull CompletionHandler<FetchMessagesResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchMessages.validateParams(fetchMessagesQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch messages count.
   *
   * @param fetchMessagesCountQuery the fetch messages count query
   * @param completionHandler the completion handler
   */
  public void fetchMessagesCount(@NotNull FetchMessagesCountQuery fetchMessagesCountQuery,
      @NotNull CompletionHandler<FetchMessagesCountResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchMessagesCount.validateParams(fetchMessagesCountQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Add metadata.
   *
   * @param addMetadataQuery the add metadata query
   * @param completionHandler the completion handler
   */
  public void addMetadata(@NotNull AddMetadataQuery addMetadataQuery,
      @NotNull CompletionHandler<AddMetadataResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      addMetadata.validateParams(addMetadataQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Send message reply.
   *
   * @param sendMessageReplyQuery the send message reply query
   * @param completionHandler the completion handler
   */
  public void sendMessageReply(@NotNull SendMessageReplyQuery sendMessageReplyQuery,
      @NotNull CompletionHandler<SendMessageReplyResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      addMessageReply.validateParams(sendMessageReplyQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Delete message reply.
   *
   * @param deleteMessageReplyQuery the delete message reply query
   * @param completionHandler the completion handler
   */
  public void deleteMessageReply(@NotNull DeleteMessageReplyQuery deleteMessageReplyQuery,
      @NotNull CompletionHandler<DeleteMessageReplyResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      deleteMessageReply.validateParams(deleteMessageReplyQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch message replies.
   *
   * @param fetchMessageRepliesQuery the fetch message replies query
   * @param completionHandler the completion handler
   */
  public void fetchMessageReplies(@NotNull FetchMessageRepliesQuery fetchMessageRepliesQuery,
      @NotNull CompletionHandler<FetchMessageRepliesResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchMessageReplies.validateParams(fetchMessageRepliesQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }
}
