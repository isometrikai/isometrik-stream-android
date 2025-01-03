package io.isometrik.gs.ui.pk.invitesent;

import io.isometrik.gs.ui.pk.response.PkInviteRelatedEvent;
import io.isometrik.gs.ui.pk.invitationList.InviteUserModel;


/**
 * The interface PK Invite action callbacks for communication back to Scrollable Stream activity, on click of
 * various action buttons.
 */
public interface PkInviteActionCallbacks {

  /**
   * click on Accept PK  request .
   */
  void clickOnPkAccept(String invitedId, String initiatorStreamId, PkInviteRelatedEvent pkInvitationGetEvent);

  /**
   * Reject PK request.
   */
  void clickOnPkReject(String invitedId,String initiatorStreamId);

  void onPkInviteSent(InviteUserModel inviteUserModel);
}
