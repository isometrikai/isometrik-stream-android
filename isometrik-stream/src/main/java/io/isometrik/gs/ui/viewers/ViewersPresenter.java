package io.isometrik.gs.ui.viewers;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.utils.Constants;
import io.isometrik.gs.ui.utils.StreamDialogEnum;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.viewer.FetchViewersQuery;
import io.isometrik.gs.builder.viewer.RemoveViewerQuery;
import io.isometrik.gs.callbacks.CopublishEventCallback;
import io.isometrik.gs.callbacks.MemberEventCallback;
import io.isometrik.gs.callbacks.ViewerEventCallback;
import io.isometrik.gs.events.copublish.CopublishRequestAcceptEvent;
import io.isometrik.gs.events.copublish.CopublishRequestAddEvent;
import io.isometrik.gs.events.copublish.CopublishRequestDenyEvent;
import io.isometrik.gs.events.copublish.CopublishRequestRemoveEvent;
import io.isometrik.gs.events.copublish.CopublishRequestSwitchProfileEvent;
import io.isometrik.gs.events.member.MemberAddEvent;
import io.isometrik.gs.events.member.MemberLeaveEvent;
import io.isometrik.gs.events.member.MemberRemoveEvent;
import io.isometrik.gs.events.member.MemberTimeoutEvent;
import io.isometrik.gs.events.member.NoPublisherLiveEvent;
import io.isometrik.gs.events.member.PublishStartEvent;
import io.isometrik.gs.events.member.PublishStopEvent;
import io.isometrik.gs.events.viewer.ViewerJoinEvent;
import io.isometrik.gs.events.viewer.ViewerLeaveEvent;
import io.isometrik.gs.events.viewer.ViewerRemoveEvent;
import io.isometrik.gs.events.viewer.ViewerTimeoutEvent;
import io.isometrik.gs.response.viewer.FetchViewersResult;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * The viewers presenter listens for the viewers, members events.Fetches list
 * of viewers in a stream group.Removes a viewer from a stream group.
 * It implements ViewersContract.Presenter{@link ViewersContract.Presenter}
 *
 * @see ViewersContract.Presenter
 */
public class ViewersPresenter implements ViewersContract.Presenter {

  /**
   * Instantiates a new viewers presenter.
   */
  ViewersPresenter() {
  }

  private ViewersContract.View viewersView;

  private String streamId = "";
  private ArrayList<String> memberIds = new ArrayList<>();
  private boolean isLoading;
  private String pageToken;
  private boolean isLastPage;
  private boolean isModerator;

  private final Isometrik isometrik = IsometrikStreamSdk.getInstance().getIsometrik();
  private boolean isSearchRequest;
  private String searchTag;
  private final int PAGE_SIZE = Constants.VIEWERS_PAGE_SIZE;
  private int offset;

  @Override
  public void attachView(ViewersContract.View viewersView) {
    this.viewersView = viewersView;
  }

  @Override
  public void detachView() {
    this.viewersView = null;
  }

  /**
   * {@link ViewersContract.Presenter#initialize(String, ArrayList, boolean)}
   */
  @Override
  public void initialize(String streamId, ArrayList<String> memberIds, boolean isModerator) {

    this.memberIds = memberIds;
    this.streamId = streamId;
    this.isModerator = isModerator;
  }

  /**
   * @see ViewerEventCallback
   */
  private final ViewerEventCallback viewerEventCallback = new ViewerEventCallback() {
    @Override
    public void viewerJoined(@NotNull Isometrik isometrik,
        @NotNull ViewerJoinEvent viewerJoinEvent) {

      if (viewerJoinEvent.getStreamId().equals(streamId)) {

        boolean joinedViewerIsMember = false;

        if (memberIds.contains(viewerJoinEvent.getViewerId())) {
          joinedViewerIsMember = true;
        }
        if (viewersView != null) {
          viewersView.addViewerEvent(
              new ViewersModel(viewerJoinEvent, joinedViewerIsMember, isModerator),
              viewerJoinEvent.getViewersCount());
        }
      }
    }

    @Override
    public void viewerLeft(@NotNull Isometrik isometrik,
        @NotNull ViewerLeaveEvent viewerLeaveEvent) {

      if (viewerLeaveEvent.getStreamId().equals(streamId)) {
        if (viewersView != null) {
          viewersView.removeViewerEvent(viewerLeaveEvent.getViewerId(),
              viewerLeaveEvent.getViewersCount());
        }
      }
    }

    @Override
    public void viewerRemoved(@NotNull Isometrik isometrik,
        @NotNull ViewerRemoveEvent viewerRemoveEvent) {
      if (viewerRemoveEvent.getStreamId().equals(streamId)) {

        if (viewerRemoveEvent.getViewerId()
            .equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId())) {
          if (viewersView != null) {
            viewersView.onStreamOffline(IsometrikStreamSdk.getInstance()
                    .getContext()
                    .getString(R.string.ism_viewer_kicked_out, viewerRemoveEvent.getInitiatorName()),
                StreamDialogEnum.KickedOut.getValue());
          }
        } else {
          if (viewersView != null) {
            viewersView.removeViewerEvent(viewerRemoveEvent.getViewerId(),
                viewerRemoveEvent.getViewersCount());
          }
        }
      }
    }

