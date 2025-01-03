package io.isometrik.gs.ui.viewers;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.utils.DateUtil;
import io.isometrik.gs.events.viewer.ViewerJoinEvent;
import io.isometrik.gs.response.viewer.FetchViewersResult;

/**
 * The type Viewers model.
 */
public class ViewersModel {

  private final String viewerId;
  private final String viewerName;
  private final String viewerIdentifier;
  private final String viewerProfilePic;
  private final String joinTime;
  private final boolean isMember;
  private final boolean canRemoveViewer;

  /**
   * Instantiates a new Viewers model.
   *
   * @param viewer the viewer
   * @param isMember the is member
   * @param canRemoveViewer the can remove viewer
   */
  public ViewersModel(FetchViewersResult.StreamViewer viewer, boolean isMember,
      boolean canRemoveViewer) {

    viewerId = viewer.getUserId();

    if (viewerId.equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId())) {

      viewerName = IsometrikStreamSdk.getInstance()
          .getContext()
          .getString(R.string.ism_you, viewer.getUserName());
      this.canRemoveViewer = false;
    } else {
      viewerName = viewer.getUserName();

      this.canRemoveViewer = canRemoveViewer;
    }
    viewerIdentifier = viewer.getUserIdentifier();
    viewerProfilePic = viewer.getUserProfileImageUrl();
    joinTime = DateUtil.getDate(viewer.getSessionStartTime());

    this.isMember = isMember;
  }

  /**
   * Instantiates a new Viewers model.
   *
   * @param viewerJoinEvent the viewer join event
   * @param isMember the is member
   * @param canRemoveViewer the can remove viewer
   */
  public ViewersModel(ViewerJoinEvent viewerJoinEvent, boolean isMember, boolean canRemoveViewer) {

    viewerId = viewerJoinEvent.getViewerId();

    if (viewerId.equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId())) {

      viewerName = IsometrikStreamSdk.getInstance()
          .getContext()
          .getString(R.string.ism_you, viewerJoinEvent.getViewerName());
      this.canRemoveViewer = false;
    } else {
      viewerName = viewerJoinEvent.getViewerName();
      this.canRemoveViewer = canRemoveViewer;
    }

    viewerIdentifier = viewerJoinEvent.getViewerIdentifier();
    viewerProfilePic = viewerJoinEvent.getViewerProfilePic();
    joinTime = DateUtil.getDate(viewerJoinEvent.getTimestamp());

    this.isMember = isMember;
  }

  /**
   * Gets viewer id.
   *
   * @return the viewer id
   */
  String getViewerId() {
    return viewerId;
  }

  /**
   * Gets viewer name.
   *
   * @return the viewer name
   */
  String getViewerName() {
    return viewerName;
  }

  /**
   * Gets viewer identifier.
   *
   * @return the viewer identifier
   */
  String getViewerIdentifier() {
    return viewerIdentifier;
  }

  /**
   * Gets viewer profile pic.
   *
   * @return the viewer profile pic
   */
  String getViewerProfilePic() {
    return viewerProfilePic;
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
   * Is member boolean.
   *
   * @return the boolean
   */
  public boolean isMember() {
    return isMember;
  }

  /**
   * Is can remove viewer boolean.
   *
   * @return the boolean
   */
  boolean isCanRemoveViewer() {
    return canRemoveViewer;
  }
}
