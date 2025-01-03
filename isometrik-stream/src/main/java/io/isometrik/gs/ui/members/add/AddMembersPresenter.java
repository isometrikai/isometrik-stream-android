package io.isometrik.gs.ui.members.add;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.utils.Constants;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.member.AddMemberQuery;
import io.isometrik.gs.builder.member.FetchEligibleMembersQuery;
import io.isometrik.gs.builder.viewer.FetchViewersQuery;
import io.isometrik.gs.callbacks.CopublishEventCallback;
import io.isometrik.gs.callbacks.ViewerEventCallback;
import io.isometrik.gs.events.copublish.CopublishRequestAcceptEvent;
import io.isometrik.gs.events.copublish.CopublishRequestAddEvent;
import io.isometrik.gs.events.copublish.CopublishRequestDenyEvent;
import io.isometrik.gs.events.copublish.CopublishRequestRemoveEvent;
import io.isometrik.gs.events.copublish.CopublishRequestSwitchProfileEvent;
import io.isometrik.gs.events.viewer.ViewerJoinEvent;
import io.isometrik.gs.events.viewer.ViewerLeaveEvent;
import io.isometrik.gs.events.viewer.ViewerRemoveEvent;
import io.isometrik.gs.events.viewer.ViewerTimeoutEvent;
import io.isometrik.gs.response.member.FetchEligibleMembersResult;
import io.isometrik.gs.response.viewer.FetchViewersResult;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * The members presenter to fetch list of users and viewers that can be added as a member to a
 * broadcast.
 * It implements MembersContract.Presenter{@link AddMembersContract.Presenter}
 *
 * @see AddMembersContract.Presenter
 */
public class AddMembersPresenter implements AddMembersContract.Presenter {

  /**
   * Instantiates a new add member presenter.
   */
  AddMembersPresenter() {

  }

  private AddMembersContract.View addMemberView;
  private boolean isLastPageUser, isLastPageViewer;
  private boolean isLoadingUser, isLoadingViewer;
  private String streamId;
  private ArrayList<String> memberIds;

  private final Isometrik isometrik = IsometrikStreamSdk.getInstance().getIsometrik();

  private boolean isSearchRequestUser, isSearchRequestViewer;
  private String searchTagUser, searchTagViewer;
  private final int PAGE_SIZE = Constants.USERS_PAGE_SIZE;

  private int offsetUser, offsetViewer;

  @Override
  public void initialize(String streamId, ArrayList<String> memberIds) {
    this.streamId = streamId;
    this.memberIds = memberIds;
  }

  /**
   * @see ViewerEventCallback
   */
  private final ViewerEventCallback viewerEventCallback = new ViewerEventCallback() {
    @Override
    public void viewerJoined(@NotNull Isometrik isometrik,
        @NotNull ViewerJoinEvent viewerJoinEvent) {

      if (viewerJoinEvent.getStreamId().equals(streamId)) {

        boolean joinedViewerIsMember = memberIds.contains(viewerJoinEvent.getViewerId());

        if (addMemberView != null) {
          addMemberView.addViewerEvent(new AddMembersModel(viewerJoinEvent, joinedViewerIsMember));
        }
      }
    }

    @Override
    public void viewerLeft(@NotNull Isometrik isometrik,
        @NotNull ViewerLeaveEvent viewerLeaveEvent) {

      if (viewerLeaveEvent.getStreamId().equals(streamId)) {
        if (addMemberView != null) {
          addMemberView.removeViewerEvent(viewerLeaveEvent.getViewerId());
        }
      }
    }

    @Override
    public void viewerRemoved(@NotNull Isometrik isometrik,
        @NotNull ViewerRemoveEvent viewerRemoveEvent) {
      if (viewerRemoveEvent.getStreamId().equals(streamId)) {

        if (addMemberView != null) {
          addMemberView.removeViewerEvent(viewerRemoveEvent.getViewerId());
        }
      }
    }

    @Override
    public void viewerTimedOut(@NotNull Isometrik isometrik,
        @NotNull ViewerTimeoutEvent viewerTimeoutEvent) {
      if (viewerTimeoutEvent.getStreamId().equals(streamId)) {
        if (addMemberView != null) {
          addMemberView.removeViewerEvent(viewerTimeoutEvent.getViewerId());
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

        if (addMemberView != null) {
          addMemberView.onProfileSwitched(copublishRequestSwitchProfileEvent.getUserId());
        }
      }
    }
  };

  /**
   * {@link AddMembersContract.Presenter#requestEligibleUsersData(int, boolean, boolean, String)}
   */
  @Override
  public void requestEligibleUsersData(int offset, boolean refreshRequest, boolean isSearchRequest,
      String searchTag) {

    isLoadingUser = true;

    if (refreshRequest) {
      this.offsetUser = 0;
      isLastPageUser = false;
    }
    this.isSearchRequestUser = isSearchRequest;
    this.searchTagUser = isSearchRequest ? searchTag : null;

    FetchEligibleMembersQuery.Builder fetchEligibleMembersQuery =
        new FetchEligibleMembersQuery.Builder().setUserToken(
                IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
            .setStreamId(streamId)
            .setLimit(PAGE_SIZE)
            .setSkip(offset);
    if (isSearchRequest && searchTag != null) {
      fetchEligibleMembersQuery.setSearchTag(searchTag);
    }
    isometrik.getRemoteUseCases()
        .getMembersUseCases()
        .fetchEligibleMembers(fetchEligibleMembersQuery.build(), (var1, var2) -> {

          if (var1 != null) {

            ArrayList<FetchEligibleMembersResult.StreamEligibleMember> users =
                var1.getStreamEligibleMembers();
            int size = users.size();
            if (size < PAGE_SIZE) {

              isLastPageUser = true;
            }
            ArrayList<AddMembersModel> usersModels = new ArrayList<>();

            for (int i = 0; i < size; i++) {

              usersModels.add(new AddMembersModel(users.get(i)));
            }

            if (addMemberView != null) {
              addMemberView.onEligibleUsersDataReceived(usersModels, refreshRequest);
            }
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {

              if (refreshRequest) {
                //No members found
                if (addMemberView != null) {
                  addMemberView.onEligibleUsersDataReceived(new ArrayList<>(), true);
                }
              } else {
                isLastPageUser = true;
              }
            } else {

              if (addMemberView != null) {
                addMemberView.onError(var2.getErrorMessage());
              }
            }
          }
          isLoadingUser = false;
        });
  }

  /**
   * {@link AddMembersContract.Presenter#requestEligibleUsersDataOnScroll(int, int, int)}
   */
  @Override
  public void requestEligibleUsersDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {

    if (!isLoadingUser && !isLastPageUser) {

      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount) >= PAGE_SIZE) {

        offsetUser++;
        requestEligibleUsersData(offsetUser * PAGE_SIZE, false, isSearchRequestUser, searchTagUser);
      }
    }
  }

