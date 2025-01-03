package io.isometrik.gs.ui.messages.replies;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.utils.DateUtil;
import io.isometrik.gs.ui.utils.MessageTypeEnum;
import io.isometrik.gs.ui.utils.UserSession;
import io.isometrik.gs.events.message.MessageReplyAddEvent;
import io.isometrik.gs.response.message.FetchMessageRepliesResult;

public class MessageRepliesModel {

  private String messageId;

  private long timestamp;

  private final String senderId;

  private final String senderIdentifier;

  private final String senderImage;

  private final String senderName;

  private String message;

  private final int messageType;

  private boolean delivered;

  private final boolean receivedMessage;

  private final String messageTime;

  private boolean canRemoveMessage;
  /**
   * Type 0- normal message, Type 3- presence message, Type 2- Removed message,  Type 1- like
   * message,
   */
  private int messageItemType;

  /**
   * Instantiates a new Messages model.
   *
   * @param message the message
   * @param isModerator the given user is moderator
   * @param userId the user id
   */
  public MessageRepliesModel(FetchMessageRepliesResult.StreamMessageReply message,
      boolean isModerator, String userId) {

    this.message = message.getBody();
    messageId = message.getMessageId();
    messageType = message.getMessageType();
    senderId = message.getSenderId();
    senderIdentifier = message.getSenderIdentifier();
    senderImage = message.getSenderProfileImageUrl();
    if (userId.equals(senderId)) {

      senderName = IsometrikStreamSdk.getInstance()
          .getContext()
          .getString(R.string.ism_you, message.getSenderName());
    } else {

      senderName = message.getSenderName();
    }
    timestamp = message.getSentAt();
    canRemoveMessage = isModerator;

    messageItemType = MessageTypeEnum.NormalMessage.getValue();

    messageTime = DateUtil.getDate(timestamp);
    if (userId.equals(senderId)) {
      delivered = true;
      receivedMessage = false;
    } else {
      receivedMessage = true;
    }
  }

  /**
   * Instantiates a new Messages model.
   *
   * @param messageReplyAddEvent the message reply add event
   * @param isModerator the given user is moderator
   */
  public MessageRepliesModel(MessageReplyAddEvent messageReplyAddEvent, boolean isModerator) {

    this.message = messageReplyAddEvent.getBody();
    messageId = messageReplyAddEvent.getMessageId();
    messageType = messageReplyAddEvent.getMessageType();

    senderId = messageReplyAddEvent.getSenderId();
    senderIdentifier = messageReplyAddEvent.getSenderIdentifier();
    senderImage = messageReplyAddEvent.getSenderProfileImageUrl();
    senderName = messageReplyAddEvent.getSenderName();
    timestamp = messageReplyAddEvent.getSentAt();
    canRemoveMessage = isModerator;
    messageItemType = MessageTypeEnum.NormalMessage.getValue();
    messageTime = DateUtil.getDate(timestamp);
    receivedMessage = true;
  }

  /**
   * Instantiates a new Messages model.
   *
   * @param message the message
   * @param messageId the message id
   * @param messageType the message type
   * @param timestamp the timestamp
   * @param isModerator the given user is moderator
   * @param userSession the user session
   */
  public MessageRepliesModel(String message, String messageId, int messageType, long timestamp,
      boolean isModerator, UserSession userSession) {

    this.message = message;
    this.messageId = messageId;
    this.messageType = messageType;
    this.timestamp = timestamp;

    senderId = userSession.getUserId();
    senderIdentifier = userSession.getUserIdentifier();
    senderImage = userSession.getUserProfilePic();
    senderName = IsometrikStreamSdk.getInstance()
        .getContext()
        .getString(R.string.ism_you, userSession.getUserName());

    canRemoveMessage = isModerator;
    messageItemType = MessageTypeEnum.NormalMessage.getValue();
    messageTime = DateUtil.getDate(timestamp);
    delivered = false;
    receivedMessage = false;
  }

  /**
   * Gets message id.
   *
   * @return the message id
   */
  public String getMessageId() {
    return messageId;
  }

  /**
   * Gets timestamp.
   *
   * @return the timestamp
   */
  long getTimestamp() {
    return timestamp;
  }

  /**
   * Gets sender id.
   *
   * @return the sender id
   */
  public String getSenderId() {
    return senderId;
  }

  /**
   * Gets sender identifier.
   *
   * @return the sender identifier
   */
  public String getSenderIdentifier() {
    return senderIdentifier;
  }

  /**
   * Gets sender image.
   *
   * @return the sender image
   */
  String getSenderImage() {
    return senderImage;
  }

  /**
   * Gets sender name.
   *
   * @return the sender name
   */
  String getSenderName() {
    return senderName;
  }

  /**
   * Gets message.
   *
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets message type.
   *
   * @return the message type
   */
  public int getMessageType() {
    return messageType;
  }

  /**
   * Is delivered boolean.
   *
   * @return the boolean
   */
  boolean isDelivered() {
    return delivered;
  }

  /**
   * Is can remove message boolean.
   *
   * @return the boolean
   */
  boolean isCanRemoveMessage() {
    return canRemoveMessage;
  }

  /**
   * Sets whether given user can remove a message or not.
   *
   * @param canRemoveMessage whether given user can remove a message or not
   */
  public void setCanRemoveMessage(boolean canRemoveMessage) {
    this.canRemoveMessage = canRemoveMessage;
  }

  /**
   * Gets message time.
   *
   * @return the message time
   */
  String getMessageTime() {
    return messageTime;
  }

  /**
   * Gets message item type.
   *
   * @return the message item type
   */
  public int getMessageItemType() {
    return messageItemType;
  }

  /**
   * Is received message boolean.
   *
   * @return the boolean
   */
  boolean isReceivedMessage() {
    return receivedMessage;
  }

  /**
   * Sets message.
   *
   * @param message the message
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Sets message item type.
   *
   * @param messageItemType the message item type
   */
  public void setMessageItemType(int messageItemType) {
    this.messageItemType = messageItemType;
  }

  /**
   * Sets message id.
   *
   * @param messageId the message id
   */
  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  /**
   * Sets timestamp.
   *
   * @param timestamp the timestamp
   */
  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * Sets delivered.
   *
   * @param delivered the delivered
   */
  public void setDelivered(boolean delivered) {
    this.delivered = delivered;
  }
}
