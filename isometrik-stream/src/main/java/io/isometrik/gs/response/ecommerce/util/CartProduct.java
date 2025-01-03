package io.isometrik.gs.response.ecommerce.util;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

/**
 * The class containing details of the product in cart.
 */
public class CartProduct {
  @SerializedName("externalProductId")
  @Expose
  private String externalProductId;

  @SerializedName("productId")
  @Expose
  private String productId;

  @SerializedName("productName")
  @Expose
  private String productName;

  @SerializedName("quantity")
  @Expose
  private int quantity;

  @SerializedName("metadata")
  @Expose
  private Object metadata;

  @SerializedName("timestamp")
  @Expose
  private long timestamp;

  /**
   * Gets metadata.
   *
   * @return the metadata of the product
   */
  public JSONObject getMetadata() {

    try {
      return new JSONObject(new Gson().toJson(metadata));
    } catch (Exception ignore) {
      return new JSONObject();
    }
  }

  /**
   * Gets externalProductId.
   *
   * @return the external id of the product
   */
  public String getExternalProductId() {
    return externalProductId;
  }

  /**
   * Gets productId.
   *
   * @return the id of product
   */
  public String getProductId() {
    return productId;
  }

  /**
   * Gets productName.
   *
   * @return the name of product
   */
  public String getProductName() {
    return productName;
  }

  /**
   * Gets count.
   *
   * @return the number of units added to cart
   */
  public int getQuantity() {
    return quantity;
  }

  /**
   * Gets timestamp.
   *
   * @return the epoch time when product was added to cart
   */
  public long getTimestamp() {
    return timestamp;
  }
}
