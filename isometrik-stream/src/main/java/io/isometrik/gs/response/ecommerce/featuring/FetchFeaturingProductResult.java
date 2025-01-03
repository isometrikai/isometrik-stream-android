package io.isometrik.gs.response.ecommerce.featuring;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.ecommerce.featuring.FetchFeaturingProductQuery;
import io.isometrik.gs.response.ecommerce.util.Product;

/**
 * The class to parse fetch featuring product details result of fetching featuring product details
 * query
 * FetchFeaturingProductQuery{@link FetchFeaturingProductQuery}.
 *
 * @see FetchFeaturingProductQuery
 */
public class FetchFeaturingProductResult implements Serializable {

  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("productDetails")
  @Expose
  private FeaturingProduct productDetails;

  public static class FeaturingProduct extends Product {

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
  public FeaturingProduct getProductDetails() {
    return productDetails;
  }
}