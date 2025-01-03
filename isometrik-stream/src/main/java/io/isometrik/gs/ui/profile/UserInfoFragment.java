package io.isometrik.gs.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmBottomsheetUserdetailsBinding;
import io.isometrik.gs.ui.utils.GlideApp;

public class UserInfoFragment extends BottomSheetDialogFragment {

  public static final String TAG = "UserInfoFragment";

  private String userName, userIdentifier, userImageUrl;

  private IsmBottomsheetUserdetailsBinding ismBottomsheetUserdetailsBinding;

  public UserInfoFragment() {
    // Required empty public constructor
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (ismBottomsheetUserdetailsBinding == null) {
      ismBottomsheetUserdetailsBinding =
          IsmBottomsheetUserdetailsBinding.inflate(inflater, container, false);
    } else {

      if (ismBottomsheetUserdetailsBinding.getRoot().getParent() != null) {
        ((ViewGroup) ismBottomsheetUserdetailsBinding.getRoot().getParent()).removeView(
            ismBottomsheetUserdetailsBinding.getRoot());
      }
    }
    ismBottomsheetUserdetailsBinding.tvUserName.setText(userName);
    ismBottomsheetUserdetailsBinding.tvUserIdetifier.setText(userIdentifier);

    try {
      GlideApp.with(this).load(userImageUrl).placeholder(R.drawable.ism_default_profile_image)
          .transform(new CircleCrop())
          .into(ismBottomsheetUserdetailsBinding.ivUserImage);
    } catch (IllegalArgumentException | NullPointerException ignore) {
    }

    return ismBottomsheetUserdetailsBinding.getRoot();
  }

  public void updateParameters(String userName, String userIdentifier, String userImageUrl) {
    this.userName = userName;
    this.userIdentifier = userIdentifier;
    this.userImageUrl = userImageUrl;
  }
}
