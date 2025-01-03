package io.isometrik.gs.ui.ecommerce.products.link;

import io.isometrik.gs.ui.utils.BasePresenter;
import java.util.ArrayList;

/**
 * The interface link products contract containing interfaces Presenter and View to be implemented
 * by the
 * LinkProductsPresenter{@link LinkProductsPresenter} and
 * LinkProductsFragment{@link LinkProductsFragment} respectively.
 *
 * @see LinkProductsPresenter
 * @see LinkProductsFragment
 */
public interface LinkProductsContract {
  /**
   * The interface LinkProductsContract.Presenter to be implemented by LinkProductsPresenter{@link
   * LinkProductsPresenter}
   *
   * @see LinkProductsFragment
   */
  interface Presenter extends BasePresenter<View> {

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
  }

  /**
   * The interface LinkProductsContract.View to be implemented by LinkProductsFragment{@link
   * LinkProductsFragment}
   *
   * @see LinkProductsFragment
   */
  interface View {
    void onProductsFetchedSuccessfully(ArrayList<LinkProductsModel> productsModels,
        boolean resultsOnScroll);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);
  }
}
