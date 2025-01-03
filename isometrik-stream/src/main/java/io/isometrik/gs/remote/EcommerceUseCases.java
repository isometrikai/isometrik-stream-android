package io.isometrik.gs.remote;

import com.google.gson.Gson;
import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.ecommerce.cart.AddProductToCartQuery;
import io.isometrik.gs.builder.ecommerce.cart.FetchProductsInCartQuery;
import io.isometrik.gs.builder.ecommerce.cart.RemoveProductFromCartQuery;
import io.isometrik.gs.builder.ecommerce.cart.UpdateProductQuantityInCartQuery;
import io.isometrik.gs.builder.ecommerce.checkout.CheckoutCartQuery;
import io.isometrik.gs.builder.ecommerce.checkout.VerifyCheckoutQuery;
import io.isometrik.gs.builder.ecommerce.featuring.FetchFeaturingProductQuery;
import io.isometrik.gs.builder.ecommerce.featuring.UpdateFeaturingProductStatusQuery;
import io.isometrik.gs.builder.ecommerce.products.AddProductQuery;
import io.isometrik.gs.builder.ecommerce.products.DeleteProductQuery;
import io.isometrik.gs.builder.ecommerce.products.FetchProductDetailsQuery;
import io.isometrik.gs.builder.ecommerce.products.FetchProductsQuery;
import io.isometrik.gs.builder.ecommerce.products.UpdateProductInventoryQuery;
import io.isometrik.gs.builder.ecommerce.products.UpdateProductQuery;
import io.isometrik.gs.builder.ecommerce.session.FetchCartProductDetailsQuery;
import io.isometrik.gs.builder.ecommerce.session.FetchLinkedProductsQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.models.ecommerce.cart.AddProductToCart;
import io.isometrik.gs.models.ecommerce.cart.FetchProductsInCart;
import io.isometrik.gs.models.ecommerce.cart.RemoveProductFromCart;
import io.isometrik.gs.models.ecommerce.cart.UpdateProductQuantityInCart;
import io.isometrik.gs.models.ecommerce.checkout.CheckoutCart;
import io.isometrik.gs.models.ecommerce.checkout.VerifyCheckout;
import io.isometrik.gs.models.ecommerce.featuring.FetchFeaturingProduct;
import io.isometrik.gs.models.ecommerce.featuring.UpdateFeaturingProduct;
import io.isometrik.gs.models.ecommerce.products.AddProduct;
import io.isometrik.gs.models.ecommerce.products.DeleteProduct;
import io.isometrik.gs.models.ecommerce.products.FetchProductDetails;
import io.isometrik.gs.models.ecommerce.products.FetchProducts;
import io.isometrik.gs.models.ecommerce.products.UpdateProduct;
import io.isometrik.gs.models.ecommerce.products.UpdateProductInventory;
import io.isometrik.gs.models.ecommerce.session.FetchCartProductDetails;
import io.isometrik.gs.models.ecommerce.session.FetchLinkedProducts;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.ecommerce.cart.AddProductToCartResult;
import io.isometrik.gs.response.ecommerce.cart.FetchProductsInCartResult;
import io.isometrik.gs.response.ecommerce.cart.RemoveProductFromCartResult;
import io.isometrik.gs.response.ecommerce.cart.UpdateProductQuantityInCartResult;
import io.isometrik.gs.response.ecommerce.checkout.CheckoutCartResult;
import io.isometrik.gs.response.ecommerce.checkout.VerifyCheckoutResult;
import io.isometrik.gs.response.ecommerce.featuring.FetchFeaturingProductResult;
import io.isometrik.gs.response.ecommerce.featuring.UpdateFeaturingProductStatusResult;
import io.isometrik.gs.response.ecommerce.products.AddProductResult;
import io.isometrik.gs.response.ecommerce.products.DeleteProductResult;
import io.isometrik.gs.response.ecommerce.products.FetchProductDetailsResult;
import io.isometrik.gs.response.ecommerce.products.FetchProductsResult;
import io.isometrik.gs.response.ecommerce.products.UpdateProductInventoryResult;
import io.isometrik.gs.response.ecommerce.products.UpdateProductResult;
import io.isometrik.gs.response.ecommerce.session.FetchCartProductDetailsResult;
import io.isometrik.gs.response.ecommerce.session.FetchLinkedProductsResult;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.IsometrikError;
import org.jetbrains.annotations.NotNull;

