package io.isometrik.gs.ui.ecommerce.cart.items;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmBottomsheetCartItemsBinding;
import io.isometrik.gs.ui.ecommerce.EcommerceOperationsCallback;
import io.isometrik.gs.ui.utils.AlertProgress;
import io.isometrik.gs.ui.utils.Constants;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * Bottomsheet dialog fragment to show products in cart.Implements interface
 * CartItemsContract view{@link CartItemsContract.View}
 *
 * @see CartItemsContract.View
 */
public class CartItemsFragment extends BottomSheetDialogFragment implements CartItemsContract.View {

  public static final String TAG = "CartItemsBottomSheetFragment";

  private CartItemsContract.Presenter cartItemsPresenter;
  private AlertProgress alertProgress;
  private AlertDialog alertDialog;
  private Activity activity;
  private LinearLayoutManager productsLayoutManager;

  private ArrayList<CartItemsModel> products;
  private CartItemsAdapter productsAdapter;
  private String streamId, hostname;
  private IsmBottomsheetCartItemsBinding ismBottomsheetCartItemsBinding;
  private EcommerceOperationsCallback ecommerceOperationsCallback;

  public CartItemsFragment() {
  }

  @SuppressLint("ClickableViewAccessibility")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (ismBottomsheetCartItemsBinding == null) {
      ismBottomsheetCartItemsBinding =
          IsmBottomsheetCartItemsBinding.inflate(inflater, container, false);
    } else {

      if (ismBottomsheetCartItemsBinding.getRoot().getParent() != null) {
        ((ViewGroup) ismBottomsheetCartItemsBinding.getRoot().getParent()).removeView(
            ismBottomsheetCartItemsBinding.getRoot());
      }
    }
    alertProgress = new AlertProgress();

    productsLayoutManager = new LinearLayoutManager(activity);
    ismBottomsheetCartItemsBinding.rvCartProducts.setLayoutManager(productsLayoutManager);
    products = new ArrayList<>();
    productsAdapter = new CartItemsAdapter(activity, products, this);
    ismBottomsheetCartItemsBinding.rvCartProducts.addOnScrollListener(productsOnScrollListener);
    ismBottomsheetCartItemsBinding.rvCartProducts.setAdapter(productsAdapter);

    cartItemsPresenter.initialize(streamId);
    ismBottomsheetCartItemsBinding.tvHeading.setText(
        getString(R.string.ism_sold_shipped, hostname));

    ismBottomsheetCartItemsBinding.tvProductsCount.setText("");
    ismBottomsheetCartItemsBinding.tvCheckout.setVisibility(View.GONE);
    ismBottomsheetCartItemsBinding.rvCartProducts.setVisibility(View.VISIBLE);
    ismBottomsheetCartItemsBinding.tvNoProducts.setVisibility(View.GONE);

    fetchProducts();

    //To allow scroll on product's recyclerview
    ismBottomsheetCartItemsBinding.rvCartProducts.setOnTouchListener((v, event) -> {
      v.getParent().requestDisallowInterceptTouchEvent(true);
      v.onTouchEvent(event);
      return true;
    });

    ismBottomsheetCartItemsBinding.tvCheckout.setOnClickListener(v -> {
      showProgressDialog(getString(R.string.ism_requesting_checkout));
      cartItemsPresenter.requestCheckout();
    });

    return ismBottomsheetCartItemsBinding.getRoot();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
    cartItemsPresenter = new CartItemsPresenter();
    cartItemsPresenter.attachView(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    cartItemsPresenter.detachView();
    activity = null;
  }

