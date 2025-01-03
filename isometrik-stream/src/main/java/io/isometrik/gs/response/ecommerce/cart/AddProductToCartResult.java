package io.isometrik.gs.response.ecommerce.cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.ecommerce.cart.AddProductToCartQuery;

/**
 * The class to parse add product to cart result of adding a product to cart query
 * AddProductToCartQuery{@link AddProductToCartQuery}.
 *
 * @see AddProductToCartQuery
 */
public class AddProductToCartResult implements Serializable {

  @SerializedName("msg")
  @Expose
  private String message;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }
}