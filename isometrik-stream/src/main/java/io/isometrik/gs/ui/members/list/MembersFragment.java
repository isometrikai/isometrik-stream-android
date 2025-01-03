package io.isometrik.gs.ui.members.list;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
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
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmBottomsheetMembersBinding;
import io.isometrik.gs.ui.utils.AlertProgress;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * Bottomsheet dialog fragment to show list of members in a
 * broadcast along with their publishing status.Host can kick out a member.Implements interface
 * MembersContract view{@link MembersContract.View}
 *
 * @see MembersContract.View
 */

public class MembersFragment extends BottomSheetDialogFragment implements MembersContract.View {

  public static final String TAG = "MembersBottomSheetFragment";

  private MembersContract.Presenter membersPresenter;

  private AlertProgress alertProgress;
  private AlertDialog alertDialog;

  private ArrayList<MembersModel> members;
  private MembersAdapter membersAdapter;

  private String streamId, membersCount;
  private boolean isAdmin, isModerator;

  private Activity activity;
  private LinearLayoutManager layoutManager;
  private IsmBottomsheetMembersBinding ismBottomsheetMembersBinding;

  public MembersFragment() {
    // Required empty public constructor
  }

  @SuppressLint("ClickableViewAccessibility")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (ismBottomsheetMembersBinding == null) {
      ismBottomsheetMembersBinding =
          IsmBottomsheetMembersBinding.inflate(inflater, container, false);
    } else {

      if (ismBottomsheetMembersBinding.getRoot().getParent() != null) {
        ((ViewGroup) ismBottomsheetMembersBinding.getRoot().getParent()).removeView(
            ismBottomsheetMembersBinding.getRoot());
      }
    }
    ismBottomsheetMembersBinding.tvNoMember.setVisibility(View.GONE);
    alertProgress = new AlertProgress();
    updateShimmerVisibility(true);
    ismBottomsheetMembersBinding.tvMembersCount.setText(membersCount);
    layoutManager = new LinearLayoutManager(activity);
    ismBottomsheetMembersBinding.rvMembers.setLayoutManager(layoutManager);
    ismBottomsheetMembersBinding.rvMembers.addOnScrollListener(recyclerViewOnScrollListener);

    members = new ArrayList<>();
    membersAdapter = new MembersAdapter(activity, members, this);
    ismBottomsheetMembersBinding.rvMembers.setAdapter(membersAdapter);

    membersPresenter.initialize(streamId, isAdmin, isModerator);

    fetchStreamMembers(false, null);

