package io.isometrik.gs.ui.action.reject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.action.CopublishActionCallbacks;
import io.isometrik.gs.ui.databinding.IsmBottomsheetRejectedCopublishBinding;
import io.isometrik.gs.ui.utils.GlideApp;

/**
 * The Rejected copublish request bottomsheet dialog fragment to show the bottomsheet when copublish
 * request has been
 * rejected.
 */
public class RejectedCopublishRequestFragment extends BottomSheetDialogFragment {

  public static final String TAG = "RejectedCopublishRequestFragment";

  private CopublishActionCallbacks copublishActionCallbacks;

  private String userImageUrl, userName;

  private IsmBottomsheetRejectedCopublishBinding ismBottomsheetRejectedCopublishBinding;

  /**
   * Instantiates a new Rejected copublish request fragment.
   */
  public RejectedCopublishRequestFragment() {
    // Required empty public constructor
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (ismBottomsheetRejectedCopublishBinding == null) {
      ismBottomsheetRejectedCopublishBinding =
          IsmBottomsheetRejectedCopublishBinding.inflate(inflater, container, false);
    } else {

      if (ismBottomsheetRejectedCopublishBinding.getRoot().getParent() != null) {
        ((ViewGroup) ismBottomsheetRejectedCopublishBinding.getRoot().getParent()).removeView(
            ismBottomsheetRejectedCopublishBinding.getRoot());
      }
    }
    ismBottomsheetRejectedCopublishBinding.tvRejectedHeading.setText(
        getString(R.string.ism_copublish_rejected_heading, userName));
    try {
      GlideApp.with(this)
          .load(userImageUrl)
          .placeholder(R.drawable.ism_default_profile_image)
          .transform(new CircleCrop())
          .into(ismBottomsheetRejectedCopublishBinding.ivUserImage);
    } catch (IllegalArgumentException | NullPointerException ignore) {

    }
    /*
     * Exit on copublish request rejected.
     */
    ismBottomsheetRejectedCopublishBinding.tvExit.setOnClickListener(
        view -> copublishActionCallbacks.exitOnCopublishRequestRejected());
    /*
     * Continue watching on copublish request rejected.
     */
    ismBottomsheetRejectedCopublishBinding.tvContinue.setOnClickListener(
        view -> copublishActionCallbacks.continueWatching());

    return ismBottomsheetRejectedCopublishBinding.getRoot();
  }

  /**
   * Update parameters.
   *
   * @param userImageUrl the user image url
   * @param userName the user name
   * @param copublishActionCallbacks the copublish action callbacks
   */
  public void updateParameters(String userImageUrl, String userName,
      CopublishActionCallbacks copublishActionCallbacks) {
    this.userImageUrl = userImageUrl;
    this.userName = userName;
    this.copublishActionCallbacks = copublishActionCallbacks;
  }
}
