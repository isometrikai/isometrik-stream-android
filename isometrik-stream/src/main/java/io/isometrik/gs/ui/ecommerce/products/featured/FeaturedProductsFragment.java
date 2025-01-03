package io.isometrik.gs.ui.ecommerce.products.featured;

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
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmBottomsheetFeaturedProductsBinding;
import io.isometrik.gs.ui.ecommerce.EcommerceOperationsCallback;
import io.isometrik.gs.ui.ecommerce.productdetails.ProductDetailModel;
import io.isometrik.gs.ui.utils.AlertProgress;
import io.isometrik.gs.ui.utils.Constants;
import io.isometrik.gs.ui.utils.GlideApp;
import io.isometrik.gs.ui.utils.PlaceholderUtils;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * Bottomsheet dialog fragment to show products that are linked to a
 * broadcast.Implements interface
 * FeaturedProductsContract view{@link FeaturedProductsContract.View}
 *
 * @see FeaturedProductsContract.View
 */
public class FeaturedProductsFragment extends BottomSheetDialogFragment
    implements FeaturedProductsContract.View {

  public static final String TAG = "FeaturedProductsBottomSheetFragment";

  private FeaturedProductsContract.Presenter featuredProductsPresenter;

  private AlertProgress alertProgress;
  private AlertDialog alertDialog;
  private Activity activity;
  private EcommerceOperationsCallback ecommerceOperationsCallback;

  private LinearLayoutManager productsLayoutManager;
  private ArrayList<FeaturedProductsModel> products;
  private FeaturedProductsAdapter productsAdapter;

  private String streamId, hostname, streamDescription, hostImageUrl;
  private boolean isAdmin, insertMetadata;

  private IsmBottomsheetFeaturedProductsBinding ismBottomsheetFeaturedProductsBinding;

  public FeaturedProductsFragment() {
  }

  @SuppressLint("ClickableViewAccessibility")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (ismBottomsheetFeaturedProductsBinding == null) {
      ismBottomsheetFeaturedProductsBinding =
          IsmBottomsheetFeaturedProductsBinding.inflate(inflater, container, false);
    } else {

      if (ismBottomsheetFeaturedProductsBinding.getRoot().getParent() != null) {
        ((ViewGroup) ismBottomsheetFeaturedProductsBinding.getRoot().getParent()).removeView(
            ismBottomsheetFeaturedProductsBinding.getRoot());
      }
    }
    alertProgress = new AlertProgress();

    productsLayoutManager = new LinearLayoutManager(activity);
    ismBottomsheetFeaturedProductsBinding.rvFeaturedProducts.setLayoutManager(
        productsLayoutManager);
    products = new ArrayList<>();
    productsAdapter = new FeaturedProductsAdapter(activity, products, isAdmin, this);
    ismBottomsheetFeaturedProductsBinding.rvFeaturedProducts.addOnScrollListener(
        productsOnScrollListener);
    ismBottomsheetFeaturedProductsBinding.rvFeaturedProducts.setAdapter(productsAdapter);

    featuredProductsPresenter.initialize(streamId);
    ismBottomsheetFeaturedProductsBinding.tvHostName.setText(hostname);
    ismBottomsheetFeaturedProductsBinding.tvStreamDescription.setText(streamDescription);

    if (isAdmin) {
      ismBottomsheetFeaturedProductsBinding.rlViewCart.setVisibility(View.GONE);
    } else {
      ismBottomsheetFeaturedProductsBinding.rlViewCart.setVisibility(View.VISIBLE);
    }
    fetchProducts();
    if (PlaceholderUtils.isValidImageUrl(hostImageUrl)) {
      try {
        GlideApp.with(this)
            .load(hostImageUrl)
            .placeholder(R.drawable.ism_default_profile_image)
            .transform(new CircleCrop())
            .into(ismBottomsheetFeaturedProductsBinding.ivHostImage);
      } catch (IllegalArgumentException | NullPointerException ignore) {

      }
    } else {
      PlaceholderUtils.setTextRoundDrawable(activity, hostname,
          ismBottomsheetFeaturedProductsBinding.ivHostImage, 20);
    }
    ismBottomsheetFeaturedProductsBinding.rlViewCart.setOnClickListener(v -> {
      ecommerceOperationsCallback.viewCartRequested();
      dismissDialog();
    });

    //To allow scroll on product's recyclerview
    ismBottomsheetFeaturedProductsBinding.rvFeaturedProducts.setOnTouchListener((v, event) -> {
      v.getParent().requestDisallowInterceptTouchEvent(true);
      v.onTouchEvent(event);
      return true;
    });
    return ismBottomsheetFeaturedProductsBinding.getRoot();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
    featuredProductsPresenter = new FeaturedProductsPresenter();
    featuredProductsPresenter.attachView(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    featuredProductsPresenter.detachView();
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

          featuredProductsPresenter.fetchProductsOnScroll(
              productsLayoutManager.findFirstVisibleItemPosition(),
              productsLayoutManager.getChildCount(), productsLayoutManager.getItemCount());
        }
      };

  @Override
  public void onProductsFetchedSuccessfully(ArrayList<FeaturedProductsModel> productModels,
      boolean resultsOnScroll) {
    if (activity != null) {
      if (!resultsOnScroll) {
        products.clear();
      }
      products.addAll(productModels);

      activity.runOnUiThread(() -> {

        productsAdapter.notifyDataSetChanged();
        updateShimmerVisibility(false);
        //hideProgressDialog();
      });
    }
  }

  /**
   * {@link FeaturedProductsContract.View#onError(String)}
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

  private void fetchProducts() {
    updateShimmerVisibility(true);
    //showProgressDialog(getString(R.string.ism_fetching_products));
    featuredProductsPresenter.fetchProducts(0, Constants.PRODUCTS_PAGE_SIZE, false);
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

  private void dismissDialog() {
    try {
      dismiss();
    } catch (Exception ignore) {
    }
  }

  private void updateShimmerVisibility(boolean visible) {
    if (visible) {
      ismBottomsheetFeaturedProductsBinding.shimmerFrameLayout.startShimmer();
      ismBottomsheetFeaturedProductsBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
    } else {
      if (ismBottomsheetFeaturedProductsBinding.shimmerFrameLayout.getVisibility()
          == View.VISIBLE) {
        ismBottomsheetFeaturedProductsBinding.shimmerFrameLayout.setVisibility(View.GONE);
        ismBottomsheetFeaturedProductsBinding.shimmerFrameLayout.stopShimmer();
      }
    }
  }

  public void updateParameters(String streamId, boolean isAdmin, String hostname,
      String streamDescription, String hostImageUrl,
      EcommerceOperationsCallback ecommerceOperationsCallback, boolean insertMetadata) {
    this.streamId = streamId;
    this.isAdmin = isAdmin;
    this.hostname = hostname;
    this.streamDescription = streamDescription;
    this.hostImageUrl = hostImageUrl;
    this.ecommerceOperationsCallback = ecommerceOperationsCallback;
    this.insertMetadata = insertMetadata;
  }

  public void updateFeaturingProductStatus(FeaturedProductsModel product) {
    showProgressDialog(getString(R.string.ism_updating_featuring_product_status));
    featuredProductsPresenter.updateFeaturingProductStatus(product, insertMetadata);
  }

  @Override
  public void onFeaturingProductStatusUpdateSuccessfully(String productId, boolean startFeaturing) {
    if (activity != null) {

      activity.runOnUiThread(() -> {
        int size = products.size();
        for (int i = 0; i < size; i++) {

          FeaturedProductsModel featuredProductsModel = products.get(i);

          featuredProductsModel.setFeaturing(
              featuredProductsModel.getProductId().equals(productId) && startFeaturing);

          products.set(i, featuredProductsModel);
        }

        productsAdapter.notifyDataSetChanged();
      });
    }
    hideProgressDialog();
  }

  public void viewProductDetails(FeaturedProductsModel featuredProductsModel) {
    ecommerceOperationsCallback.viewProductDetails(new ProductDetailModel(featuredProductsModel));
    dismissDialog();
  }
}
