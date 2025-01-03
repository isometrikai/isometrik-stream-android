package io.isometrik.gs.ui.messages.replies;

/**
 * The interface message reply callbacks for communication back to scrollable streams activity, on
 * click of reply for a message being added to show VIEW REPLIES button if not showing already.
 */
public interface MessageReplyCallback {
  /**
   * Callback when reply to a message has been added
   *
   * @param messageId the id of the message for which the reply was added
   */
  void onMessageReplyAdded(String messageId);
}
