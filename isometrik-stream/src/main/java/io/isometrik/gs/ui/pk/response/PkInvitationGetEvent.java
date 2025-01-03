package io.isometrik.gs.ui.pk.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PkInvitationGetEvent implements Serializable {

  @SerializedName("streamId")
  @Expose
  private String streamId;
  @SerializedName("userId")
  @Expose
  private String userId;
  @SerializedName("firstName")
  @Expose
  private String firstName;
  @SerializedName("lastName")
  @Expose
  private String lastName;
  @SerializedName("userName")
  @Expose
  private String userName;
  @SerializedName("isStar")
  @Expose
  private boolean isStar;
  @SerializedName("profilepic")
  @Expose
  private String profilepic;
  @SerializedName("inviteId")
  @Expose
  private String inviteId;



  public String getStreamId() {
    return streamId;
  }

  public void setStreamId(String streamId) {
    this.streamId = streamId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
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

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public boolean isStar() {
    return isStar;
  }

  public void setStar(boolean star) {
    isStar = star;
  }

  public String getProfilepic() {
    return profilepic;
  }

  public void setProfilepic(String profilepic) {
    this.profilepic = profilepic;
  }

  public String getInviteId() {
    return inviteId;
  }

  public void setInviteId(String inviteId) {
    this.inviteId = inviteId;
  }
}
