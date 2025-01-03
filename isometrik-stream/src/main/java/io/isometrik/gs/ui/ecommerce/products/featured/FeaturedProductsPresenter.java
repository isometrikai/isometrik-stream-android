package io.isometrik.gs.ui.ecommerce.products.featured;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.utils.Constants;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.ecommerce.featuring.UpdateFeaturingProductStatusQuery;
import io.isometrik.gs.builder.ecommerce.session.FetchLinkedProductsQuery;
import io.isometrik.gs.builder.message.AddMetadataQuery;
import io.isometrik.gs.response.ecommerce.session.FetchLinkedProductsResult;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The featured products presenter to fetch the products linked to a broadcast.
 * It implements FeaturedProductsContract.Presenter{@link FeaturedProductsContract.Presenter}
 *
 * @see FeaturedProductsContract.Presenter
 */
public class FeaturedProductsPresenter implements FeaturedProductsContract.Presenter {

  /**
   * Instantiates a new featured products presenter.
   */
  FeaturedProductsPresenter() {

  }

  private FeaturedProductsContract.View featuredProductsView;
  private final Isometrik isometrik = IsometrikStreamSdk.getInstance().getIsometrik();

  private int offset;
  private boolean isLastPage;
  private boolean isLoading;
  private String streamId;

  @Override
  public void initialize(String streamId) {
    this.streamId = streamId;
  }

  @Override
  public void attachView(FeaturedProductsContract.View featuredProductsView) {
    this.featuredProductsView = featuredProductsView;
  }

  @Override
  public void detachView() {
    this.featuredProductsView = null;
  }

  @Override
  public void fetchProducts(int skip, int limit, boolean onScroll) {
    isLoading = true;

    if (skip == 0) {
      offset = 0;
      isLastPage = false;
    }

    isometrik.getRemoteUseCases()
        .getEcommerceUseCases()
        .fetchLinkedProducts(new FetchLinkedProductsQuery.Builder().setLimit(limit)
            .setSkip(skip)
            .setStreamId(streamId)
            .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
            .build(), (var1, var2) -> {
          if (var1 != null) {
            isLoading = false;

            ArrayList<FetchLinkedProductsResult.LinkedProduct> products = var1.getProducts();

            if (products.size() > 0) {
              ArrayList<FeaturedProductsModel> productsModels = new ArrayList<>();
              int size = products.size();

              for (int i = 0; i < size; i++) {

                productsModels.add(new FeaturedProductsModel(products.get(i)));
              }
              if (size < limit) {

                isLastPage = true;
              }
              if (featuredProductsView != null) {
                featuredProductsView.onProductsFetchedSuccessfully(productsModels, onScroll);
              }
            } else {
              if (!onScroll) {
                //No products found
                if (featuredProductsView != null) {
                  featuredProductsView.onProductsFetchedSuccessfully(new ArrayList<>(), false);
                }
              } else {
                isLastPage = true;
              }
            }
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 2) {
              if (!onScroll) {
                //No products found
                if (featuredProductsView != null) {
                  featuredProductsView.onProductsFetchedSuccessfully(new ArrayList<>(), false);
                }
              } else {
                isLastPage = true;
              }
            } else {
              if (featuredProductsView != null) {
                featuredProductsView.onError(var2.getErrorMessage());
              }
            }
          }
        });
  }

  @Override
  public void fetchProductsOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {

    if (!isLoading && !isLastPage) {

      int PAGE_SIZE = Constants.PRODUCTS_PAGE_SIZE;
      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount) >= PAGE_SIZE) {

        offset++;
        fetchProducts(offset * PAGE_SIZE, PAGE_SIZE, false);
      }
    }
  }

  @Override
  public void updateFeaturingProductStatus(FeaturedProductsModel product, boolean insertMetadata) {
    boolean startFeaturing = !product.isFeaturing();

    isometrik.getRemoteUseCases()
        .getEcommerceUseCases()
        .updateFeaturingProductStatus(
            new UpdateFeaturingProductStatusQuery.Builder().setStartFeaturing(startFeaturing)
                .setStreamId(streamId)
                .setProductId(product.getProductId())
                .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                .build(), (var1, var2) -> {
              if (var1 != null) {

                if (insertMetadata) {

                  addProductMetadata(product, startFeaturing);
                }
                if (featuredProductsView != null) {
                  featuredProductsView.onFeaturingProductStatusUpdateSuccessfully(
                      product.getProductId(), startFeaturing);
                }
              } else if (var2.getHttpResponseCode() == 409 && (var2.getRemoteErrorCode() == 0
                  || var2.getRemoteErrorCode() == 1)) {
                //Product already featuring/not featuring for corner case when request timeout happens
                if (featuredProductsView != null) {
                  featuredProductsView.onFeaturingProductStatusUpdateSuccessfully(
                      product.getProductId(), startFeaturing);
                }
              } else {
                if (featuredProductsView != null) {
                  featuredProductsView.onError(var2.getErrorMessage());
                }
              }
            });
  }

  private void addProductMetadata(FeaturedProductsModel product, boolean startFeaturing) {
    JSONObject object = new JSONObject();
    try {
      object.put("productId", product.getProductId());

      if (startFeaturing) {
        object.put("action", "startFeaturing");
        object.put("productName", product.getProductName());
        object.put("unitsInStock", product.getUnitsInStock());
        object.put("productMetadata", product.getProductMetadata().toString());
        object.put("productImageUrl", product.getProductImageUrl());
      } else {
        object.put("action", "stopFeaturing");
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    isometrik.getRemoteUseCases()
        .getMessagesUseCases()
        .addMetadata(new AddMetadataQuery.Builder().setStreamId(streamId)
            .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
            .setMetadata(object)
            .build(), (var1, var2) -> {

        });
  }
}