  public RecyclerView.OnScrollListener productsOnScrollListener =
      new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);

          cartItemsPresenter.fetchProductsInCartOnScroll(
              productsLayoutManager.findFirstVisibleItemPosition(),
              productsLayoutManager.getChildCount(), productsLayoutManager.getItemCount());
        }
      };

  @Override
  public void onProductsInCartFetchedSuccessfully(ArrayList<CartItemsModel> productModels,
      boolean resultsOnScroll, int productsCount) {
    if (activity != null) {
      if (!resultsOnScroll) {
        products.clear();
        ismBottomsheetCartItemsBinding.tvProductsCount.setText(
            productsCount > 1 ? getString(R.string.ism_cart_products_count, productsCount)
                : getString(R.string.ism_cart_product_count, productsCount));
      }
      products.addAll(productModels);

      activity.runOnUiThread(() -> {
        if (!resultsOnScroll) {
          if (products.size() > 0) {

            ismBottomsheetCartItemsBinding.tvCheckout.setVisibility(View.VISIBLE);
            ismBottomsheetCartItemsBinding.tvNoProducts.setVisibility(View.GONE);
            ismBottomsheetCartItemsBinding.rvCartProducts.setVisibility(View.VISIBLE);
          } else {
            ismBottomsheetCartItemsBinding.tvCheckout.setVisibility(View.GONE);
            ismBottomsheetCartItemsBinding.tvNoProducts.setVisibility(View.VISIBLE);
            ismBottomsheetCartItemsBinding.rvCartProducts.setVisibility(View.GONE);
          }
        }
        productsAdapter.notifyDataSetChanged();
        updateShimmerVisibility(false);
        //hideProgressDialog();
      });
    }
  }

  /**
   * {@link CartItemsContract.View#onError(String)}
   */
  @Override
  public void onError(String errorMessage) {

    updateShimmerVisibility(false);
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

  private void fetchProducts() {
    updateShimmerVisibility(true);
    //showProgressDialog(getString(R.string.ism_fetching_products));
    cartItemsPresenter.fetchProductsInCart(0, Constants.PRODUCTS_PAGE_SIZE, false);
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
      ismBottomsheetCartItemsBinding.shimmerFrameLayout.startShimmer();
      ismBottomsheetCartItemsBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
    } else {
      if (ismBottomsheetCartItemsBinding.shimmerFrameLayout.getVisibility() == View.VISIBLE) {
        ismBottomsheetCartItemsBinding.shimmerFrameLayout.setVisibility(View.GONE);
        ismBottomsheetCartItemsBinding.shimmerFrameLayout.stopShimmer();
      }
    }
  }

  public void updateParameters(String streamId, String hostname,
      EcommerceOperationsCallback ecommerceOperationsCallback) {
    this.streamId = streamId;
    this.hostname = hostname;
    this.ecommerceOperationsCallback = ecommerceOperationsCallback;
  }

  public void updateProductQuantityInCart(String productId, String productName, boolean increment,
      int currentQuantity) {

    if (currentQuantity == 1 && !increment) {
      if (activity != null) {
        Toast.makeText(activity, R.string.ism_minimum_units_in_cart, Toast.LENGTH_SHORT).show();
      }
    } else {
      showProgressDialog(getString(R.string.ism_updating_quantity_in_cart));
      cartItemsPresenter.updateProductQuantityInCart(productId, productName,
          increment ? currentQuantity + 1 : currentQuantity - 1);
    }
  }

  public void removeProductFromCart(String productId) {
    showProgressDialog(getString(R.string.ism_removing_product_from_cart));
    cartItemsPresenter.removeProductFromCart(productId);
  }

  @Override
  public void onProductRemovedFromCartSuccessfully(String productId) {

    if (activity != null) {
      activity.runOnUiThread(() -> {
        int size = products.size();
        for (int i = 0; i < size; i++) {
          if (products.get(i).getProductId().equals(productId)) {
            products.remove(i);
            productsAdapter.notifyItemRemoved(i);
            if (size == 1) {
              ismBottomsheetCartItemsBinding.tvCheckout.setVisibility(View.GONE);
              ismBottomsheetCartItemsBinding.tvNoProducts.setVisibility(View.VISIBLE);
              ismBottomsheetCartItemsBinding.rvCartProducts.setVisibility(View.GONE);
            }
            int productsCount = size - 1;
            ismBottomsheetCartItemsBinding.tvProductsCount.setText(
                productsCount > 1 ? getString(R.string.ism_cart_products_count, productsCount)
                    : getString(R.string.ism_cart_product_count, productsCount));
            break;
          }
        }
      });
    }

    hideProgressDialog();
  }

  @Override
  public void onProductQuantityInCartUpdatedSuccessfully(String productId, int newQuantity) {

    if (activity != null) {
      activity.runOnUiThread(() -> {
        int size = products.size();
        for (int i = 0; i < size; i++) {
          if (products.get(i).getProductId().equals(productId)) {
            CartItemsModel cartItemsModel = products.get(i);
            cartItemsModel.setUnitsInCart(newQuantity);
            products.set(i, cartItemsModel);
            productsAdapter.notifyItemChanged(i);
            break;
          }
        }
      });
    }
    hideProgressDialog();
  }

  @Override
  public void insufficientStock(String productName, int newQuantity) {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        Toast.makeText(activity,
            getString(R.string.ism_maximum_units_in_cart, newQuantity - 1, productName),
            Toast.LENGTH_SHORT).show();
      });
    }
    hideProgressDialog();
  }

  @Override
  public void onCheckoutDetailsReceived(String checkoutUrl) {
    hideProgressDialog();
    ecommerceOperationsCallback.checkoutRequested(checkoutUrl);
    try {
      dismiss();
    } catch (Exception ignore) {
    }
  }
}
