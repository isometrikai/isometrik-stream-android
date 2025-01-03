package io.isometrik.gs.ui.ecommerce.products.featuring;

import io.isometrik.gs.ui.utils.BasePresenter;

/**
 * The interface featuring product contract containing interfaces Presenter and View to be
 * implemented
 * by the
 * FeaturingProductPresenter{@link FeaturingProductPresenter} and
 * FeaturingProductFragment{@link FeaturingProductFragment} respectively.
 *
 * @see FeaturingProductPresenter
 * @see FeaturingProductFragment
 */
public interface FeaturingProductContract {
  /**
   * The interface FeaturingProductContract.Presenter to be implemented by
   * FeaturingProductPresenter{@link
   * FeaturingProductPresenter}
   *
   * @see FeaturingProductFragment
   */
  interface Presenter extends BasePresenter<View> {

    /**
     * Fetch featuring product in a broadcast.
     *
     * @param streamId the id of the stream in which to fetch featuring product
     */
    void fetchFeaturingProduct(String streamId);

    void addProductToCart(String streamId, String productId, int units);

    void stopFeaturingProduct(String streamId, String productId);
  }

  /**
   * The interface FeaturingProductContract.View to be implemented by FeaturingProductFragment{@link
   * FeaturingProductFragment}
   *
   * @see FeaturingProductFragment
   */
  interface View {

    void productAddedToCartSuccessfully();

    /**
     * On featuring product being fetched.
     */
    void onFeaturingProductFetchedSuccessfully(FeaturingProductModel product);

    void onFeaturingProductStoppedSuccessfully();

    void noProductCurrentlyFeaturing();

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);
  }
}
