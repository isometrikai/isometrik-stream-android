package io.isometrik.gs.ui.profile;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.utils.UserSession;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.user.DeleteUserQuery;
import io.isometrik.gs.builder.user.FetchUserDetailsQuery;

/**
 * The user details presenter to fetch the user details and to delete a user.
 * It implements UserDetailsContract.Presenter{@link UserDetailsContract.Presenter}
 *
 * @see UserDetailsContract.Presenter
 */
public class UserDetailsPresenter implements UserDetailsContract.Presenter {

    /**
     * Instantiates a new User details presenter.
     */
    UserDetailsPresenter(UserDetailsContract.View userDetailsView) {
        this.userDetailsView = userDetailsView;
    }

    private final UserDetailsContract.View userDetailsView;
    private boolean deletingUser;
    private boolean fetchingUserDetails;

    private final Isometrik isometrik = IsometrikStreamSdk.getInstance().getIsometrik();

    /**
     * {@link UserDetailsContract.Presenter#requestUserDetails()}
     */
    @Override
    public void requestUserDetails() {
        if (!fetchingUserDetails) {
            fetchingUserDetails = true;
            isometrik.getRemoteUseCases()
                    .getUsersUseCases()
                    .fetchUserDetails(new FetchUserDetailsQuery.Builder().setUserToken(
                                    IsometrikStreamSdk.getInstance().getUserSession().getUserToken()).build(),
                            (var1, var2) -> {
                                fetchingUserDetails = false;
                                if (var1 != null) {

                                    try {

                                        UserSession userSession = IsometrikStreamSdk.getInstance().getUserSession();

                                        boolean nameToBeUpdated = true, identifierToBeUpdated = true,
                                                profilePicToBeUpdated = true;

                                        if (userSession.getUserName() != null) {

                                            nameToBeUpdated = !userSession.getUserName().equals(var1.getUserName());
                                        }

                                        if (userSession.getUserIdentifier() != null) {

                                            identifierToBeUpdated =
                                                    !userSession.getUserIdentifier().equals(var1.getUserIdentifier());
                                        }
                                        if (userSession.getUserProfilePic() != null) {

                                            profilePicToBeUpdated =
                                                    !userSession.getUserProfilePic().equals(var1.getUserProfileImageUrl());
                                        }

                                        userDetailsView.onUserDetailsReceived(var1.getUserName(),
                                                var1.getUserIdentifier(), var1.getUserProfileImageUrl(), nameToBeUpdated,
                                                identifierToBeUpdated, profilePicToBeUpdated, var1.getRtmpIngestUrl());
                                    } catch (Exception ignore) {
                                        //For the exception,in case profile has been switched before response for user details api came
                                    }
                                } else {

                                    userDetailsView.onError(var2.getErrorMessage());
                                }
                            });
        }
    }

    /**
     * {@link UserDetailsContract.Presenter#clearUserSession()}
     */
    @Override
    public void clearUserSession() {
        IsometrikStreamSdk.getInstance().getUserSession().clear();
    }

    /**
     * {@link UserDetailsContract.Presenter#requestUserDelete(String)}
     */
    @Override
    public void requestUserDelete(String userId) {
        if (!deletingUser) {
            deletingUser = true;
            isometrik.getRemoteUseCases()
                    .getUsersUseCases()
                    .deleteUser(new DeleteUserQuery.Builder().setUserToken(
                                    IsometrikStreamSdk.getInstance().getUserSession().getUserToken()).build(),
                            (var1, var2) -> {
                                deletingUser = false;
                                if (var1 != null) {
                                    clearUserSession();
                                    userDetailsView.onUserDeleted();
                                } else {

                                    userDetailsView.onError(var2.getErrorMessage());
                                }
                            });
        }
    }
}
