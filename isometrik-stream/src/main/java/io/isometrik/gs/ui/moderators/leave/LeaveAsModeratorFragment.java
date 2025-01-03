package io.isometrik.gs.ui.moderators.leave;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmBottomsheetModeratorLeaveBinding;
import io.isometrik.gs.ui.settings.callbacks.SettingsActionCallback;
import io.isometrik.gs.ui.utils.AlertProgress;

/**
 * Bottomsheet dialog fragment to allow a moderator to leave a broadcast.Implements interface
 * ModeratorsContract view{@link LeaveAsModeratorContract.View}
 *
 * @see LeaveAsModeratorContract.View
 */
public class LeaveAsModeratorFragment extends BottomSheetDialogFragment
    implements LeaveAsModeratorContract.View {

  public static final String TAG = "LeaveAsModeratorBottomSheetFragment";

  private LeaveAsModeratorContract.Presenter leaveAsModeratorPresenter;

  private AlertProgress alertProgress;
  private AlertDialog alertDialog;

  private String streamId;
  private boolean isAdmin;
  private Activity activity;
  private SettingsActionCallback settingsActionCallback;

  private IsmBottomsheetModeratorLeaveBinding ismBottomsheetLeaveAsModeratorBinding;

  public LeaveAsModeratorFragment() {
    // Required empty public constructor
  }

  @SuppressLint("ClickableViewAccessibility")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (ismBottomsheetLeaveAsModeratorBinding == null) {
      ismBottomsheetLeaveAsModeratorBinding =
          IsmBottomsheetModeratorLeaveBinding.inflate(inflater, container, false);
    } else {

      if (ismBottomsheetLeaveAsModeratorBinding.getRoot().getParent() != null) {
        ((ViewGroup) ismBottomsheetLeaveAsModeratorBinding.getRoot().getParent()).removeView(
            ismBottomsheetLeaveAsModeratorBinding.getRoot());
      }
    }

    alertProgress = new AlertProgress();

    ismBottomsheetLeaveAsModeratorBinding.tvAction.setText(
        isAdmin ? getString(R.string.ism_manage_moderators)
            : getString(R.string.ism_stop_moderating));

    ismBottomsheetLeaveAsModeratorBinding.tvOk.setOnClickListener(v -> {
      dismissDialog();
    });

    ismBottomsheetLeaveAsModeratorBinding.tvAction.setOnClickListener(v -> {

      if (isAdmin) {
        settingsActionCallback.fetchModerators(streamId);
      } else {
        showProgressDialog(getString(R.string.ism_leaving_as_moderator));
        leaveAsModeratorPresenter.leaveAsModerator(streamId);
      }
    });

    return ismBottomsheetLeaveAsModeratorBinding.getRoot();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
    leaveAsModeratorPresenter = new LeaveAsModeratorPresenter();
    leaveAsModeratorPresenter.attachView(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    leaveAsModeratorPresenter.detachView();
    activity = null;
  }

  /**
   * {@link LeaveAsModeratorContract.View#onError(String)}
   */
  @Override
  public void onError(String errorMessage) {

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
   * @param isAdmin whether given user is the admin of the broadcast or not
   */
  public void updateParameters(String streamId, boolean isAdmin,
      SettingsActionCallback settingsActionCallback) {
    this.streamId = streamId;
    this.isAdmin = isAdmin;
    this.settingsActionCallback = settingsActionCallback;
  }

  /**
   * {@link LeaveAsModeratorContract.View#onLeftAsModeratorSuccessfully()}
   */
  @Override
  public void onLeftAsModeratorSuccessfully() {

    hideProgressDialog();
    settingsActionCallback.moderatorLeft();
    dismissDialog();
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

  private void dismissDialog() {
    try {
      dismiss();
    } catch (Exception ignore) {
    }
  }
}