package io.isometrik.gs.ui.viewers;

public class ViewersListModel {

  private final String viewerId;
  private final String viewerProfilePic;

  public ViewersListModel(String viewerId, String viewerProfilePic) {
    this.viewerId = viewerId;
    this.viewerProfilePic = viewerProfilePic;
  }

  public String getViewerId() {
    return viewerId;
  }

  public String getViewerProfilePic() {
    return viewerProfilePic;
  }
}
