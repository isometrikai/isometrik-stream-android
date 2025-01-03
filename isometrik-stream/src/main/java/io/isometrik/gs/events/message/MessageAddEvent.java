package io.isometrik.gs.events.message;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The class containing message add event details.
 */
public class MessageAddEvent implements Serializable {

    @SerializedName("action")
    @Expose
    private String action;

    @SerializedName("streamId")
    @Expose
    private String streamId;

    @SerializedName("messageId")
    @Expose
    private String messageId;

    @SerializedName("sentAt")
    @Expose
    private long sentAt;

    @SerializedName("senderId")
    @Expose
    private String senderId;

    @SerializedName("senderIdentifier")
    @Expose
    private String senderIdentifier;

    @SerializedName("senderProfileImageUrl")
    @Expose
    private String senderProfileImageUrl;

    @SerializedName("senderName")
    @Expose
    private String senderName;

    @SerializedName("receiverName")
    @Expose
    private String receiverName;


    @SerializedName("body")
    @Expose
    private String message;

    @SerializedName("messageType")
    @Expose
    private int messageType;

    @SerializedName("deviceId")
    @Expose
    private String deviceId;

    @SerializedName("customType")
    @Expose
    private String customType;

    @SerializedName("repliesCount")
    @Expose
    private int repliesCount;

    @SerializedName("replyMessage")
    @Expose
    private boolean replyMessage;
    @SerializedName("searchableTags")
    @Expose
    private ArrayList<String> searchableTags;
    @SerializedName("metaData")
    @Expose
    private Object metaData;

    /**
     * Gets action.
     *
     * @return the action specifying the message received event  in the stream group
     */
    public String getAction() {
        return action;
    }

    /**
     * Gets stream id.
     *
     * @return the id of the stream group in which message has been received
     */
    public String getStreamId() {
        return streamId;
    }

    /**
     * Gets message id.
     *
     * @return the id of the message received in the stream group
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Gets timestamp.
     *
     * @return the timestamp at which message was sent in the stream group
     */
    public long getSentAt() {
        return sentAt;
    }

    /**
     * Gets sender id.
     *
     * @return the id of the user who sent the message in the stream group
     */
    public String getSenderId() {
        return senderId;
    }

    /**
     * Gets sender identifier.
     *
     * @return the identifier of the user who sent the message in the stream group
     */
    public String getSenderIdentifier() {
        return senderIdentifier;
    }

    /**
     * Gets sender image.
     *
     * @return the image of the user who sent the message in the stream group
     */
    public String getSenderProfileImageUrl() {
        return senderProfileImageUrl;
    }

    /**
     * Gets sender name.
     *
     * @return the name of the user who sent the message in the stream group
     */
    public String getSenderName() {
        return senderName;
    }

    /**
     * Gets message.
     *
     * @return the message that was sent in the stream group
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets message type.
     *
     * @return the type of message sent in the stream group
     */
    public int getMessageType() {
        return messageType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getCustomType() {
        return customType;
    }

    public int getRepliesCount() {
        return repliesCount;
    }

    public boolean isReplyMessage() {
        return replyMessage;
    }

    public ArrayList<String> getSearchableTags() {
        return searchableTags;
    }

    public JSONObject getMetaData() {

        try {
            return new JSONObject(new Gson().toJson(metaData));
        } catch (Exception ignore) {
            return new JSONObject();
        }
    }

    public String getReceiverName() {
        return receiverName;
    }
}
