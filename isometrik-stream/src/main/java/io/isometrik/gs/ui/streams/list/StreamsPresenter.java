package io.isometrik.gs.ui.streams.list;

import io.isometrik.gs.response.stream.Streams;
import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.utils.Constants;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.stream.FetchStreamsQuery;
import io.isometrik.gs.callbacks.CopublishEventCallback;
import io.isometrik.gs.callbacks.MemberEventCallback;
import io.isometrik.gs.callbacks.PresenceEventCallback;
import io.isometrik.gs.callbacks.StreamEventCallback;
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
import io.isometrik.gs.events.presence.PresenceStreamStartEvent;
import io.isometrik.gs.events.presence.PresenceStreamStopEvent;
import io.isometrik.gs.events.stream.StreamStartEvent;
import io.isometrik.gs.events.stream.StreamStopEvent;
import io.isometrik.gs.events.viewer.ViewerJoinEvent;
import io.isometrik.gs.events.viewer.ViewerLeaveEvent;
import io.isometrik.gs.events.viewer.ViewerRemoveEvent;
import io.isometrik.gs.events.viewer.ViewerTimeoutEvent;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * The streams presenter to listen for the streams, members, viewers and stream
 * presence
 * events.Also fetches the list of live stream groups along with paging.
 *
 * It implements StreamsContract.Presenter{@link StreamsContract.Presenter}
 *
 * @see StreamsContract.Presenter
 */
public class StreamsPresenter implements StreamsContract.Presenter {

  @Override
  public void attachView(StreamsContract.View streamsView) {
    this.streamsView = streamsView;
  }

  @Override
  public void detachView() {
    this.streamsView = null;
  }

  private StreamsContract.View streamsView;
  private boolean isLoading;

  private final Isometrik isometrik = IsometrikStreamSdk.getInstance().getIsometrik();
  private boolean isSearchRequest;
  private String searchTag;
  private final int PAGE_SIZE = Constants.STREAMS_PAGE_SIZE;
  private boolean isLastPage;
  private int offset;
  private int activeStreamCategoryTab;
  /**
   * @see CopublishEventCallback
   */
  private final CopublishEventCallback copublishEventCallback = new CopublishEventCallback() {
    @Override
    public void copublishRequestAccepted(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestAcceptEvent copublishRequestAcceptEvent) {

      if (streamsView != null) {
        streamsView.onCopublishRequestAccepted(copublishRequestAcceptEvent,
            copublishRequestAcceptEvent.getUserId()
                .equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId()));
      }
    }

    @Override
    public void copublishRequestDenied(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestDenyEvent copublishRequestDenyEvent) {
      if (streamsView != null) {
        streamsView.updateMembersAndViewersCount(copublishRequestDenyEvent.getStreamId(),
            copublishRequestDenyEvent.getMembersCount(),
            copublishRequestDenyEvent.getViewersCount());
      }
    }

    @Override
    public void copublishRequestAdded(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestAddEvent copublishRequestAddEvent) {
      if (streamsView != null) {
        streamsView.updateMembersAndViewersCount(copublishRequestAddEvent.getStreamId(),
            copublishRequestAddEvent.getMembersCount(), copublishRequestAddEvent.getViewersCount());
      }
    }

    @Override
    public void copublishRequestRemoved(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestRemoveEvent copublishRequestRemoveEvent) {
      if (streamsView != null) {
        streamsView.updateMembersAndViewersCount(copublishRequestRemoveEvent.getStreamId(),
            copublishRequestRemoveEvent.getMembersCount(),
            copublishRequestRemoveEvent.getViewersCount());
      }
    }

