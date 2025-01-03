package io.isometrik.gs.response.ecommerce.featuring;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.ecommerce.featuring.UpdateFeaturingProductStatusQuery;

/**
 * The class to parse update featuring product status result of updating featuring product status in
 * a broadcast
 * query UpdateFeaturingProductStatusQuery{@link UpdateFeaturingProductStatusQuery}.
 *
 * @see UpdateFeaturingProductStatusQuery
 */
public class UpdateFeaturingProductStatusResult implements Serializable {

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