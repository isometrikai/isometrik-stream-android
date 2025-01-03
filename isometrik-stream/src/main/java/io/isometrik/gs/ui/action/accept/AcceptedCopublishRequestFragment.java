package io.isometrik.gs.ui.action.accept;

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
import io.isometrik.gs.ui.databinding.IsmBottomsheetAcceptedCopublishBinding;
import io.isometrik.gs.ui.utils.GlideApp;
import io.isometrik.gs.ui.utils.PlaceholderUtils;

/**
 * The Accepted copublish request bottomsheet dialog fragment to show the bottomsheet when copublish
 * request has been
 * accepted.
 */
public class AcceptedCopublishRequestFragment extends BottomSheetDialogFragment {

  public static final String TAG = "AcceptedCopublishRequestFragment";

  private CopublishActionCallbacks copublishActionCallbacks;

  private String userImageUrl, userName;

  private IsmBottomsheetAcceptedCopublishBinding ismBottomsheetAcceptedCopublishBinding;

  /**
   * Instantiates a new Accepted copublish request fragment.
   */
  public AcceptedCopublishRequestFragment() {
    // Required empty public constructor
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (ismBottomsheetAcceptedCopublishBinding == null) {
      ismBottomsheetAcceptedCopublishBinding =
          IsmBottomsheetAcceptedCopublishBinding.inflate(inflater, container, false);
    } else {

      if (ismBottomsheetAcceptedCopublishBinding.getRoot().getParent() != null) {
        ((ViewGroup) ismBottomsheetAcceptedCopublishBinding.getRoot().getParent()).removeView(
            ismBottomsheetAcceptedCopublishBinding.getRoot());
      }
    }
    ismBottomsheetAcceptedCopublishBinding.tvAcceptedHeading.setText(
        getString(R.string.ism_copublish_accepted_heading, userName));
    if (PlaceholderUtils.isValidImageUrl(userImageUrl)) {
      try {
        GlideApp.with(this)
            .load(userImageUrl)
            .placeholder(R.drawable.ism_default_profile_image)
            .transform(new CircleCrop())
            .into(ismBottomsheetAcceptedCopublishBinding.ivUserImage);
      } catch (IllegalArgumentException | NullPointerException ignore) {
      }
    } else {
      PlaceholderUtils.setTextRoundDrawable(getActivity(), userName,
          ismBottomsheetAcceptedCopublishBinding.ivUserImage, 33);
    }
    ismBottomsheetAcceptedCopublishBinding.btStartPublish.setOnClickListener(
        view -> copublishActionCallbacks.startPublishingOnCopublishRequestAccepted());

    return ismBottomsheetAcceptedCopublishBinding.getRoot();
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
