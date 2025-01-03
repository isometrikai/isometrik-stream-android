package io.isometrik.gs.ui.action.request;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.action.CopublishActionCallbacks;
import io.isometrik.gs.ui.databinding.IsmBottomsheetRequestCopublishBinding;
import io.isometrik.gs.ui.utils.GlideApp;
import io.isometrik.gs.ui.utils.PlaceholderUtils;

/**
 * The Request copublish bottomsheet dialog fragment to add a copublish request for a stream group.
 */
public class RequestCopublishFragment extends BottomSheetDialogFragment {

  public static final String TAG = "RequestCopublishFragment";

  private String initiatorImageUrl, userImageUrl, userName, initiatorName;

  private CopublishActionCallbacks copublishActionCallbacks;
  private IsmBottomsheetRequestCopublishBinding ismBottomsheetRequestCopublishBinding;

  /**
   * Instantiates a new Request copublish fragment.
   */
  public RequestCopublishFragment() {
    // Required empty public constructor
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {

    if (ismBottomsheetRequestCopublishBinding == null) {
      ismBottomsheetRequestCopublishBinding =
              IsmBottomsheetRequestCopublishBinding.inflate(inflater, container, false);
    } else {

      if (ismBottomsheetRequestCopublishBinding.getRoot().getParent() != null) {
        ((ViewGroup) ismBottomsheetRequestCopublishBinding.getRoot().getParent()).removeView(
                ismBottomsheetRequestCopublishBinding.getRoot());
      }
    }
    if (PlaceholderUtils.isValidImageUrl(initiatorImageUrl)) {
      try {
        GlideApp.with(this)
                .load(initiatorImageUrl)
                .placeholder(R.drawable.ism_default_profile_image)
                .transform(new CircleCrop())
                .into(ismBottomsheetRequestCopublishBinding.ivInitiatorImage);
      } catch (IllegalArgumentException | NullPointerException ignore) {

      }
    } else {
      PlaceholderUtils.setTextRoundDrawable(IsometrikStreamSdk.getInstance().getContext(), initiatorName,
              ismBottomsheetRequestCopublishBinding.ivInitiatorImage, 16);
    }
    if (PlaceholderUtils.isValidImageUrl(userImageUrl)) {
      try {
        GlideApp.with(this)
                .load(userImageUrl)
                .placeholder(R.drawable.ism_default_profile_image)
                .transform(new CircleCrop())
                .into(ismBottomsheetRequestCopublishBinding.ivUserImage);
      } catch (IllegalArgumentException | NullPointerException ignore) {

      }
    } else {
      PlaceholderUtils.setTextRoundDrawable(IsometrikStreamSdk.getInstance().getContext(), userName,
              ismBottomsheetRequestCopublishBinding.ivUserImage, 16);
    }
    ismBottomsheetRequestCopublishBinding.btAction.setOnClickListener(
            view -> copublishActionCallbacks.requestCopublish());

    return ismBottomsheetRequestCopublishBinding.getRoot();
  }

  /**
   * Update parameters.
   *
   * @param initiatorImageUrl the initiator image url
   * @param userImageUrl the user image url
   * @param copublishActionCallbacks the copublish action callbacks
   */
  public void updateParameters(String initiatorImageUrl, String userImageUrl,
                               CopublishActionCallbacks copublishActionCallbacks, String userName, String initiatorName) {
    this.initiatorImageUrl = initiatorImageUrl;
    this.userImageUrl = userImageUrl;
    this.copublishActionCallbacks = copublishActionCallbacks;
    this.userName = userName;
    this.initiatorName = initiatorName;
  }
}
