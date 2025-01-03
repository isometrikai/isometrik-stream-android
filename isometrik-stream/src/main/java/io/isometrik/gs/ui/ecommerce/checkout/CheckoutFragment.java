package io.isometrik.gs.ui.ecommerce.checkout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.gs.ui.databinding.IsmBottomsheetExternalCheckoutBinding;

public class CheckoutFragment extends BottomSheetDialogFragment {

  public static final String TAG = "CheckoutFragmentBottomSheetFragment";
  private String checkoutUrl;

  private IsmBottomsheetExternalCheckoutBinding ismBottomsheetExternalCheckoutBinding;

  public CheckoutFragment() {
  }

  @SuppressLint("ClickableViewAccessibility")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (ismBottomsheetExternalCheckoutBinding == null) {
      ismBottomsheetExternalCheckoutBinding =
          IsmBottomsheetExternalCheckoutBinding.inflate(inflater, container, false);
    } else {

      if (ismBottomsheetExternalCheckoutBinding.getRoot().getParent() != null) {
        ((ViewGroup) ismBottomsheetExternalCheckoutBinding.getRoot().getParent()).removeView(
            ismBottomsheetExternalCheckoutBinding.getRoot());
      }
    }
    ismBottomsheetExternalCheckoutBinding.webView.getSettings().setJavaScriptEnabled(true);
    ismBottomsheetExternalCheckoutBinding.webView.loadUrl(checkoutUrl);
    return ismBottomsheetExternalCheckoutBinding.getRoot();
  }

  public void updateParameters(String checkoutUrl) {

    this.checkoutUrl = checkoutUrl;
  }
}