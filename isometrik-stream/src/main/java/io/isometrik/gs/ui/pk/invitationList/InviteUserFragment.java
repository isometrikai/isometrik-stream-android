package io.isometrik.gs.ui.pk.invitationList;


import static io.isometrik.gs.ui.utils.Constants.USERS_PAGE_SIZE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmBottomsheetInviteBroadcasterBinding;
import io.isometrik.gs.ui.pk.invitesent.PkInviteActionCallbacks;
import io.isometrik.gs.ui.utils.AlertProgress;
import io.isometrik.gs.ui.utils.Constants;

public class InviteUserFragment extends BottomSheetDialogFragment
        implements InviteUserContract.View, InviteUserAdapter.InviteClickListener {

    public static final String TAG = "InviteUserBottomSheetFragment";

    private InviteUserContract.Presenter inviteUserPresenter;

    private AlertProgress alertProgress;
    private AlertDialog alertDialog;

    private ArrayList<InviteUserModel> online;
    private ArrayList<InviteUserModel> invite;
    private InviteUserAdapter broadCasterAdapter, inviteUserAdapter;
    private ArrayList<String> memberIds;
    private InviteUserCallback callback;

    private String streamId;
    private Activity activity;

    private int selectedTabPosition;
    private LinearLayoutManager layoutManager;
    private boolean alreadyAddedTabs;

    private Drawable noViewers;
    private Drawable noUsers;
    private IsmBottomsheetInviteBroadcasterBinding binding;
    private boolean isInitial = true;
    private PkInviteActionCallbacks pkInviteActionCallbacks;



    public InviteUserFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (binding == null) {
            binding =
                    IsmBottomsheetInviteBroadcasterBinding.inflate(inflater, container, false);
        } else {

            if (binding.getRoot().getParent() != null) {
                ((ViewGroup) binding.getRoot().getParent()).removeView(
                        binding.getRoot());
            }
        }

        noViewers = ContextCompat.getDrawable(activity, R.drawable.ism_ic_no_viewers);
        noUsers = ContextCompat.getDrawable(activity, R.drawable.ism_ic_no_users);

        if (!alreadyAddedTabs) {
            alreadyAddedTabs = true;
            binding.tabLayoutUsers.addTab(
                    binding.tabLayoutUsers.newTab()
                            .setText(getString(R.string.online_list)));
        }


        binding.tvNoUsers.setVisibility(View.GONE);
        alertProgress = new AlertProgress();

        layoutManager = new LinearLayoutManager(activity);
        binding.rvUsers.setLayoutManager(layoutManager);

        online = new ArrayList<>();
        invite = new ArrayList<>();

        broadCasterAdapter = new InviteUserAdapter(activity, online, this, true, this);
        inviteUserAdapter = new InviteUserAdapter(activity, invite, this, false, this);
        binding.rvUsers.setAdapter(broadCasterAdapter);
        binding.rvUsers.addOnScrollListener(recyclerViewOnScrollListener);
        inviteUserPresenter.initialize(streamId, memberIds);

        fetchOnlineBroadcasters("");
//        inviteUserPresenter.registerStreamViewersEventListener();
//        inviteUserPresenter.registerCopublishRequestsEventListener();
        binding.refresh.setOnRefreshListener(() -> {
            if (selectedTabPosition == 0) {
                fetchOnlineBroadcasters(binding.etSearch.getText() != null ? binding.etSearch.getText().toString() : "");
            } else {
                fetchInviteUsers();
            }
        });

        //To allow scroll on user's recyclerview
        binding.rvUsers.setOnTouchListener((v, event) -> {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            v.onTouchEvent(event);
            return true;
        });

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    isInitial = false;
                }

                if (!isInitial) {
                    fetchOnlineBroadcasters(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.swRefuseAll.setOnCheckedChangeListener((buttonView, isChecked) -> {

        });

        return binding.getRoot();
    }

    private void fetchOnlineBroadcasters(String q) {
        updateShimmerVisibility(true);
        String Search = null;
        if (!q.isEmpty()) {
            Search = q;
        }
        try {
            inviteUserPresenter.fetchOnlineBroadcasters(streamId, Search, 0, USERS_PAGE_SIZE, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchInviteUsers() {
        updateShimmerVisibility(true);

        try {
            inviteUserPresenter.fetchInviteUsersData(streamId, "", 0, Constants.USERS_PAGE_SIZE, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        inviteUserPresenter = new InviteUserPresenter();
        inviteUserPresenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        inviteUserPresenter.detachView();
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

                    if (selectedTabPosition == 0) {

                        inviteUserPresenter.requestOnlineBroadcastersOnScroll(streamId, binding.etSearch.getText() != null ? binding.etSearch.getText().toString() : "",
                                layoutManager.findFirstVisibleItemPosition(), layoutManager.getChildCount(),
                                layoutManager.getItemCount());
                    } else {
                        inviteUserPresenter.requestInviteUserOnScroll(streamId, binding.etSearch.getText() != null ? binding.etSearch.getText().toString() : "",
                                layoutManager.findFirstVisibleItemPosition(), layoutManager.getChildCount(),
                                layoutManager.getItemCount());
                    }
                }
            };

    @Override
    public void onInviteUsersDataReceived(ArrayList<InviteUserModel> users, boolean latestUsers) {
        activity.runOnUiThread(() -> {
            if (latestUsers) {
                this.invite.clear();
            }
            this.invite.addAll(users);
            if (selectedTabPosition == 1) {
                if (invite.size() > 0) {
                    binding.tvNoUsers.setVisibility(View.GONE);
                    binding.rvUsers.setVisibility(View.VISIBLE);
//                    broadCasterAdapter.updateData(this.online);
                    inviteUserAdapter.notifyDataSetChanged();
                } else {
                    binding.tvNoUsers.setCompoundDrawablesWithIntrinsicBounds(null,
                            noUsers, null, null);
                    binding.tvNoUsers.setText(
                            getString(R.string.ism_no_users_add_member));
                    binding.tvNoUsers.setVisibility(View.VISIBLE);
                    binding.rvUsers.setVisibility(View.GONE);
                }
            }
        });

        updateShimmerVisibility(false);

        if (binding.refresh.isRefreshing()) {
            binding.refresh.setRefreshing(false);
        }
    }

    @Override
    public void onBroadcastersDataReceived(ArrayList<InviteUserModel> users, boolean latestUsers) {
        activity.runOnUiThread(() -> {
            if (latestUsers) {
                this.online.clear();
            }
            this.online.addAll(users);
            if (selectedTabPosition == 0) {
                if (online.size() > 0) {
                    binding.tvNoUsers.setVisibility(View.GONE);
                    binding.rvUsers.setVisibility(View.VISIBLE);
//                    broadCasterAdapter.updateData(this.online);
                    broadCasterAdapter.notifyDataSetChanged();
                } else {
                    binding.tvNoUsers.setCompoundDrawablesWithIntrinsicBounds(null,
                            noUsers, null, null);
                    binding.tvNoUsers.setText(
                            getString(R.string.ism_no_users_add_member));
                    binding.tvNoUsers.setVisibility(View.VISIBLE);
                    binding.rvUsers.setVisibility(View.GONE);
                }
            }
        });

        updateShimmerVisibility(false);

        if (binding.refresh.isRefreshing()) {
            binding.refresh.setRefreshing(false);
        }
    }

    @Override
    public void onError(String errorMessage) {
        hideProgressDialog();
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (errorMessage != null) {
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.ism_failed), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onInviteSent(boolean isSuccess, InviteUserModel inviteUserModel, int position) {
        if (isSuccess) {
//            this.online.remove(inviteUserModel);
            broadCasterAdapter.updateItem(isSuccess, position);
            pkInviteActionCallbacks.onPkInviteSent(inviteUserModel);
        }
    }

    private void showProgressDialog(String message) {

        if (getActivity() != null) {
            alertDialog = alertProgress.getProgressDialog(getActivity(), message);
            if (!getActivity().isFinishing()) alertDialog.show();
        }
    }

    private void hideProgressDialog() {

        if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
    }

    public void updateParameters(String streamId, ArrayList<String> memberIds, PkInviteActionCallbacks pkInviteActionCallbacks) {

        this.streamId = streamId;
        this.memberIds = memberIds;
        this.pkInviteActionCallbacks = pkInviteActionCallbacks;
    }

    private void updateShimmerVisibility(boolean visible) {
        if (visible) {
            binding.shimmerFrameLayout.startShimmer();
            binding.shimmerFrameLayout.setVisibility(View.VISIBLE);
        } else {
            if (binding.shimmerFrameLayout.getVisibility() == View.VISIBLE) {
                binding.shimmerFrameLayout.setVisibility(View.GONE);
                binding.shimmerFrameLayout.stopShimmer();
            }
        }
    }

    @Override
    public void inviteButtonClick(InviteUserModel inviteUserModel, int position) {
        inviteUserPresenter.sendInvitationToUserForPK(inviteUserModel, streamId, position);
    }

}
