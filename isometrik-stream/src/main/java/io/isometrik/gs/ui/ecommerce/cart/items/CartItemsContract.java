package io.isometrik.gs.ui.ecommerce.cart.items;

import io.isometrik.gs.ui.utils.BasePresenter;
import java.util.ArrayList;

/**
 * The interface cart items contract containing interfaces Presenter and View to be
 * implemented
 * by the
 * CartItemsPresenter{@link CartItemsPresenter} and
 * CartItemsFragment{@link CartItemsFragment} respectively.
 *
 * @see CartItemsPresenter
 * @see CartItemsFragment
 */
public interface CartItemsContract {
  /**
   * The interface CartItemsContract.Presenter to be implemented by
   * CartItemsPresenter{@link
   * CartItemsPresenter}
   *
   * @see CartItemsFragment
   */
  interface Presenter extends BasePresenter<CartItemsContract.View> {
    /**
     * Initialize.
     *
     * @param streamId the stream id
     */
    void initialize(String streamId);

    void fetchProductsInCart(int skip, int limit, boolean onScroll);

    /**
     * Request products in cart on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void fetchProductsInCartOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);

    void removeProductFromCart(String productId);

    void updateProductQuantityInCart(String productId, String productName, int newQuantity);

    void requestCheckout();
  }

  /**
   * The interface CartItemsContract.View to be implemented by CartItemsFragment{@link
   * CartItemsFragment}
   *
   * @see CartItemsFragment
   */
  interface View {

    void onProductsInCartFetchedSuccessfully(ArrayList<CartItemsModel> productsModels,
        boolean resultsOnScroll, int totalProductsCount);

    void onProductRemovedFromCartSuccessfully(String productId);

    void onProductQuantityInCartUpdatedSuccessfully(String productId, int newQuantity);

    void insufficientStock(String productName, int newQuantity);

    void onCheckoutDetailsReceived(String checkoutUrl);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);
  }
}
