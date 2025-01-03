package io.isometrik.gs.response.ecommerce.session;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.ecommerce.session.FetchCartProductDetailsQuery;
import io.isometrik.gs.response.ecommerce.util.Product;

/**
 * The class to parse fetch product cart details result of fetching cart product details query
 * FetchCartProductDetailsQuery{@link FetchCartProductDetailsQuery}.
 *
 * @see FetchCartProductDetailsQuery
 */
public class FetchCartProductDetailsResult implements Serializable {

  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("productDetails")
  @Expose
  private CartProduct productDetails;

  public static class CartProduct extends Product {

    @SerializedName("productAlreadyInCart")
    @Expose
    private boolean productAlreadyInCart;

    @SerializedName("quantity")
    @Expose
    private Integer quantity;

    @SerializedName("timestamp")
    @Expose
    private Long timestamp;

    /**
     * Gets productAlreadyInCart.
     *
     * @return Whether product has already been added to cart by the given user
     */
    public boolean isProductAlreadyInCart() {
      return productAlreadyInCart;
    }

    /**
     * Gets quantity.
     *
     * @return the number of units added to cart, if product is already added to cart by the given user
     */
    public Integer getQuantity() {
      return quantity;
    }

    /**
     * Gets timestamp.
     *
     * @return the time at which product was added to cart, if product is already added to cart by
     * the given user
     */
    public Long getTimestamp() {
      return timestamp;
    }
  }

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets productDetails.
   *
   * @return the class containing details of the product
   */
  public CartProduct getProductDetails() {
    return productDetails;
  }
}