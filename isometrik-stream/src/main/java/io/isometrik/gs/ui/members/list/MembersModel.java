package io.isometrik.gs.ui.members.list;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.utils.DateUtil;
import io.isometrik.gs.events.member.MemberAddEvent;
import io.isometrik.gs.response.member.FetchMembersResult;

/**
 * The type Members model.
 */
class MembersModel {

  private boolean isPublishing;
  private final boolean isAdmin;
  private final String memberId;
  private final String memberName;
  private final String memberIdentifier;
  private final String memberProfilePic;
  private String joinTime;

  private final boolean canRemoveMember;

  /**
   * Instantiates a new Members model.
   *
   * @param member the member
   * @param canRemoveMember the can remove member
   */
  MembersModel(FetchMembersResult.StreamMember member, boolean canRemoveMember) {

    memberId = member.getUserId();

    if (memberId.equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId())) {

      memberName = IsometrikStreamSdk.getInstance()
          .getContext()
          .getString(R.string.ism_you, member.getUserName());
      this.canRemoveMember = false;
    } else {
      memberName = member.getUserName();
      this.canRemoveMember = canRemoveMember;
    }

    memberIdentifier = member.getUserIdentifier();
    memberProfilePic = member.getUserProfileImageUrl();

    if (member.getJoinTime() != null) {
      joinTime = DateUtil.getDate(member.getJoinTime());
    } else {
      joinTime = IsometrikStreamSdk.getInstance().getContext().getString(R.string.ism_not_publishing);
    }
    isPublishing = member.getPublishing();
    isAdmin = member.getAdmin();
  }

  /**
   * Instantiates a new Members model.
   *
   * @param memberAddEvent the member add event
   * @param canRemoveMember the can remove member
   * @param isAdmin whether given user is admin or not
   */
  MembersModel(MemberAddEvent memberAddEvent, boolean canRemoveMember, boolean isAdmin) {

    memberId = memberAddEvent.getMemberId();

    if (memberId.equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId())) {

      memberName = IsometrikStreamSdk.getInstance()
          .getContext()
          .getString(R.string.ism_you, memberAddEvent.getMemberName());
      this.canRemoveMember = false;
    } else {
      memberName = memberAddEvent.getMemberName();
      this.canRemoveMember = canRemoveMember;
    }

    memberIdentifier = memberAddEvent.getMemberIdentifier();
    memberProfilePic = memberAddEvent.getMemberProfilePic();
    joinTime = IsometrikStreamSdk.getInstance().getContext().getString(R.string.ism_not_publishing);

    isPublishing = false;
    this.isAdmin = isAdmin;
  }

  /**
   * Is publishing boolean.
   *
   * @return the boolean
   */
  boolean isPublishing() {
    return isPublishing;
  }

  /**
   * Is admin boolean.
   *
   * @return the boolean
   */
  boolean isAdmin() {
    return isAdmin;
  }

  /**
   * Gets member id.
   *
   * @return the member id
   */
  String getMemberId() {
    return memberId;
  }

  /**
   * Gets member name.
   *
   * @return the member name
   */
  String getMemberName() {
    return memberName;
  }

  /**
   * Gets member identifier.
   *
   * @return the member identifier
   */
  String getMemberIdentifier() {
    return memberIdentifier;
  }

  /**
   * Gets member profile pic.
   *
   * @return the member profile pic
   */
  String getMemberProfilePic() {
    return memberProfilePic;
  }

  /**
   * Gets join time.
   *
   * @return the join time
   */
  String getJoinTime() {
    return joinTime;
  }

  /**
   * Is can remove member boolean.
   *
   * @return the boolean
   */
  boolean isCanRemoveMember() {
    return canRemoveMember;
  }

  /**
   * Sets publishing.
   *
   * @param publishing the publishing
   */
  void setPublishing(boolean publishing) {
    isPublishing = publishing;
  }

  /**
   * Sets jointime
   *
   * @param joinTime the time of stream group join
   */
  public void setJoinTime(String joinTime) {
    this.joinTime = joinTime;
  }
}
