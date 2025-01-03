package io.isometrik.gs.ui.moderators.add;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.utils.Constants;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.moderator.AddModeratorQuery;
import io.isometrik.gs.builder.user.FetchUsersQuery;
import io.isometrik.gs.response.user.FetchUsersResult;
import java.util.ArrayList;

/**
 * The add moderators presenter to fetch list of users that can be added as moderator to a stream
 * group and add moderators.
 * It implements ModeratorsContract.Presenter{@link AddModeratorsContract.Presenter}
 *
 * @see AddModeratorsContract.Presenter
 */
public class AddModeratorsPresenter implements AddModeratorsContract.Presenter {

  private final Isometrik isometrik = IsometrikStreamSdk.getInstance().getIsometrik();
  private AddModeratorsContract.View addModeratorView;
  private boolean isLastPage;
  private boolean isLoading;
  private String streamId;
  private ArrayList<String> moderatorIds;

  private boolean isSearchRequest;
  private String searchTag;
  private final int PAGE_SIZE = Constants.USERS_PAGE_SIZE;
  private int offset, count;

  /**
   * Instantiates a new add moderator presenter.
   */
  AddModeratorsPresenter() {

  }

  @Override
  public void initialize(String streamId, ArrayList<String> moderatorIds) {
    this.streamId = streamId;
    this.moderatorIds = moderatorIds;
  }

  /**
   * {@link AddModeratorsContract.Presenter#requestUsersData(int, boolean, boolean, String)}
   */
  @Override
  public void requestUsersData(int offset, boolean refreshRequest, boolean isSearchRequest,
      String searchTag) {
    isLoading = true;

    if (refreshRequest) {
      this.offset = 0;
      isLastPage = false;
      count = 0;
    }
    this.isSearchRequest = isSearchRequest;
    this.searchTag = isSearchRequest ? searchTag : null;

    FetchUsersQuery.Builder fetchUsersQuery =
        new FetchUsersQuery.Builder().setLimit(PAGE_SIZE).setSkip(offset);
    if (isSearchRequest && searchTag != null) {
      fetchUsersQuery.setSearchTag(searchTag);
    }
    isometrik.getRemoteUseCases()
        .getUsersUseCases()
        .fetchUsers(fetchUsersQuery.build(), (var1, var2) -> {

          if (var1 != null) {

            ArrayList<FetchUsersResult.User> users = var1.getUsers();
            int size = users.size();
            ArrayList<AddModeratorsModel> usersModels = new ArrayList<>();
            String userId = IsometrikStreamSdk.getInstance().getUserSession().getUserId();

            if (size < PAGE_SIZE) {

              isLastPage = true;
            }
            for (int i = 0; i < size; i++) {

              String currentUserId = users.get(i).getUserId();

              if (currentUserId.equals(userId) || moderatorIds.contains(currentUserId)) {
                count++;
              } else {

                usersModels.add(new AddModeratorsModel(users.get(i)));
              }
            }
            if (addModeratorView != null) {
              addModeratorView.onUsersDataReceived(usersModels, refreshRequest);
            }
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {

              if (refreshRequest) {
                //No users found
                if (addModeratorView != null) {
                  addModeratorView.onUsersDataReceived(new ArrayList<>(), true);
                }
              } else {
                isLastPage = true;
              }
            } else {
              if (addModeratorView != null) {
                addModeratorView.onError(var2.getErrorMessage());
              }
            }
          }
          isLoading = false;
        });
  }

  /**
   * {@link AddModeratorsContract.Presenter#requestUsersDataOnScroll(int, int, int)}
   */
  @Override
  public void requestUsersDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {
    if (!isLoading && !isLastPage) {

      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount + count) >= PAGE_SIZE) {

        offset++;
        requestUsersData(offset * PAGE_SIZE, false, isSearchRequest, searchTag);
      }
    }
  }

  /**
   * {@link AddModeratorsContract.Presenter#addModerator(String)}
   */
  @Override
  public void addModerator(String moderatorId) {

    isometrik.getRemoteUseCases()
        .getModeratorsUseCases()
        .addModerator(new AddModeratorQuery.Builder().setModeratorId(moderatorId)
            .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
            .setStreamId(streamId)
            .build(), (var1, var2) -> {

          if (var1 != null) {
            if (addModeratorView != null) {
              addModeratorView.onModeratorAdded(moderatorId);
            }
          } else {

            if (addModeratorView != null) {
              addModeratorView.onError(var2.getErrorMessage());
            }
          }
        });
  }

  @Override
  public void attachView(AddModeratorsContract.View addModeratorView) {
    this.addModeratorView = addModeratorView;
  }

  @Override
  public void detachView() {
    this.addModeratorView = null;
  }
}
