package io.isometrik.gs.ui.members.list;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.utils.Constants;
import io.isometrik.gs.ui.utils.DateUtil;
import io.isometrik.gs.ui.utils.StreamDialogEnum;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.member.FetchMembersQuery;
import io.isometrik.gs.builder.member.RemoveMemberQuery;
import io.isometrik.gs.callbacks.CopublishEventCallback;
import io.isometrik.gs.callbacks.MemberEventCallback;
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
import io.isometrik.gs.response.member.FetchMembersResult;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * The members presenter to listen for the members and to fetch
 * list of members in a stream group and to remove a member from the stream group.
 * It implements MembersContract.Presenter{@link MembersContract.Presenter}
 *
 * @see MembersContract.Presenter
 */
public class MembersPresenter implements MembersContract.Presenter {

  /**
   * Instantiates a new members presenter.
   */
  MembersPresenter() {

  }

  private MembersContract.View membersView;

  private String streamId = "";
  private boolean isLoading;
  private boolean isAdmin, isModerator;

  private final Isometrik isometrik = IsometrikStreamSdk.getInstance().getIsometrik();
  private boolean isSearchRequest;
  private String searchTag;
  private final int PAGE_SIZE = Constants.USERS_PAGE_SIZE;
  private boolean isLastPage;
  private int offset;

  @Override
  public void attachView(MembersContract.View membersView) {
    this.membersView = membersView;
  }

  @Override
  public void detachView() {
    this.membersView = null;
  }

  @Override
  public void initialize(String streamId, boolean isAdmin, boolean isModerator) {

    this.streamId = streamId;
    this.isAdmin = isAdmin;
    this.isModerator = isModerator;
  }

  /**
   * @see MemberEventCallback
   */
  private final MemberEventCallback memberEventCallback = new MemberEventCallback() {
    @Override
    public void memberAdded(@NotNull Isometrik isometrik, @NotNull MemberAddEvent memberAddEvent) {
      if (memberAddEvent.getStreamId().equals(streamId)) {

        if (membersView != null) {
          membersView.addMemberEvent(new MembersModel(memberAddEvent, isModerator, isAdmin),
              memberAddEvent.getMembersCount());
        }
      }
    }

    @Override
    public void memberLeft(@NotNull Isometrik isometrik,
        @NotNull MemberLeaveEvent memberLeaveEvent) {
      if (memberLeaveEvent.getStreamId().equals(streamId)) {
        if (membersView != null) {
          membersView.removeMemberEvent(memberLeaveEvent.getMemberId(),
              memberLeaveEvent.getMembersCount());
        }
      }
    }

    @Override
    public void memberRemoved(@NotNull Isometrik isometrik,
        @NotNull MemberRemoveEvent memberRemoveEvent) {
      if (memberRemoveEvent.getStreamId().equals(streamId)) {

        if (memberRemoveEvent.getMemberId()
            .equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId())) {
          if (membersView != null) {
            membersView.onStreamOffline(IsometrikStreamSdk.getInstance()
                    .getContext()
                    .getString(R.string.ism_member_kicked_out, memberRemoveEvent.getInitiatorName()),
                StreamDialogEnum.KickedOut.getValue());
          }
        } else {
          if (membersView != null) {
            membersView.removeMemberEvent(memberRemoveEvent.getMemberId(),
                memberRemoveEvent.getMembersCount());
          }
        }
      }
    }

    @Override
    public void memberTimedOut(@NotNull Isometrik isometrik,
        @NotNull MemberTimeoutEvent memberTimeoutEvent) {
      if (memberTimeoutEvent.getStreamId().equals(streamId)) {
        if (membersView != null) {
          membersView.publishStatusChanged(memberTimeoutEvent.getMemberId(), false,
              memberTimeoutEvent.getMembersCount(), null);
        }
      }
    }

    @Override
    public void memberPublishStarted(@NotNull Isometrik isometrik,
        @NotNull PublishStartEvent publishStartEvent) {
      if (publishStartEvent.getStreamId().equals(streamId)) {
        if (membersView != null) {
          membersView.publishStatusChanged(publishStartEvent.getMemberId(), true,
              publishStartEvent.getMembersCount(),
              DateUtil.getDate(publishStartEvent.getTimestamp()));
        }
      }
    }

    @Override
    public void memberPublishStopped(@NotNull Isometrik isometrik,
        @NotNull PublishStopEvent publishStopEvent) {
      if (publishStopEvent.getStreamId().equals(streamId)) {
        if (membersView != null) {
          membersView.publishStatusChanged(publishStopEvent.getMemberId(), false,
              publishStopEvent.getMembersCount(), null);
        }
      }
    }