    @Override
    public void switchProfile(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestSwitchProfileEvent copublishRequestSwitchProfileEvent) {
      if (streamsView != null) {
        streamsView.updateMembersAndViewersCount(copublishRequestSwitchProfileEvent.getStreamId(),
            copublishRequestSwitchProfileEvent.getMembersCount(),
            copublishRequestSwitchProfileEvent.getViewersCount());
      }
    }
  };

  /**
   * @see StreamEventCallback
   */
  private final StreamEventCallback streamEventCallback = new StreamEventCallback() {
    @Override
    public void streamStarted(@NotNull Isometrik isometrik,
        @NotNull StreamStartEvent streamStartEvent) {

      String userId = IsometrikStreamSdk.getInstance().getUserSession().getUserId();
      boolean showStreamInUI = false;
      switch (activeStreamCategoryTab) {
        case 0: {
          showStreamInUI = true;
          break;
        }
        case 1: {
          if (streamStartEvent.isAudioOnly()) {
            showStreamInUI = true;
          }
          break;
        }
        case 2: {

          if (streamStartEvent.isMultiLive()) {
            showStreamInUI = true;
          }
          break;
        }
        case 3: {
          if (!streamStartEvent.isPublic()) {
            showStreamInUI = true;
          }
          break;
        }
        case 4: {
          if (streamStartEvent.isProductsLinked()) {
            showStreamInUI = true;
          }
          break;
        }
        case 5: {
          if (streamStartEvent.isRestream()) {
            showStreamInUI = true;
          }
          break;
        }
        case 6: {
          if (streamStartEvent.isHdBroadcast()) {
            showStreamInUI = true;
          }
          break;
        }
        //case 7: {
        //  if(streamStartEvent.isRecorded()){
        //    showStreamInUI=     true;
        //  }
        //  break;
        //}
      }
      if (showStreamInUI) {
        if (!userId.equals(streamStartEvent.getInitiatorId())) {

          boolean givenUserIsMember = false;
          List<StreamStartEvent.StreamMember> members = streamStartEvent.getMembers();

          ArrayList<String> memberIds = new ArrayList<>();
          String memberId;
          int size = members.size();
          for (int i = 0; i < size; i++) {
            memberId = members.get(i).getMemberId();
            memberIds.add(memberId);
            if (memberId.equals(userId)) {
              givenUserIsMember = true;
            }
          }
          if (streamsView != null) {

            streamsView.onStreamStarted(
                new LiveStreamsModel(streamStartEvent.getStreamId(), streamStartEvent.getStreamImage(),
                    streamStartEvent.getStreamDescription(), streamStartEvent.getMembersCount(), 0,
                    1, streamStartEvent.getInitiatorId(), streamStartEvent.getInitiatorName(),
                    streamStartEvent.getInitiatorIdentifier(), streamStartEvent.getInitiatorImage(),
                    streamStartEvent.getTimestamp(), memberIds, givenUserIsMember,
                    streamStartEvent.isPublic(), streamStartEvent.isAudioOnly(),
                    streamStartEvent.isMultiLive(), streamStartEvent.isHdBroadcast(),
                    streamStartEvent.isRestream(), streamStartEvent.isProductsLinked(),
                    streamStartEvent.getProductsCount(), streamStartEvent.isSelfHosted()));
          }
        }
      }
    }

    @Override
    public void streamStopped(@NotNull Isometrik isometrik,
        @NotNull StreamStopEvent streamStopEvent) {

      if (!streamStopEvent.getInitiatorId()
          .equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId())) {
        if (streamsView != null) {
          streamsView.onStreamEnded(streamStopEvent.getStreamId());
        }
      }
    }
  };

  /**
   * @see PresenceEventCallback
   */
  private final PresenceEventCallback presenceEventCallback = new PresenceEventCallback() {
    @Override
    public void streamStarted(@NotNull Isometrik isometrik,
        @NotNull PresenceStreamStartEvent presenceStreamStartEvent) {

      String userId = IsometrikStreamSdk.getInstance().getUserSession().getUserId();

      if (!userId.equals(presenceStreamStartEvent.getInitiatorId())) {

        boolean showStreamInUI = false;
        switch (activeStreamCategoryTab) {
          case 0: {
            showStreamInUI = true;
            break;
          }
          case 1: {
            if (presenceStreamStartEvent.isAudioOnly()) {
              showStreamInUI = true;
            }
            break;
          }
          case 2: {

            if (presenceStreamStartEvent.isMultiLive()) {
              showStreamInUI = true;
            }
            break;
          }
          case 3: {
            if (!presenceStreamStartEvent.isPublic()) {
              showStreamInUI = true;
            }
            break;
          }
          case 4: {
            if (presenceStreamStartEvent.isProductsLinked()) {
              showStreamInUI = true;
            }
            break;
          }
          case 5: {
            if (presenceStreamStartEvent.isRestream()) {
              showStreamInUI = true;
            }
            break;
          }
          case 6: {
            if (presenceStreamStartEvent.isHdBroadcast()) {
              showStreamInUI = true;
            }
            break;
          }
          //case 7: {
          //  if(presenceStreamStartEvent.isRecorded()){
          //    showStreamInUI=     true;
          //  }
          //  break;
          //}
        }
        if (showStreamInUI) {
          boolean givenUserIsMember = false;
          List<PresenceStreamStartEvent.StreamMember> members =
              presenceStreamStartEvent.getMembers();

          ArrayList<String> memberIds = new ArrayList<>();
          String memberId;
          int size = members.size();
          for (int i = 0; i < size; i++) {
            memberId = members.get(i).getMemberId();
            memberIds.add(memberId);
            if (memberId.equals(userId)) {
              givenUserIsMember = true;
            }
          }
          if (streamsView != null) {
            streamsView.onStreamStarted(new LiveStreamsModel(presenceStreamStartEvent.getStreamId(),
                presenceStreamStartEvent.getStreamImage(),
                presenceStreamStartEvent.getStreamDescription(),
                presenceStreamStartEvent.getMembersCount(), 0, 1,
                presenceStreamStartEvent.getInitiatorId(),
                presenceStreamStartEvent.getInitiatorName(),
                presenceStreamStartEvent.getInitiatorIdentifier(),
                presenceStreamStartEvent.getInitiatorImage(),
                presenceStreamStartEvent.getTimestamp(), memberIds, givenUserIsMember,
                presenceStreamStartEvent.isPublic(), presenceStreamStartEvent.isAudioOnly(),
                presenceStreamStartEvent.isMultiLive(), presenceStreamStartEvent.isHdBroadcast(),
                presenceStreamStartEvent.isRestream(), presenceStreamStartEvent.isProductsLinked(),
                presenceStreamStartEvent.getProductsCount(),
                presenceStreamStartEvent.isSelfHosted()));
          }
        }
      }
    }

    @Override
    public void streamStopped(@NotNull Isometrik isometrik,
        @NotNull PresenceStreamStopEvent presenceStreamStopEvent) {

      if (!presenceStreamStopEvent.getInitiatorId()
          .equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId())) {
        if (streamsView != null) {
          streamsView.onStreamEnded(presenceStreamStopEvent.getStreamId());
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
      if (streamsView != null) {
        streamsView.onMemberAdded(memberAddEvent, memberAddEvent.getMemberId()
            .equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId()));
      }
    }

    @Override
    public void memberLeft(@NotNull Isometrik isometrik,
        @NotNull MemberLeaveEvent memberLeaveEvent) {
      if (streamsView != null) {
        streamsView.onMemberLeft(memberLeaveEvent, memberLeaveEvent.getMemberId()
            .equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId()));
      }
    }

    @Override
    public void memberRemoved(@NotNull Isometrik isometrik,
        @NotNull MemberRemoveEvent memberRemoveEvent) {
      if (streamsView != null) {
        streamsView.onMemberRemoved(memberRemoveEvent, memberRemoveEvent.getMemberId()
            .equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId()));
      }
    }

    @Override
    public void memberTimedOut(@NotNull Isometrik isometrik,
        @NotNull MemberTimeoutEvent memberTimeoutEvent) {
      if (streamsView != null) {
        streamsView.updateMembersAndViewersCount(memberTimeoutEvent.getStreamId(),
            memberTimeoutEvent.getMembersCount(), memberTimeoutEvent.getViewersCount());
      }
    }

    @Override
    public void memberPublishStarted(@NotNull Isometrik isometrik,
        @NotNull PublishStartEvent publishStartEvent) {

      if (publishStartEvent.getMemberId()
          .equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId())) {
        if (streamsView != null) {
          streamsView.onPublishStarted(publishStartEvent, publishStartEvent.getMemberId());
        }
      } else {
        if (streamsView != null) {
          streamsView.updateMembersAndViewersCount(publishStartEvent.getStreamId(),
              publishStartEvent.getMembersCount(), publishStartEvent.getViewersCount());
        }
      }
    }

    @Override
    public void memberPublishStopped(@NotNull Isometrik isometrik,
        @NotNull PublishStopEvent publishStopEvent) {
      if (publishStopEvent.getMemberId()
          .equals(IsometrikStreamSdk.getInstance().getUserSession().getUserId())) {
        if (streamsView != null) {
          streamsView.onPublishStopped(publishStopEvent, publishStopEvent.getMemberId());
        }
      } else {
        if (streamsView != null) {
          streamsView.updateMembersAndViewersCount(publishStopEvent.getStreamId(),
              publishStopEvent.getMembersCount(), publishStopEvent.getViewersCount());
        }
      }
    }

    @Override
    public void noMemberPublishing(@NotNull Isometrik isometrik,
        @NotNull NoPublisherLiveEvent noPublisherLiveEvent) {
      if (streamsView != null) {
        streamsView.onStreamEnded(noPublisherLiveEvent.getStreamId());
      }
    }
  };

  /**
   * @see ViewerEventCallback
   */
  private final ViewerEventCallback viewerEventCallback = new ViewerEventCallback() {
    @Override
    public void viewerJoined(@NotNull Isometrik isometrik,
        @NotNull ViewerJoinEvent viewerJoinEvent) {
      if (streamsView != null) {
        streamsView.updateMembersAndViewersCount(viewerJoinEvent.getStreamId(),
            viewerJoinEvent.getMembersCount(), viewerJoinEvent.getViewersCount());
      }
    }

    @Override
    public void viewerLeft(@NotNull Isometrik isometrik,
        @NotNull ViewerLeaveEvent viewerLeaveEvent) {
      if (streamsView != null) {
        streamsView.updateMembersAndViewersCount(viewerLeaveEvent.getStreamId(),
            viewerLeaveEvent.getMembersCount(), viewerLeaveEvent.getViewersCount());
      }
    }

    @Override
    public void viewerRemoved(@NotNull Isometrik isometrik,
        @NotNull ViewerRemoveEvent viewerRemoveEvent) {
      if (streamsView != null) {
        streamsView.updateMembersAndViewersCount(viewerRemoveEvent.getStreamId(),
            viewerRemoveEvent.getMembersCount(), viewerRemoveEvent.getViewersCount());
      }
    }

    @Override
    public void viewerTimedOut(@NotNull Isometrik isometrik,
        @NotNull ViewerTimeoutEvent viewerTimeoutEvent) {
      if (streamsView != null) {
        streamsView.updateMembersAndViewersCount(viewerTimeoutEvent.getStreamId(),
            viewerTimeoutEvent.getMembersCount(), viewerTimeoutEvent.getViewersCount());
      }
    }
  };

  /**
   * {@link StreamsContract.Presenter#registerStreamsEventListener()}
   */
  @Override
  public void registerStreamsEventListener() {
    isometrik.getRealtimeEventsListenerManager().addStreamEventListener(streamEventCallback);
  }

  /**
   * {@link StreamsContract.Presenter#unregisterStreamsEventListener()}
   */
  @Override
  public void unregisterStreamsEventListener() {
    isometrik.getRealtimeEventsListenerManager().removeStreamEventListener(streamEventCallback);
  }

  /**
   * {@link StreamsContract.Presenter#registerCopublishRequestsEventListener()}
   */
  @Override
  public void registerCopublishRequestsEventListener() {
    isometrik.getRealtimeEventsListenerManager().addCopublishEventListener(copublishEventCallback);
  }

  /**
   * {@link StreamsContract.Presenter#unregisterCopublishRequestsEventListener()}
   */
  @Override
  public void unregisterCopublishRequestsEventListener() {
    isometrik.getRealtimeEventsListenerManager()
        .removeCopublishEventListener(copublishEventCallback);
  }

  /**
   * {@link StreamsContract.Presenter#registerPresenceEventListener()}
   */
  @Override
  public void registerPresenceEventListener() {
    isometrik.getRealtimeEventsListenerManager().addPresenceEventListener(presenceEventCallback);
  }

  /**
   * {@link StreamsContract.Presenter#unregisterPresenceEventListener()}
   */
  @Override
  public void unregisterPresenceEventListener() {
    isometrik.getRealtimeEventsListenerManager().removePresenceEventListener(presenceEventCallback);
  }

  /**
   * {@link StreamsContract.Presenter#registerStreamMembersEventListener()}
   */
  @Override
  public void registerStreamMembersEventListener() {
    isometrik.getRealtimeEventsListenerManager().addMemberEventListener(memberEventCallback);
  }

  /**
   * {@link StreamsContract.Presenter#unregisterStreamMembersEventListener()}
   */
  @Override
  public void unregisterStreamMembersEventListener() {
    isometrik.getRealtimeEventsListenerManager().removeMemberEventListener(memberEventCallback);
  }

  /**
   * {@link StreamsContract.Presenter#registerStreamViewersEventListener()}
   */
  @Override
  public void registerStreamViewersEventListener() {
    isometrik.getRealtimeEventsListenerManager().addViewerEventListener(viewerEventCallback);
  }

  /**
   * {@link StreamsContract.Presenter#unregisterStreamViewersEventListener()}
   */
  @Override
  public void unregisterStreamViewersEventListener() {
    isometrik.getRealtimeEventsListenerManager().removeViewerEventListener(viewerEventCallback);
  }

  /**
   * {@link StreamsContract.Presenter#requestStreamsData(int, boolean, boolean, String, int)}
   */
  @Override
  public void requestStreamsData(int offset, boolean refreshRequest, boolean isSearchRequest,
      String searchTag, int activeStreamCategoryTab) {

    isLoading = true;
    this.activeStreamCategoryTab = activeStreamCategoryTab;
    if (refreshRequest) {
      this.offset = 0;
      isLastPage = false;
    }
    this.isSearchRequest = isSearchRequest;
    this.searchTag = isSearchRequest ? searchTag : null;

    FetchStreamsQuery.Builder fetchStreamsQuery = new FetchStreamsQuery.Builder().setUserToken(
            IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
        .setLimit(PAGE_SIZE)
        .setSkip(offset);
    if (isSearchRequest && searchTag != null) {
      fetchStreamsQuery.setSearchTag(searchTag);
    }

    switch (activeStreamCategoryTab) {

      case 1: {
        fetchStreamsQuery.setAudioOnly(true);
        break;
      }
      case 2: {
        fetchStreamsQuery.setMultiLive(true);
        break;
      }
      case 3: {
        fetchStreamsQuery.setPublic(false);
        break;
      }
      case 4: {
        fetchStreamsQuery.setProductsLinked(true);
        break;
      }
      case 5: {
        fetchStreamsQuery.setRestream(true);
        break;
      }
      case 6: {
        fetchStreamsQuery.setHdBroadcast(true);
        break;
      }
      case 7: {
        fetchStreamsQuery.setRecorded(true);
        break;
      }
    }
    isometrik.getRemoteUseCases()
        .getStreamsUseCases()
        .fetchStreams(fetchStreamsQuery.build(), (var1, var2) -> {

          if (var1 != null) {

            ArrayList<Streams> streams = var1.getStreams();
            int size = streams.size();

            if (size < PAGE_SIZE) {

              isLastPage = true;
            }

            ArrayList<LiveStreamsModel> liveStreamsModels = new ArrayList<>();

            for (int i = 0; i < size; i++) {

              liveStreamsModels.add(new LiveStreamsModel(streams.get(i)));
            }
            if (streamsView != null) {
              streamsView.onStreamsDataReceived(liveStreamsModels, refreshRequest);
            }
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {

              if (refreshRequest) {
                //No streams found
                if (streamsView != null) {
                  streamsView.onStreamsDataReceived(new ArrayList<>(), true);
                }
              } else {
                isLastPage = true;
              }
            } else {
              if (streamsView != null) {
                streamsView.onError(var2.getErrorMessage());
              }
            }
          }

          isLoading = false;
        });
  }

  /**
   * {@link StreamsContract.Presenter#requestStreamsDataOnScroll(int, int, int)}
   */
  @Override
  public void requestStreamsDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {
    if (!isLoading && !isLastPage) {
      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount) >= PAGE_SIZE) {

        offset++;
        requestStreamsData(offset * PAGE_SIZE, false, isSearchRequest, searchTag,
            activeStreamCategoryTab);
      }
    }
  }
}
