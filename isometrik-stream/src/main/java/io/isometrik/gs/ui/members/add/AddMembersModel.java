package io.isometrik.gs.ui.members.add;

import io.isometrik.gs.ui.utils.DateUtil;
import io.isometrik.gs.events.viewer.ViewerJoinEvent;
import io.isometrik.gs.response.member.FetchEligibleMembersResult;
import io.isometrik.gs.response.viewer.FetchViewersResult;

/**
 * The type Members model.
 */
class AddMembersModel {

  private final String userId;
  private final String userName;
  private final String userIdentifier;
  private final String userProfilePic;
  private final String joinTime;
  private boolean isMember;
  private final boolean normaUser;

  /**
   * Instantiates a new Add member model.
   *
   * @param streamEligibleMember the user
   */
  AddMembersModel(FetchEligibleMembersResult.StreamEligibleMember streamEligibleMember) {

    userId = streamEligibleMember.getUserId();
    userName = streamEligibleMember.getUserName();
    userIdentifier = streamEligibleMember.getUserIdentifier();
    userProfilePic = streamEligibleMember.getUserProfileImageUrl();
    isMember = false;
    joinTime = null;
    normaUser = true;
  }

  /**
   * Instantiates a new Viewers model.
   *
   * @param viewer the viewer
   * @param isMember the is member
   */
  AddMembersModel(FetchViewersResult.StreamViewer viewer, boolean isMember) {

    userId = viewer.getUserId();
    userName = viewer.getUserName();
    userIdentifier = viewer.getUserIdentifier();
    userProfilePic = viewer.getUserProfileImageUrl();

    joinTime = DateUtil.getDate(viewer.getSessionStartTime());
    this.isMember = isMember;
    normaUser = false;
  }

  /**
   * Instantiates a new Viewers model.
   *
   * @param viewerJoinEvent the viewer join event
   * @param isMember the is member
   */
  AddMembersModel(ViewerJoinEvent viewerJoinEvent, boolean isMember) {

    userId = viewerJoinEvent.getViewerId();
    userName = viewerJoinEvent.getViewerName();
    userIdentifier = viewerJoinEvent.getViewerIdentifier();
    userProfilePic = viewerJoinEvent.getViewerProfilePic();

    joinTime = DateUtil.getDate(viewerJoinEvent.getTimestamp());
    this.isMember = isMember;
    normaUser = false;
  }

  /**
   * Gets user id.
   *
   * @return the user id
   */
  public String getUserId() {
    return userId;
  }

  /**
   * Gets user name.
   *
   * @return the user name
   */
  String getUserName() {
    return userName;
  }

  /**
   * Gets user identifier.
   *
   * @return the user identifier
   */
  String getUserIdentifier() {
    return userIdentifier;
  }

  /**
   * Gets user profile pic.
   *
   * @return the user profile pic
   */
  String getUserProfilePic() {
    return userProfilePic;
  }

  public String getJoinTime() {
    return joinTime;
  }

  public boolean isMember() {
    return isMember;
  }

  public boolean isNormaUser() {
    return normaUser;
  }

  public void setMember(boolean member) {
    isMember = member;
  }
}
