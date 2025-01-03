package io.isometrik.gs.ui.ecommerce.cart.alert;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmBottomsheetAddedToCartBinding;
import io.isometrik.gs.ui.ecommerce.EcommerceOperationsCallback;
import io.isometrik.gs.ui.utils.GlideApp;

public class ProductAddedToCartAlertFragment extends BottomSheetDialogFragment {

  public static final String TAG = "ProductAddedToCartAlertBottomSheetFragment";

  private String productImageUrl, productName;
  private CharSequence productPrice;
  private int unitsAddedToCart;
  private EcommerceOperationsCallback ecommerceOperationsCallback;

  private IsmBottomsheetAddedToCartBinding ismBottomsheetAddedToCartBinding;

  public ProductAddedToCartAlertFragment() {
  }

  @SuppressLint("ClickableViewAccessibility")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (ismBottomsheetAddedToCartBinding == null) {
      ismBottomsheetAddedToCartBinding =
          IsmBottomsheetAddedToCartBinding.inflate(inflater, container, false);
    } else {

      if (ismBottomsheetAddedToCartBinding.getRoot().getParent() != null) {
        ((ViewGroup) ismBottomsheetAddedToCartBinding.getRoot().getParent()).removeView(
            ismBottomsheetAddedToCartBinding.getRoot());
      }
    }

    ismBottomsheetAddedToCartBinding.tvQuantity.setText(
        getString(R.string.ism_quantity, unitsAddedToCart));
    ismBottomsheetAddedToCartBinding.tvProductPrice.setText(productPrice);
    ismBottomsheetAddedToCartBinding.tvProductName.setText(productName);

    try {
      GlideApp.with(this)
          .load(productImageUrl)
          .placeholder(R.drawable.ism_ic_product_default_image)
          .transform(new CenterCrop(),
              new RoundedCorners((int) (Resources.getSystem().getDisplayMetrics().density * 6)))
          .into(ismBottomsheetAddedToCartBinding.ivProductImage);
    } catch (IllegalArgumentException | NullPointerException ignore) {

    }
    ismBottomsheetAddedToCartBinding.rlViewCart.setOnClickListener(v -> {
      ecommerceOperationsCallback.viewCartRequested();
      try {
        dismiss();
      } catch (Exception ignore) {
      }
    });
    return ismBottomsheetAddedToCartBinding.getRoot();
  }

  public void updateParameters(String productImageUrl, String productName,
      CharSequence productPrice, int unitsAddedToCart,
      EcommerceOperationsCallback ecommerceOperationsCallback) {
    this.productImageUrl = productImageUrl;
    this.productName = productName;
    this.productPrice = productPrice;
    this.unitsAddedToCart = unitsAddedToCart;
    this.ecommerceOperationsCallback = ecommerceOperationsCallback;
  }
}
