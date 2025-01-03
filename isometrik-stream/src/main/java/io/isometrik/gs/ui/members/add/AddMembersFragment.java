package io.isometrik.gs.ui.members.add;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmBottomsheetAddMembersBinding;
import io.isometrik.gs.ui.utils.AlertProgress;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * Bottomsheet dialog fragment to show list of viewers and users which can be added to a
 * broadcast.Implements interface
 * MembersContract view{@link AddMembersContract.View}
 *
 * @see AddMembersContract.View
 */
public class AddMembersFragment extends BottomSheetDialogFragment
    implements AddMembersContract.View {

  public static final String TAG = "AddMembersBottomSheetFragment";

  private AddMembersContract.Presenter addMembersPresenter;

  private AlertProgress alertProgress;
  private AlertDialog alertDialog;

  private ArrayList<AddMembersModel> users;
  private ArrayList<AddMembersModel> viewers;
  private AddMembersAdapter addMembersAdapter;
  private ArrayList<String> memberIds;

  private String streamId;
  private Activity activity;

  private int selectedTabPosition;
  private LinearLayoutManager layoutManager;
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

            addMembersPresenter.requestEligibleUsersDataOnScroll(
                layoutManager.findFirstVisibleItemPosition(), layoutManager.getChildCount(),
                layoutManager.getItemCount());
          } else {
            addMembersPresenter.requestViewersDataOnScroll(
                layoutManager.findFirstVisibleItemPosition(), layoutManager.getChildCount(),
                layoutManager.getItemCount());
          }
        }
      };
  private boolean alreadyAddedTabs;
  private Drawable noViewers;
  private Drawable noUsers;
  private IsmBottomsheetAddMembersBinding ismBottomsheetAddMembersBinding;

  public AddMembersFragment() {
    // Required empty public constructor
  }

  @SuppressLint("ClickableViewAccessibility")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (ismBottomsheetAddMembersBinding == null) {
      ismBottomsheetAddMembersBinding =
          IsmBottomsheetAddMembersBinding.inflate(inflater, container, false);
    } else {

      if (ismBottomsheetAddMembersBinding.getRoot().getParent() != null) {
        ((ViewGroup) ismBottomsheetAddMembersBinding.getRoot().getParent()).removeView(
            ismBottomsheetAddMembersBinding.getRoot());
      }
    }
    noViewers = ContextCompat.getDrawable(activity, R.drawable.ism_ic_no_viewers);
    noUsers = ContextCompat.getDrawable(activity, R.drawable.ism_ic_no_users);
    updateShimmerVisibility(true);
    if (!alreadyAddedTabs) {
      alreadyAddedTabs = true;
      ismBottomsheetAddMembersBinding.tabLayoutUsers.addTab(
          ismBottomsheetAddMembersBinding.tabLayoutUsers.newTab()
              .setText(getString(R.string.ism_users))
              .setIcon(R.drawable.ism_ic_members));
      ismBottomsheetAddMembersBinding.tabLayoutUsers.addTab(
          ismBottomsheetAddMembersBinding.tabLayoutUsers.newTab()
              .setText(getString(R.string.ism_stream_viewers))
              .setIcon(R.drawable.ism_ic_viewers));
    }

    ismBottomsheetAddMembersBinding.tabLayoutUsers.addOnTabSelectedListener(
        new TabLayout.OnTabSelectedListener() {
          @Override
          public void onTabSelected(TabLayout.Tab tab) {
            selectedTabPosition =
                ismBottomsheetAddMembersBinding.tabLayoutUsers.getSelectedTabPosition();
            if (tab.getIcon() != null) {
              tab.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            }
            switch (selectedTabPosition) {

              case 0: {
                //Users selected
                addMembersAdapter = new AddMembersAdapter(activity, users, AddMembersFragment.this);
                ismBottomsheetAddMembersBinding.rvUsers.setAdapter(addMembersAdapter);
                addMembersAdapter.notifyDataSetChanged();
                if (users.size() > 0) {
                  ismBottomsheetAddMembersBinding.tvNoUsers.setVisibility(View.GONE);
                  ismBottomsheetAddMembersBinding.rvUsers.setVisibility(View.VISIBLE);
                } else {
                  ismBottomsheetAddMembersBinding.tvNoUsers.setCompoundDrawablesWithIntrinsicBounds(
                      null, noUsers, null, null);
                  ismBottomsheetAddMembersBinding.tvNoUsers.setText(
                      getString(R.string.ism_no_users_add_member));
                  ismBottomsheetAddMembersBinding.tvNoUsers.setVisibility(View.VISIBLE);
                  ismBottomsheetAddMembersBinding.rvUsers.setVisibility(View.GONE);
                }
                ismBottomsheetAddMembersBinding.etSearch.setHint(
                    getString(R.string.ism_search_users_hint));
                break;
              }
              case 1: {
                //Viewers selected
                addMembersAdapter =
                    new AddMembersAdapter(activity, viewers, AddMembersFragment.this);
                ismBottomsheetAddMembersBinding.rvUsers.setAdapter(addMembersAdapter);
                addMembersAdapter.notifyDataSetChanged();
                if (viewers.size() > 0) {
                  ismBottomsheetAddMembersBinding.tvNoUsers.setVisibility(View.GONE);
                  ismBottomsheetAddMembersBinding.rvUsers.setVisibility(View.VISIBLE);
                } else {
                  ismBottomsheetAddMembersBinding.tvNoUsers.setCompoundDrawablesWithIntrinsicBounds(
                      null, noViewers, null, null);
                  ismBottomsheetAddMembersBinding.tvNoUsers.setText(
                      getString(R.string.ism_no_viewers_add_member));
                  ismBottomsheetAddMembersBinding.tvNoUsers.setVisibility(View.VISIBLE);
                  ismBottomsheetAddMembersBinding.rvUsers.setVisibility(View.GONE);
                }
                ismBottomsheetAddMembersBinding.etSearch.setHint(
                    getString(R.string.ism_search_viewers_hint));
                break;
              }
            }
          }

          @Override
          public void onTabUnselected(TabLayout.Tab tab) {
            if (tab.getIcon() != null) {
              tab.getIcon()
                  .setColorFilter(ContextCompat.getColor(activity, R.color.ism_grey),
                      PorterDuff.Mode.SRC_IN);
            }
          }

          @Override
          public void onTabReselected(TabLayout.Tab tab) {
          }
        });

    try {
      ismBottomsheetAddMembersBinding.tabLayoutUsers.getTabAt(0)
          .getIcon()
          .setColorFilter(ContextCompat.getColor(activity, R.color.ism_white),
              PorterDuff.Mode.SRC_IN);
    } catch (NullPointerException ignore) {
    }

    try {
      ismBottomsheetAddMembersBinding.tabLayoutUsers.getTabAt(0).select();
    } catch (NullPointerException ignore) {
    }

    ismBottomsheetAddMembersBinding.tvNoUsers.setVisibility(View.GONE);
    alertProgress = new AlertProgress();

    layoutManager = new LinearLayoutManager(activity);
    ismBottomsheetAddMembersBinding.rvUsers.setLayoutManager(layoutManager);

    users = new ArrayList<>();
    viewers = new ArrayList<>();

    addMembersAdapter = new AddMembersAdapter(activity, users, this);
    ismBottomsheetAddMembersBinding.rvUsers.setAdapter(addMembersAdapter);
    ismBottomsheetAddMembersBinding.rvUsers.addOnScrollListener(recyclerViewOnScrollListener);
    addMembersPresenter.initialize(streamId, memberIds);

    fetchLatestUsers(false, null);
    fetchLatestViewers(false, null);
    addMembersPresenter.registerStreamViewersEventListener();
    addMembersPresenter.registerCopublishRequestsEventListener();
    ismBottomsheetAddMembersBinding.refresh.setOnRefreshListener(() -> {
      if (selectedTabPosition == 0) {
        fetchLatestUsers(false, null);
      } else {
        fetchLatestViewers(false, null);
      }
    });
    ismBottomsheetAddMembersBinding.etSearch.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
          if (selectedTabPosition == 0) {
            fetchLatestUsers(true, s.toString());
          } else {
            fetchLatestViewers(true, s.toString());
          }
        } else {
          if (selectedTabPosition == 0) {
            fetchLatestUsers(false, null);
          } else {
            fetchLatestViewers(false, null);
          }
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });
    //To allow scroll on user's recyclerview
    ismBottomsheetAddMembersBinding.rvUsers.setOnTouchListener((v, event) -> {
      v.getParent().requestDisallowInterceptTouchEvent(true);
      v.onTouchEvent(event);
      return true;
    });

    return ismBottomsheetAddMembersBinding.getRoot();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    activity = getActivity();
    addMembersPresenter = new AddMembersPresenter();
    addMembersPresenter.attachView(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    addMembersPresenter.detachView();
    activity = null;
  }

  /**
   * {@link AddMembersContract.View#onError(String)}
   */
  @Override
  public void onError(String errorMessage) {
    if (ismBottomsheetAddMembersBinding.refresh.isRefreshing()) {
      ismBottomsheetAddMembersBinding.refresh.setRefreshing(false);
    }
    updateShimmerVisibility(false);
    hideProgressDialog();
    if (activity != null) {
      activity.runOnUiThread(() -> {
        if (errorMessage != null) {
          Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(activity, getString(R.string.ism_error), Toast.LENGTH_SHORT).show();
        }
      });
    }
  }

  /**
   * {@link AddMembersContract.View#onStreamOffline(String,
   * int)}
   */
  @Override
  public void onStreamOffline(String message, int dialogType) {
    updateShimmerVisibility(false);
    hideProgressDialog();
  }

  /**
   * @param streamId id of the stream group
   * @param memberIds list containing ids of the stream group members
   */
  public void updateParameters(String streamId, ArrayList<String> memberIds) {
    this.streamId = streamId;
    this.memberIds = memberIds;
  }

  /**
   * Add member.
   *
   * @param memberId the member id
   */
  public void addMember(String memberId) {
    showProgressDialog(getString(R.string.ism_adding_member));
    addMembersPresenter.addMember(memberId, streamId);
  }

  /**
   * {@link AddMembersContract.View#onEligibleUsersDataReceived(ArrayList, boolean)}
   */
  @Override
  public void onEligibleUsersDataReceived(ArrayList<AddMembersModel> userModels,
      boolean refreshRequest) {

    activity.runOnUiThread(() -> {
      if (refreshRequest) {
        users.clear();
      }
      users.addAll(userModels);
      if (selectedTabPosition == 0) {
        if (users.size() > 0) {
          ismBottomsheetAddMembersBinding.tvNoUsers.setVisibility(View.GONE);
          ismBottomsheetAddMembersBinding.rvUsers.setVisibility(View.VISIBLE);
          addMembersAdapter.notifyDataSetChanged();
        } else {
          ismBottomsheetAddMembersBinding.tvNoUsers.setCompoundDrawablesWithIntrinsicBounds(null,
              noUsers, null, null);
          ismBottomsheetAddMembersBinding.tvNoUsers.setText(
              getString(R.string.ism_no_users_add_member));
          ismBottomsheetAddMembersBinding.tvNoUsers.setVisibility(View.VISIBLE);
          ismBottomsheetAddMembersBinding.rvUsers.setVisibility(View.GONE);
        }
      }
    });

    updateShimmerVisibility(false);

    if (ismBottomsheetAddMembersBinding.refresh.isRefreshing()) {
      ismBottomsheetAddMembersBinding.refresh.setRefreshing(false);
    }
  }

  /**
   * {@link AddMembersContract.View#onMemberAdded(String)}
   */
  @Override
  public void onMemberAdded(String memberId) {

    activity.runOnUiThread(() -> {

      if (selectedTabPosition == 0) {
        int size = users.size();
        for (int i = 0; i < size; i++) {

          if (users.get(i).getUserId().equals(memberId)) {

            users.remove(i);
            addMembersAdapter.notifyItemRemoved(i);

            if (users.size() == 0) {

              ismBottomsheetAddMembersBinding.tvNoUsers.setCompoundDrawablesWithIntrinsicBounds(
                  null, noUsers, null, null);
              ismBottomsheetAddMembersBinding.tvNoUsers.setText(
                  getString(R.string.ism_no_users_add_member));
              ismBottomsheetAddMembersBinding.tvNoUsers.setVisibility(View.VISIBLE);
              ismBottomsheetAddMembersBinding.rvUsers.setVisibility(View.GONE);
            }
            break;
          }
        }
      } else {
        int size = viewers.size();
        for (int i = 0; i < size; i++) {

          if (viewers.get(i).getUserId().equals(memberId)) {
            AddMembersModel addMembersModel = viewers.get(i);
            addMembersModel.setMember(true);
            viewers.set(i, addMembersModel);
            addMembersAdapter.notifyItemChanged(i);
            break;
          }
        }
      }
    });

    hideProgressDialog();
  }

  /**
   * {@link AddMembersContract.View#onViewersDataReceived(ArrayList, boolean)}
   */
  @Override
  public void onViewersDataReceived(ArrayList<AddMembersModel> viewerModels,
      boolean refreshRequest) {

    if (activity != null) {
      activity.runOnUiThread(() -> {
        if (refreshRequest) {
          viewers.clear();
        }
        viewers.addAll(viewerModels);
        if (selectedTabPosition == 1) {
          if (viewers.size() > 0) {
            ismBottomsheetAddMembersBinding.tvNoUsers.setVisibility(View.GONE);
            ismBottomsheetAddMembersBinding.rvUsers.setVisibility(View.VISIBLE);
            addMembersAdapter.notifyDataSetChanged();
          } else {
            ismBottomsheetAddMembersBinding.tvNoUsers.setCompoundDrawablesWithIntrinsicBounds(null,
                noViewers, null, null);
            ismBottomsheetAddMembersBinding.tvNoUsers.setText(
                getString(R.string.ism_no_viewers_add_member));
            ismBottomsheetAddMembersBinding.tvNoUsers.setVisibility(View.VISIBLE);
            ismBottomsheetAddMembersBinding.rvUsers.setVisibility(View.GONE);
          }
        }
      });
    }

    updateShimmerVisibility(false);

    if (ismBottomsheetAddMembersBinding.refresh.isRefreshing()) {
      ismBottomsheetAddMembersBinding.refresh.setRefreshing(false);
    }
  }

  /**
   * {@link AddMembersContract.View#removeViewerEvent(String)}
   */
  @Override
  public void removeViewerEvent(String viewerId) {

    if (activity != null) {
      activity.runOnUiThread(() -> {
        int size = viewers.size();

        for (int i = 0; i < size; i++) {

          if (viewers.get(i).getUserId().equals(viewerId)) {

            viewers.remove(i);
            if (selectedTabPosition == 1) {
              addMembersAdapter.notifyItemRemoved(i);
              //viewersAdapter.notifyDataSetChanged();}
            }
            break;
          }
        }
        if (selectedTabPosition == 1) {
          size = viewers.size();
          if (size == 0) {
            ismBottomsheetAddMembersBinding.tvNoUsers.setCompoundDrawablesWithIntrinsicBounds(null,
                noViewers, null, null);
            ismBottomsheetAddMembersBinding.tvNoUsers.setText(
                getString(R.string.ism_no_viewers_add_member));
            ismBottomsheetAddMembersBinding.tvNoUsers.setVisibility(View.VISIBLE);
            ismBottomsheetAddMembersBinding.rvUsers.setVisibility(View.GONE);
          }
        }
      });
    }
  }

  /**
   * {@link AddMembersContract.View#addViewerEvent(AddMembersModel)}
   */
  @Override
  public void addViewerEvent(AddMembersModel addMembersModel) {
    if (activity != null) {
      activity.runOnUiThread(() -> {

        viewers.add(0, addMembersModel);

        if (selectedTabPosition == 1) {
          addMembersAdapter.notifyItemInserted(0);
          //viewersAdapter.notifyDataSetChanged();

          if (ismBottomsheetAddMembersBinding.tvNoUsers.getVisibility() == View.VISIBLE) {

            ismBottomsheetAddMembersBinding.tvNoUsers.setVisibility(View.GONE);
            ismBottomsheetAddMembersBinding.rvUsers.setVisibility(View.VISIBLE);
          }
        }
      });
    }
  }

  /**
   * {@link AddMembersContract.View#onProfileSwitched(String)}
   */
  @Override
  public void onProfileSwitched(String viewerId) {
    removeViewerEvent(viewerId);
  }

  @Override
  public void onCancel(@NotNull DialogInterface dialog) {
    super.onCancel(dialog);
    addMembersPresenter.unregisterStreamViewersEventListener();
    addMembersPresenter.unregisterCopublishRequestsEventListener();
  }

  private void showProgressDialog(String message) {

    if (activity != null) {
      alertDialog = alertProgress.getProgressDialog(activity, message);
      if (!activity.isFinishing() && !activity.isDestroyed()) alertDialog.show();
    }
  }

  private void hideProgressDialog() {

    if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
  }

  private void fetchLatestUsers(boolean isSearchRequest, String searchTag) {

    try {
      addMembersPresenter.requestEligibleUsersData(0, true, isSearchRequest, searchTag);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void fetchLatestViewers(boolean isSearchRequest, String searchTag) {

    try {
      addMembersPresenter.requestViewersData(0, true, isSearchRequest, searchTag);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void updateShimmerVisibility(boolean visible) {
    if (visible) {
      ismBottomsheetAddMembersBinding.shimmerFrameLayout.startShimmer();
      ismBottomsheetAddMembersBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
    } else {
      if (ismBottomsheetAddMembersBinding.shimmerFrameLayout.getVisibility() == View.VISIBLE) {
        ismBottomsheetAddMembersBinding.shimmerFrameLayout.setVisibility(View.GONE);
        ismBottomsheetAddMembersBinding.shimmerFrameLayout.stopShimmer();
      }
    }
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    Dialog dialog = super.onCreateDialog(savedInstanceState);
    try {
      dialog.setOnShowListener(dialog1 -> new Handler().postDelayed(() -> {
        BottomSheetDialog d = (BottomSheetDialog) dialog1;
        FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        @SuppressWarnings("rawtypes")
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
      }, 0));
    } catch (Exception ignore) {
    }
    return dialog;
  }

  @Override
  public void onDismiss(@NonNull DialogInterface dialog) {

    if (activity != null) {
      activity.getWindow()
          .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    super.onDismiss(dialog);
  }
}