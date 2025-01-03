package io.isometrik.gs.response.stream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import io.isometrik.gs.builder.stream.StartStreamQuery;

/**
 * The class to parse start stream result of creating a stream group query StartStreamQuery{@link
 * StartStreamQuery}.
 *
 * @see StartStreamQuery
 */
public class StartStreamResult implements Serializable {

  @SerializedName("streamId")
  @Expose
  private String streamId;
  @SerializedName("msg")
  @Expose
  private String message;
  @SerializedName("startTime")
  @Expose
  private long startTime;
  @SerializedName("rtcToken")
  @Expose
  private String rtcToken;
  @SerializedName("ingestEndpoint")
  @Expose
  private String ingestEndpoint;
  @SerializedName("streamKey")
  @Expose
  private String streamKey;

  @SerializedName("restreamEndpoints")
  @Expose
  private ArrayList<String> restreamEndpoints;
  @SerializedName("uid")
  @Expose
  private long userUid;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets start time.
   *
   * @return the time at which stream group was created
   */
  public long getStartTime() {
    return startTime;
  }

  /**
   * Gets rtc token.
   *
   * @return the rtc token received
   */
  public String getRtcToken() {
    return rtcToken;
  }

  /**
   * Gets ingest endpoint.
   *
   * @return the ingest endpoint to be used for ingesting media to server,if stream is non multi-live
   */
  public String getIngestEndpoint() {
    return ingestEndpoint;
  }

  /**
   * Gets stream key.
   *
   * @return the stream key to be used for ingesting to endpoint,if stream is non multi-live
   */
  public String getStreamKey() {
    return streamKey;
  }

  /**
   * Gets restream endpoints.
   *
   * @return the list of the stream endpoints, where to restream the broadcast.
   */
  public ArrayList<String> getRestreamEndpoints() {
    return restreamEndpoints;
  }

  public long getUserUid() {
    return userUid;
  }
}
