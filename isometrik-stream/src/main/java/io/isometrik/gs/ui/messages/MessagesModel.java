package io.isometrik.gs.ui.messages;

import android.util.Log;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.gifts.GiftsModel;
import io.isometrik.gs.ui.utils.DateUtil;
import io.isometrik.gs.ui.utils.MessageTypeEnum;
import io.isometrik.gs.ui.utils.UserSession;
import io.isometrik.gs.events.message.MessageAddEvent;
import io.isometrik.gs.response.message.utils.StreamMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The type Messages model.
 */
public class MessagesModel {

    private String messageId;

    private long timestamp;

    private String senderId;

    private String senderIdentifier;

    private String senderImage;

    private String senderName;

    private String receiverName;

    private String message;
    private JSONObject metaData;

    private int messageType;

    private boolean delivered;

    private boolean canRemoveMessage;

    private boolean receivedMessage;

    private boolean canJoin;
    private String messageTime;

    private int coinValue;
    private String giftName;
    private boolean isInitiator;
    /**
     * Type 0- normal message, Type 3- presence message, Type 2- Removed message,  Type 1- like
     * message,
     */
    private int messageItemType;

    private boolean canReply, hasReplies;

    /**
     * Instantiates a new Messages model.
     *
     * @param message     the message
     * @param isModerator the given user is moderator
     * @param userId      the user id
     */
    public MessagesModel(StreamMessage message, boolean isModerator, String userId) {

        this.metaData = message.getMetaData();
        this.message = message.getBody();
        messageId = message.getMessageId();
        messageType = message.getMessageType();
        senderId = message.getSenderId();
        senderIdentifier = message.getSenderIdentifier();
        senderImage = message.getSenderProfileImageUrl();
        if (userId.equals(senderId)) {
            canReply = true;
            senderName = IsometrikStreamSdk.getInstance()
                    .getContext()
                    .getString(R.string.ism_you, message.getSenderName());
        } else {
            canReply = isModerator;
            senderName = message.getSenderName();
        }
        timestamp = message.getSentAt();
        canRemoveMessage = isModerator;

        if (messageType == MessageTypeEnum.HeartMessage.getValue()) {
            messageItemType = MessageTypeEnum.HeartMessage.getValue();
        } else if (messageType == MessageTypeEnum.GiftMessage.getValue()) {
            messageItemType = MessageTypeEnum.GiftMessage.getValue();


            try {
                if (metaData.has("coinsValue")) {
                    coinValue = metaData.getInt("coinsValue");
                } else {
                    Log.e("Error: ", "MessagesModel==> required coinsValue not found in metaData");
                }
                if (metaData.has("giftName")) {
                    giftName = metaData.getString("giftName");
                } else {
                    Log.e("Error: ", "MessagesModel==> required giftName not found in metaData");
                }

//        JSONObject jsonObject = new JSONObject(this.message);
//        this.message = jsonObject.getString("message");
//        coinValue = jsonObject.getInt("coinsValue");
//        giftName = jsonObject.getString("giftName");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            messageItemType = MessageTypeEnum.NormalMessage.getValue();
        }
        messageTime = DateUtil.getDate(timestamp);
        if (userId.equals(senderId)) {
            delivered = true;
            receivedMessage = false;
        } else {
            receivedMessage = true;
        }
        hasReplies = message.getRepliesCount() > 0;
    }

    /**
     * Instantiates a new Messages model.
     *
     * @param message         the message
     * @param messageItemType the message item type
     * @param timestamp       the timestamp
     */
    public MessagesModel(String message, int messageItemType, long timestamp) {

        this.message = message;
        this.messageItemType = messageItemType;
        messageTime = DateUtil.getDate(timestamp);
    }

    /**
     * Instantiates a new Messages model.
     *
     * @param messageAddEvent the message add event
     * @param isModerator     the given user is moderator
     */
    public MessagesModel(MessageAddEvent messageAddEvent, boolean isModerator) {

        this.metaData = messageAddEvent.getMetaData();
        this.message = messageAddEvent.getMessage();
        messageId = messageAddEvent.getMessageId();
        messageType = messageAddEvent.getMessageType();

        senderId = messageAddEvent.getSenderId();
        senderIdentifier = messageAddEvent.getSenderIdentifier();
        senderImage = messageAddEvent.getSenderProfileImageUrl();
        senderName = messageAddEvent.getSenderName();
        timestamp = messageAddEvent.getSentAt();
        canRemoveMessage = isModerator;
        messageItemType = MessageTypeEnum.NormalMessage.getValue();
        messageTime = DateUtil.getDate(timestamp);
        receivedMessage = true;

        canReply =
                senderId.equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId()) || isModerator;
        hasReplies = false;
    }

    /**
     * Instantiates a new Messages model.
     *
     * @param message     the message
     * @param messageId   the message id
     * @param messageType the message type
     * @param timestamp   the timestamp
     * @param isModerator the given user is moderator
     * @param userSession the user session
     */
    public MessagesModel(String message, String messageId, int messageType, long timestamp,
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
        canReply = true;
        hasReplies = false;
    }

    public MessagesModel(String message, int messageItemType, String senderId,
                         String senderIdentifier, String senderImage, String senderName, long timestamp,
                         boolean isInitiator, boolean canJoin) {

        this.message = message;
        this.messageItemType = messageItemType;
        this.messageTime = DateUtil.getDate(timestamp);

        this.senderId = senderId;
        this.senderIdentifier = senderIdentifier;
        this.senderImage = senderImage;
        this.senderName = senderName;
        this.timestamp = timestamp;
        this.messageTime = DateUtil.getDate(timestamp);
        this.isInitiator = isInitiator;
        this.canJoin = canJoin;
    }

