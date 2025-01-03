package io.isometrik.gs.ui.messages.replies;

import io.isometrik.gs.ui.utils.BasePresenter;
import java.util.ArrayList;

/**
 * The interface cart items contract containing interfaces Presenter and View to be
 * implemented
 * by the
 * MessageRepliesPresenter{@link MessageRepliesPresenter} and
 * MessageRepliesFragment{@link MessageRepliesFragment} respectively.
 *
 * @see MessageRepliesPresenter
 * @see MessageRepliesFragment
 */
public interface MessageRepliesContract {
  /**
   * The interface MessageRepliesContract.Presenter to be implemented by
   * MessageRepliesPresenter{@link
   * MessageRepliesPresenter}
   *
   * @see MessageRepliesFragment
   */
  interface Presenter extends BasePresenter<View> {

    void initialize(String streamId, String parentMessageId, boolean isModerator);

    /**
     * Request message replies.
     *
     * @param offset the offset
     * @param refreshRequest the refresh request
     * @param isSearchRequest whether fetch message replies request is from the search or not
     * @param searchTag the message reply to be searched
     */
    void fetchMessageReplies(int offset, boolean refreshRequest, boolean isSearchRequest,
        String searchTag);

    /**
     * Fetch message replies on scroll.
     */
    void fetchMessageRepliesOnScroll();

    void removeMessageReply(String messageId, long messageSentAt);

    void sendMessageReply(String messageReply, long localMessageId);

    /**
     * Register stream messages event listener.
     */
    void registerStreamMessagesEventListener();

    /**
     * Unregister stream messages event listener.
     */
    void unregisterStreamMessagesEventListener();
  }

  /**
   * The interface MessageRepliesContract.View to be implemented by MessageRepliesFragment{@link
   * MessageRepliesFragment}
   *
   * @see MessageRepliesFragment
   */
  interface View {
    /**
     * On message replies data received.
     *
     * @param messageReplies the list of message replies MessageRepliesModel{@link
     * io.isometrik.gs.ui.messages.replies.MessageRepliesModel}
     * fetched @param latestMessageReplies whether the list of message replies fetched is for the
     * first page
     * or with paging
     * @see io.isometrik.gs.ui.messages.replies.MessageRepliesModel
     */
    void onMessageRepliesDataReceived(ArrayList<MessageRepliesModel> messageReplies,
        boolean latestMessageReplies);

    /**
     * On message reply removed event.
     *
     * @param messageId the id of the message which was deleted
     * @param message the placeholder message to be updated in the UI,in place of the deleted
     * message
     */
    void onMessageRemovedEvent(String messageId, String message);

    /**
     * @param messageId the id of the message reply which was successfully sent
     * @param temporaryMessageId the temporary message reply id(local unique identifier for the
     * message)
     */
    void onMessageDelivered(String messageId, String temporaryMessageId);

    /**
     * On text message received.
     *
     * @param messageRepliesModel the message replies model containing details of the text message
     * received
     * @see MessageRepliesModel
     */
    void onTextMessageReceived(MessageRepliesModel messageRepliesModel);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);
  }
}
