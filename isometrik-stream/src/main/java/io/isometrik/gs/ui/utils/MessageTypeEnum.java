package io.isometrik.gs.ui.utils;

/**
 * The enum Message type enum.
 */
public enum MessageTypeEnum {

  /**
   * Normal message message type enum.
   */
  NormalMessage(0),

  /**
   * Pinned product message type enum.
   */
  PinnedProduct(1),
  /**
   * Gift message message type enum.
   */
  GiftMessage(2),

  /**
   * Heart message message type enum.
   */
  HeartMessage(3),
  /**
   * Removed message message type enum.
   */
  ViewerBoughtProductMessage(4),
  /**
   * Removed message message type enum.
   */
  RemovedMessage(4),
  /**
   * Presence message message type enum.
   */
  PresenceMessage(5),

  /**
   * Copublish request message type enum.
   */
  CopublishRequestMessage(6),

  /**
   * Copublish request accepted message type enum.
   */
  CopublishRequestAcceptedMessage(7),

  /**
   * Copublish request action(deleted,switch profile etc) message type enum.
   */
  CopublishRequestActionMessage(8),
  /**
   * for GIF which should show in full screen
   * */
  ThreeDMessage(10),


  OfferUpdated(13),

  /**
   * stream is mearged for Pk challenge
   * */
  ChangeToPk(20),

  /**
   * change stream
   * */
  ChangeStream(21),
  /**
   * Pk start event
   * */
  StartPk(23);


  private final int value;

  MessageTypeEnum(int value) {
    this.value = value;
  }

  /**
   * Gets value.
   *
   * @return the value
   */
  public final int getValue() {
    return this.value;
  }
}