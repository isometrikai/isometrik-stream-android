package io.isometrik.gs.ivs.preview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;

public class BroadcastCueMetadata implements Serializable {

  @SerializedName("streamId")
  @Expose
  private String streamId;

  @SerializedName("senderId")
  @Expose
  private String senderId;

  @SerializedName("timestamp")
  @Expose
  private long timestamp;

  @SerializedName("metadata")
  @Expose
  private JSONObject metadata;

  public String getStreamId() {
    return streamId;
  }

  public String getSenderId() {
    return senderId;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public JSONObject getMetadata() {

    return metadata;
  }
}
