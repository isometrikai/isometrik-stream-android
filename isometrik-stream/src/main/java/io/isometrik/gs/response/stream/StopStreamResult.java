package io.isometrik.gs.response.stream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.stream.StopStreamQuery;

/**
 * The class to parse stop stream result of ending a stream group query StopStreamQuery{@link
 * StopStreamQuery}.
 *
 * @see StopStreamQuery
 */
public class StopStreamResult implements Serializable {
  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("recordingUrl")
  @Expose
  private String recordingUrl;
  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets recordingUrl.
   *
   * @return the recording url of the broadcast if recording was enabled
   */
  public String getRecordingUrl() {
    return recordingUrl;
  }
}