    public MessagesModel(MessageAddEvent messageAddEvent) {

        this.metaData = messageAddEvent.getMetaData();
        this.message = messageAddEvent.getMessage();
        messageId = messageAddEvent.getMessageId();
        messageType = messageAddEvent.getMessageType();

        senderId = messageAddEvent.getSenderId();
        senderIdentifier = messageAddEvent.getSenderIdentifier();
        senderImage = messageAddEvent.getSenderProfileImageUrl();
        senderName = messageAddEvent.getSenderName();
        receiverName = messageAddEvent.getReceiverName();
        timestamp = messageAddEvent.getSentAt();
        canRemoveMessage = false;
        if (messageType == MessageTypeEnum.HeartMessage.getValue()) {
            messageItemType = MessageTypeEnum.HeartMessage.getValue();
        } else if (messageType == MessageTypeEnum.GiftMessage.getValue()) {
            messageItemType = MessageTypeEnum.GiftMessage.getValue();
            try {
                if (metaData.has("message")) {
                    this.message = metaData.getString("message");
                } else {
                    Log.e("Error: ", "MessagesModel==> required message not found in metaData");
                }
                if (metaData.has("coinsValue")) {
                    coinValue = metaData.getInt("coinsValue");
                } else {
                    Log.e("Error: ", "MessagesModel==> required coinsValue not found in metaData");
                }
                if (metaData.has("giftName")) {
                    giftName = metaData.getString("giftName");
                } else {
                    Log.e("Error: ", "MessagesModel==> required giftName not found in metaData");
                }
                if (metaData.has("receiverName")) {
                    receiverName = metaData.getString("receiverName");
                } else {
                    Log.e("Error: ", "MessagesModel==> required receiverName not found in metaData");
                }

//                JSONObject jsonObject = new JSONObject(this.message);
//                this.message = jsonObject.getString("message");
//                coinValue = jsonObject.getInt("coinsValue");
//                giftName = jsonObject.getString("giftName");
//                receiverName = jsonObject.getString("receiverName");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            messageItemType = MessageTypeEnum.NormalMessage.getValue();
        }
        messageTime = DateUtil.getDate(timestamp);
        receivedMessage = true;

        canReply = false;
        hasReplies = false;
    }


    public MessagesModel(GiftsModel giftsModel) {

        message = giftsModel.getGiftThumbnailUrl();
        messageId = String.valueOf(System.currentTimeMillis());
        messageType = MessageTypeEnum.GiftMessage.getValue();

        senderId = giftsModel.getSenderId();
        senderIdentifier = giftsModel.getSenderIdentifier();
        senderImage = giftsModel.getSenderImage();
        senderName = giftsModel.getSenderName();
        receiverName = giftsModel.getReceiverName();
        timestamp = System.currentTimeMillis();
        coinValue = giftsModel.getCoinValue();
        giftName = giftsModel.getGiftName();
        canRemoveMessage = false;
        if (messageType == MessageTypeEnum.HeartMessage.getValue()) {
            messageItemType = MessageTypeEnum.HeartMessage.getValue();
        } else if (messageType == MessageTypeEnum.GiftMessage.getValue()) {
            messageItemType = MessageTypeEnum.GiftMessage.getValue();
        } else {
            messageItemType = MessageTypeEnum.NormalMessage.getValue();
        }
        messageTime = DateUtil.getDate(timestamp);
        receivedMessage = true;

        canReply = false;
        hasReplies = false;



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

    /**
     * Gets coin value.
     *
     * @return the coin value
     */
    int getCoinValue() {
        return coinValue;
    }

    /**
     * Gets gift name.
     *
     * @return the gift name
     */
    String getGiftName() {
        return giftName;
    }

    /**
     * Gets whether given user is the initiator of the stream.
     *
     * @return the boolean isInitiator
     */
    public boolean isInitiator() {
        return isInitiator;
    }

    /**
     * Gets whether given user can join the stream as co-publisher.
     *
     * @return the boolean canJoin
     */
    public boolean isCanJoin() {
        return canJoin;
    }

    /**
     * Updates status of a user as initiator
     *
     * @param initiator whether given user is initiator or not
     */
    public void setInitiator(boolean initiator) {
        isInitiator = initiator;
    }

    /**
     * Updates status of a user as can join as publisher or not
     *
     * @param canJoin whether given user can join a broadcast a publisher
     */
    public void setCanJoin(boolean canJoin) {
        this.canJoin = canJoin;
    }

    /**
     * Gets whether given user can reply to the message or not
     *
     * @return Whether given user can reply to the message or not
     */
    public boolean canReply() {
        return canReply;
    }

    /**
     * Updates whether given user can reply to the message or not
     *
     * @param canReply whether given user can reply to the message or not
     */
    public void setCanReplyMessage(boolean canReply) {
        this.canReply = canReply;
    }

    /**
     * Gets whether given message has replies to it or not
     *
     * @return Whether given message has replies to it or not
     */
    public boolean hasReplies() {
        return hasReplies;
    }

    /**
     * Updates whether given message has replies to it or not
     *
     * @param hasReplies whether given message has replies to it or not
     */
    public void setHasReplies(boolean hasReplies) {
        this.hasReplies = hasReplies;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
}
