package io.isometrik.gs.response.ecommerce.cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import io.isometrik.gs.builder.ecommerce.cart.FetchProductsInCartQuery;
import io.isometrik.gs.response.ecommerce.util.CartProduct;

/**
 * The class to parse fetch products in cart result of fetching products in cart query
 * FetchProductsInCartQuery{@link FetchProductsInCartQuery}.
 *
 * @see FetchProductsInCartQuery
 */
public class FetchProductsInCartResult implements Serializable {

  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("totalProductsCount")
  @Expose
  private int totalProductsCount;

  @SerializedName("products")
  @Expose
  private ArrayList<CartProduct> products;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets products in cart.
   *
   * @return the list of products in cart
   * @see CartProduct
   */
  public ArrayList<CartProduct> getProducts() {
    return products;
  }

  /**
   * Gets totalProductsCount.
   *
   * @return the total number of products added to cart
   */
  public int getTotalProductsCount() {
    return totalProductsCount;
  }
}