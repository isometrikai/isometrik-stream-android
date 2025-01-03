package io.isometrik.gs.response.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.message.AddMetadataQuery;

/**
 * The class to parse result of adding metadata in a stream group query
 * AddMetadataQuery{@link AddMetadataQuery}.
 *
 * @see AddMetadataQuery
 */
public class AddMetadataResult implements Serializable {

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
