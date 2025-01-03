package io.isometrik.gs.response.ecommerce.cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.ecommerce.cart.RemoveProductFromCartQuery;

/**
 * The class to parse remove product from cart result of removing a product from cart query
 * RemoveProductFromCartQuery{@link RemoveProductFromCartQuery}.
 *
 * @see RemoveProductFromCartQuery
 */
public class RemoveProductFromCartResult implements Serializable {

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