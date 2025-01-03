package io.isometrik.gs.response.ecommerce.products;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import io.isometrik.gs.builder.ecommerce.products.FetchProductsQuery;
import io.isometrik.gs.response.ecommerce.util.Product;

/**
 * The class to parse fetch products result of fetching products query
 * FetchProductsQuery{@link FetchProductsQuery}.
 *
 * @see FetchProductsQuery
 */
public class FetchProductsResult implements Serializable {

  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("products")
  @Expose
  private ArrayList<Product> products;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets products.
   *
   * @return the list of products
   * @see Product
   */
  public ArrayList<Product> getProducts() {
    return products;
  }
}