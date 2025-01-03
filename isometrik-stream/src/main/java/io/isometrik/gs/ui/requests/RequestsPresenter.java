package io.isometrik.gs.ui.requests;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.utils.Constants;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.copublish.AcceptCopublishRequestQuery;
import io.isometrik.gs.builder.copublish.DenyCopublishRequestQuery;
import io.isometrik.gs.builder.copublish.FetchCopublishRequestsQuery;
import io.isometrik.gs.callbacks.CopublishEventCallback;
import io.isometrik.gs.events.copublish.CopublishRequestAcceptEvent;
import io.isometrik.gs.events.copublish.CopublishRequestAddEvent;
import io.isometrik.gs.events.copublish.CopublishRequestDenyEvent;
import io.isometrik.gs.events.copublish.CopublishRequestRemoveEvent;
import io.isometrik.gs.events.copublish.CopublishRequestSwitchProfileEvent;
import io.isometrik.gs.response.copublish.FetchCopublishRequestsResult;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * The viewers presenter listens for the requests, members events.Fetches list
 * of requests in a stream group.Accepts or declines a request in a stream group.
 * It implements RequestsContract.Presenter{@link RequestsContract.Presenter}
 *
 * @see RequestsContract.Presenter
 */
public class RequestsPresenter implements RequestsContract.Presenter {

  /**
   * Instantiates a new request presenter.
   */
  RequestsPresenter() {
  }

  private RequestsContract.View requestsView;

  private String streamId = "";
  private boolean initiator;
  private boolean isLoading;
  private final Isometrik isometrik = IsometrikStreamSdk.getInstance().getIsometrik();
  private boolean isSearchRequest;
  private String searchTag;
  private final int PAGE_SIZE = Constants.USERS_PAGE_SIZE;
  private boolean isLastPage;
  private int offset;

  @Override
  public void attachView(RequestsContract.View requestsView) {
    this.requestsView = requestsView;
  }

  @Override
  public void detachView() {
    this.requestsView = null;
  }

  /**
   * {@link RequestsContract.Presenter#initialize(String, boolean)}
   */
  @Override
  public void initialize(String streamId, boolean initiator) {
    this.initiator = initiator;
    this.streamId = streamId;
  }

  /**
   * @see CopublishEventCallback
   */
  private final CopublishEventCallback copublishEventCallback = new CopublishEventCallback() {
    @Override
    public void copublishRequestAccepted(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestAcceptEvent copublishRequestAcceptEvent) {
      if (copublishRequestAcceptEvent.getStreamId().equals(streamId)) {
        if (requestsView != null) {
          requestsView.onCopublishRequestAccepted(copublishRequestAcceptEvent.getUserId());
        }
      }
    }

    @Override
    public void copublishRequestDenied(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestDenyEvent copublishRequestDenyEvent) {
      if (copublishRequestDenyEvent.getStreamId().equals(streamId)) {
        if (requestsView != null) {
          requestsView.onCopublishRequestDeclined(copublishRequestDenyEvent.getUserId());
        }
      }
    }

    @Override
    public void copublishRequestAdded(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestAddEvent copublishRequestAddEvent) {
      if (copublishRequestAddEvent.getStreamId().equals(streamId)) {
        if (requestsView != null) {
          requestsView.addRequestEvent(new RequestsModel(copublishRequestAddEvent, initiator));
        }
      }
    }

    @Override
    public void copublishRequestRemoved(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestRemoveEvent copublishRequestRemoveEvent) {
      if (copublishRequestRemoveEvent.getStreamId().equals(streamId)) {
        if (requestsView != null) {
          requestsView.removeRequestEvent(copublishRequestRemoveEvent.getUserId());
        }
      }
    }

    @Override
    public void switchProfile(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestSwitchProfileEvent copublishRequestSwitchProfileEvent) {
      //TODO Nothing
    }
  };

  /**
   * {@link RequestsContract.Presenter#registerStreamRequestsEventListener()}
   */
  @Override
  public void registerStreamRequestsEventListener() {
    isometrik.getRealtimeEventsListenerManager().addCopublishEventListener(copublishEventCallback);
  }