    @Override
    public void viewerTimedOut(@NotNull Isometrik isometrik,
        @NotNull ViewerTimeoutEvent viewerTimeoutEvent) {
      if (viewerTimeoutEvent.getStreamId().equals(streamId)) {
        if (viewersView != null) {
          viewersView.removeViewerEvent(viewerTimeoutEvent.getViewerId(),
              viewerTimeoutEvent.getViewersCount());
        }
      }
    }
  };

  /**
   * @see CopublishEventCallback
   */
  private final CopublishEventCallback copublishEventCallback = new CopublishEventCallback() {
    @Override
    public void copublishRequestAccepted(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestAcceptEvent copublishRequestAcceptEvent) {
      //Todo Nothing
    }

    @Override
    public void copublishRequestDenied(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestDenyEvent copublishRequestDenyEvent) {
      //Todo Nothing
    }

    @Override
    public void copublishRequestAdded(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestAddEvent copublishRequestAddEvent) {
      //Todo Nothing
    }

    @Override
    public void copublishRequestRemoved(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestRemoveEvent copublishRequestRemoveEvent) {
      //Todo Nothing
    }

    @Override
    public void switchProfile(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestSwitchProfileEvent copublishRequestSwitchProfileEvent) {
      if (copublishRequestSwitchProfileEvent.getStreamId().equals(streamId)) {

        if (viewersView != null) {
          viewersView.onProfileSwitched(copublishRequestSwitchProfileEvent.getUserId());
        }
      }
    }
  };

  /**
   * @see MemberEventCallback
   */
  private final MemberEventCallback memberEventCallback = new MemberEventCallback() {
    @Override
    public void memberAdded(@NotNull Isometrik isometrik, @NotNull MemberAddEvent memberAddEvent) {

      if (memberAddEvent.getStreamId().equals(streamId)) {

        if (!memberIds.contains(memberAddEvent.getMemberId())) {
          memberIds.add(memberAddEvent.getMemberId());
        }
      }
    }

    @Override
    public void memberLeft(@NotNull Isometrik isometrik,
        @NotNull MemberLeaveEvent memberLeaveEvent) {
      if (memberLeaveEvent.getStreamId().equals(streamId)) {

        memberIds.remove(memberLeaveEvent.getMemberId());
      }
    }

    @Override
    public void memberRemoved(@NotNull Isometrik isometrik,
        @NotNull MemberRemoveEvent memberRemoveEvent) {
      if (memberRemoveEvent.getStreamId().equals(streamId)) {

        memberIds.remove(memberRemoveEvent.getMemberId());

        if (memberRemoveEvent.getMemberId()
            .equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId())) {
          if (viewersView != null) {
            viewersView.onStreamOffline(IsometrikStreamSdk.getInstance()
                    .getContext()
                    .getString(R.string.ism_member_kicked_out, memberRemoveEvent.getInitiatorName()),
                StreamDialogEnum.KickedOut.getValue());
          }
        }
      }
    }

    @Override
    public void memberTimedOut(@NotNull Isometrik isometrik,
        @NotNull MemberTimeoutEvent memberTimeoutEvent) {
      //TODO nothing
    }

    @Override
    public void memberPublishStarted(@NotNull Isometrik isometrik,
        @NotNull PublishStartEvent publishStartEvent) {
      //TODO nothing
    }

    @Override
    public void memberPublishStopped(@NotNull Isometrik isometrik,
        @NotNull PublishStopEvent publishStopEvent) {
      //TODO nothing
    }

    @Override
    public void noMemberPublishing(@NotNull Isometrik isometrik,
        @NotNull NoPublisherLiveEvent noPublisherLiveEvent) {

      if (noPublisherLiveEvent.getStreamId().equals(streamId)) {
        if (viewersView != null) {
          viewersView.onStreamOffline(IsometrikStreamSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_stream_offline_no_publisher),
              StreamDialogEnum.StreamOffline.getValue());
        }
      }
    }
  };

  /**
   * {@link ViewersContract.Presenter#registerStreamViewersEventListener()}
   */
  @Override
  public void registerStreamViewersEventListener() {
    isometrik.getRealtimeEventsListenerManager().addViewerEventListener(viewerEventCallback);
  }

  /**
   * {@link ViewersContract.Presenter#unregisterStreamViewersEventListener()}
   */
  @Override
  public void unregisterStreamViewersEventListener() {
    isometrik.getRealtimeEventsListenerManager().removeViewerEventListener(viewerEventCallback);
  }

  /**
   * {@link ViewersContract.Presenter#registerStreamMembersEventListener()}
   */
  @Override
  public void registerStreamMembersEventListener() {
    isometrik.getRealtimeEventsListenerManager().addMemberEventListener(memberEventCallback);
  }

  /**
   * {@link ViewersContract.Presenter#unregisterStreamMembersEventListener()}
   */
  @Override
  public void unregisterStreamMembersEventListener() {
    isometrik.getRealtimeEventsListenerManager().removeMemberEventListener(memberEventCallback);
  }

  /**
   * {@link ViewersContract.Presenter#registerCopublishRequestsEventListener()}
   */
  @Override
  public void registerCopublishRequestsEventListener() {
    isometrik.getRealtimeEventsListenerManager().addCopublishEventListener(copublishEventCallback);
  }

  /**
   * {@link ViewersContract.Presenter#unregisterCopublishRequestsEventListener()}
   */
  @Override
  public void unregisterCopublishRequestsEventListener() {
    isometrik.getRealtimeEventsListenerManager()
        .removeCopublishEventListener(copublishEventCallback);
  }

  /**
   * {@link ViewersContract.Presenter#requestStreamViewersData(int, boolean, boolean, String)}
   */
  @Override
  public void requestStreamViewersData(int offset, boolean refreshRequest, boolean isSearchRequest,
      String searchTag) {
    isLoading = true;

    if (refreshRequest) {
      this.offset = 0;
      isLastPage = false;
    }
    this.isSearchRequest = isSearchRequest;
    this.searchTag = isSearchRequest ? searchTag : null;

    FetchViewersQuery.Builder fetchViewersQuery = new FetchViewersQuery.Builder().setUserToken(
            IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
        .setStreamId(streamId)
        .setLimit(PAGE_SIZE)
        .setSkip(offset);
    if (isSearchRequest && searchTag != null) {
      fetchViewersQuery.setSearchTag(searchTag);
    }
    isometrik.getRemoteUseCases()
        .getViewersUseCases()
        .fetchViewers(fetchViewersQuery.build(), (var1, var2) -> {

          if (var1 != null) {

            ArrayList<FetchViewersResult.StreamViewer> viewers = var1.getStreamViewers();
            int size = viewers.size();
            if (size < PAGE_SIZE) {

              isLastPage = true;
            }
            ArrayList<ViewersModel> viewersModels = new ArrayList<>();
            FetchViewersResult.StreamViewer viewer;

            for (int i = 0; i < size; i++) {

              viewer = viewers.get(i);

              boolean givenViewerIsMember = this.memberIds.contains(viewer.getUserId());

              viewersModels.add(new ViewersModel(viewer, givenViewerIsMember, isModerator));
            }
            if (viewersView != null) {
              viewersView.onStreamViewersDataReceived(viewersModels, refreshRequest,
                  var1.getViewersCount());
            }
          } else {

            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {

              if (refreshRequest) {
                //No viewers found
                if (viewersView != null) {
                  viewersView.onStreamViewersDataReceived(new ArrayList<>(), true,
                      offset == 0 ? 0 : -1);
                }
              } else {
                isLastPage = true;
              }
            } else {

              if (viewersView != null) {
                viewersView.onError(var2.getErrorMessage());
              }
            }
          }
          isLoading = false;
        });
  }

  /**
   * {@link ViewersContract.Presenter#requestRemoveViewer(String)}
   */
  @Override
  public void requestRemoveViewer(String viewerId) {

    isometrik.getRemoteUseCases()
        .getViewersUseCases()
        .removeViewer(new RemoveViewerQuery.Builder().setStreamId(streamId)
            .setViewerId(viewerId)
            .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
            .build(), (var1, var2) -> {

          if (var1 != null) {
            if (viewersView != null) {
              viewersView.onViewerRemovedResult(viewerId);
            }
          } else {

            if (viewersView != null) {
              viewersView.onError(var2.getErrorMessage());
            }
          }
        });
  }

  @Override
  public void requestViewersDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {

    if (!isLoading && !isLastPage) {

      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount) >= PAGE_SIZE) {

        offset++;
        requestStreamViewersData(offset * PAGE_SIZE, false, isSearchRequest, searchTag);
      }
    }
  }
}
