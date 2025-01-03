package io.isometrik.gs.ui.ecommerce.productdetails;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmBottomsheetAddToCartBinding;
import io.isometrik.gs.ui.ecommerce.EcommerceOperationsCallback;
import io.isometrik.gs.ui.utils.AlertProgress;
import io.isometrik.gs.ui.utils.Constants;
import io.isometrik.gs.ui.utils.GlideApp;
import io.isometrik.gs.response.ecommerce.session.FetchCartProductDetailsResult;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

/**
 * Bottomsheet dialog fragment to fetch product details  and add product to cart if not already in
 * cart.Implements interface
 * ProductDetailsContract view{@link ProductDetailsContract.View}
 *
 * @see ProductDetailsContract.View
 */
public class ProductDetailsFragment extends BottomSheetDialogFragment
    implements ProductDetailsContract.View {

  public static final String TAG = "ProductDetailsBottomSheetFragment";

  private ProductDetailsContract.Presenter productDetailsPresenter;

  private AlertProgress alertProgress;
  private AlertDialog alertDialog;
  private Activity activity;
  private IsmBottomsheetAddToCartBinding ismBottomsheetAddToCartBinding;
  private ProductDetailModel product;
  private String streamId, hostname, hostImageUrl;
  private boolean isAdmin, previewProductFromRecordedVideo;

  private EcommerceOperationsCallback ecommerceOperationsCallback;

  @SuppressLint("ClickableViewAccessibility")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (ismBottomsheetAddToCartBinding == null) {
      ismBottomsheetAddToCartBinding =
          IsmBottomsheetAddToCartBinding.inflate(inflater, container, false);
    } else {

      if (ismBottomsheetAddToCartBinding.getRoot().getParent() != null) {
        ((ViewGroup) ismBottomsheetAddToCartBinding.getRoot().getParent()).removeView(
            ismBottomsheetAddToCartBinding.getRoot());
      }
    }
    alertProgress = new AlertProgress();

    ismBottomsheetAddToCartBinding.tvPrice.setText(product.getPrice());
    ismBottomsheetAddToCartBinding.tvProductName.setText(product.getProductName());

    ismBottomsheetAddToCartBinding.rlBuyOptions.setVisibility(
        previewProductFromRecordedVideo ? View.VISIBLE : View.GONE);
    ismBottomsheetAddToCartBinding.rlViewInCart.setVisibility(View.GONE);

    ismBottomsheetAddToCartBinding.tvStock.setText(
        getString(R.string.ism_units_in_stock, product.getUnitsInStock()));

    if (isAdmin) {
      ismBottomsheetAddToCartBinding.tvHeading.setText(getString(R.string.ism_product_details));
    } else if (previewProductFromRecordedVideo) {
      ismBottomsheetAddToCartBinding.tvHeading.setText(getString(R.string.ism_featuring));
    } else {

      productDetailsPresenter.checkProductExistenceInCart(streamId, product.getProductId());
      ismBottomsheetAddToCartBinding.tvHeading.setText(getString(R.string.ism_add_to_cart));
    }

    try {
      ismBottomsheetAddToCartBinding.tvProductDescription.setText(
          product.getProductMetadata().getString("description"));
    } catch (JSONException ignore) {
    }
    ismBottomsheetAddToCartBinding.rvAttributes.setLayoutManager(new LinearLayoutManager(activity));
    ArrayList<ProductAttributesModel> productAttributes =
        productDetailsPresenter.parseProductAttributes(product.getProductMetadata());

    ProductAttributesAdapter productAttributesAdapter =
        new ProductAttributesAdapter(activity, productAttributes);
    ismBottomsheetAddToCartBinding.rvAttributes.setAdapter(productAttributesAdapter);

    if (previewProductFromRecordedVideo) {
      ismBottomsheetAddToCartBinding.tvHostName.setVisibility(View.GONE);
      ismBottomsheetAddToCartBinding.ivHostImage.setVisibility(View.GONE);
    } else {
      ismBottomsheetAddToCartBinding.tvHostName.setText(hostname);
      try {
        GlideApp.with(this)
            .load(hostImageUrl)
            .placeholder(R.drawable.ism_default_profile_image)
            .transform(new CircleCrop())
            .into(ismBottomsheetAddToCartBinding.ivHostImage);
      } catch (IllegalArgumentException | NullPointerException ignore) {

      }
      ismBottomsheetAddToCartBinding.tvHostName.setVisibility(View.VISIBLE);
      ismBottomsheetAddToCartBinding.ivHostImage.setVisibility(View.VISIBLE);
    }
    try {
      GlideApp.with(this)
          .load(product.getProductImageUrl())
          .placeholder(R.drawable.ism_ic_product_default_image)
          .transform(new CenterCrop(),
              new RoundedCorners((int) (Resources.getSystem().getDisplayMetrics().density * 6)))
          .into(ismBottomsheetAddToCartBinding.ivProductImage);
    } catch (IllegalArgumentException | NullPointerException ignore) {

    }
    //To allow scroll on product's recyclerview
    ismBottomsheetAddToCartBinding.rvAttributes.setOnTouchListener((v, event) -> {
      v.getParent().requestDisallowInterceptTouchEvent(true);
      v.onTouchEvent(event);
      return true;
    });

    ismBottomsheetAddToCartBinding.tvBuyNow.setOnClickListener(v -> {

      if (previewProductFromRecordedVideo) {
        Toast.makeText(activity, R.string.ism_buy_now_not_available, Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(activity, R.string.ism_buy_now_warning, Toast.LENGTH_SHORT).show();
      }
    });

    ismBottomsheetAddToCartBinding.rlAddToCart.setOnClickListener(v -> {

      if (previewProductFromRecordedVideo) {
        Toast.makeText(activity, R.string.ism_add_to_cart_not_available, Toast.LENGTH_SHORT).show();
      } else {
        showProgressDialog(getString(R.string.ism_adding_to_cart));
        productDetailsPresenter.addProductToCart(streamId, product.getProductId(),
            Constants.DEFAULT_NUMBER_OF_UNITS_TO_ADD_IN_CART);
      }
    });

    ismBottomsheetAddToCartBinding.rlViewInCart.setOnClickListener(v -> {
      ecommerceOperationsCallback.viewCartRequested();
      dismissDialog();
    });

    return ismBottomsheetAddToCartBinding.getRoot();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
    productDetailsPresenter = new ProductDetailsPresenter();
    productDetailsPresenter.attachView(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    productDetailsPresenter.detachView();
    activity = null;
  }

  @Override
  public @NotNull Dialog onCreateDialog(Bundle savedInstanceState) {
    BottomSheetDialog bottomSheetDialog =
        (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

    try {
      bottomSheetDialog.setOnShowListener(dialog -> {
        BottomSheetDialog d = (BottomSheetDialog) dialog;
        FrameLayout bottomSheet =
            d.findViewById(com.google.android.material.R.id.design_bottom_sheet);

        //noinspection rawtypes
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
      });
    } catch (Exception ignore) {
    }

    return bottomSheetDialog;
  }

  /**
   * {@link ProductDetailsContract.View#onError(String)}
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

  private void dismissDialog() {
    try {
      dismiss();
    } catch (Exception ignore) {
    }
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

  @Override
  public void productExistenceInCartResult(
      FetchCartProductDetailsResult.CartProduct productDetails) {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        if (productDetails.isProductAlreadyInCart()) {
          ismBottomsheetAddToCartBinding.rlBuyOptions.setVisibility(View.GONE);
          ismBottomsheetAddToCartBinding.rlViewInCart.setVisibility(View.VISIBLE);
          ismBottomsheetAddToCartBinding.tvUnitsInCart.setText(
              getString(R.string.ism_units_in_cart, productDetails.getQuantity()));
        } else {
          ismBottomsheetAddToCartBinding.rlBuyOptions.setVisibility(View.VISIBLE);
          ismBottomsheetAddToCartBinding.rlViewInCart.setVisibility(View.GONE);
        }
      });
    }
  }

  @Override
  public void productAddedToCartSuccessfully() {
    hideProgressDialog();
    ecommerceOperationsCallback.productAddedToCartSuccessfully(product.getProductName(),
        product.getPrice(), product.getProductImageUrl(),
        Constants.DEFAULT_NUMBER_OF_UNITS_TO_ADD_IN_CART);
    dismissDialog();
  }

  public void updateParameters(ProductDetailModel product, String streamId, boolean isAdmin,
      String hostname, String hostImageUrl, boolean previewProductFromRecordedVideo,
      EcommerceOperationsCallback ecommerceOperationsCallback) {
    this.product = product;
    this.streamId = streamId;
    this.isAdmin = isAdmin;
    this.hostname = hostname;
    this.hostImageUrl = hostImageUrl;
    this.ecommerceOperationsCallback = ecommerceOperationsCallback;
    this.previewProductFromRecordedVideo = previewProductFromRecordedVideo;
  }
}
