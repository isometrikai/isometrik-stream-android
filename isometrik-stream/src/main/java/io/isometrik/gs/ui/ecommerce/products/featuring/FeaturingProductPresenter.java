package io.isometrik.gs.ui.ecommerce.products.featuring;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.ecommerce.cart.AddProductToCartQuery;
import io.isometrik.gs.builder.ecommerce.featuring.FetchFeaturingProductQuery;
import io.isometrik.gs.builder.ecommerce.featuring.UpdateFeaturingProductStatusQuery;

/**
 * The featuring product presenter to fetch the details of the featuring product in a broadcast.
 * It implements FeaturingProductContract.Presenter{@link FeaturingProductContract.Presenter}
 *
 * @see FeaturingProductContract.Presenter
 */
public class FeaturingProductPresenter implements FeaturingProductContract.Presenter {

  /**
   * Instantiates a new product details presenter.
   */
  FeaturingProductPresenter() {

  }

  private FeaturingProductContract.View featuringProductView;
  private final Isometrik isometrik = IsometrikStreamSdk.getInstance().getIsometrik();

  @Override
  public void attachView(FeaturingProductContract.View FeaturingProductView) {
    this.featuringProductView = FeaturingProductView;
  }

  @Override
  public void detachView() {
    this.featuringProductView = null;
  }

  /**
   * {@link FeaturingProductContract.Presenter#fetchFeaturingProduct(String)}
   */
  @Override
  public void fetchFeaturingProduct(String streamId) {
    isometrik.getRemoteUseCases()
        .getEcommerceUseCases()
        .fetchFeaturingProduct(new FetchFeaturingProductQuery.Builder().setStreamId(streamId)
            .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
            .build(), (var1, var2) -> {

          if (var1 != null) {
            if (featuringProductView != null) {
              featuringProductView.onFeaturingProductFetchedSuccessfully(
                  new FeaturingProductModel(var1.getProductDetails()));
            }
          } else if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 2) {
            //No product already featuring
            if (featuringProductView != null) {
              featuringProductView.noProductCurrentlyFeaturing();
            }
          } else {

            if (featuringProductView != null) {
              featuringProductView.onError(var2.getErrorMessage());
            }
          }
        });
  }

  /**
   * {@link FeaturingProductContract.Presenter#addProductToCart(String, String, int)}
   */
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
            if (featuringProductView != null) {
              featuringProductView.productAddedToCartSuccessfully();
            }
          } else {

            if (featuringProductView != null) {
              featuringProductView.onError(var2.getErrorMessage());
            }
          }
        });
  }

  /**
   * {@link FeaturingProductContract.Presenter#stopFeaturingProduct(String, String)}
   */
  @Override
  public void stopFeaturingProduct(String streamId, String productId) {
    isometrik.getRemoteUseCases()
        .getEcommerceUseCases()
        .updateFeaturingProductStatus(
            new UpdateFeaturingProductStatusQuery.Builder().setStartFeaturing(false)
                .setStreamId(streamId)
                .setProductId(productId)
                .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                .build(), (var1, var2) -> {
              if (var1 != null) {
                if (featuringProductView != null) {
                  featuringProductView.onFeaturingProductStoppedSuccessfully();
                }
              } else if (var2.getHttpResponseCode() == 409 && var2.getRemoteErrorCode() == 1) {
                //Product already not featuring for corner case when request timeout happens
                if (featuringProductView != null) {
                  featuringProductView.onFeaturingProductStoppedSuccessfully();
                }
              } else {
                if (featuringProductView != null) {
                  featuringProductView.onError(var2.getErrorMessage());
                }
              }
            });
  }
}
