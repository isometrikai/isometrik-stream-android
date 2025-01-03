package io.isometrik.gs.remote;

import com.google.gson.Gson;
import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.user.AuthenticateUserQuery;
import io.isometrik.gs.builder.user.CreateUserQuery;
import io.isometrik.gs.builder.user.DeleteUserQuery;
import io.isometrik.gs.builder.user.FetchCreateUserPresignedUrlQuery;
import io.isometrik.gs.builder.user.FetchUpdateUserPresignedUrlQuery;
import io.isometrik.gs.builder.user.FetchUserDetailsQuery;
import io.isometrik.gs.builder.user.FetchUsersQuery;
import io.isometrik.gs.builder.user.UpdateUserQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.models.user.AuthenticateUser;
import io.isometrik.gs.models.user.CreateUser;
import io.isometrik.gs.models.user.DeleteUser;
import io.isometrik.gs.models.user.FetchCreateUserPresignedUrl;
import io.isometrik.gs.models.user.FetchUpdateUserPresignedUrl;
import io.isometrik.gs.models.user.FetchUserDetails;
import io.isometrik.gs.models.user.FetchUsers;
import io.isometrik.gs.models.user.UpdateUser;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.user.AuthenticateUserResult;
import io.isometrik.gs.response.user.CreateUserResult;
import io.isometrik.gs.response.user.DeleteUserResult;
import io.isometrik.gs.response.user.FetchCreateUserPresignedUrlResult;
import io.isometrik.gs.response.user.FetchUpdateUserPresignedUrlResult;
import io.isometrik.gs.response.user.FetchUserDetailsResult;
import io.isometrik.gs.response.user.FetchUsersResult;
import io.isometrik.gs.response.user.UpdateUserResult;
import org.jetbrains.annotations.NotNull;

/**
 * Classes containing use cases for various users operations, allowing ui sdk to communicate with
 * the remote backend using respective model classes.
 */
public class UsersUseCases {

  /**
   * Model classes for user
   */
  private final CreateUser createUser;
  private final DeleteUser deleteUser;
  private final FetchUserDetails fetchUserDetails;
  private final FetchUsers fetchUsers;
  private final UpdateUser updateUser;
  private final AuthenticateUser authenticateUser;
  private final FetchCreateUserPresignedUrl fetchCreateUserPresignedUrl;
  private final FetchUpdateUserPresignedUrl fetchUpdateUserPresignedUrl;
  private final IMConfiguration configuration;
  private final RetrofitManager retrofitManager;
  private final BaseResponse baseResponse;
  private final Gson gson;

  public UsersUseCases(IMConfiguration imConfiguration, RetrofitManager retrofitManager,
      BaseResponse baseResponse, Gson gson) {
    configuration = imConfiguration;
    this.retrofitManager = retrofitManager;
    this.gson = gson;
    this.baseResponse = baseResponse;

    createUser = new CreateUser();
    deleteUser = new DeleteUser();
    fetchUserDetails = new FetchUserDetails();
    fetchUsers = new FetchUsers();
    updateUser = new UpdateUser();
    authenticateUser = new AuthenticateUser();
    fetchCreateUserPresignedUrl = new FetchCreateUserPresignedUrl();
    fetchUpdateUserPresignedUrl = new FetchUpdateUserPresignedUrl();
  }

  /**
   * Authenticate user.
   *
   * @param authenticateUserQuery the authenticate user query
   * @param completionHandler the completion handler
   */
  public void authenticateUser(@NotNull AuthenticateUserQuery authenticateUserQuery,
      @NotNull CompletionHandler<AuthenticateUserResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(false);
    if (isometrikError == null) {
      authenticateUser.validateParams(authenticateUserQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Create user.
   *
   * @param createUserQuery the create user query
   * @param completionHandler the completion handler
   */
  public void createUser(@NotNull CreateUserQuery createUserQuery,
      @NotNull CompletionHandler<CreateUserResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(false);
    if (isometrikError == null) {
      createUser.validateParams(createUserQuery, completionHandler, retrofitManager, configuration,
          baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Delete user.
   *
   * @param deleteUserQuery the delete user query
   * @param completionHandler the completion handler
   */
  public void deleteUser(@NotNull DeleteUserQuery deleteUserQuery,
      @NotNull CompletionHandler<DeleteUserResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      deleteUser.validateParams(deleteUserQuery, completionHandler, retrofitManager, configuration,
          baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch user details.
   *
   * @param fetchUserDetailsQuery the fetch user details query
   * @param completionHandler the completion handler
   */
  public void fetchUserDetails(@NotNull FetchUserDetailsQuery fetchUserDetailsQuery,
      @NotNull CompletionHandler<FetchUserDetailsResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchUserDetails.validateParams(fetchUserDetailsQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch users.
   *
   * @param fetchUsersQuery the fetch users query
   * @param completionHandler the completion handler
   */
  public void fetchUsers(@NotNull FetchUsersQuery fetchUsersQuery,
      @NotNull CompletionHandler<FetchUsersResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(false);
    if (isometrikError == null) {
      fetchUsers.validateParams(fetchUsersQuery, completionHandler, retrofitManager, configuration,
          baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Update user.
   *
   * @param updateUserQuery the update user query
   * @param completionHandler the completion handler
   */
  public void updateUser(@NotNull UpdateUserQuery updateUserQuery,
      @NotNull CompletionHandler<UpdateUserResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      updateUser.validateParams(updateUserQuery, completionHandler, retrofitManager, configuration,
          baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch create user presigned url.
   *
   * @param fetchCreateUserPresignedUrlQuery the fetch create user presigned url query
   * @param completionHandler the completion handler
   */
  public void fetchCreateUserPresignedUrl(
      @NotNull FetchCreateUserPresignedUrlQuery fetchCreateUserPresignedUrlQuery,
      @NotNull CompletionHandler<FetchCreateUserPresignedUrlResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchCreateUserPresignedUrl.validateParams(fetchCreateUserPresignedUrlQuery,
          completionHandler, retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch update user presigned url.
   *
   * @param fetchUpdateUserPresignedUrlQuery the fetch update user presigned url query
   * @param completionHandler the completion handler
   */
  public void fetchUpdateUserPresignedUrl(
      @NotNull FetchUpdateUserPresignedUrlQuery fetchUpdateUserPresignedUrlQuery,
      @NotNull CompletionHandler<FetchUpdateUserPresignedUrlResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchUpdateUserPresignedUrl.validateParams(fetchUpdateUserPresignedUrlQuery,
          completionHandler, retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }
}
