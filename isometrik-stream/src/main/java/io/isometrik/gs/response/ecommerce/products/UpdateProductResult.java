package io.isometrik.gs.response.ecommerce.products;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

import io.isometrik.gs.builder.ecommerce.products.UpdateProductQuery;

/**
 * The class to parse update product result of updating a product query
 * UpdateProductQuery{@link UpdateProductQuery}.
 *
 * @see UpdateProductQuery
 */
public class UpdateProductResult implements Serializable {

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