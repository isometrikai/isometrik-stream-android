package io.isometrik.gs.ui.utils;

public class StreamMemberInfo {

  private final String userId, userIdentifier, userName, userProfilePic;
  private final boolean publishing;

  public StreamMemberInfo(String userId, String userIdentifier, String userName,
      String userProfilePic, boolean publishing) {
    this.userId = userId;
    this.userIdentifier = userIdentifier;
    this.userName = userName;
    this.userProfilePic = userProfilePic;
    this.publishing = publishing;
  }

  public String getUserId() {
    return userId;
  }

  public String getUserIdentifier() {
    return userIdentifier;
  }

  public String getUserName() {
    return userName;
  }

  public String getUserProfilePic() {
    return userProfilePic;
  }

  public boolean isPublishing() {
    return publishing;
  }
}
