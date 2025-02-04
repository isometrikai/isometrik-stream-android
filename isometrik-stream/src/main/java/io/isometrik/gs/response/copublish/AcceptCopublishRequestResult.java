package io.isometrik.gs.response.copublish;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.copublish.AcceptCopublishRequestQuery;

/**
 * The class to parse accept copublish request result of accepting a copublish request in a stream
 * group query
 * AcceptCopublishRequestQuery{@link AcceptCopublishRequestQuery}.
 *
 * @see AcceptCopublishRequestQuery
 */
public class AcceptCopublishRequestResult implements Serializable {

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