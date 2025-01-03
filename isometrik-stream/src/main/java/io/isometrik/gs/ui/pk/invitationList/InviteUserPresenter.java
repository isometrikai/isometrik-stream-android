package io.isometrik.gs.ui.pk.invitationList;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;

import java.util.ArrayList;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.user.LiveUsersQuery;
import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.pk.invitesent.PkInviteSentQuery;
import io.isometrik.gs.ui.utils.Constants;

public class InviteUserPresenter implements InviteUserContract.Presenter {
    private final Isometrik isometrik = IsometrikStreamSdk.getInstance().getIsometrik();

    private boolean isLoadingBroadcasters;
    private boolean isLoadingInvite;
    private int usersOffset;
    private int inviteOffset;
    private boolean isLastPageUsers;
    private boolean isLastInvite;
    private int count;
    private int countInvitePage;
    private ArrayList<String> memberIds;


    private InviteUserContract.View inviteUserView;

    @Override
    public void attachView(InviteUserContract.View inviteUserView) {
        this.inviteUserView = inviteUserView;
    }

    @Override
    public void detachView() {
        this.inviteUserView = null;
    }

    @Override
    public void initialize(String streamId, ArrayList<String> memberIds) {

    }

    @Override
    public void fetchOnlineBroadcasters(String streamId, String searchData, int offset, int pageSize, boolean refreshRequest) {
        isLoadingBroadcasters = true;

        if (refreshRequest) {
            this.usersOffset = 0;
            isLastPageUsers = false;
        }

        LiveUsersQuery.Builder liveUserQuery =
                new LiveUsersQuery.Builder().setUserToken(
                                IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                        .setSearchTag(searchData)
                        .setLimit(10)
                        .setSkip(offset);

        isometrik.getRemoteUseCases()
                .getPkUseCases()
                .liveUsers(liveUserQuery.build(), (var1, var2) -> {
                    if (var1 != null) {
                        int usersSize = var1.getStreams().size();
                        isLastPageUsers = usersSize < pageSize;
                        if (refreshRequest && usersSize < 1) {
                            if (inviteUserView != null) {
                                //No users found
                                inviteUserView.onBroadcastersDataReceived(new ArrayList<>(), true);
                            }
                        } else {

                            ArrayList<InviteUserModel> usersModels = new ArrayList<>();
                            String userId = IsometrikStreamSdk.getInstance().getUserSession().getUserId();

                            ArrayList<InviteUserModel> users = var1.getStreams();
                            int size = users.size();

                            for (int i = 0; i < size; i++) {

                                String currentUserId = users.get(i).getUserId();

//                                            if (currentUserId != null) {
//                                                if (currentUserId.equals(userId) || memberIds.contains(currentUserId)) {
//                                                    count++;
//                                                } else {
                                usersModels.add(users.get(i));
//                                                }
//                                            }
                            }
                            if (inviteUserView != null) {
                                inviteUserView.onBroadcastersDataReceived(usersModels, refreshRequest);
                            }
                        }
                    } else {
                        if (var2.getHttpResponseCode() == 204) {
                            isLastPageUsers = true;
                            if (refreshRequest) {
                                if (inviteUserView != null) {
                                    inviteUserView.onBroadcastersDataReceived(new ArrayList<>(), true);
                                }
                            }
                        } else if (var2.getHttpResponseCode() == 404) {
                            isLoadingBroadcasters = false;
                            if (refreshRequest) {
                                if (inviteUserView != null) {
                                    inviteUserView.onBroadcastersDataReceived(new ArrayList<>(), true);
                                }
                            }
                        } else if (var2.getHttpResponseCode() == 400) {
                            isLoadingBroadcasters = false;
                            if (refreshRequest) {
                                if (inviteUserView != null) {
                                    inviteUserView.onError("error_404");
                                }
                            }
                        }
                    }

                });
    }

    @Override
    public void fetchInviteUsersData(String streamId, String searchData, int offset, int pageSize, boolean refreshRequest) {
        isLoadingInvite = true;

        if (refreshRequest) {
            this.inviteOffset = 0;
            isLastInvite = false;
        }

//        livejetApisService.fetchInviteUser(AppController.getInstance().getApiToken(),
//                        com.appscrip.socialecom.app.utilities.Constants.LANGUAGE, offset, pageSize, streamId, null)
//                .enqueue(new Callback<InviteUsersResponse>() {
//                    @Override
//                    public void onResponse(@NotNull Call<InviteUsersResponse> call,
//                                           @NotNull Response<InviteUsersResponse> response) {
//                        isLoadingInvite = false;
//
//                        switch (response.code()) {
//                            case 200:
//
//                                if (response.body() != null) {
//
//                                    int usersSize = response.body().getStreams().size();
//
//                                    isLastInvite = usersSize < pageSize;
//
//                                    if (refreshRequest && usersSize < 1) {
//                                        if (inviteUserView != null) {
//                                            //No users found
//                                            inviteUserView.onInviteUsersDataReceived(new ArrayList<>(), true);
//                                        }
//                                    } else {
//
//                                        ArrayList<InviteUserModel> usersModels = new ArrayList<>();
//                                        String userId = IsometrikUiSdk.getInstance().getUserSession().getUserId();
//
//                                        ArrayList<InviteUserModel> users = response.body().getStreams();
//                                        int size = users.size();
//
//                                        for (int i = 0; i < size; i++) {
//
////                                            String currentUserId = users.get(i).getUserId();
//
////                                            if (currentUserId != null) {
////                                                if (currentUserId.equals(userId) || memberIds.contains(currentUserId)) {
////                                                    countInvitePage++;
////                                                } else {
//                                            usersModels.add(users.get(i));
////                                                }
////                                            }
//                                        }
//                                        if (inviteUserView != null) {
//                                            inviteUserView.onInviteUsersDataReceived(usersModels, refreshRequest);
//                                        }
//                                    }
//                                }
//
//                                break;
//
//                            case 404:
//
//                            case 204:
//                                isLastInvite = true;
//                                if (refreshRequest) {
//                                    if (inviteUserView != null) {
//                                        inviteUserView.onInviteUsersDataReceived(new ArrayList<>(), true);
//                                    }
//                                }
//                                break;
//                            case 406:
//                                SessionObserver sessionObserver = new SessionObserver();
//                                sessionObserver.getObservable()
//                                        .subscribeOn(Schedulers.io())
//                                        .observeOn(AndroidSchedulers.mainThread())
//                                        .subscribe(new DisposableObserver<Boolean>() {
//                                            @Override
//                                            public void onNext(@NotNull Boolean flag) {
//                                                Handler handler = new Handler();
//                                                handler.postDelayed(
//                                                        () -> fetchInviteUsersData(streamId, searchData, offset, pageSize, refreshRequest), 1000);
//                                            }
//
//                                            @Override
//                                            public void onError(@NotNull Throwable e) {
//
//                                            }
//
//                                            @Override
//                                            public void onComplete() {
//                                                isLoadingInvite = false;
//                                                this.dispose();
//                                            }
//                                        });
//                                sessionApiCall.getNewSession(sessionObserver);
//                                break;
//
//                            case 400:
//                                isLoadingInvite = false;
//                                if (refreshRequest) {
//                                    if (inviteUserView != null) {
//                                        inviteUserView.onError(AppController.getInstance()
//                                                .getString(R.string.error_404));
//                                    }
//                                }
//                                break;
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(@NotNull Call<InviteUsersResponse> call, @NotNull Throwable t) {
//                        isLoadingInvite = false;
//                        if (refreshRequest) {
//                            if (inviteUserView != null) {
//                                inviteUserView.onError(AppController.getInstance()
//                                        .getString(com.appscrip.socialecom.app.R.string.ism_fetch_followers_followees_failed));
//                            }
//                        }
//                    }
//                });
    }

    @Override
    public void requestOnlineBroadcastersOnScroll(String streamId, String searchData, int firstVisibleItemPosition, int visibleItemCount, int totalItemCount) {

        if (!isLoadingBroadcasters && !isLastPageUsers) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= Constants.USERS_PAGE_SIZE) {
                usersOffset++;
                fetchOnlineBroadcasters(streamId, searchData, usersOffset * Constants.USERS_PAGE_SIZE, Constants.USERS_PAGE_SIZE, false);
            }
        }
    }

