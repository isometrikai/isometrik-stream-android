package io.isometrik.gs.ui.moderators.list;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.utils.Constants;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.moderator.FetchModeratorsQuery;
import io.isometrik.gs.builder.moderator.RemoveModeratorQuery;
import io.isometrik.gs.callbacks.ModeratorEventCallback;
import io.isometrik.gs.events.moderator.ModeratorAddEvent;
import io.isometrik.gs.events.moderator.ModeratorLeaveEvent;
import io.isometrik.gs.events.moderator.ModeratorRemoveEvent;
import io.isometrik.gs.response.moderator.FetchModeratorsResult;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * The moderators presenter to listen for the moderators and to fetch
 * list of moderators in a stream group and to remove a moderator from the stream group.
 * It implements ModeratorsContract.Presenter{@link ModeratorsContract.Presenter}
 *
 * @see ModeratorsContract.Presenter
 */
public class ModeratorsPresenter implements ModeratorsContract.Presenter {

  private final Isometrik isometrik = IsometrikStreamSdk.getInstance().getIsometrik();
  private ModeratorsContract.View moderatorsView;

  private String streamId = "";
  private boolean isLoading;
  private boolean isAdmin;
  private boolean isSearchRequest;
  private String searchTag;
  private final int PAGE_SIZE = Constants.USERS_PAGE_SIZE;
  private boolean isLastPage;
  private int offset;

  /**
   * Instantiates a new moderators presenter.
   */
  ModeratorsPresenter() {

  }

  @Override
  public void attachView(ModeratorsContract.View moderatorsView) {
    this.moderatorsView = moderatorsView;
  }

  @Override
  public void detachView() {
    this.moderatorsView = null;
  }

  @Override
  public void initialize(String streamId, boolean isAdmin) {

    this.streamId = streamId;
    this.isAdmin = isAdmin;
  }

  /**
   * @see ModeratorEventCallback
   */
  private final ModeratorEventCallback moderatorEventCallback = new ModeratorEventCallback() {
    @Override
    public void moderatorAdded(@NotNull Isometrik isometrik,
        @NotNull ModeratorAddEvent moderatorAddEvent) {
      if (moderatorAddEvent.getStreamId().equals(streamId)) {

        if (moderatorsView != null) {
          moderatorsView.addModeratorEvent(new ModeratorsModel(moderatorAddEvent, isAdmin),
              moderatorAddEvent.getModeratorsCount());
        }
      }
    }

    @Override
    public void moderatorLeft(@NotNull Isometrik isometrik,
        @NotNull ModeratorLeaveEvent moderatorLeaveEvent) {
      if (moderatorLeaveEvent.getStreamId().equals(streamId)) {
        if (moderatorsView != null) {
          moderatorsView.removeModeratorEvent(moderatorLeaveEvent.getModeratorId(),
              moderatorLeaveEvent.getModeratorsCount());
        }
      }
    }

    @Override
    public void moderatorRemoved(@NotNull Isometrik isometrik,
        @NotNull ModeratorRemoveEvent moderatorRemoveEvent) {
      if (moderatorRemoveEvent.getStreamId().equals(streamId)) {

        if (moderatorsView != null) {
          moderatorsView.removeModeratorEvent(moderatorRemoveEvent.getModeratorId(),
              moderatorRemoveEvent.getModeratorsCount());
        }
      }
    }
  };

  /**
   * {@link ModeratorsContract.Presenter#registerStreamModeratorsEventListener()}
   */
  @Override
  public void registerStreamModeratorsEventListener() {
    isometrik.getRealtimeEventsListenerManager().addModeratorEventListener(moderatorEventCallback);
  }

  /**
   * {@link ModeratorsContract.Presenter#unregisterStreamModeratorsEventListener()}
   */
  @Override
  public void unregisterStreamModeratorsEventListener() {
    isometrik.getRealtimeEventsListenerManager()
        .removeModeratorEventListener(moderatorEventCallback);
  }

  /**
   * {@link ModeratorsContract.Presenter#requestModeratorsData(int, boolean, boolean, String)}
   */
  @Override
  public void requestModeratorsData(int offset, boolean refreshRequest, boolean isSearchRequest,
      String searchTag) {

    isLoading = true;

    if (refreshRequest) {
      this.offset = 0;
      isLastPage = false;
    }
    this.isSearchRequest = isSearchRequest;
    this.searchTag = isSearchRequest ? searchTag : null;

    FetchModeratorsQuery.Builder fetchModeratorsQuery =
        new FetchModeratorsQuery.Builder().setUserToken(
                IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
            .setStreamId(streamId)
            .setLimit(PAGE_SIZE)
            .setSkip(offset);
    if (isSearchRequest && searchTag != null) {
      fetchModeratorsQuery.setSearchTag(searchTag);
    }
    isometrik.getRemoteUseCases()
        .getModeratorsUseCases()
        .fetchModerators(fetchModeratorsQuery.build(), (var1, var2) -> {

          if (var1 != null) {

            ArrayList<ModeratorsModel> moderatorsModels = new ArrayList<>();

            ArrayList<FetchModeratorsResult.Moderator> moderators = var1.getStreamModerators();
            int size = moderators.size();

            if (size < PAGE_SIZE) {

              isLastPage = true;
            }
            for (int i = 0; i < size; i++) {

              moderatorsModels.add(new ModeratorsModel(moderators.get(i), isAdmin));
            }

            if (moderatorsView != null) {
              moderatorsView.onStreamModeratorsDataReceived(moderatorsModels, refreshRequest,
                  var1.getModeratorsCount());
            }
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {

              if (refreshRequest) {
                //No moderators found
                if (moderatorsView != null) {
                  moderatorsView.onStreamModeratorsDataReceived(new ArrayList<>(), true,
                      offset == 0 ? 0 : -1);
                }
              } else {
                isLastPage = true;
              }
            } else {

              if (moderatorsView != null) {
                moderatorsView.onError(var2.getErrorMessage());
              }
            }
          }
          isLoading = false;
        });
  }

  /**
   * {@link ModeratorsContract.Presenter#requestRemoveModerator(String)}
   */
  @Override
  public void requestRemoveModerator(String moderatorId) {

    isometrik.getRemoteUseCases()
        .getModeratorsUseCases()
        .removeModerator(new RemoveModeratorQuery.Builder().setStreamId(streamId)
            .setModeratorId(moderatorId)
            .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
            .build(), (var1, var2) -> {

          if (var1 != null) {
            if (moderatorsView != null) {
              moderatorsView.onModeratorRemovedResult(moderatorId);
            }
          } else {

            if (moderatorsView != null) {
              moderatorsView.onError(var2.getErrorMessage());
            }
          }
        });
  }

  /**
   * {@link ModeratorsContract.Presenter#getModeratorIds(ArrayList)}
   */
  @Override
  public ArrayList<String> getModeratorIds(ArrayList<ModeratorsModel> moderators) {
    ArrayList<String> moderatorIds = new ArrayList<>();
    for (int i = 0; i < moderators.size(); i++) {
      moderatorIds.add(moderators.get(i).getModeratorId());
    }
    return moderatorIds;
  }

  @Override
  public void requestModeratorsDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {

    if (!isLoading && !isLastPage) {

      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount) >= PAGE_SIZE) {

        offset++;
        requestModeratorsData(offset * PAGE_SIZE, false, isSearchRequest, searchTag);
      }
    }
  }
}
