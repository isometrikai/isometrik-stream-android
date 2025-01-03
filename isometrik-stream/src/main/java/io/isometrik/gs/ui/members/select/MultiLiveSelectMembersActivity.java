package io.isometrik.gs.ui.members.select;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmActivityMultiliveSelectMemberBinding;
import io.isometrik.gs.ui.restream.RestreamChannelsActionCallback;
import io.isometrik.gs.ui.restream.add.AddOrEditChannelsFragment;
import io.isometrik.gs.ui.restream.list.FetchChannelsFragment;
import io.isometrik.gs.ui.scrollable.webrtc.MultiliveStreamsActivity;
import io.isometrik.gs.ui.utils.AlertProgress;
import io.isometrik.gs.ui.utils.Constants;
import io.isometrik.gs.ui.utils.RecyclerItemClickListener;

import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;

/**
 * The type Select members activity for the multi live.
 * It implements MultiLiveSelectMembersContract.View{@link MultiLiveSelectMembersContract.View}
 *
 * @see MultiLiveSelectMembersContract.View
 */
public class MultiLiveSelectMembersActivity extends AppCompatActivity
        implements MultiLiveSelectMembersContract.View, RestreamChannelsActionCallback {

    private MultiLiveSelectMembersContract.Presenter multiLiveSelectMembersPresenter;

    private AlertProgress alertProgress;
    private AlertDialog alertDialog;

    private final ArrayList<MultiLiveSelectMembersModel> users = new ArrayList<>();
    private final ArrayList<MultiLiveSelectMembersModel> selectedUsersModels = new ArrayList<>();

    private MultiLiveUsersAdapter usersAdapter;
    private MultiLiveSelectedUsersAdapter multiLiveSelectedUsersAdapter;

    private LinearLayoutManager layoutManager;
    private LinearLayoutManager selectedUsersLayoutManager;

    private boolean cleanUpRequested = false;
    private int count;
    private String streamImage, streamDescription;
    private boolean isPublic;
    private boolean audioOnly;
    private static final int MAXIMUM_MEMBERS = Constants.MAXIMUM_ALLOWED_MEMBERS;

    private FetchChannelsFragment fetchChannelsFragment;
    private AddOrEditChannelsFragment addOrEditChannelsFragment;

    private IsmActivityMultiliveSelectMemberBinding ismActivityMultiliveSelectMemberBinding;
    private boolean selectedUsersStateNeedToBeSaved = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ismActivityMultiliveSelectMemberBinding =
                IsmActivityMultiliveSelectMemberBinding.inflate(getLayoutInflater());
        View view = ismActivityMultiliveSelectMemberBinding.getRoot();
        setContentView(view);

        multiLiveSelectMembersPresenter = new MultiLiveSelectMembersPresenter(this);
        alertProgress = new AlertProgress();
        streamImage = getIntent().getStringExtra("streamImage");
        streamDescription = getIntent().getStringExtra("streamDescription");
        isPublic = getIntent().getBooleanExtra("isPublic", true);
        audioOnly = getIntent().getBooleanExtra("audioOnly", false);

        layoutManager = new LinearLayoutManager(this);
        ismActivityMultiliveSelectMemberBinding.rvUsers.setLayoutManager(layoutManager);
        usersAdapter = new MultiLiveUsersAdapter(this, users);
        ismActivityMultiliveSelectMemberBinding.rvUsers.addOnScrollListener(
                recyclerViewOnScrollListener);
        ismActivityMultiliveSelectMemberBinding.rvUsers.setAdapter(usersAdapter);

        selectedUsersLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        ismActivityMultiliveSelectMemberBinding.rvUsersSelected.setLayoutManager(
                selectedUsersLayoutManager);
        multiLiveSelectedUsersAdapter = new MultiLiveSelectedUsersAdapter(this, selectedUsersModels);
        ismActivityMultiliveSelectMemberBinding.rvUsersSelected.setAdapter(
                multiLiveSelectedUsersAdapter);

        fetchLatestUsers(false, null, false);

        ismActivityMultiliveSelectMemberBinding.rvUsers.addOnItemTouchListener(
                new RecyclerItemClickListener(this, ismActivityMultiliveSelectMemberBinding.rvUsers,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                if (position >= 0) {

                                    MultiLiveSelectMembersModel user = users.get(position);

                                    if (user.isSelected()) {
                                        user.setSelected(false);
                                        count--;
                                        removeSelectedUser(user.getUserId());
                                    } else {

                                        if (count < MAXIMUM_MEMBERS) {
                                            //Maximum 5 members can be added
                                            user.setSelected(true);
                                            count++;
                                            addSelectedUser(user);
                                        } else {
                                            Toast.makeText(MultiLiveSelectMembersActivity.this,
                                                    getString(R.string.ism_max_members_selected), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    updateSelectedMembersText();
                                    users.set(position, user);
                                    usersAdapter.notifyItemChanged(position);
                                }
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                            }
                        }));

        ismActivityMultiliveSelectMemberBinding.refresh.setOnRefreshListener(
                () -> fetchLatestUsers(false, null, true));

        fetchChannelsFragment = new FetchChannelsFragment();
        addOrEditChannelsFragment = new AddOrEditChannelsFragment();

        ismActivityMultiliveSelectMemberBinding.ibBack.setOnClickListener(view1 -> onBackPressed());

        ismActivityMultiliveSelectMemberBinding.ivNext.setOnClickListener(view12 -> {
            showProgressDialog(getString(R.string.ism_starting_broadcast));

            multiLiveSelectMembersPresenter.startBroadcast(streamDescription, streamImage,
                    selectedUsersModels, isPublic, audioOnly, true,
                    getIntent().getBooleanExtra("enableRecording", false),
                    getIntent().getBooleanExtra("hdBroadcast", true), true,
                    getIntent().getBooleanExtra("restream", false),
                    getIntent().getBooleanExtra("productsLinked", false),
                    getIntent().getStringArrayListExtra("productIds"),
                    getIntent().getBooleanExtra("selfHosted", false),
                    getIntent().getBooleanExtra("rtmpIngest", false),
                    getIntent().getBooleanExtra("persistRtmpIngestEndpoint", false)

            );
        });
        ismActivityMultiliveSelectMemberBinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    fetchLatestUsers(true, s.toString(), false);
                } else {
                    if (selectedUsersModels.size() > 0) {
                        selectedUsersStateNeedToBeSaved = true;
                    }
                    fetchLatestUsers(false, null, false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * The Recycler view on scroll listener.
     */
    public RecyclerView.OnScrollListener recyclerViewOnScrollListener =
            new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    multiLiveSelectMembersPresenter.requestUsersDataOnScroll(
                            layoutManager.findFirstVisibleItemPosition(), layoutManager.getChildCount(),
                            layoutManager.getItemCount());
                }
            };

    /**
     * {@link MultiLiveSelectMembersContract.View#onUsersDataReceived(ArrayList, boolean, boolean)}
     */
    @Override
    public void onUsersDataReceived(ArrayList<MultiLiveSelectMembersModel> userModels,
                                    boolean latestUsers, boolean isSearchRequest) {

        if (latestUsers) {
            users.clear();
            if (isSearchRequest || selectedUsersStateNeedToBeSaved) {
                int size = userModels.size();
                MultiLiveSelectMembersModel multiLiveSelectMembersModel;

                for (int i = 0; i < size; i++) {

                    for (int j = 0; j < selectedUsersModels.size(); j++) {
                        if (selectedUsersModels.get(j).getUserId().equals(userModels.get(i).getUserId())) {
                            multiLiveSelectMembersModel = userModels.get(i);
                            multiLiveSelectMembersModel.setSelected(true);
                            userModels.set(i, multiLiveSelectMembersModel);
                            break;
                        }
                    }
                }
                if (!isSearchRequest) selectedUsersStateNeedToBeSaved = false;
            } else {

                runOnUiThread(() -> {
                    selectedUsersModels.clear();
                    count = 0;
                    multiLiveSelectedUsersAdapter.notifyDataSetChanged();
                    updateSelectedMembersText();
                });
            }
        } else {

            int size = userModels.size();
            MultiLiveSelectMembersModel multiLiveSelectMembersModel;

            for (int i = 0; i < size; i++) {

                for (int j = 0; j < selectedUsersModels.size(); j++) {
                    if (selectedUsersModels.get(j).getUserId().equals(userModels.get(i).getUserId())) {
                        multiLiveSelectMembersModel = userModels.get(i);
                        multiLiveSelectMembersModel.setSelected(true);
                        userModels.set(i, multiLiveSelectMembersModel);
                        break;
                    }
                }
            }
        }
        users.addAll(userModels);

        runOnUiThread(() -> {
            if (latestUsers) {
                if (users.size() > 0) {
                    ismActivityMultiliveSelectMemberBinding.tvNoUsers.setVisibility(View.GONE);
                    ismActivityMultiliveSelectMemberBinding.rvUsers.setVisibility(View.VISIBLE);
                } else {
                    ismActivityMultiliveSelectMemberBinding.tvNoUsers.setVisibility(View.VISIBLE);
                    ismActivityMultiliveSelectMemberBinding.rvUsers.setVisibility(View.GONE);
                }
            }
            usersAdapter.notifyDataSetChanged();

            hideProgressDialog();
            if (ismActivityMultiliveSelectMemberBinding.refresh.isRefreshing()) {
                ismActivityMultiliveSelectMemberBinding.refresh.setRefreshing(false);
            }
        });
    }

    /**
     * {@link MultiLiveSelectMembersContract.View#onError(String)}
     */
    @Override
    public void onError(String errorMessage) {
        if (ismActivityMultiliveSelectMemberBinding.refresh.isRefreshing()) {
            ismActivityMultiliveSelectMemberBinding.refresh.setRefreshing(false);
        }

        hideProgressDialog();

        runOnUiThread(() -> {
            if (errorMessage != null) {
                Toast.makeText(MultiLiveSelectMembersActivity.this, errorMessage, Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(MultiLiveSelectMembersActivity.this, getString(R.string.ism_error),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Remove user.
     *
     * @param userId the user id
     */
    public void removeUser(String userId) {
        int size = users.size();
        for (int i = 0; i < size; i++) {

            if (users.get(i).getUserId().equals(userId)) {

                MultiLiveSelectMembersModel selectMembersModel = users.get(i);
                selectMembersModel.setSelected(false);
                users.set(i, selectMembersModel);
                if (i == 0) {
                    usersAdapter.notifyDataSetChanged();
                } else {
                    usersAdapter.notifyItemChanged(i);
                }
                count--;
                updateSelectedMembersText();
                break;
            }
        }

        for (int i = 0; i < selectedUsersModels.size(); i++) {

            if (selectedUsersModels.get(i).getUserId().equals(userId)) {
                selectedUsersModels.remove(i);
                if (i == 0) {
                    multiLiveSelectedUsersAdapter.notifyDataSetChanged();
                } else {
                    multiLiveSelectedUsersAdapter.notifyItemRemoved(i);
                }

                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        cleanupOnActivityDestroy();
        try {
            super.onBackPressed();
        } catch (Exception ignore) {
        }
    }

    @Override
    protected void onDestroy() {
        cleanupOnActivityDestroy();
        super.onDestroy();
    }

    /**
     * {@link MultiLiveSelectMembersContract.View#onBroadcastStarted(String, String, String,
     * ArrayList, long,
     * String, String, String, boolean, boolean, String, ArrayList, boolean, int, String)}
     */
    @Override
    public void onBroadcastStarted(String streamId, String streamDescription, String streamImageUrl,
                                   ArrayList<String> memberIds, long startTime, String userId, String ingestEndpoint,
                                   String streamKey, boolean hdBroadcast, boolean restream, String rtcToken,
                                   ArrayList<String> restreamEndpoints, boolean productsLinked, int productsCount, String rtmpIngestUrl) {
        hideProgressDialog();
        memberIds.add(userId);
        Intent intent;
        intent = new Intent(MultiLiveSelectMembersActivity.this, MultiliveStreamsActivity.class);


        intent.putExtra("streamId", streamId);
        intent.putExtra("streamDescription", streamDescription);
        intent.putExtra("streamImage", streamImageUrl);
        intent.putExtra("startTime", startTime);
        intent.putExtra("membersCount", memberIds.size());
        intent.putExtra("viewersCount", 0);
        intent.putExtra("publishersCount", 1);
        intent.putExtra("joinRequest", true);
        intent.putExtra("isAdmin", true);
        intent.putStringArrayListExtra("memberIds", memberIds);
        intent.putExtra("initiatorName", IsometrikStreamSdk.getInstance().getUserSession().getUserName());
        intent.putExtra("publishRequired", false);
        intent.putExtra("initiatorId", IsometrikStreamSdk.getInstance().getUserSession().getUserId());

        intent.putExtra("initiatorIdentifier",
                IsometrikStreamSdk.getInstance().getUserSession().getUserIdentifier());
        intent.putExtra("initiatorImage",
                IsometrikStreamSdk.getInstance().getUserSession().getUserProfilePic());
        intent.putExtra("isPublic", isPublic);
        intent.putExtra("audioOnly", audioOnly);
        intent.putExtra("isBroadcaster", true);
        intent.putExtra("ingestEndpoint", ingestEndpoint);
        intent.putExtra("streamKey", streamKey);
        intent.putExtra("multiLive", true);
        intent.putExtra("hdBroadcast", hdBroadcast);
        intent.putExtra("rtcToken", rtcToken);
        intent.putExtra("restream", restream);
        intent.putStringArrayListExtra("restreamEndpoints", restreamEndpoints);
        intent.putExtra("productsLinked", productsLinked);
        intent.putExtra("productsCount", productsCount);
        intent.putExtra("isModerator", true);
        intent.putExtra("selfHosted", getIntent().getBooleanExtra("selfHosted", false));
        intent.putExtra("rtmpIngest", getIntent().getBooleanExtra("rtmpIngest", false));
        intent.putExtra("persistRtmpIngestEndpoint", getIntent().getBooleanExtra("persistRtmpIngestEndpoint", false));
        intent.putExtra("rtmpIngestUrl", rtmpIngestUrl);
        startActivity(intent);
        finish();
    }

    private void fetchLatestUsers(boolean isSearchRequest, String searchTag,
                                  boolean showProgressDialog) {

        if (showProgressDialog) showProgressDialog(getString(R.string.ism_fetching_users));

        try {
            multiLiveSelectMembersPresenter.requestUsersData(0, true, isSearchRequest, searchTag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeSelectedUser(String userId) {

        for (int i = 0; i < selectedUsersModels.size(); i++) {
            if (selectedUsersModels.get(i).getUserId().equals(userId)) {
                selectedUsersModels.remove(i);
                if (i == 0) {
                    multiLiveSelectedUsersAdapter.notifyDataSetChanged();
                } else {
                    multiLiveSelectedUsersAdapter.notifyItemRemoved(i);
                }
                break;
            }
        }
    }

    private void addSelectedUser(MultiLiveSelectMembersModel selectMembersModel) {
        selectedUsersModels.add(selectMembersModel);
        try {
            multiLiveSelectedUsersAdapter.notifyDataSetChanged();
            selectedUsersLayoutManager.scrollToPosition(selectedUsersModels.size() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateSelectedMembersText() {

        if (count > 0) {
            ismActivityMultiliveSelectMemberBinding.tvMembersCount.setText(
                    getString(R.string.ism_multilive_members_selected, String.valueOf(count),
                            String.valueOf(MAXIMUM_MEMBERS)));
        } else {
            ismActivityMultiliveSelectMemberBinding.tvMembersCount.setText(
                    getString(R.string.ism_add_members));
        }
    }

    private void showProgressDialog(String message) {

        alertDialog = alertProgress.getProgressDialog(this, message);
        if (!isFinishing() && !isDestroyed()) alertDialog.show();
    }

    private void hideProgressDialog() {

        if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
    }

    private void cleanupOnActivityDestroy() {
        if (!cleanUpRequested) {
            cleanUpRequested = true;
            hideProgressDialog();
        }
    }

    private void dismissAllDialogs() {

        if (!isFinishing() && !isDestroyed()) {
            try {
                if (fetchChannelsFragment.getDialog() != null && fetchChannelsFragment.getDialog()
                        .isShowing() && !fetchChannelsFragment.isRemoving()) {
                    fetchChannelsFragment.dismiss();
                } else if (addOrEditChannelsFragment.getDialog() != null
                        && addOrEditChannelsFragment.getDialog().isShowing()
                        && !addOrEditChannelsFragment.isRemoving()) {
                    addOrEditChannelsFragment.dismiss();
                }
            } catch (Exception ignore) {
            }
        }
    }

    @Override
    public void editChannel(String channelId, String channelName, String ingestUrl, boolean enabled,
                            int channelType) {

        addOrEditChannelsFragment = new AddOrEditChannelsFragment();
        dismissAllDialogs();
        addOrEditChannelsFragment.updateParams(false, channelId, channelName, ingestUrl, enabled,
                channelType);
        addOrEditChannelsFragment.show(getSupportFragmentManager(), AddOrEditChannelsFragment.TAG);
    }

    @Override
    public void addChannel() {
        //Added due to fragment ui was not getting refreshed
        addOrEditChannelsFragment = new AddOrEditChannelsFragment();
        dismissAllDialogs();
        addOrEditChannelsFragment.updateParams(true, null, null, null, false, -1);
        addOrEditChannelsFragment.show(getSupportFragmentManager(), AddOrEditChannelsFragment.TAG);
    }

    @Override
    public void noRestreamChannelsFound() {
        hideProgressDialog();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(getString(R.string.ism_add_restream_channels));
        alertDialog.setPositiveButton(getString(R.string.ism_add_update), (dialogInterface, i) -> {
            dismissAllDialogs();
            fetchChannelsFragment.updateParams(MultiLiveSelectMembersActivity.this);
            fetchChannelsFragment.show(getSupportFragmentManager(), FetchChannelsFragment.TAG);
        });
        alertDialog.setCancelable(true);
        if (!isFinishing() && !isDestroyed()) {
            alertDialog.show();
        }
    }
}
