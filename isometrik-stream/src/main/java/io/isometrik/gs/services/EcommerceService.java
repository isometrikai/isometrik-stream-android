package io.isometrik.gs.services;

import java.util.Map;

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
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;

public interface EcommerceService {

  /**
   * Add product to cart call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<AddProductToCartResult>
   * @see AddProductToCartResult
   */
  @POST("/streaming/v2/ecommerce/cart/product")
  Call<AddProductToCartResult> addProductToCart(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * @param headers the headers
   * @return the Call<FetchProductsInCartResult>
   * @see FetchProductsInCartResult
   */
  @GET("/streaming/v2/ecommerce/cart/products")
  Call<FetchProductsInCartResult> fetchProductsInCart(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * @param headers the headers
   * @return the Call<RemoveProductFromCartResult>
   * @see RemoveProductFromCartResult
   */
  @DELETE("/streaming/v2/ecommerce/cart/product")
  Call<RemoveProductFromCartResult> removeProductFromCart(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Update quantity of a product in cart call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<UpdateProductQuantityInCartResult>
   * @see UpdateProductQuantityInCartResult
   */
  @PATCH("/streaming/v2/ecommerce/cart/product/quantity")
  Call<UpdateProductQuantityInCartResult> updateProductQuantityInCart(
      @HeaderMap Map<String, String> headers, @Body Map<String, Object> bodyParams);

  /**
   * Checkout products added to cart call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<CheckoutCartResult>
   * @see CheckoutCartResult
   */
  @POST("/streaming/v2/ecommerce/checkout")
  Call<CheckoutCartResult> checkoutCart(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Verify external checkout request call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<VerifyCheckoutResult>
   * @see VerifyCheckoutResult
   */
  @POST("/streaming/v2/ecommerce/checkout/verify")
  Call<VerifyCheckoutResult> verifyCheckout(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Add a product call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<AddProductResult>
   * @see AddProductResult
   */
  @POST("/streaming/v2/ecommerce/product")
  Call<AddProductResult> addProduct(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * @param headers the headers
   * @return the Call<DeleteProductResult>
   * @see DeleteProductResult
   */
  @DELETE("/streaming/v2/ecommerce/product")
  Call<DeleteProductResult> deleteProduct(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * @param headers the headers
   * @return the Call<FetchProductDetailsResult>
   * @see FetchProductDetailsResult
   */
  @GET("/streaming/v2/ecommerce/product/details")
  Call<FetchProductDetailsResult> fetchProductDetails(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * @param headers the headers
   * @return the Call<FetchProductsResult>
   * @see FetchProductsResult
   */
  @GET("/streaming/v2/ecommerce/products")
  Call<FetchProductsResult> fetchProducts(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Update a product call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<UpdateProductResult>
   * @see UpdateProductResult
   */
  @PATCH("/streaming/v2/ecommerce/product")
  Call<UpdateProductResult> updateProduct(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Update inventory of a product call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<UpdateProductInventoryResult>
   * @see UpdateProductInventoryResult
   */
  @PATCH("/streaming/v2/ecommerce/product/inventory")
  Call<UpdateProductInventoryResult> updateProductInventory(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * @param headers the headers
   * @return the Call<FetchLinkedProductsResult>
   * @see FetchLinkedProductsResult
   */
  @GET("/streaming/v2/ecommerce/session/products")
  Call<FetchLinkedProductsResult> fetchLinkedProducts(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * @param headers the headers
   * @return the Call<FetchCartProductDetailsResult>
   * @see FetchProductDetailsResult
   */
  @GET("/streaming/v2/ecommerce/session/product/details")
  Call<FetchCartProductDetailsResult> fetchCartProductDetails(
      @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);

  /**
   * @param headers the headers
   * @return the Call<FetchFeaturingProductResult>
   * @see FetchFeaturingProductResult
   */
  @GET("/streaming/v2/ecommerce/featuring/product")
  Call<FetchFeaturingProductResult> fetchFeaturingProductQuery(
      @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);

  /**
   * Update featuring product status in a broadcast call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<UpdateFeaturingProductStatusResult>
   * @see UpdateFeaturingProductStatusResult
   */
  @PUT("/streaming/v2/ecommerce/featuring/product")
  Call<UpdateFeaturingProductStatusResult> updateFeaturingProductStatus(
      @HeaderMap Map<String, String> headers, @Body Map<String, Object> bodyParams);
}
