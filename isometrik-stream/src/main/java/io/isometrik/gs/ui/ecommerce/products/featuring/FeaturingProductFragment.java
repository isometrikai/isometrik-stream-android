package io.isometrik.gs.ui.ecommerce.products.featuring;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmBottomsheetFeaturingProductBinding;
import io.isometrik.gs.ui.ecommerce.EcommerceOperationsCallback;
import io.isometrik.gs.ui.ecommerce.productdetails.ProductDetailModel;
import io.isometrik.gs.ui.utils.AlertProgress;
import io.isometrik.gs.ui.utils.Constants;
import io.isometrik.gs.ui.utils.GlideApp;

/**
 * Bottomsheet dialog fragment to fetch featuring product and add product to cart if not already in
 * cart.Implements interface
 * FeaturingProductContract view{@link FeaturingProductContract.View}
 *
 * @see FeaturingProductContract.View
 */
public class FeaturingProductFragment extends BottomSheetDialogFragment
    implements FeaturingProductContract.View {

  public static final String TAG = "FeaturingProductBottomSheetFragment";

  private FeaturingProductContract.Presenter featuringProductPresenter;

  private AlertProgress alertProgress;
  private AlertDialog alertDialog;
  private Activity activity;
  private IsmBottomsheetFeaturingProductBinding ismBottomsheetFeaturingProductBinding;

  private String streamId;
  private boolean isAdmin;
  private FeaturingProductModel product;
  private EcommerceOperationsCallback ecommerceOperationsCallback;

  @SuppressLint("ClickableViewAccessibility")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (ismBottomsheetFeaturingProductBinding == null) {
      ismBottomsheetFeaturingProductBinding =
          IsmBottomsheetFeaturingProductBinding.inflate(inflater, container, false);
    } else {

      if (ismBottomsheetFeaturingProductBinding.getRoot().getParent() != null) {
        ((ViewGroup) ismBottomsheetFeaturingProductBinding.getRoot().getParent()).removeView(
            ismBottomsheetFeaturingProductBinding.getRoot());
      }
    }
    alertProgress = new AlertProgress();

    ismBottomsheetFeaturingProductBinding.tvNoFeaturingProduct.setVisibility(View.GONE);
    ismBottomsheetFeaturingProductBinding.rlProductDetails.setVisibility(View.VISIBLE);

    updateShimmerVisibility(true);
    featuringProductPresenter.fetchFeaturingProduct(streamId);

    ismBottomsheetFeaturingProductBinding.btItemDetails.setOnClickListener(v -> {
      ecommerceOperationsCallback.viewProductDetails(new ProductDetailModel(product));
      dismissDialog();
    });

    ismBottomsheetFeaturingProductBinding.btViewInCart.setOnClickListener(v -> {
      ecommerceOperationsCallback.viewCartRequested();
      dismissDialog();
    });

    ismBottomsheetFeaturingProductBinding.btAddToCart.setOnClickListener(v -> {
      if (isAdmin) {
        showProgressDialog(getString(R.string.ism_stopping_featuring));
        featuringProductPresenter.stopFeaturingProduct(streamId, product.getProductId());
      } else {
        showProgressDialog(getString(R.string.ism_adding_to_cart));
        featuringProductPresenter.addProductToCart(streamId, product.getProductId(),
            Constants.DEFAULT_NUMBER_OF_UNITS_TO_ADD_IN_CART);
      }
    });

    return ismBottomsheetFeaturingProductBinding.getRoot();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
    featuringProductPresenter = new FeaturingProductPresenter();
    featuringProductPresenter.attachView(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    featuringProductPresenter.detachView();
    activity = null;
  }

  @Override
  public void onFeaturingProductFetchedSuccessfully(FeaturingProductModel product) {

    this.product = product;
    if (activity != null) {

      activity.runOnUiThread(() -> {
        ismBottomsheetFeaturingProductBinding.tvNoFeaturingProduct.setVisibility(View.GONE);
        ismBottomsheetFeaturingProductBinding.rlProductDetails.setVisibility(View.VISIBLE);
        ismBottomsheetFeaturingProductBinding.tvProductName.setText(product.getProductName());
        ismBottomsheetFeaturingProductBinding.tvPrice.setText(product.getPrice());
        try {
          GlideApp.with(activity)
              .load(product.getProductImageUrl())
              .placeholder(R.drawable.ism_ic_product_default_image)
              .transform(new CenterCrop(),
                  new RoundedCorners((int) (Resources.getSystem().getDisplayMetrics().density * 6)))
              .into(ismBottomsheetFeaturingProductBinding.ivProductImage);
        } catch (IllegalArgumentException | NullPointerException ignore) {

        }

        if (isAdmin) {
          ismBottomsheetFeaturingProductBinding.btAddToCart.setText(
              getString(R.string.ism_stop_featuring));
          ismBottomsheetFeaturingProductBinding.btAddToCart.setVisibility(View.VISIBLE);
          ismBottomsheetFeaturingProductBinding.btViewInCart.setVisibility(View.GONE);
        } else {
          if (product.isProductAlreadyInCart()) {
            ismBottomsheetFeaturingProductBinding.btAddToCart.setVisibility(View.GONE);
            ismBottomsheetFeaturingProductBinding.btViewInCart.setText(
                getString(R.string.ism_units_in_cart, product.getQuantity()));
            ismBottomsheetFeaturingProductBinding.btViewInCart.setVisibility(View.VISIBLE);
          } else {
            ismBottomsheetFeaturingProductBinding.btAddToCart.setText(
                getString(R.string.ism_add_to_cart));
            ismBottomsheetFeaturingProductBinding.btAddToCart.setVisibility(View.VISIBLE);
            ismBottomsheetFeaturingProductBinding.btViewInCart.setVisibility(View.GONE);
          }
        }
        updateShimmerVisibility(false);
      });
    }
  }

  /**
   * {@link FeaturingProductContract.View#onError(String)}
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
        updateShimmerVisibility(false);
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

  private void updateShimmerVisibility(boolean visible) {

    if (visible) {
      ismBottomsheetFeaturingProductBinding.shimmerFrameLayout.startShimmer();
      ismBottomsheetFeaturingProductBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
      ismBottomsheetFeaturingProductBinding.rlShimmer.setVisibility(View.VISIBLE);
    } else {
      if (ismBottomsheetFeaturingProductBinding.shimmerFrameLayout.getVisibility()
          == View.VISIBLE) {
        ismBottomsheetFeaturingProductBinding.shimmerFrameLayout.setVisibility(View.GONE);
        ismBottomsheetFeaturingProductBinding.rlShimmer.setVisibility(View.GONE);
        ismBottomsheetFeaturingProductBinding.shimmerFrameLayout.stopShimmer();
      }
    }
  }

  @Override
  public void noProductCurrentlyFeaturing() {
    hideProgressDialog();
    if (activity != null) {
      activity.runOnUiThread(() -> {
        updateShimmerVisibility(false);
        ismBottomsheetFeaturingProductBinding.tvNoFeaturingProduct.setVisibility(View.VISIBLE);
        ismBottomsheetFeaturingProductBinding.rlProductDetails.setVisibility(View.GONE);
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

  @Override
  public void onFeaturingProductStoppedSuccessfully() {
    hideProgressDialog();
    dismissDialog();
  }

  public void updateParameters(String streamId, boolean isAdmin,
      EcommerceOperationsCallback ecommerceOperationsCallback) {

    this.streamId = streamId;
    this.isAdmin = isAdmin;
    this.ecommerceOperationsCallback = ecommerceOperationsCallback;
  }
}
