package io.isometrik.gs.response.copublish;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.copublish.DenyCopublishRequestQuery;

/**
 * The class to parse deny copublish request result of denying a copublish request in a stream
 * group
 * query DenyCopublishRequestQuery{@link DenyCopublishRequestQuery}.
 *
 * @see DenyCopublishRequestQuery
 */
public class DenyCopublishRequestResult implements Serializable {

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