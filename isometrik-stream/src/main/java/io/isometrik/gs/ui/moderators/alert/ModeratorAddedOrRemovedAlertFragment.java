package io.isometrik.gs.ui.moderators.alert;

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
import io.isometrik.gs.ui.databinding.IsmBottomsheetModeratorAddedRemovedBinding;
import io.isometrik.gs.ui.settings.callbacks.SettingsActionCallback;
import io.isometrik.gs.ui.utils.AlertProgress;

/**
 * Bottomsheet dialog fragment to show alert for member added/removed event and allow a moderator to
 * leave a broadcast.Implements interface
 * ModeratorsContract view{@link ModeratorAddedOrRemovedAlertContract.View}
 *
 * @see ModeratorAddedOrRemovedAlertContract.View
 */
public class ModeratorAddedOrRemovedAlertFragment extends BottomSheetDialogFragment
    implements ModeratorAddedOrRemovedAlertContract.View {

  public static final String TAG = "ModeratorAddedOrRemovedAlertBottomSheetFragment";

  private ModeratorAddedOrRemovedAlertContract.Presenter moderatorAddedOrRemovedAlertPresenter;

  private AlertProgress alertProgress;
  private AlertDialog alertDialog;

  private String streamId, initiatorName, moderatorName;
  private boolean isAdmin, addedAsModerator;
  private Activity activity;
  private SettingsActionCallback settingsActionCallback;

  private IsmBottomsheetModeratorAddedRemovedBinding
      ismBottomsheetModeratorAddedOrRemovedAlertBinding;

  public ModeratorAddedOrRemovedAlertFragment() {
    // Required empty public constructor
  }

  @SuppressLint("ClickableViewAccessibility")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (ismBottomsheetModeratorAddedOrRemovedAlertBinding == null) {
      ismBottomsheetModeratorAddedOrRemovedAlertBinding =
          IsmBottomsheetModeratorAddedRemovedBinding.inflate(inflater, container, false);
    } else {

      if (ismBottomsheetModeratorAddedOrRemovedAlertBinding.getRoot().getParent() != null) {
        ((ViewGroup) ismBottomsheetModeratorAddedOrRemovedAlertBinding.getRoot()
            .getParent()).removeView(ismBottomsheetModeratorAddedOrRemovedAlertBinding.getRoot());
      }
    }

    alertProgress = new AlertProgress();

    if (addedAsModerator) {
      ismBottomsheetModeratorAddedOrRemovedAlertBinding.tvHeading.setText(
          getString(R.string.ism_added_as_moderator));
      ismBottomsheetModeratorAddedOrRemovedAlertBinding.tvDescription.setText(
          getString(R.string.ism_added_as_moderator_description,
              getString(R.string.ism_you, moderatorName), initiatorName));
      ismBottomsheetModeratorAddedOrRemovedAlertBinding.llModeratorAdded.setVisibility(
          View.VISIBLE);
      ismBottomsheetModeratorAddedOrRemovedAlertBinding.tvGotIt.setVisibility(View.GONE);
      ismBottomsheetModeratorAddedOrRemovedAlertBinding.tvAction.setText(
          isAdmin ? getString(R.string.ism_manage_moderators)
              : getString(R.string.ism_stop_moderating));
    } else {
      ismBottomsheetModeratorAddedOrRemovedAlertBinding.tvHeading.setText(
          getString(R.string.ism_removed_as_moderator));
      ismBottomsheetModeratorAddedOrRemovedAlertBinding.tvDescription.setText(
          getString(R.string.ism_removed_as_moderator_description,
              getString(R.string.ism_you, moderatorName), initiatorName));
      ismBottomsheetModeratorAddedOrRemovedAlertBinding.llModeratorAdded.setVisibility(View.GONE);
      ismBottomsheetModeratorAddedOrRemovedAlertBinding.tvGotIt.setVisibility(View.VISIBLE);
    }

    ismBottomsheetModeratorAddedOrRemovedAlertBinding.tvOk.setOnClickListener(v -> {
      dismissDialog();
    });

    ismBottomsheetModeratorAddedOrRemovedAlertBinding.tvGotIt.setOnClickListener(v -> {
      dismissDialog();
    });

    ismBottomsheetModeratorAddedOrRemovedAlertBinding.tvAction.setOnClickListener(v -> {

      if (isAdmin) {
        settingsActionCallback.fetchModerators(streamId);
      } else {
        showProgressDialog(getString(R.string.ism_leaving_as_moderator));
        moderatorAddedOrRemovedAlertPresenter.leaveAsModerator(streamId);
      }
    });

    return ismBottomsheetModeratorAddedOrRemovedAlertBinding.getRoot();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
    moderatorAddedOrRemovedAlertPresenter = new ModeratorAddedOrRemovedAlertPresenter();
    moderatorAddedOrRemovedAlertPresenter.attachView(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    moderatorAddedOrRemovedAlertPresenter.detachView();
    activity = null;
  }

  /**
   * {@link ModeratorAddedOrRemovedAlertContract.View#onError(String)}
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
   * @param settingsActionCallback interface to send callback on click of manage moderators
   * @param addedAsModerator whether given user has been added or removed as moderator
   * @param initiatorName name of the user who added/removed moderator
   * @param moderatorName name of the moderator who was added/removed
   */
  public void updateParameters(String streamId, boolean isAdmin,
      SettingsActionCallback settingsActionCallback, boolean addedAsModerator, String initiatorName,
      String moderatorName) {
    this.streamId = streamId;
    this.isAdmin = isAdmin;
    this.settingsActionCallback = settingsActionCallback;
    this.addedAsModerator = addedAsModerator;
    this.initiatorName = initiatorName;
    this.moderatorName = moderatorName;
  }

  /**
   * {@link ModeratorAddedOrRemovedAlertContract.View#onLeftAsModeratorSuccessfully()}
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