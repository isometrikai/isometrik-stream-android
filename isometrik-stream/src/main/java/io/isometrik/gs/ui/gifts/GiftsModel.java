package io.isometrik.gs.ui.gifts;

/**
 * Model class containing details of the gift been sent and of the user who sent the gift in a
 * broadcast.
 */
public class GiftsModel {

  private String senderIdentifier;

  private String senderImage;

  private String senderName;

  private String senderId;

  private final String message;

  private  String giftName, giftThumbnailUrl;

  private  String reciverId;

  private  String reciverStreamUserId;

  private  String receiverName;

  private final int coinValue;
  /**
   * Instantiates a new Gifts model.
   *
   * @param senderId the sender id
   * @param senderName the sender name
   * @param senderImage the sender image
   * @param senderIdentifier the sender identifier
   * @param message the message
   * @param coinValue the coin value
   * @param giftName the gift name
   * @param giftThumbnailUrl the gift thumbnail url
   * @param reciverId reciverId of gift
   */
  public GiftsModel(String senderId, String senderName, String senderImage, String senderIdentifier,
                    String message, int coinValue, String giftName, String giftThumbnailUrl, String reciverId, String reciverStreamUserId, String receiverName) {
    this.senderId = senderId;
    this.senderName = senderName;
    this.senderImage = senderImage;
    this.senderIdentifier = senderIdentifier;
    this.message = message;
    this.coinValue = coinValue;
    this.giftName = giftName;
    this.giftThumbnailUrl = giftThumbnailUrl;
    this.reciverId = reciverId;
    this.reciverStreamUserId = reciverStreamUserId;
    this.receiverName = receiverName;
  }

  /**
   * Instantiates a new Gifts model.
   *
   * @param message the message
   * @param coinValue the coin value
   * @param giftName the gift name
   */
  public GiftsModel(String message, int coinValue, String giftName) {

    this.message = message;
    this.coinValue = coinValue;
    this.giftName = giftName;
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
  public String getSenderImage() {
    return senderImage;
  }

  /**
   * Gets sender name.
   *
   * @return the sender name
   */
  public String getSenderName() {
    return senderName;
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
   * Gets message.
   *
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets coin value.
   *
   * @return the coin value
   */
  public int getCoinValue() {
    return coinValue;
  }

  /**
   * Gets gift name.
   *
   * @return the gift name
   */
  public String getGiftName() {
    return giftName;
  }

  public String getGiftThumbnailUrl() {
    return giftThumbnailUrl;
  }

  public String getReciverId() {
    return reciverId;
  }

  public String getReciverStreamUserId() {
    return reciverStreamUserId;
  }

  public String getReceiverName() {
    return receiverName;
  }

  public void setReceiverName(String receiverName) {
    this.receiverName = receiverName;
  }
}
