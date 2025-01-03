package io.isometrik.gs.ui.moderators.add;

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
import io.isometrik.gs.ui.databinding.IsmBottomsheetAddModeratorsBinding;
import io.isometrik.gs.ui.utils.AlertProgress;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * Bottomsheet dialog fragment to show list of users which can be added to a
 * broadcast.Implements interface
 * ModeratorsContract view{@link AddModeratorsContract.View}
 *
 * @see AddModeratorsContract.View
 */
public class AddModeratorsFragment extends BottomSheetDialogFragment
    implements AddModeratorsContract.View {

  public static final String TAG = "AddModeratorsBottomSheetFragment";

  private AddModeratorsContract.Presenter addModeratorsPresenter;

  private AlertProgress alertProgress;
  private AlertDialog alertDialog;

  private ArrayList<AddModeratorsModel> users;
  private AddModeratorsAdapter addModeratorsAdapter;
  private ArrayList<String> moderatorIds;

  private String streamId;
  private Activity activity;

  private LinearLayoutManager layoutManager;

  private IsmBottomsheetAddModeratorsBinding ismBottomsheetAddModeratorsBinding;

  public AddModeratorsFragment() {
    // Required empty public constructor
  }

  @SuppressLint("ClickableViewAccessibility")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (ismBottomsheetAddModeratorsBinding == null) {
      ismBottomsheetAddModeratorsBinding =
          IsmBottomsheetAddModeratorsBinding.inflate(inflater, container, false);
    } else {

      if (ismBottomsheetAddModeratorsBinding.getRoot().getParent() != null) {
        ((ViewGroup) ismBottomsheetAddModeratorsBinding.getRoot().getParent()).removeView(
            ismBottomsheetAddModeratorsBinding.getRoot());
      }
    }

    ismBottomsheetAddModeratorsBinding.tvNoUsers.setVisibility(View.GONE);
    alertProgress = new AlertProgress();
    updateShimmerVisibility(true);
    layoutManager = new LinearLayoutManager(activity);
    ismBottomsheetAddModeratorsBinding.rvUsers.setLayoutManager(layoutManager);

    users = new ArrayList<>();

    addModeratorsAdapter = new AddModeratorsAdapter(activity, users, this);
    ismBottomsheetAddModeratorsBinding.rvUsers.setAdapter(addModeratorsAdapter);
    ismBottomsheetAddModeratorsBinding.rvUsers.addOnScrollListener(recyclerViewOnScrollListener);
    addModeratorsPresenter.initialize(streamId, moderatorIds);

    fetchLatestUsers(false, null);

    ismBottomsheetAddModeratorsBinding.refresh.setOnRefreshListener(
        () -> fetchLatestUsers(false, null));
    ismBottomsheetAddModeratorsBinding.etSearch.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
          fetchLatestUsers(true, s.toString());
        } else {

          fetchLatestUsers(false, null);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });
    //To allow scroll on user's recyclerview
    ismBottomsheetAddModeratorsBinding.rvUsers.setOnTouchListener((v, event) -> {
      v.getParent().requestDisallowInterceptTouchEvent(true);
      v.onTouchEvent(event);
      return true;
    });

    return ismBottomsheetAddModeratorsBinding.getRoot();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    activity = getActivity();
    addModeratorsPresenter = new AddModeratorsPresenter();
    addModeratorsPresenter.attachView(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    addModeratorsPresenter.detachView();
    activity = null;
  }

  /**
   * {@link AddModeratorsContract.View#onError(String)}
   */
  @Override
  public void onError(String errorMessage) {
    if (ismBottomsheetAddModeratorsBinding.refresh.isRefreshing()) {
      ismBottomsheetAddModeratorsBinding.refresh.setRefreshing(false);
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
   * @param streamId id of the stream group
   * @param moderatorIds list containing ids of the stream group moderators
   */
  public void updateParameters(String streamId, ArrayList<String> moderatorIds) {
    this.streamId = streamId;
    this.moderatorIds = moderatorIds;
  }

  /**
   * Add moderator.
   *
   * @param moderatorId the moderator id
   */
  public void addModerator(String moderatorId) {
    showProgressDialog(getString(R.string.ism_adding_moderator));
    addModeratorsPresenter.addModerator(moderatorId);
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

          addModeratorsPresenter.requestUsersDataOnScroll(
              layoutManager.findFirstVisibleItemPosition(), layoutManager.getChildCount(),
              layoutManager.getItemCount());
        }
      };

  /**
   * {@link AddModeratorsContract.View#onUsersDataReceived(ArrayList, boolean)}
   */
  @Override
  public void onUsersDataReceived(ArrayList<AddModeratorsModel> addModeratorsModels,
      boolean refreshRequest) {

    activity.runOnUiThread(() -> {
      if (refreshRequest) {
        users.clear();
      }
      users.addAll(addModeratorsModels);
      if (users.size() > 0) {
        ismBottomsheetAddModeratorsBinding.tvNoUsers.setVisibility(View.GONE);
        ismBottomsheetAddModeratorsBinding.rvUsers.setVisibility(View.VISIBLE);
        addModeratorsAdapter.notifyDataSetChanged();
      } else {

        ismBottomsheetAddModeratorsBinding.tvNoUsers.setText(
            getString(R.string.ism_no_users_add_moderator));
        ismBottomsheetAddModeratorsBinding.tvNoUsers.setVisibility(View.VISIBLE);
        ismBottomsheetAddModeratorsBinding.rvUsers.setVisibility(View.GONE);
      }
    });

    updateShimmerVisibility(false);

    if (ismBottomsheetAddModeratorsBinding.refresh.isRefreshing()) {
      ismBottomsheetAddModeratorsBinding.refresh.setRefreshing(false);
    }
  }

  /**
   * {@link
   * io.isometrik.gs.ui.moderators.add.AddModeratorsContract.View#onModeratorAdded(String)}
   */
  @Override
  public void onModeratorAdded(String moderatorId) {

    activity.runOnUiThread(() -> {

      int size = users.size();
      for (int i = 0; i < size; i++) {

        if (users.get(i).getUserId().equals(moderatorId)) {

          users.remove(i);
          addModeratorsAdapter.notifyItemRemoved(i);

          if (users.size() == 0) {

            ismBottomsheetAddModeratorsBinding.tvNoUsers.setText(
                getString(R.string.ism_no_users_add_moderator));
            ismBottomsheetAddModeratorsBinding.tvNoUsers.setVisibility(View.VISIBLE);
            ismBottomsheetAddModeratorsBinding.rvUsers.setVisibility(View.GONE);
          }
          break;
        }
      }
    });

    hideProgressDialog();
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
      addModeratorsPresenter.requestUsersData(0, true, isSearchRequest, searchTag);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void updateShimmerVisibility(boolean visible) {
    if (visible) {
      ismBottomsheetAddModeratorsBinding.shimmerFrameLayout.startShimmer();
      ismBottomsheetAddModeratorsBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
    } else {
      if (ismBottomsheetAddModeratorsBinding.shimmerFrameLayout.getVisibility() == View.VISIBLE) {
        ismBottomsheetAddModeratorsBinding.shimmerFrameLayout.setVisibility(View.GONE);
        ismBottomsheetAddModeratorsBinding.shimmerFrameLayout.stopShimmer();
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