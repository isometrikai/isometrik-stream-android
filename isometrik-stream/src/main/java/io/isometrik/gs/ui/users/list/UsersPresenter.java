package io.isometrik.gs.ui.users.list;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.utils.Constants;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.user.AuthenticateUserQuery;
import io.isometrik.gs.builder.user.FetchUsersQuery;
import io.isometrik.gs.response.user.FetchUsersResult;
import java.util.ArrayList;

/**
 * The presenter to fetch list of users to login with paging, search and pull to refresh
 * option.Api call to authorize credentials of selected user.
 * It implements UsersContract.Presenter{@link UsersContract.Presenter}
 *
 * @see UsersContract.Presenter
 */
public class UsersPresenter implements UsersContract.Presenter {

  /**
   * Instantiates a new users presenter.
   *
   * @param usersView the users view
   */
  UsersPresenter(UsersContract.View usersView) {
    this.usersView = usersView;
  }

  private final UsersContract.View usersView;
  private boolean isLastPage;
  private boolean isLoading, authenticatingUser;
  private int offset;
  private final Isometrik isometrik = IsometrikStreamSdk.getInstance().getIsometrik();

  private boolean isSearchRequest;
  private String searchTag;
  private final int PAGE_SIZE = Constants.USERS_PAGE_SIZE;

  @Override
  public void requestUsersData(int offset, boolean refreshRequest, boolean isSearchRequest,
      String searchTag) {
    isLoading = true;

    if (refreshRequest) {
      this.offset = 0;
      isLastPage = false;
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
            ArrayList<UsersModel> usersModels = new ArrayList<>();
            for (int i = 0; i < size; i++) {

              usersModels.add(new UsersModel(users.get(i)));
            }

            if (size < PAGE_SIZE) {

              isLastPage = true;
            }
            usersView.onUsersDataReceived(usersModels, refreshRequest);
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {

              if (refreshRequest) {
                //No users found

                usersView.onUsersDataReceived(new ArrayList<>(), true);
              } else {
                isLastPage = true;
              }
            } else {

              usersView.onError(var2.getErrorMessage());
            }
          }
          isLoading = false;
        });
  }

  /**
   * {@link UsersContract.Presenter#requestUsersDataOnScroll(int, int, int)}
   */
  @Override
  public void requestUsersDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {

    if (!isLoading && !isLastPage) {

      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount) >= PAGE_SIZE) {

        offset++;
        requestUsersData(offset * PAGE_SIZE, false, isSearchRequest, searchTag);
      }
    }
  }

  /**
   * {@link UsersContract.Presenter#authorizeUser(UsersModel, String)}
   */
  @Override
  public void authorizeUser(UsersModel userModel, String userPassword) {
    if (!authenticatingUser) {
      authenticatingUser = true;

      AuthenticateUserQuery.Builder builder =
          new AuthenticateUserQuery.Builder().setUserIdentifier(userModel.getUserIdentifier())
              .setPassword(userPassword);

      isometrik.getRemoteUseCases()
          .getUsersUseCases()
          .authenticateUser(builder.build(), (var1, var2) -> {
            authenticatingUser = false;
            if (var1 != null) {
              IsometrikStreamSdk.getInstance()
                  .getUserSession()
                  .switchUser(var1.getUserId(), var1.getUserToken(), userModel.getUserName(),
                      userModel.getUserIdentifier(), userModel.getUserProfileImageUrl(), true,
                      userModel.isNotification());
              usersView.onUserAuthorized();
            } else {

              usersView.onUserAuthorizationError(var2.getErrorMessage());
            }
          });
    }
  }
}
