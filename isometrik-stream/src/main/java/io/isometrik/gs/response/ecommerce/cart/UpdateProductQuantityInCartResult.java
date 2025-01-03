package io.isometrik.gs.response.ecommerce.cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.ecommerce.cart.UpdateProductQuantityInCartQuery;

/**
 * The class to parse update products quantity in cart result of updating products quantity in cart
 * query
 * UpdateProductQuantityInCartQuery{@link UpdateProductQuantityInCartQuery}.
 *
 * @see UpdateProductQuantityInCartQuery
 */
public class UpdateProductQuantityInCartResult implements Serializable {

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