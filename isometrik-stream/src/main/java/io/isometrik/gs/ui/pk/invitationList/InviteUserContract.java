package io.isometrik.gs.ui.pk.invitationList;

import java.util.ArrayList;


import io.isometrik.gs.ui.utils.BasePresenter;

public interface InviteUserContract {

    interface Presenter extends BasePresenter<View> {

        /**
         * Initialize.
         *
         * @param streamId  the stream id
         * @param memberIds the member ids
         */
        void initialize(String streamId, ArrayList<String> memberIds);

        void fetchOnlineBroadcasters(String streamId, String searchData, int offset, int pageSize, boolean refreshRequest);

        void fetchInviteUsersData(String streamId, String searchData, int offset, int pageSize, boolean refreshRequest);

        void requestOnlineBroadcastersOnScroll(String streamId, String searchData,int firstVisibleItemPosition, int visibleItemCount, int totalItemCount);

        void requestInviteUserOnScroll(String streamId, String searchData,int firstVisibleItemPosition, int visibleItemCount, int totalItemCount);

        void sendInvitationToUserForPK(InviteUserModel inviteUserModel, String streamId, int position);
    }

    interface View {

        void onInviteUsersDataReceived(ArrayList<InviteUserModel> users, boolean latestUsers);

        void onBroadcastersDataReceived(ArrayList<InviteUserModel> users, boolean latestUsers);

        void onError(String errorMessage);

        void onInviteSent(boolean isSuccess,InviteUserModel inviteUserModel, int position);
    }
}
