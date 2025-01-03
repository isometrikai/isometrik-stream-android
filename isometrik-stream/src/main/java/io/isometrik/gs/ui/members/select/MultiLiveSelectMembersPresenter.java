package io.isometrik.gs.ui.members.select;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.utils.Constants;
import io.isometrik.gs.ui.utils.UserSession;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.stream.StartStreamQuery;
import io.isometrik.gs.builder.user.FetchUsersQuery;
import io.isometrik.gs.response.user.FetchUsersResult;

import java.util.ArrayList;
import java.util.List;

/**
 * The select members presenter for the multi live to fetch list of users with paging and to start
 * a
 * broadcast.
 * <p>
 * It implements
 * MultiLiveSelectMembersContract.Presenter{@link MultiLiveSelectMembersContract.Presenter}
 *
 * @see MultiLiveSelectMembersContract.Presenter
 */
public class MultiLiveSelectMembersPresenter implements MultiLiveSelectMembersContract.Presenter {

    /**
     * Instantiates a new Select members presenter.
     */
    MultiLiveSelectMembersPresenter(MultiLiveSelectMembersContract.View selectMembersView) {
        this.selectMembersView = selectMembersView;
    }

    private final MultiLiveSelectMembersContract.View selectMembersView;

    private boolean isLastPage;
    private boolean isLoading;
    private int count, offset;
    private final Isometrik isometrik = IsometrikStreamSdk.getInstance().getIsometrik();
    private boolean isSearchRequest;
    private String searchTag;
    private final int PAGE_SIZE = Constants.USERS_PAGE_SIZE;

    /**
     * {@link MultiLiveSelectMembersContract.Presenter#requestUsersData(int, boolean, boolean,
     * String)}
     */
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

                        ArrayList<MultiLiveSelectMembersModel> usersModels = new ArrayList<>();

                        ArrayList<FetchUsersResult.User> users = var1.getUsers();
                        int size = users.size();
                        if (size < PAGE_SIZE) {

                            isLastPage = true;
                        }
                        String userId = IsometrikStreamSdk.getInstance().getUserSession().getUserId();

                        for (int i = 0; i < size; i++) {
                            if (users.get(i).getUserId().equals(userId)) {

                                count = 1;
                            } else {

                                usersModels.add(new MultiLiveSelectMembersModel(users.get(i)));
                            }
                        }

                        selectMembersView.onUsersDataReceived(usersModels, refreshRequest, isSearchRequest);
                    } else {
                        if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {

                            if (refreshRequest) {
                                //No users found
                                selectMembersView.onUsersDataReceived(new ArrayList<>(), true, isSearchRequest);
                            } else {
                                isLastPage = true;
                            }
                        } else {
                            selectMembersView.onError(var2.getErrorMessage());
                        }
                    }
                    isLoading = false;
                });
    }

    /**
     * {@link MultiLiveSelectMembersContract.Presenter#requestUsersDataOnScroll(int, int, int)}
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
     * {@link MultiLiveSelectMembersContract.Presenter#startBroadcast(String, String, ArrayList,
     * boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, ArrayList, boolean, boolean, boolean)}
     */
    @Override
    public void startBroadcast(String streamDescription, String streamImageUrl,
                               ArrayList<MultiLiveSelectMembersModel> members, boolean isPublic, boolean audioOnly,
                               boolean multiLive, boolean enableRecording, boolean hdBroadcast, boolean lowLatencyMode,
                               boolean restream, boolean productsLinked, ArrayList<String> productIds, boolean selfHosted, boolean rtmpIngest, boolean persistRtmpIngestEndpoint) {

        UserSession userSession = IsometrikStreamSdk.getInstance().getUserSession();

        ArrayList<String> memberIds = new ArrayList<>();
        int size = members.size();
        for (int i = 0; i < size; i++) {

            memberIds.add(members.get(i).getUserId());
        }
        List<String> searchableTags = new ArrayList<>();
        searchableTags.add(streamDescription);
        searchableTags.add(IsometrikStreamSdk.getInstance().getUserSession().getUserName());

        isometrik.getRemoteUseCases()
                .getStreamsUseCases()
                .startStream(new StartStreamQuery.Builder().setStreamDescription(streamDescription)
                        .setUserId(IsometrikStreamSdk.getInstance().getUserSession().getUserId())
                        .setStreamImage(streamImageUrl)
                        .setMembers(memberIds)
                        .setPublic(isPublic)
                        .setMultiLive(multiLive)
                        .setAudioOnly(audioOnly)
                        .setEnableRecording(enableRecording)
                        .setHdBroadcast(hdBroadcast)
                        .setLowLatencyMode(lowLatencyMode)
                        .setRestream(restream)
                        .setProductsLinked(productsLinked)
                        .setProductIds(productIds)
                        .setSelfHosted(selfHosted)
                        .setSearchableTags(searchableTags)
                        .setRtmpIngest(rtmpIngest)
                        .setPersistRtmpIngestEndpoint(persistRtmpIngestEndpoint)
                        .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                        .build(), (var1, var2) -> {

                    if (var1 != null) {
                        String rtmpIngestUrl = null;
                        if (selfHosted && rtmpIngest) {
                            rtmpIngestUrl = var1.getIngestEndpoint() + "/" + var1.getStreamKey();
                        }

                        selectMembersView.onBroadcastStarted(var1.getStreamId(), streamDescription,
                                streamImageUrl, memberIds, var1.getStartTime(), userSession.getUserId(),
                                var1.getIngestEndpoint(), var1.getStreamKey(), hdBroadcast, restream,
                                var1.getRtcToken(), var1.getRestreamEndpoints(), productsLinked, productIds.size(), rtmpIngestUrl);
                    }
                    //else if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 5) {
                    //  selectMembersView.noRestreamChannelsFound();
                    //}
                    else {

                        selectMembersView.onError(var2.getErrorMessage());
                    }
                });
    }
}
