package io.isometrik.gs.response.ecommerce.products;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.ecommerce.products.UpdateProductInventoryQuery;

/**
 * The class to parse update product inventory result of updating a product inventory query
 * UpdateProductInventoryQuery{@link UpdateProductInventoryQuery}.
 *
 * @see UpdateProductInventoryQuery
 */
public class UpdateProductInventoryResult implements Serializable {

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