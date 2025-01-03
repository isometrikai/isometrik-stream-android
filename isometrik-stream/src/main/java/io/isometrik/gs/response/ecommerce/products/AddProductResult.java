package io.isometrik.gs.response.ecommerce.products;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.ecommerce.products.AddProductQuery;

/**
 * The class to parse add product result of adding a product query
 * AddProductQuery{@link AddProductQuery}.
 *
 * @see AddProductQuery
 */
public class AddProductResult implements Serializable {

  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("productId")
  @Expose
  private String productId;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets productId.
   *
   * @return the id of the newly created product
   */
  public String getProductId() {
    return productId;
  }
}