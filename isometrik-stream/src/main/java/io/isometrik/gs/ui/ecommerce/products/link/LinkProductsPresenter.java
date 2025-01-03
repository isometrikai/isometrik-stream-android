package io.isometrik.gs.ui.ecommerce.products.link;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.utils.Constants;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.ecommerce.products.FetchProductsQuery;
import io.isometrik.gs.response.ecommerce.util.Product;
import java.util.ArrayList;

/**
 * The link products presenter to fetch the products that can be linked to a broadcast.
 * It implements LinkProductsContract.Presenter{@link LinkProductsContract.Presenter}
 *
 * @see LinkProductsContract.Presenter
 */
public class LinkProductsPresenter implements LinkProductsContract.Presenter {

  /**
   * Instantiates a new link products presenter.
   */
  LinkProductsPresenter() {

  }

  private LinkProductsContract.View linkProductsView;
  private final Isometrik isometrik = IsometrikStreamSdk.getInstance().getIsometrik();

  private int offset;
  private boolean isLastPage;
  private boolean isLoading;

  @Override
  public void attachView(LinkProductsContract.View linkProductsView) {
    this.linkProductsView = linkProductsView;
  }

  @Override
  public void detachView() {
    this.linkProductsView = null;
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
        .fetchProducts(new FetchProductsQuery.Builder().setLimit(limit).setSkip(skip).build(),
            (var1, var2) -> {
              if (var1 != null) {
                isLoading = false;

                ArrayList<Product> products = var1.getProducts();

                if (products.size() > 0) {
                  ArrayList<LinkProductsModel> productsModels = new ArrayList<>();
                  int size = products.size();

                  for (int i = 0; i < size; i++) {

                    productsModels.add(new LinkProductsModel(products.get(i)));
                  }
                  if (size < limit) {

                    isLastPage = true;
                  }
                  if (linkProductsView != null) {
                    linkProductsView.onProductsFetchedSuccessfully(productsModels, onScroll);
                  }
                } else {
                  if (!onScroll) {
                    //No products found
                    if (linkProductsView != null) {
                      linkProductsView.onProductsFetchedSuccessfully(new ArrayList<>(), false);
                    }
                  } else {
                    isLastPage = true;
                  }
                }
              } else {
                if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {
                  if (!onScroll) {
                    //No products found
                    if (linkProductsView != null) {
                      linkProductsView.onProductsFetchedSuccessfully(new ArrayList<>(), false);
                    }
                  } else {
                    isLastPage = true;
                  }
                } else {
                  if (linkProductsView != null) linkProductsView.onError(var2.getErrorMessage());
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
}