  /**
   * {@link AddMembersContract.Presenter#addMember(String, String)}
   */
  @Override
  public void addMember(String memberId, String streamId) {

    isometrik.getRemoteUseCases()
        .getMembersUseCases()
        .addMember(new AddMemberQuery.Builder().setMemberId(memberId)
            .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
            .setStreamId(streamId)
            .build(), (var1, var2) -> {

          if (var1 != null) {
            if (addMemberView != null) {
              addMemberView.onMemberAdded(memberId);
            }
          } else {

            if (addMemberView != null) {
              addMemberView.onError(var2.getErrorMessage());
            }
          }
        });
  }

  /**
   * {@link AddMembersContract.Presenter#requestViewersData(int, boolean, boolean, String)}
   */
  @Override
  public void requestViewersData(int offset, boolean refreshRequest, boolean isSearchRequest,
      String searchTag) {

    isLoadingViewer = true;

    if (refreshRequest) {
      this.offsetViewer = 0;
      isLastPageViewer = false;
    }
    this.isSearchRequestViewer = isSearchRequest;
    this.searchTagViewer = isSearchRequest ? searchTag : null;

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

              isLastPageViewer = true;
            }
            ArrayList<AddMembersModel> viewersModels = new ArrayList<>();

            for (int i = 0; i < size; i++) {

              viewersModels.add(new AddMembersModel(viewers.get(i),
                  memberIds.contains(viewers.get(i).getUserId())));
            }

            if (addMemberView != null) {
              addMemberView.onViewersDataReceived(viewersModels, refreshRequest);
            }
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {

              if (refreshRequest) {
                //No viewers found
                if (addMemberView != null) {
                  addMemberView.onViewersDataReceived(new ArrayList<>(), true);
                }
              } else {
                isLastPageViewer = true;
              }
            } else {

              if (addMemberView != null) {
                addMemberView.onError(var2.getErrorMessage());
              }
            }
          }
          isLoadingViewer = false;
        });
  }

  /**
   * {@link AddMembersContract.Presenter#requestViewersDataOnScroll(int, int, int)}
   */
  @Override
  public void requestViewersDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {

    if (!isLoadingViewer && !isLastPageViewer) {

      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount) >= PAGE_SIZE) {

        offsetViewer++;
        requestViewersData(offsetViewer * PAGE_SIZE, false, isSearchRequestViewer, searchTagViewer);
      }
    }
  }

  @Override
  public void attachView(AddMembersContract.View addMemberView) {
    this.addMemberView = addMemberView;
  }

  @Override
  public void detachView() {
    this.addMemberView = null;
  }

  /**
   * {@link AddMembersContract.Presenter#registerStreamViewersEventListener()}
   */
  @Override
  public void registerStreamViewersEventListener() {
    isometrik.getRealtimeEventsListenerManager().addViewerEventListener(viewerEventCallback);
  }

  /**
   * {@link AddMembersContract.Presenter#unregisterStreamViewersEventListener()}
   */
  @Override
  public void unregisterStreamViewersEventListener() {
    isometrik.getRealtimeEventsListenerManager().removeViewerEventListener(viewerEventCallback);
  }

  /**
   * {@link AddMembersContract.Presenter#registerCopublishRequestsEventListener()}
   */
  @Override
  public void registerCopublishRequestsEventListener() {
    isometrik.getRealtimeEventsListenerManager().addCopublishEventListener(copublishEventCallback);
  }

  /**
   * {@link AddMembersContract.Presenter#unregisterCopublishRequestsEventListener()}
   */
  @Override
  public void unregisterCopublishRequestsEventListener() {
    isometrik.getRealtimeEventsListenerManager()
        .removeCopublishEventListener(copublishEventCallback);
  }
}
