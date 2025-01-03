package io.isometrik.gs.response.ecommerce.util;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.json.JSONObject;

/**
 * The class containing details of the product.
 */
public class Product {
  @SerializedName("externalProductId")
  @Expose
  private String externalProductId;

  @SerializedName("productId")
  @Expose
  private String productId;

  @SerializedName("productName")
  @Expose
  private String productName;

  @SerializedName("count")
  @Expose
  private int count;

  @SerializedName("metadata")
  @Expose
  private Object metadata;

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
   * @return the current quantity in stock of product
   */
  public int getCount() {
    return count;
  }
}
