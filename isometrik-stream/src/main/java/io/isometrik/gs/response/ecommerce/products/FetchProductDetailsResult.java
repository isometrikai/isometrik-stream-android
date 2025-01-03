package io.isometrik.gs.response.ecommerce.products;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.ecommerce.products.FetchProductDetailsQuery;
import io.isometrik.gs.response.ecommerce.util.Product;

/**
 * The class to parse fetch product details result of fetching product details query
 * FetchProductDetailsQuery{@link FetchProductDetailsQuery}.
 *
 * @see FetchProductDetailsQuery
 */
public class FetchProductDetailsResult implements Serializable {

  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("productDetails")
  @Expose
  private Product productDetails;

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
  public Product getProductDetails() {
    return productDetails;
  }
}