    @Override
    public void noMemberPublishing(@NotNull Isometrik isometrik,
        @NotNull NoPublisherLiveEvent noPublisherLiveEvent) {
      //TODO Nothing
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

        if (membersView != null) {

          membersView.onProfileSwitched(copublishRequestSwitchProfileEvent.getUserId(),
              copublishRequestSwitchProfileEvent.getMembersCount(),
              DateUtil.getDate(copublishRequestSwitchProfileEvent.getTimestamp()));
        }
      }
    }
  };

  /**
   * {@link MembersContract.Presenter#registerStreamMembersEventListener()}
   */
  @Override
  public void registerStreamMembersEventListener() {
    isometrik.getRealtimeEventsListenerManager().addMemberEventListener(memberEventCallback);
  }

  /**
   * {@link MembersContract.Presenter#unregisterStreamMembersEventListener()}
   */
  @Override
  public void unregisterStreamMembersEventListener() {
    isometrik.getRealtimeEventsListenerManager().removeMemberEventListener(memberEventCallback);
  }

  /**
   * {@link MembersContract.Presenter#registerCopublishRequestsEventListener()}
   */
  @Override
  public void registerCopublishRequestsEventListener() {
    isometrik.getRealtimeEventsListenerManager().addCopublishEventListener(copublishEventCallback);
  }

  /**
   * {@link MembersContract.Presenter#unregisterCopublishRequestsEventListener()}
   */
  @Override
  public void unregisterCopublishRequestsEventListener() {
    isometrik.getRealtimeEventsListenerManager()
        .removeCopublishEventListener(copublishEventCallback);
  }

  /**
   * {@link MembersContract.Presenter#requestMembersData(int, boolean, boolean, String)}
   */
  @Override
  public void requestMembersData(int offset, boolean refreshRequest, boolean isSearchRequest,
      String searchTag) {

    isLoading = true;

    if (refreshRequest) {
      this.offset = 0;
      isLastPage = false;
    }
    this.isSearchRequest = isSearchRequest;
    this.searchTag = isSearchRequest ? searchTag : null;

    FetchMembersQuery.Builder fetchMembersQuery = new FetchMembersQuery.Builder().setUserToken(
            IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
        .setStreamId(streamId)
        .setLimit(PAGE_SIZE)
        .setSkip(offset);
    if (isSearchRequest && searchTag != null) {
      fetchMembersQuery.setSearchTag(searchTag);
    }
    isometrik.getRemoteUseCases()
        .getMembersUseCases()
        .fetchMembers(fetchMembersQuery.build(), (var1, var2) -> {

          if (var1 != null) {

            ArrayList<MembersModel> membersModels = new ArrayList<>();

            ArrayList<FetchMembersResult.StreamMember> members = var1.getStreamMembers();
            int size = members.size();

            if (size < PAGE_SIZE) {

              isLastPage = true;
            }
            for (int i = 0; i < size; i++) {

              membersModels.add(new MembersModel(members.get(i), isModerator));
            }

            if (membersView != null) {
              membersView.onStreamMembersDataReceived(membersModels, refreshRequest,
                  var1.getMembersCount());
            }
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {

              if (refreshRequest) {
                //No members found
                if (membersView != null) {
                  membersView.onStreamMembersDataReceived(new ArrayList<>(), true,
                      offset == 0 ? 0 : -1);
                }
              } else {
                isLastPage = true;
              }
            } else {

              if (membersView != null) {
                membersView.onError(var2.getErrorMessage());
              }
            }
          }
          isLoading = false;
        });
  }

  /**
   * {@link MembersContract.Presenter#requestRemoveMember(String)}
   */
  @Override
  public void requestRemoveMember(String memberId) {

    isometrik.getRemoteUseCases()
        .getMembersUseCases()
        .removeMember(new RemoveMemberQuery.Builder().setStreamId(streamId)
            .setMemberId(memberId)
            .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
            .build(), (var1, var2) -> {

          if (var1 != null) {
            if (membersView != null) {
              membersView.onMemberRemovedResult(memberId);
            }
          } else {

            if (membersView != null) {
              membersView.onError(var2.getErrorMessage());
            }
          }
        });
  }

  /**
   * {@link MembersContract.Presenter#getMemberIds(ArrayList)}
   */
  @Override
  public ArrayList<String> getMemberIds(ArrayList<MembersModel> members) {
    ArrayList<String> memberIds = new ArrayList<>();
    for (int i = 0; i < members.size(); i++) {
      memberIds.add(members.get(i).getMemberId());
    }
    return memberIds;
  }

  @Override
  public void requestMembersDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {

    if (!isLoading && !isLastPage) {

      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount) >= PAGE_SIZE) {

        offset++;
        requestMembersData(offset * PAGE_SIZE, false, isSearchRequest, searchTag);
      }
    }
  }
}
