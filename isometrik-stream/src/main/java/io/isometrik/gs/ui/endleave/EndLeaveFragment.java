package io.isometrik.gs.ui.endleave;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmBottomsheetEndLeaveBinding;
import io.isometrik.gs.ui.settings.callbacks.SettingsActionCallback;
import org.jetbrains.annotations.NotNull;

/**
 * Bottomsheet dialog fragment to allow publisher to either stop publishing or leave a
 * broadcast.Host can end a live broadcast as well.Action callbacks are triggered using interface
 * SettingsActionCallback{@link SettingsActionCallback}
 *
 * @see SettingsActionCallback
 */
public class EndLeaveFragment extends BottomSheetDialogFragment {

  public static final String TAG = "EndLeaveFragment";

  private SettingsActionCallback actionCallback;
  private boolean isAdmin;
  private Activity activity;

  private IsmBottomsheetEndLeaveBinding ismBottomsheetEndLeaveBinding;

  public EndLeaveFragment() {
    // Required empty public constructor
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (ismBottomsheetEndLeaveBinding == null) {
      ismBottomsheetEndLeaveBinding =
          IsmBottomsheetEndLeaveBinding.inflate(inflater, container, false);
    } else {

      if (ismBottomsheetEndLeaveBinding.getRoot().getParent() != null) {
        ((ViewGroup) ismBottomsheetEndLeaveBinding.getRoot().getParent()).removeView(
            ismBottomsheetEndLeaveBinding.getRoot());
      }
    }
    if (isAdmin) {
      ismBottomsheetEndLeaveBinding.tvWarning.setText(getString(R.string.ism_warning_end));
      ismBottomsheetEndLeaveBinding.tvAction.setText(getString(R.string.ism_end_broadcast));
      ismBottomsheetEndLeaveBinding.tvCancel.setText(getString(R.string.ism_cancel));
    } else {
      ismBottomsheetEndLeaveBinding.tvWarning.setText(getString(R.string.ism_warning_leave));
      ismBottomsheetEndLeaveBinding.tvAction.setText(getString(R.string.ism_leave_broadcast));
      ismBottomsheetEndLeaveBinding.tvCancel.setText(getString(R.string.ism_stop_publishing));
    }

    ismBottomsheetEndLeaveBinding.tvCancel.setOnClickListener(view -> {
      if (isAdmin) {
        if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) dismiss();
      } else {
        if (actionCallback != null) {
          actionCallback.stopPublishingRequested();
        }
      }
    });

    ismBottomsheetEndLeaveBinding.tvAction.setOnClickListener(view -> {
      if (actionCallback != null) {
        if (isAdmin) {
          actionCallback.endBroadcastRequested();
        } else {
          actionCallback.leaveBroadcastRequested();
        }
      }
    });

    return ismBottomsheetEndLeaveBinding.getRoot();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    activity = null;
  }

  @Override
  public void onAttach(@NotNull Context context) {
    super.onAttach(context);

    if (context instanceof SettingsActionCallback) {
      actionCallback = (SettingsActionCallback) context;
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    actionCallback = null;
  }

  public void updateParameters(boolean isAdmin) {

    this.isAdmin = isAdmin;
  }
}
