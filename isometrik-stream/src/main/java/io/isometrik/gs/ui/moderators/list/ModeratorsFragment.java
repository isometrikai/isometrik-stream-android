package io.isometrik.gs.ui.moderators.list;

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
import io.isometrik.gs.ui.databinding.IsmBottomsheetModeratorsBinding;
import io.isometrik.gs.ui.settings.callbacks.SettingsActionCallback;
import io.isometrik.gs.ui.utils.AlertProgress;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * Bottomsheet dialog fragment to show list of moderators in a
 * broadcast.Host can kick out a moderator.Implements interface
 * ModeratorsContract view{@link ModeratorsContract.View}
 *
 * @see ModeratorsContract.View
 */

public class ModeratorsFragment extends BottomSheetDialogFragment
    implements ModeratorsContract.View {

  public static final String TAG = "ModeratorsBottomSheetFragment";

  private ModeratorsContract.Presenter moderatorsPresenter;

  private AlertProgress alertProgress;
  private AlertDialog alertDialog;
  private SettingsActionCallback settingsActionCallback;

  private ArrayList<ModeratorsModel> moderators;
  private ModeratorsAdapter moderatorsAdapter;

  private String streamId;
  private boolean isAdmin;

  private Activity activity;

  private IsmBottomsheetModeratorsBinding ismBottomsheetModeratorsBinding;
  private LinearLayoutManager layoutManager;

  public ModeratorsFragment() {
    // Required empty public constructor
  }

  @SuppressLint("ClickableViewAccessibility")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (ismBottomsheetModeratorsBinding == null) {
      ismBottomsheetModeratorsBinding =
          IsmBottomsheetModeratorsBinding.inflate(inflater, container, false);
    } else {

      if (ismBottomsheetModeratorsBinding.getRoot().getParent() != null) {
        ((ViewGroup) ismBottomsheetModeratorsBinding.getRoot().getParent()).removeView(
            ismBottomsheetModeratorsBinding.getRoot());
      }
    }
    ismBottomsheetModeratorsBinding.tvNoModerator.setVisibility(View.GONE);
    alertProgress = new AlertProgress();
    updateShimmerVisibility(true);
    ismBottomsheetModeratorsBinding.tvModeratorsCount.setText("0");
    layoutManager = new LinearLayoutManager(activity);
    ismBottomsheetModeratorsBinding.rvModerators.setLayoutManager(layoutManager);
    ismBottomsheetModeratorsBinding.rvModerators.addOnScrollListener(recyclerViewOnScrollListener);

    moderators = new ArrayList<>();
    moderatorsAdapter = new ModeratorsAdapter(activity, moderators, this);
    ismBottomsheetModeratorsBinding.rvModerators.setAdapter(moderatorsAdapter);

    moderatorsPresenter.initialize(streamId, isAdmin);

    ismBottomsheetModeratorsBinding.tvAddModerators.setVisibility(View.GONE);

    fetchStreamModerators(false, null);

    moderatorsPresenter.registerStreamModeratorsEventListener();

    ismBottomsheetModeratorsBinding.refresh.setOnRefreshListener(
        () -> fetchStreamModerators(false, null));
    ismBottomsheetModeratorsBinding.etSearch.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
          fetchStreamModerators(true, s.toString());
        } else {

          fetchStreamModerators(false, null);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });
    //To allow scroll on moderator's recyclerview
    ismBottomsheetModeratorsBinding.rvModerators.setOnTouchListener((v, event) -> {
      v.getParent().requestDisallowInterceptTouchEvent(true);
      v.onTouchEvent(event);
      return true;
    });

    ismBottomsheetModeratorsBinding.tvAddModerators.setOnClickListener(v -> {
      settingsActionCallback.addModerator(moderatorsPresenter.getModeratorIds(moderators));
      try {
        dismiss();
      } catch (Exception ignore) {
      }
    });
    return ismBottomsheetModeratorsBinding.getRoot();
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

          moderatorsPresenter.requestModeratorsDataOnScroll(
              layoutManager.findFirstVisibleItemPosition(), layoutManager.getChildCount(),
              layoutManager.getItemCount());
        }
      };

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    activity = getActivity();
    moderatorsPresenter = new ModeratorsPresenter();
    moderatorsPresenter.attachView(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    moderatorsPresenter.detachView();
    activity = null;
  }

  /**
   * {@link ModeratorsContract.View#onError(String)}
   */
  @Override
  public void onError(String errorMessage) {
    if (ismBottomsheetModeratorsBinding.refresh.isRefreshing()) {
      ismBottomsheetModeratorsBinding.refresh.setRefreshing(false);
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
   * Request remove moderator.
   *
   * @param moderatorId the id of the moderator to be removed
   */
  public void requestRemoveModerator(String moderatorId) {
    showProgressDialog(getString(R.string.ism_removing_moderator));
    moderatorsPresenter.requestRemoveModerator(moderatorId);
  }

  /**
   * {@link ModeratorsContract.View#onStreamModeratorsDataReceived(ArrayList, boolean, int)}
   */
  @Override
  public void onStreamModeratorsDataReceived(ArrayList<ModeratorsModel> moderatorModels,
      boolean refreshRequest, int moderatorsCount) {
    if (activity != null) {
      activity.runOnUiThread(() -> {

        if (refreshRequest) {
          moderators.clear();
        }
        if (moderatorsCount != -1) {
          ismBottomsheetModeratorsBinding.tvModeratorsCount.setText(
              String.valueOf(moderatorsCount));
        }
        moderators.addAll(moderatorModels);

        if (moderators.size() > 0) {
          ismBottomsheetModeratorsBinding.tvNoModerator.setVisibility(View.GONE);
          ismBottomsheetModeratorsBinding.rvModerators.setVisibility(View.VISIBLE);
          moderatorsAdapter.notifyDataSetChanged();
        } else {
          ismBottomsheetModeratorsBinding.tvNoModerator.setVisibility(View.VISIBLE);
          ismBottomsheetModeratorsBinding.rvModerators.setVisibility(View.GONE);
        }

        ismBottomsheetModeratorsBinding.tvAddModerators.setVisibility(
            isAdmin ? View.VISIBLE : View.GONE);
      });
    }
    updateShimmerVisibility(false);
    if (ismBottomsheetModeratorsBinding.refresh.isRefreshing()) {
      ismBottomsheetModeratorsBinding.refresh.setRefreshing(false);
    }
  }

  /**
   * {@link ModeratorsContract.View#onModeratorRemovedResult(String)}
   */
  @Override
  public void onModeratorRemovedResult(String moderatorId) {

    hideProgressDialog();
    removeModeratorEvent(moderatorId, -1);
  }

  /**
   * {@link ModeratorsContract.View#addModeratorEvent(ModeratorsModel,
   * int)}
   */
  @Override
  public void addModeratorEvent(ModeratorsModel moderatorsModel, int moderatorsCount) {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        moderators.add(0, moderatorsModel);
        ismBottomsheetModeratorsBinding.tvModeratorsCount.setText(String.valueOf(moderatorsCount));
        moderatorsAdapter.notifyItemInserted(0);
        //moderatorsAdapter.notifyDataSetChanged();

        if (ismBottomsheetModeratorsBinding.tvNoModerator.getVisibility() == View.VISIBLE) {

          ismBottomsheetModeratorsBinding.tvNoModerator.setVisibility(View.GONE);
          ismBottomsheetModeratorsBinding.rvModerators.setVisibility(View.VISIBLE);
        }
      });
    }
  }

  /**
   * {@link ModeratorsContract.View#removeModeratorEvent(String,
   * int)}
   */
  @Override
  public void removeModeratorEvent(String moderatorId, int moderatorsCount) {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        int size = moderators.size();
        for (int i = 0; i < size; i++) {

          if (moderators.get(i).getModeratorId().equals(moderatorId)) {

            moderators.remove(i);
            moderatorsAdapter.notifyItemRemoved(i);
            // moderatorsAdapter.notifyDataSetChanged();}

            break;
          }
        }

        size = moderators.size();
        if (size == 0) {
          ismBottomsheetModeratorsBinding.tvNoModerator.setVisibility(View.VISIBLE);
          ismBottomsheetModeratorsBinding.rvModerators.setVisibility(View.GONE);
        }
        if (moderatorsCount == -1) {
          ismBottomsheetModeratorsBinding.tvModeratorsCount.setText(String.valueOf(size));
        } else {
          ismBottomsheetModeratorsBinding.tvModeratorsCount.setText(
              String.valueOf(moderatorsCount));
        }
      });
    }
  }

  /**
   * @param streamId id of the stream group
   * @param isAdmin whether given user is admin or not
   * @param settingsActionCallback to request addition of moderator
   */
  public void updateParameters(String streamId, boolean isAdmin,
      SettingsActionCallback settingsActionCallback) {
    this.streamId = streamId;
    this.isAdmin = isAdmin;
    this.settingsActionCallback = settingsActionCallback;
  }

  @Override
  public void onCancel(@NotNull DialogInterface dialog) {
    super.onCancel(dialog);
    moderatorsPresenter.unregisterStreamModeratorsEventListener();
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
      ismBottomsheetModeratorsBinding.shimmerFrameLayout.startShimmer();
      ismBottomsheetModeratorsBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
    } else {
      if (ismBottomsheetModeratorsBinding.shimmerFrameLayout.getVisibility() == View.VISIBLE) {
        ismBottomsheetModeratorsBinding.shimmerFrameLayout.setVisibility(View.GONE);
        ismBottomsheetModeratorsBinding.shimmerFrameLayout.stopShimmer();
      }
    }
  }

  private void fetchStreamModerators(boolean isSearchRequest, String searchTag) {

    try {
      moderatorsPresenter.requestModeratorsData(0, true, isSearchRequest, searchTag);
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