package io.isometrik.gs.response.ecommerce.session;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import io.isometrik.gs.builder.ecommerce.session.FetchLinkedProductsQuery;
import io.isometrik.gs.response.ecommerce.util.Product;

/**
 * The class to parse fetch linked products result of fetching linked products to a stream session
 * query
 * FetchLinkedProductsQuery{@link FetchLinkedProductsQuery}.
 *
 * @see FetchLinkedProductsQuery
 */
public class FetchLinkedProductsResult implements Serializable {

  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("products")
  @Expose
  private ArrayList<LinkedProduct> products;

  public static class LinkedProduct extends Product {

    @SerializedName("featuring")
    @Expose
    private boolean featuring;

    /**
     * Gets featuring.
     *
     * @return whether given product is featuring or not
     */
    public boolean isFeaturing() {
      return featuring;
    }
  }

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets products linked to a stream group.
   *
   * @return the list of products linked to a stream group
   * @see LinkedProduct
   */
  public ArrayList<LinkedProduct> getProducts() {
    return products;
  }
}