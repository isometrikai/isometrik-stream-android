package io.isometrik.gs.ui.ecommerce.products.featured;

import io.isometrik.gs.ui.utils.BasePresenter;
import java.util.ArrayList;

/**
 * The interface featured products contract containing interfaces Presenter and View to be
 * implemented
 * by the
 * FeaturedProductsPresenter{@link FeaturedProductsPresenter} and
 * FeaturedProductsFragment{@link FeaturedProductsFragment} respectively.
 *
 * @see FeaturedProductsPresenter
 * @see FeaturedProductsFragment
 */
public interface FeaturedProductsContract {
  /**
   * The interface FeaturedProductsContract.Presenter to be implemented by
   * FeaturedProductsPresenter{@link
   * FeaturedProductsPresenter}
   *
   * @see FeaturedProductsFragment
   */
  interface Presenter extends BasePresenter<FeaturedProductsContract.View> {
    /**
     * Initialize.
     *
     * @param streamId the stream id
     */
    void initialize(String streamId);

    void fetchProducts(int skip, int limit, boolean onScroll);

    /**
     * Request products on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void fetchProductsOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);

    void updateFeaturingProductStatus(FeaturedProductsModel product, boolean insertMetadata);
  }

  /**
   * The interface FeaturedProductsContract.View to be implemented by FeaturedProductsFragment{@link
   * FeaturedProductsFragment}
   *
   * @see FeaturedProductsFragment
   */
  interface View {
    void onProductsFetchedSuccessfully(ArrayList<FeaturedProductsModel> productsModels,
        boolean resultsOnScroll);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);

    void onFeaturingProductStatusUpdateSuccessfully(String productId, boolean startFeaturing);
  }
}