    @Override
    public void requestInviteUserOnScroll(String streamId, String searchData, int firstVisibleItemPosition, int visibleItemCount, int totalItemCount) {
        if (!isLoadingInvite && !isLastInvite) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= Constants.USERS_PAGE_SIZE) {
                inviteOffset++;
                fetchInviteUsersData(streamId, searchData, inviteOffset * Constants.USERS_PAGE_SIZE, Constants.USERS_PAGE_SIZE, false);
            }
        }
    }

    @Override
    public void sendInvitationToUserForPK(InviteUserModel inviteUserModel, String streamId, int position) {

        PkInviteSentQuery.Builder pkInviteSentQuery =
                new PkInviteSentQuery.Builder().setUserToken(
                                IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                        .setUserId(inviteUserModel.getUserId())
                        .setReceiverStreamId(inviteUserModel.getStreamId())
                        .setSenderStreamId(streamId)
                        .setLimit(PAGE_SIZE)
                        .setSkip(0);
        isometrik.getRemoteUseCases()
                .getPkUseCases().sendInvitationToUserForPK(pkInviteSentQuery.build(), ((var1, var2) -> {

                    if (var1 != null) {
                            if (inviteUserView != null) {
                                inviteUserView.onInviteSent(true, inviteUserModel, position);
                            }
                    } else {
                        if (inviteUserView != null) {
                            inviteUserView.onInviteSent(false, inviteUserModel, position);
                        }
                    }
                }));
    }
}
