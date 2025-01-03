package io.isometrik.gs.ui.ecommerce.productdetails;

import io.isometrik.gs.ui.utils.BasePresenter;
import io.isometrik.gs.response.ecommerce.session.FetchCartProductDetailsResult;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 * The interface product details contract containing interfaces Presenter and View to be
 * implemented
 * by the
 * ProductDetailsPresenter{@link ProductDetailsPresenter} and
 * ProductDetailsFragment{@link ProductDetailsFragment} respectively.
 *
 * @see ProductDetailsPresenter
 * @see ProductDetailsFragment
 */
public interface ProductDetailsContract {
  /**
   * The interface ProductDetailsContract.Presenter to be implemented by
   * ProductDetailsPresenter{@link
   * ProductDetailsPresenter}
   *
   * @see ProductDetailsFragment
   */
  interface Presenter extends BasePresenter<ProductDetailsContract.View> {

    ArrayList<ProductAttributesModel> parseProductAttributes(JSONObject productMetadata);

    void checkProductExistenceInCart(String streamId, String productId);

    void addProductToCart(String streamId, String productId, int units);
  }

  /**
   * The interface ProductDetailsContract.View to be implemented by ProductDetailsFragment{@link
   * ProductDetailsFragment}
   *
   * @see ProductDetailsFragment
   */
  interface View {

    void productAddedToCartSuccessfully();

    void productExistenceInCartResult(FetchCartProductDetailsResult.CartProduct productDetails);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);
  }
}