/**
 * Classes containing use cases for various ecommerce operations, allowing ui sdk to communicate
 * with the remote backend using respective model classes.
 */
public class EcommerceUseCases {
  /**
   * Model classes for ecommerce
   */
  private final AddProductToCart addProductToCart;
  private final FetchProductsInCart fetchProductsInCart;
  private final RemoveProductFromCart removeProductFromCart;
  private final UpdateProductQuantityInCart updateProductQuantityInCart;
  private final CheckoutCart checkoutCart;
  private final VerifyCheckout verifyCheckout;
  private final AddProduct addProduct;
  private final DeleteProduct deleteProduct;
  private final FetchProductDetails fetchProductDetails;
  private final FetchProducts fetchProducts;
  private final UpdateProduct updateProduct;
  private final UpdateProductInventory updateProductInventory;
  private final FetchLinkedProducts fetchLinkedProducts;
  private final FetchCartProductDetails fetchCartProductDetails;
  private final FetchFeaturingProduct fetchFeaturingProduct;
  private final UpdateFeaturingProduct updateFeaturingProduct;

  private final IMConfiguration configuration;
  private final RetrofitManager retrofitManager;
  private final BaseResponse baseResponse;
  private final Gson gson;

  public EcommerceUseCases(IMConfiguration imConfiguration, RetrofitManager retrofitManager,
      BaseResponse baseResponse, Gson gson) {
    configuration = imConfiguration;
    this.retrofitManager = retrofitManager;
    this.gson = gson;
    this.baseResponse = baseResponse;

    addProductToCart = new AddProductToCart();
    fetchProductsInCart = new FetchProductsInCart();
    removeProductFromCart = new RemoveProductFromCart();
    updateProductQuantityInCart = new UpdateProductQuantityInCart();
    checkoutCart = new CheckoutCart();
    verifyCheckout = new VerifyCheckout();
    addProduct = new AddProduct();
    deleteProduct = new DeleteProduct();
    fetchProductDetails = new FetchProductDetails();
    fetchProducts = new FetchProducts();
    updateProduct = new UpdateProduct();
    updateProductInventory = new UpdateProductInventory();
    fetchLinkedProducts = new FetchLinkedProducts();
    fetchCartProductDetails = new FetchCartProductDetails();
    fetchFeaturingProduct = new FetchFeaturingProduct();
    updateFeaturingProduct = new UpdateFeaturingProduct();
  }

