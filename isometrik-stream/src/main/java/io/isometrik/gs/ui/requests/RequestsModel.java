package io.isometrik.gs.ui.requests;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.utils.DateUtil;
import io.isometrik.gs.events.copublish.CopublishRequestAddEvent;
import io.isometrik.gs.response.copublish.FetchCopublishRequestsResult;

public class RequestsModel {

  private final String userId;
  private final String userName;
  private final String userIdentifier;
  private final String userProfilePic;
  private String requestTime;
  private boolean pending;
  private boolean accepted;
  private final boolean initiator;

  /**
   * Instantiates a new requests model.
   *
   * @param request the copublish request
   * @param initiator whether given user is initiator of the stream
   */
  RequestsModel(FetchCopublishRequestsResult.CopublishRequest request, boolean initiator) {

    userId = request.getUserId();
    userIdentifier = request.getUserIdentifier();
    userProfilePic = request.getUserProfileImageUrl();

    pending = request.isPending();

    this.initiator = initiator;

    if (userId.equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId())) {

      userName = IsometrikStreamSdk.getInstance()
          .getContext()
          .getString(R.string.ism_you, request.getUserName());
    } else {
      userName = request.getUserName();
    }

    if (pending) {
      requestTime = DateUtil.getDate(request.getTimestamp());
    } else {
      accepted = request.isAccepted();
    }
  }

  /**
   * Instantiates a new requests model.
   *
   * @param copublishRequestAddEvent the copublish request add event
   * @param initiator whether given user is initiator of the stream
   */
  RequestsModel(CopublishRequestAddEvent copublishRequestAddEvent, boolean initiator) {

    userId = copublishRequestAddEvent.getUserId();
    userIdentifier = copublishRequestAddEvent.getUserIdentifier();
    userProfilePic = copublishRequestAddEvent.getUserProfilePic();

    this.pending = true;
    this.accepted = false;
    this.initiator = initiator;

    if (userId.equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId())) {

      userName = IsometrikStreamSdk.getInstance()
          .getContext()
          .getString(R.string.ism_you, copublishRequestAddEvent.getUserName());
    } else {
      userName = copublishRequestAddEvent.getUserName();
    }

    requestTime = DateUtil.getDate(copublishRequestAddEvent.getTimestamp());
  }

  public String getUserId() {
    return userId;
  }

  public String getUserName() {
    return userName;
  }

  public String getUserIdentifier() {
    return userIdentifier;
  }

  public String getUserProfilePic() {
    return userProfilePic;
  }

  public String getRequestTime() {
    return requestTime;
  }

  public boolean isPending() {
    return pending;
  }

  public boolean isAccepted() {
    return accepted;
  }

  public boolean isInitiator() {
    return initiator;
  }

  public void setPending(boolean pending) {
    this.pending = pending;
  }

  public void setAccepted(boolean accepted) {
    this.accepted = accepted;
  }
}