  /**
   * {@link RequestsContract.Presenter#unregisterStreamRequestsEventListener()}
   */
  @Override
  public void unregisterStreamRequestsEventListener() {
    isometrik.getRealtimeEventsListenerManager()
        .removeCopublishEventListener(copublishEventCallback);
  }

  /**
   * {@link RequestsContract.Presenter#requestCopublishRequestsData(int, boolean, boolean, String)}
   */
  @Override
  public void requestCopublishRequestsData(int offset, boolean refreshRequest,
      boolean isSearchRequest, String searchTag) {
    isLoading = true;

    if (refreshRequest) {
      this.offset = 0;
      isLastPage = false;
    }
    this.isSearchRequest = isSearchRequest;
    this.searchTag = isSearchRequest ? searchTag : null;

    FetchCopublishRequestsQuery.Builder fetchCopublishRequestsQuery =
        new FetchCopublishRequestsQuery.Builder().setUserToken(
                IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
            .setStreamId(streamId)
            .setLimit(PAGE_SIZE)
            .setSkip(offset);
    if (isSearchRequest && searchTag != null) {
      fetchCopublishRequestsQuery.setSearchTag(searchTag);
    }
    isometrik.getRemoteUseCases()
        .getCopublishUseCases()
        .fetchCopublishRequests(fetchCopublishRequestsQuery.build(), (var1, var2) -> {

          if (var1 != null) {

            ArrayList<RequestsModel> requestsModels = new ArrayList<>();

            ArrayList<FetchCopublishRequestsResult.CopublishRequest> requests =
                var1.getCopublishRequests();
            int size = requests.size();
            if (size < PAGE_SIZE) {

              isLastPage = true;
            }
            for (int i = 0; i < size; i++) {

              requestsModels.add(new RequestsModel(requests.get(i), initiator));
            }

            if (requestsView != null) {
              requestsView.onCopublishRequestsDataReceived(requestsModels, refreshRequest,
                  var1.getCopublishRequestsCount());
            }
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {

              if (refreshRequest) {
                //No copublish requests found
                if (requestsView != null) {
                  requestsView.onCopublishRequestsDataReceived(new ArrayList<>(), true,
                      offset == 0 ? 0 : -1);
                }
              } else {
                isLastPage = true;
              }
            } else {

              if (requestsView != null) {
                requestsView.onError(var2.getErrorMessage());
              }
            }
          }
          isLoading = false;
        });
  }

  /**
   * {@link RequestsContract.Presenter#acceptCopublishRequest(String)}
   */
  @Override
  public void acceptCopublishRequest(String userId) {

    isometrik.getRemoteUseCases()
        .getCopublishUseCases()
        .acceptCopublishRequest(new AcceptCopublishRequestQuery.Builder().setStreamId(streamId)
            .setUserId(userId)
            .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
            .build(), (var1, var2) -> {

          if (var1 != null) {
            if (requestsView != null) {
              requestsView.onCopublishRequestAccepted(userId);
            }
          } else {

            if (requestsView != null) {
              requestsView.onError(var2.getErrorMessage());
            }
          }
        });
  }

  /**
   * {@link RequestsContract.Presenter#declineCopublishRequest(String)}
   */
  @Override
  public void declineCopublishRequest(String userId) {

    isometrik.getRemoteUseCases()
        .getCopublishUseCases()
        .denyCopublishRequest(new DenyCopublishRequestQuery.Builder().setStreamId(streamId)
            .setUserId(userId)
            .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
            .build(), (var1, var2) -> {

          if (var1 != null) {
            if (requestsView != null) {
              requestsView.onCopublishRequestDeclined(userId);
            }
          } else {

            if (requestsView != null) {
              requestsView.onError(var2.getErrorMessage());
            }
          }
        });
  }

  @Override
  public void requestCopublishRequestsDataOnScroll(int firstVisibleItemPosition,
      int visibleItemCount, int totalItemCount) {

    if (!isLoading && !isLastPage) {

      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount) >= PAGE_SIZE) {

        offset++;
        requestCopublishRequestsData(offset * PAGE_SIZE, false, isSearchRequest, searchTag);
      }
    }
  }
}