    membersPresenter.registerStreamMembersEventListener();
    membersPresenter.registerCopublishRequestsEventListener();
    ismBottomsheetMembersBinding.refresh.setOnRefreshListener(
        () -> fetchStreamMembers(false, null));
    ismBottomsheetMembersBinding.etSearch.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
          fetchStreamMembers(true, s.toString());
        } else {

          fetchStreamMembers(false, null);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });
    //To allow scroll on member's recyclerview
    ismBottomsheetMembersBinding.rvMembers.setOnTouchListener((v, event) -> {
      v.getParent().requestDisallowInterceptTouchEvent(true);
      v.onTouchEvent(event);
      return true;
    });

    return ismBottomsheetMembersBinding.getRoot();
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

          membersPresenter.requestMembersDataOnScroll(layoutManager.findFirstVisibleItemPosition(),
              layoutManager.getChildCount(), layoutManager.getItemCount());
        }
      };

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    activity = getActivity();
    membersPresenter = new MembersPresenter();
    membersPresenter.attachView(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    membersPresenter.detachView();
    activity = null;
  }

  /**
   * {@link MembersContract.View#onError(String)}
   */
  @Override
  public void onError(String errorMessage) {
    if (ismBottomsheetMembersBinding.refresh.isRefreshing()) {
      ismBottomsheetMembersBinding.refresh.setRefreshing(false);
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
   * Request remove member.
   *
   * @param memberId the id of the member to be removed
   */
  public void requestRemoveMember(String memberId) {
    showProgressDialog(getString(R.string.ism_removing_member));
    membersPresenter.requestRemoveMember(memberId);
  }

  /**
   * {@link MembersContract.View#onStreamMembersDataReceived(ArrayList, boolean, int)}
   */
  @Override
  public void onStreamMembersDataReceived(ArrayList<MembersModel> memberModels,
      boolean refreshRequest, int membersCount) {
    if (activity != null) {
      activity.runOnUiThread(() -> {

        if (refreshRequest) {
          members.clear();
        }

        if (membersCount != -1) {
          ismBottomsheetMembersBinding.tvMembersCount.setText(String.valueOf(membersCount));
        }
        members.addAll(memberModels);
        if (members.size() > 0) {
          ismBottomsheetMembersBinding.tvNoMember.setVisibility(View.GONE);
          ismBottomsheetMembersBinding.rvMembers.setVisibility(View.VISIBLE);
          membersAdapter.notifyDataSetChanged();
        } else {
          ismBottomsheetMembersBinding.tvNoMember.setVisibility(View.VISIBLE);
          ismBottomsheetMembersBinding.rvMembers.setVisibility(View.GONE);
        }
      });
    }
    updateShimmerVisibility(false);
    if (ismBottomsheetMembersBinding.refresh.isRefreshing()) {
      ismBottomsheetMembersBinding.refresh.setRefreshing(false);
    }
  }

  /**
   * {@link MembersContract.View#onMemberRemovedResult(String)}
   */
  @Override
  public void onMemberRemovedResult(String memberId) {

    hideProgressDialog();
    removeMemberEvent(memberId, -1);
  }

  /**
   * {@link MembersContract.View#addMemberEvent(MembersModel,
   * int)}
   */
  @Override
  public void addMemberEvent(MembersModel membersModel, int membersCount) {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        members.add(0, membersModel);
        ismBottomsheetMembersBinding.tvMembersCount.setText(String.valueOf(membersCount));
        membersAdapter.notifyItemInserted(0);
        //membersAdapter.notifyDataSetChanged();

        if (ismBottomsheetMembersBinding.tvNoMember.getVisibility() == View.VISIBLE) {

          ismBottomsheetMembersBinding.tvNoMember.setVisibility(View.GONE);
          ismBottomsheetMembersBinding.rvMembers.setVisibility(View.VISIBLE);
        }
      });
    }
  }

  /**
   * {@link MembersContract.View#removeMemberEvent(String,
   * int)}
   */
  @Override
  public void removeMemberEvent(String memberId, int membersCount) {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        int size = members.size();
        for (int i = 0; i < size; i++) {

          if (members.get(i).getMemberId().equals(memberId)) {

            members.remove(i);
            membersAdapter.notifyItemRemoved(i);
            // membersAdapter.notifyDataSetChanged();}

            break;
          }
        }

        size = members.size();
        if (size == 0) {
          ismBottomsheetMembersBinding.tvNoMember.setVisibility(View.VISIBLE);
          ismBottomsheetMembersBinding.rvMembers.setVisibility(View.GONE);
        }
        if (membersCount == -1) {
          ismBottomsheetMembersBinding.tvMembersCount.setText(String.valueOf(size));
        } else {
          ismBottomsheetMembersBinding.tvMembersCount.setText(String.valueOf(membersCount));
        }
      });
    }
  }

  /**
   * {@link MembersContract.View#publishStatusChanged(String,
   * boolean, int, String)}
   */
  @Override
  public void publishStatusChanged(String memberId, boolean publishing, int membersCount,
      String joinTime) {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        int size = members.size();
        for (int i = 0; i < size; i++) {

          if (members.get(i).getMemberId().equals(memberId)) {

            MembersModel membersModel = members.get(i);
            membersModel.setPublishing(publishing);
            if (publishing) {

              membersModel.setJoinTime(joinTime);
            } else {

              membersModel.setJoinTime(getString(R.string.ism_not_publishing));
            }

            members.set(i, membersModel);
            membersAdapter.notifyItemChanged(i);
            // membersAdapter.notifyDataSetChanged();}

            break;
          }
        }

        ismBottomsheetMembersBinding.tvMembersCount.setText(String.valueOf(membersCount));
      });
    }
  }

  /**
   * {@link MembersContract.View#onStreamOffline(String,
   * int)}
   */
  @Override
  public void onStreamOffline(String message, int dialogType) {
    updateShimmerVisibility(false);
    hideProgressDialog();
  }

  /**
   * @param streamId id of the stream group
   * @param isAdmin whether given user is admin or not
   * @param membersCount number of members
   * @param isModerator whether given user is a moderator of stream group or not
   */
  public void updateParameters(String streamId, boolean isAdmin, String membersCount,
      boolean isModerator) {
    this.streamId = streamId;
    this.isAdmin = isAdmin;
    this.membersCount = membersCount;
    this.isModerator = isModerator;
  }

  @Override
  public void onCancel(@NotNull DialogInterface dialog) {
    super.onCancel(dialog);
    membersPresenter.unregisterStreamMembersEventListener();
    membersPresenter.unregisterCopublishRequestsEventListener();
  }

  /**
   * {@link MembersContract.View#onProfileSwitched(String, int, String)}
   */
  @Override
  public void onProfileSwitched(String memberId, int membersCount, String joinTime) {

    publishStatusChanged(memberId, true, membersCount, joinTime);
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

  private void updateShimmerVisibility(boolean visible) {
    if (visible) {
      ismBottomsheetMembersBinding.shimmerFrameLayout.startShimmer();
      ismBottomsheetMembersBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
    } else {
      if (ismBottomsheetMembersBinding.shimmerFrameLayout.getVisibility() == View.VISIBLE) {
        ismBottomsheetMembersBinding.shimmerFrameLayout.setVisibility(View.GONE);
        ismBottomsheetMembersBinding.shimmerFrameLayout.stopShimmer();
      }
    }
  }

  private void fetchStreamMembers(boolean isSearchRequest, String searchTag) {

    try {
      membersPresenter.requestMembersData(0, true, isSearchRequest, searchTag);
    } catch (Exception e) {
      e.printStackTrace();
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