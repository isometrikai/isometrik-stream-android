package io.isometrik.gs.ui.ecommerce.productdetails;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.ecommerce.cart.AddProductToCartQuery;
import io.isometrik.gs.builder.ecommerce.session.FetchCartProductDetailsQuery;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * The product details presenter to fetch the details of a product linked to a broadcast.
 * It implements ProductDetailsContract.Presenter{@link ProductDetailsContract.Presenter}
 *
 * @see ProductDetailsContract.Presenter
 */
public class ProductDetailsPresenter implements ProductDetailsContract.Presenter {

  /**
   * Instantiates a new product details presenter.
   */
  ProductDetailsPresenter() {

  }

  private ProductDetailsContract.View productDetailsView;
  private final Isometrik isometrik = IsometrikStreamSdk.getInstance().getIsometrik();

  @Override
  public void attachView(ProductDetailsContract.View productDetailsView) {
    this.productDetailsView = productDetailsView;
  }

  @Override
  public void detachView() {
    this.productDetailsView = null;
  }

  @Override
  public ArrayList<ProductAttributesModel> parseProductAttributes(JSONObject productMetadata) {

    ArrayList<ProductAttributesModel> productAttributes = new ArrayList<>();
    if (productMetadata != null) {
      try {
        JSONArray attributes = productMetadata.getJSONArray("attributes");
        ProductAttributesModel productAttribute;
        for (int i = 0; i < attributes.length(); i++) {
          JSONObject attribute = attributes.getJSONObject(i);
          Iterator<String> keys = attribute.keys();
          String attributeName = keys.next();

          productAttribute =
              new ProductAttributesModel(attributeName, attribute.getJSONArray(attributeName));

          productAttributes.add(productAttribute);
        }
      } catch (Exception ignore) {
        return productAttributes;
      }
    }
    return productAttributes;
  }

  @Override
  public void checkProductExistenceInCart(String streamId, String productId) {
    isometrik.getRemoteUseCases()
        .getEcommerceUseCases()
        .fetchCartProductDetails(new FetchCartProductDetailsQuery.Builder().setProductId(productId)
            .setStreamId(streamId)
            .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
            .build(), (var1, var2) -> {
          if (var1 != null) {
            if (productDetailsView != null) {
              productDetailsView.productExistenceInCartResult(var1.getProductDetails());
            }
          } else {

            if (productDetailsView != null) {
              productDetailsView.onError(var2.getErrorMessage());
            }
          }
        });
  }

  @Override
  public void addProductToCart(String streamId, String productId, int units) {
    isometrik.getRemoteUseCases()
        .getEcommerceUseCases()
        .addProductToCart(new AddProductToCartQuery.Builder().setProductId(productId)
            .setStreamId(streamId)
            .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
            .setQuantity(units)
            .build(), (var1, var2) -> {
          if (var1 != null) {
            if (productDetailsView != null) {
              productDetailsView.productAddedToCartSuccessfully();
            }
          } else {

            if (productDetailsView != null) {
              productDetailsView.onError(var2.getErrorMessage());
            }
          }
        });
  }
}
