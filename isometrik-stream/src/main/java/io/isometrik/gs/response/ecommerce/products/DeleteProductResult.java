package io.isometrik.gs.response.ecommerce.products;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

import io.isometrik.gs.builder.ecommerce.products.DeleteProductQuery;

/**
 * The class to parse delete product result of deleting a product query
 * DeleteProductQuery{@link DeleteProductQuery}.
 *
 * @see DeleteProductQuery
 */
public class DeleteProductResult implements Serializable {

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