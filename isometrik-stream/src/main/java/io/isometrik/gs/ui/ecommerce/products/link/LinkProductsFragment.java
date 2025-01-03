package io.isometrik.gs.ui.ecommerce.products.link;

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
import io.isometrik.gs.ui.databinding.IsmBottomsheetLinkProductsBinding;
import io.isometrik.gs.ui.utils.AlertProgress;
import io.isometrik.gs.ui.utils.Constants;
import io.isometrik.gs.ui.utils.RecyclerItemClickListener;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * Bottomsheet dialog fragment to show products which can be linked to a
 * broadcast.Implements interface
 * LinkProductsContract view{@link LinkProductsContract.View}
 *
 * @see LinkProductsContract.View
 */
public class LinkProductsFragment extends BottomSheetDialogFragment
    implements LinkProductsContract.View {

  public static final String TAG = "LinkProductsBottomSheetFragment";

  private LinkProductsContract.Presenter linkProductsPresenter;

  private AlertProgress alertProgress;
  private AlertDialog alertDialog;
  private Activity activity;
  private LinkedProductsCallback linkedProductsCallback;

  private LinearLayoutManager productsLayoutManager;
  private final ArrayList<LinkProductsModel> products = new ArrayList<>();
  private LinkProductsAdapter productsAdapter;

  private LinearLayoutManager selectedProductsLayoutManager;
  private final ArrayList<LinkProductsModel> selectedProducts = new ArrayList<>();
  private SelectedProductsAdapter selectedProductsAdapter;
  private int count;
  private static final int MAXIMUM_PRODUCTS = Constants.MAXIMUM_ALLOWED_PRODUCTS;

  private IsmBottomsheetLinkProductsBinding ismBottomsheetLinkProductsBinding;

  public LinkProductsFragment(LinkedProductsCallback linkedProductsCallback) {
    this.linkedProductsCallback = linkedProductsCallback;
  }

  @SuppressLint("ClickableViewAccessibility")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (ismBottomsheetLinkProductsBinding == null) {
      ismBottomsheetLinkProductsBinding =
          IsmBottomsheetLinkProductsBinding.inflate(inflater, container, false);
    } else {

      if (ismBottomsheetLinkProductsBinding.getRoot().getParent() != null) {
        ((ViewGroup) ismBottomsheetLinkProductsBinding.getRoot().getParent()).removeView(
            ismBottomsheetLinkProductsBinding.getRoot());
      }
    }
    alertProgress = new AlertProgress();

    productsLayoutManager = new LinearLayoutManager(activity);
    ismBottomsheetLinkProductsBinding.rvProducts.setLayoutManager(productsLayoutManager);
    productsAdapter = new LinkProductsAdapter(activity, products);
    ismBottomsheetLinkProductsBinding.rvProducts.addOnScrollListener(productsOnScrollListener);
    ismBottomsheetLinkProductsBinding.rvProducts.setAdapter(productsAdapter);

    selectedProductsLayoutManager =
        new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
    ismBottomsheetLinkProductsBinding.rvProductsSelected.setLayoutManager(
        selectedProductsLayoutManager);
    selectedProductsAdapter = new SelectedProductsAdapter(activity, selectedProducts, this);
    ismBottomsheetLinkProductsBinding.rvProductsSelected.setAdapter(selectedProductsAdapter);

    fetchProducts();

    ismBottomsheetLinkProductsBinding.rvProducts.addOnItemTouchListener(
        new RecyclerItemClickListener(activity, ismBottomsheetLinkProductsBinding.rvProducts,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, int position) {

                if (position >= 0) {

                  LinkProductsModel product = products.get(position);

                  if (product.isSelected()) {
                    product.setSelected(false);
                    count--;
                    removeSelectedProduct(product.getProductId());
                  } else {

                    if (count < MAXIMUM_PRODUCTS) {
                      //Maximum 5 products can be added
                      product.setSelected(true);
                      count++;
                      addSelectedProduct(product);
                    } else {
                      Toast.makeText(activity, getString(R.string.ism_max_products_linked),
                          Toast.LENGTH_SHORT).show();
                    }
                  }
                  updateSelectedProductsText();
                  products.set(position, product);
                  productsAdapter.notifyItemChanged(position);
                }
              }

              @Override
              public void onItemLongClick(View view, int position) {
              }
            }));

    ismBottomsheetLinkProductsBinding.refresh.setOnRefreshListener(this::fetchProducts);

    ismBottomsheetLinkProductsBinding.ivConfirm.setOnClickListener(v -> {
      if (selectedProducts.size() == 0) {
        if (activity != null) {
          Toast.makeText(activity, getString(R.string.ism_link_atleast_one_product),
              Toast.LENGTH_SHORT).show();
        }
      } else {
        ArrayList<String> productIds = new ArrayList<>();
        int size = selectedProducts.size();
        for (int i = 0; i < size; i++) {
          productIds.add(selectedProducts.get(i).getProductId());
        }
        linkedProductsCallback.onProductsSelected(productIds);
        try {
          dismiss();
        } catch (Exception ignore) {
        }
      }
    });
    //To allow scroll on product's recyclerview
    ismBottomsheetLinkProductsBinding.rvProducts.setOnTouchListener((v, event) -> {
      v.getParent().requestDisallowInterceptTouchEvent(true);
      v.onTouchEvent(event);
      return true;
    });
    return ismBottomsheetLinkProductsBinding.getRoot();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
    linkProductsPresenter = new LinkProductsPresenter();
    linkProductsPresenter.attachView(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    linkProductsPresenter.detachView();
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

          linkProductsPresenter.fetchProductsOnScroll(
              productsLayoutManager.findFirstVisibleItemPosition(),
              productsLayoutManager.getChildCount(), productsLayoutManager.getItemCount());
        }
      };

  @Override
  public void onProductsFetchedSuccessfully(ArrayList<LinkProductsModel> productModels,
      boolean resultsOnScroll) {
    if (activity != null) {
      if (!resultsOnScroll) {
        products.clear();
        selectedProducts.clear();
        count = 0;
        selectedProductsAdapter.notifyDataSetChanged();
      }
      products.addAll(productModels);

      activity.runOnUiThread(() -> {
        if (!resultsOnScroll) {
          if (products.size() > 0) {
            ismBottomsheetLinkProductsBinding.tvNoProducts.setVisibility(View.GONE);
            ismBottomsheetLinkProductsBinding.rvProducts.setVisibility(View.VISIBLE);
          } else {
            ismBottomsheetLinkProductsBinding.tvNoProducts.setVisibility(View.VISIBLE);
            ismBottomsheetLinkProductsBinding.rvProducts.setVisibility(View.GONE);
          }
          updateSelectedProductsText();
        }
        productsAdapter.notifyDataSetChanged();
        updateShimmerVisibility(false);
        //hideProgressDialog();
        if (ismBottomsheetLinkProductsBinding.refresh.isRefreshing()) {
          ismBottomsheetLinkProductsBinding.refresh.setRefreshing(false);
        }
      });
    }
  }

  /**
   * {@link LinkProductsContract.View#onError(String)}
   */
  @Override
  public void onError(String errorMessage) {
    if (ismBottomsheetLinkProductsBinding.refresh.isRefreshing()) {
      ismBottomsheetLinkProductsBinding.refresh.setRefreshing(false);
    }
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
    linkProductsPresenter.fetchProducts(0, Constants.PRODUCTS_PAGE_SIZE, false);
  }

  private void removeSelectedProduct(String productId) {

    for (int i = 0; i < selectedProducts.size(); i++) {
      if (selectedProducts.get(i).getProductId().equals(productId)) {
        selectedProducts.remove(i);
        if (i == 0) {
          selectedProductsAdapter.notifyDataSetChanged();
        } else {
          selectedProductsAdapter.notifyItemRemoved(i);
        }
        break;
      }
    }
  }

  private void addSelectedProduct(LinkProductsModel linkProductsModel) {
    selectedProducts.add(linkProductsModel);
    try {
      selectedProductsAdapter.notifyDataSetChanged();
      selectedProductsLayoutManager.scrollToPosition(selectedProducts.size() - 1);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Remove product.
   *
   * @param productId the user id
   */
  public void removeProduct(String productId) {
    int size = products.size();
    for (int i = 0; i < size; i++) {

      if (products.get(i).getProductId().equals(productId)) {

        LinkProductsModel selectMembersModel = products.get(i);
        selectMembersModel.setSelected(false);
        products.set(i, selectMembersModel);
        if (i == 0) {
          productsAdapter.notifyDataSetChanged();
        } else {
          productsAdapter.notifyItemChanged(i);
        }
        count--;
        updateSelectedProductsText();
        break;
      }
    }

    for (int i = 0; i < selectedProducts.size(); i++) {

      if (selectedProducts.get(i).getProductId().equals(productId)) {
        selectedProducts.remove(i);
        if (i == 0) {
          selectedProductsAdapter.notifyDataSetChanged();
        } else {
          selectedProductsAdapter.notifyItemRemoved(i);
        }

        break;
      }
    }
  }

  private void updateSelectedProductsText() {

    if (count > 0) {
      ismBottomsheetLinkProductsBinding.ivConfirm.setVisibility(View.VISIBLE);
      ismBottomsheetLinkProductsBinding.tvHeading.setText(
          getString(R.string.ism_products_selected, String.valueOf(count),
              String.valueOf(MAXIMUM_PRODUCTS)));
    } else {
      ismBottomsheetLinkProductsBinding.ivConfirm.setVisibility(View.GONE);
      ismBottomsheetLinkProductsBinding.tvHeading.setText(getString(R.string.ism_link_products));
    }
  }

  private void updateShimmerVisibility(boolean visible) {
    if (visible) {
      ismBottomsheetLinkProductsBinding.shimmerFrameLayout.startShimmer();
      ismBottomsheetLinkProductsBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
    } else {
      if (ismBottomsheetLinkProductsBinding.shimmerFrameLayout.getVisibility() == View.VISIBLE) {
        ismBottomsheetLinkProductsBinding.shimmerFrameLayout.setVisibility(View.GONE);
        ismBottomsheetLinkProductsBinding.shimmerFrameLayout.stopShimmer();
      }
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
}
