package io.isometrik.gs.ui.pk.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>BlockUserResponse</h1>
 *
 * @author 3embed
 * @since 4/9/2018
 * @version 1.0
 */

public class PkInviteAcceptEvent implements Serializable {
    @SerializedName("accseptedUserId")
    @Expose
    private String accseptedUserId;
    @SerializedName("accseptedStreamUserId")
    @Expose
    private String accseptedStreamUserId;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("profilepic")
    @Expose
    private String profilepic;
    @SerializedName("isStar")
    @Expose
    private boolean isStar;
    @SerializedName("senderStreamId")
    @Expose
    private String senderStreamId;
    @SerializedName("reciverStreamId")
    @Expose
    private String reciverStreamId;
    @SerializedName("inviteId")
    @Expose
    private String inviteId;

    public String getAccseptedUserId() {
        return accseptedUserId;
    }

    public void setAccseptedUserId(String accseptedUserId) {
        this.accseptedUserId = accseptedUserId;
    }

    public String getAccseptedStreamUserId() {
        return accseptedStreamUserId;
    }

    public void setAccseptedStreamUserId(String accseptedStreamUserId) {
        this.accseptedStreamUserId = accseptedStreamUserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public boolean isStar() {
        return isStar;
    }

    public void setStar(boolean star) {
        isStar = star;
    }

    public String getSenderStreamId() {
        return senderStreamId;
    }

    public void setSenderStreamId(String senderStreamId) {
        this.senderStreamId = senderStreamId;
    }

    public String getReciverStreamId() {
        return reciverStreamId;
    }

    public void setReciverStreamId(String reciverStreamId) {
        this.reciverStreamId = reciverStreamId;
    }

    public String getInviteId() {
        return inviteId;
    }

    public void setInviteId(String inviteId) {
        this.inviteId = inviteId;
    }
}
