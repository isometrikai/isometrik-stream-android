package io.isometrik.gs.ui.users.list;

import java.util.ArrayList;

/**
 * The interface users contract containing interfaces Presenter and View to be implemented
 * by the
 * UsersPresenter{@link UsersPresenter} and
 * UsersActivity{@link UsersActivity} respectively.
 *
 * @see UsersPresenter
 * @see UsersActivity
 */
public interface UsersContract {

  /**
   * The interface UsersContract.Presenter to be implemented by UsersPresenter{@link
   * UsersPresenter}*
   *
   * @see UsersPresenter
   */
  interface Presenter {

    /**
     * Request users data.
     *
     * @param offset the offset
     * @param refreshRequest the refresh request
     * @param isSearchRequest whether fetch users request is from the search or not
     * @param searchTag the user's name to be searched
     */
    void requestUsersData(int offset, boolean refreshRequest, boolean isSearchRequest,
        String searchTag);

    /**
     * Request users data on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position in the recyclerview
     * containing list of users
     * @param visibleItemCount the visible item count in the recyclerview containing list of users
     * @param totalItemCount the total number of users in the recyclerview containing list of users
     */
    void requestUsersDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);

    /**
     * Request authorization of user selected
     *
     * @param user model containing details of the user selected
     * @param userPassword password of the user selected
     */
    void authorizeUser(UsersModel user, String userPassword);
  }

  /**
   * The interface UsersContract.View to be implemented by UsersActivity{@link
   * UsersActivity}*
   *
   * @see UsersActivity
   */
  interface View {

    /**
     * On users data received.
     *
     * @param users the list of users UsersModel{@link UsersModel} fetched
     * @param refreshRequest the latest users
     */
    void onUsersDataReceived(ArrayList<UsersModel> users, boolean refreshRequest);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);

    /**
     * On user being authorized successfully
     */
    void onUserAuthorized();

    /**
     * On user authentication failure
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * authentication
     */
    void onUserAuthorizationError(String errorMessage);
  }
}