  /**
   * Add product to cart.
   *
   * @param addProductToCartQuery the add product to cart query
   * @param completionHandler the completion handler
   */
  public void addProductToCart(@NotNull AddProductToCartQuery addProductToCartQuery,
      @NotNull CompletionHandler<AddProductToCartResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      addProductToCart.validateParams(addProductToCartQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch products in cart.
   *
   * @param fetchProductsInCartQuery the fetch products in cart query
   * @param completionHandler the completion handler
   */
  public void fetchProductsInCart(@NotNull FetchProductsInCartQuery fetchProductsInCartQuery,
      @NotNull CompletionHandler<FetchProductsInCartResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchProductsInCart.validateParams(fetchProductsInCartQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Remove product from cart.
   *
   * @param removeProductFromCartQuery the remove product from cart query
   * @param completionHandler the completion handler
   */
  public void removeProductFromCart(@NotNull RemoveProductFromCartQuery removeProductFromCartQuery,
      @NotNull CompletionHandler<RemoveProductFromCartResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      removeProductFromCart.validateParams(removeProductFromCartQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Update product quantity in cart.
   *
   * @param updateProductQuantityInCartQuery the update product quantity in cart query
   * @param completionHandler the completion handler
   */
  public void updateProductQuantityInCart(
      @NotNull UpdateProductQuantityInCartQuery updateProductQuantityInCartQuery,
      @NotNull CompletionHandler<UpdateProductQuantityInCartResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      updateProductQuantityInCart.validateParams(updateProductQuantityInCartQuery,
          completionHandler, retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Checkout cart.
   *
   * @param checkoutCartQuery the checkout cart query
   * @param completionHandler the completion handler
   */
  public void checkoutCart(@NotNull CheckoutCartQuery checkoutCartQuery,
      @NotNull CompletionHandler<CheckoutCartResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      checkoutCart.validateParams(checkoutCartQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Verify checkout.
   *
   * @param verifyCheckoutQuery the verify checkout query
   * @param completionHandler the completion handler
   */
  public void verifyCheckout(@NotNull VerifyCheckoutQuery verifyCheckoutQuery,
      @NotNull CompletionHandler<VerifyCheckoutResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      verifyCheckout.validateParams(verifyCheckoutQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Add product.
   *
   * @param addProductQuery the add product query
   * @param completionHandler the completion handler
   */
  public void addProduct(@NotNull AddProductQuery addProductQuery,
      @NotNull CompletionHandler<AddProductResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(false);
    if (isometrikError == null) {
      addProduct.validateParams(addProductQuery, completionHandler, retrofitManager, configuration,
          baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Delete product.
   *
   * @param deleteProductQuery the delete product query
   * @param completionHandler the completion handler
   */
  public void deleteProduct(@NotNull DeleteProductQuery deleteProductQuery,
      @NotNull CompletionHandler<DeleteProductResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(false);
    if (isometrikError == null) {
      deleteProduct.validateParams(deleteProductQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch product details.
   *
   * @param fetchProductDetailsQuery the fetch product details query
   * @param completionHandler the completion handler
   */
  public void fetchProductDetails(@NotNull FetchProductDetailsQuery fetchProductDetailsQuery,
      @NotNull CompletionHandler<FetchProductDetailsResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchProductDetails.validateParams(fetchProductDetailsQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch products.
   *
   * @param fetchProductsQuery the fetch products query
   * @param completionHandler the completion handler
   */
  public void fetchProducts(@NotNull FetchProductsQuery fetchProductsQuery,
      @NotNull CompletionHandler<FetchProductsResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchProducts.validateParams(fetchProductsQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Update product.
   *
   * @param updateProductQuery the update product query
   * @param completionHandler the completion handler
   */
  public void updateProduct(@NotNull UpdateProductQuery updateProductQuery,
      @NotNull CompletionHandler<UpdateProductResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(false);
    if (isometrikError == null) {
      updateProduct.validateParams(updateProductQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Update product inventory.
   *
   * @param updateProductInventoryQuery the update product inventory query
   * @param completionHandler the completion handler
   */
  public void updateProductInventory(
      @NotNull UpdateProductInventoryQuery updateProductInventoryQuery,
      @NotNull CompletionHandler<UpdateProductInventoryResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(false);
    if (isometrikError == null) {
      updateProductInventory.validateParams(updateProductInventoryQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch linked products to a stream session.
   *
   * @param fetchLinkedProductsQuery the fetch linked products query
   * @param completionHandler the completion handler
   */
  public void fetchLinkedProducts(@NotNull FetchLinkedProductsQuery fetchLinkedProductsQuery,
      @NotNull CompletionHandler<FetchLinkedProductsResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchLinkedProducts.validateParams(fetchLinkedProductsQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch cart product details.
   *
   * @param fetchCartProductDetailsQuery the fetch cart product details query
   * @param completionHandler the completion handler
   */
  public void fetchCartProductDetails(
      @NotNull FetchCartProductDetailsQuery fetchCartProductDetailsQuery,
      @NotNull CompletionHandler<FetchCartProductDetailsResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchCartProductDetails.validateParams(fetchCartProductDetailsQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch featuring product.
   *
   * @param fetchFeaturingProductQuery the fetch featuring product query
   * @param completionHandler the completion handler
   */
  public void fetchFeaturingProduct(@NotNull FetchFeaturingProductQuery fetchFeaturingProductQuery,
      @NotNull CompletionHandler<FetchFeaturingProductResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchFeaturingProduct.validateParams(fetchFeaturingProductQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Update featuring product status.
   *
   * @param updateFeaturingProductStatusQuery the update featuring product status query
   * @param completionHandler the completion handler
   */
  public void updateFeaturingProductStatus(
      @NotNull UpdateFeaturingProductStatusQuery updateFeaturingProductStatusQuery,
      @NotNull CompletionHandler<UpdateFeaturingProductStatusResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      updateFeaturingProduct.validateParams(updateFeaturingProductStatusQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }
}
