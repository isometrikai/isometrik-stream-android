package io.isometrik.gs.ui.ecommerce.cart.items;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.utils.Constants;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.ecommerce.cart.FetchProductsInCartQuery;
import io.isometrik.gs.builder.ecommerce.cart.RemoveProductFromCartQuery;
import io.isometrik.gs.builder.ecommerce.cart.UpdateProductQuantityInCartQuery;
import io.isometrik.gs.builder.ecommerce.checkout.CheckoutCartQuery;
import io.isometrik.gs.response.ecommerce.checkout.CheckoutCartResult;
import io.isometrik.gs.response.ecommerce.util.CartProduct;
import java.util.ArrayList;

/**
 * The cart items presenter to fetch the products in cart, update quantity in cart and remove
 * product from cart.
 * It implements CartItemsContract.Presenter{@link CartItemsContract.Presenter}
 *
 * @see CartItemsContract.Presenter
 */
public class CartItemsPresenter implements CartItemsContract.Presenter {

  /**
   * Instantiates a new featured products presenter.
   */
  CartItemsPresenter() {

  }

  private CartItemsContract.View cartItemsView;
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
  public void attachView(CartItemsContract.View cartItemsView) {
    this.cartItemsView = cartItemsView;
  }

  @Override
  public void detachView() {
    this.cartItemsView = null;
  }

  @Override
  public void fetchProductsInCart(int skip, int limit, boolean onScroll) {
    isLoading = true;

    if (skip == 0) {
      offset = 0;
      isLastPage = false;
    }

    isometrik.getRemoteUseCases()
        .getEcommerceUseCases()
        .fetchProductsInCart(new FetchProductsInCartQuery.Builder().setLimit(limit)
            .setSkip(skip)
            .setStreamId(streamId)
            .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
            .build(), (var1, var2) -> {
          if (var1 != null) {
            isLoading = false;

            ArrayList<CartProduct> products = var1.getProducts();

            if (products.size() > 0) {
              ArrayList<CartItemsModel> productsModels = new ArrayList<>();
              int size = products.size();

              for (int i = 0; i < size; i++) {

                productsModels.add(new CartItemsModel(products.get(i)));
              }
              if (size < limit) {

                isLastPage = true;
              }
              if (cartItemsView != null) {
                cartItemsView.onProductsInCartFetchedSuccessfully(productsModels, onScroll,
                    var1.getTotalProductsCount());
              }
            } else {
              if (!onScroll) {
                //No products found
                if (cartItemsView != null) {
                  cartItemsView.onProductsInCartFetchedSuccessfully(new ArrayList<>(), false, 0);
                }
              } else {
                isLastPage = true;
              }
            }
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 2) {
              if (!onScroll) {
                //No products found
                if (cartItemsView != null) {
                  cartItemsView.onProductsInCartFetchedSuccessfully(new ArrayList<>(), false, 0);
                }
              } else {
                isLastPage = true;
              }
            } else {
              if (cartItemsView != null) {
                cartItemsView.onError(var2.getErrorMessage());
              }
            }
          }
        });
  }

  @Override
  public void fetchProductsInCartOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {

    if (!isLoading && !isLastPage) {

      int PAGE_SIZE = Constants.PRODUCTS_PAGE_SIZE;
      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount) >= PAGE_SIZE) {

        offset++;
        fetchProductsInCart(offset * PAGE_SIZE, PAGE_SIZE, false);
      }
    }
  }

  @Override
  public void removeProductFromCart(String productId) {
    isometrik.getRemoteUseCases()
        .getEcommerceUseCases()
        .removeProductFromCart(new RemoveProductFromCartQuery.Builder().setProductId(productId)
            .setStreamId(streamId)
            .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
            .build(), (var1, var2) -> {
          if (var1 != null) {
            if (cartItemsView != null) {
              cartItemsView.onProductRemovedFromCartSuccessfully(productId);
            }
          } else {
            if (cartItemsView != null) {
              cartItemsView.onError(var2.getErrorMessage());
            }
          }
        });
  }

  @Override
  public void updateProductQuantityInCart(String productId, String productName, int newQuantity) {
    isometrik.getRemoteUseCases()
        .getEcommerceUseCases()
        .updateProductQuantityInCart(
            new UpdateProductQuantityInCartQuery.Builder().setProductId(productId)
                .setQuantity(newQuantity)
                .setStreamId(streamId)
                .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                .build(), (var1, var2) -> {
              if (var1 != null) {
                if (cartItemsView != null) {
                  cartItemsView.onProductQuantityInCartUpdatedSuccessfully(productId, newQuantity);
                }
              } else if (var2.getHttpResponseCode() == 403 && var2.getRemoteErrorCode() == 3) {
                if (cartItemsView != null) {
                  cartItemsView.insufficientStock(productName, newQuantity);
                }
              } else {
                if (cartItemsView != null) {
                  cartItemsView.onError(var2.getErrorMessage());
                }
              }
            });
  }

  @Override
  public void requestCheckout() {
    isometrik.getRemoteUseCases()
        .getEcommerceUseCases()
        .checkoutCart(new CheckoutCartQuery.Builder().setStreamId(streamId)
            .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
            .build(), (var1, var2) -> {
          if (var1 != null) {
            CheckoutCartResult.CheckoutDetails checkoutDetails = var1.getCheckoutDetails();

            String checkoutUrl = checkoutDetails.getExternalCheckoutLink()
                + "?checkoutSecret="
                + var1.getCheckoutSecret();

            switch (checkoutDetails.getCheckoutLinkAuthType()) {

              case 0:
                //No Auth
                break;

              case 1: {
                //Basic Auth
                checkoutUrl = checkoutUrl
                    + ",basicUsername="
                    + checkoutDetails.getCheckoutLinkBasicUsername()
                    + ",basicPassword="
                    + checkoutDetails.getCheckoutLinkBasicPassword();
                break;
              }
              case 2: {
                //Bearer Token
                checkoutUrl =
                    checkoutUrl + ",bearerToken=" + checkoutDetails.getCheckoutLinkBearerToken();
                break;
              }
              case 3: {
                //Api Key
                checkoutUrl = checkoutUrl
                    + ",key="
                    + checkoutDetails.getCheckoutLinkApiKey()
                    + ",value="
                    + checkoutDetails.getCheckoutLinkApiValue()
                    + ",addTo="
                    + checkoutDetails.getCheckoutLinkApiAddTo();
                break;
              }
            }

            if (cartItemsView != null) {
              cartItemsView.onCheckoutDetailsReceived(checkoutUrl);
            }
          } else {
            if (cartItemsView != null) {
              cartItemsView.onError(var2.getErrorMessage());
            }
          }
        });
  }
}
