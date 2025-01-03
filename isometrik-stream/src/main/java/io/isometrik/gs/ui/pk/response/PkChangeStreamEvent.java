package io.isometrik.gs.ui.pk.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PkChangeStreamEvent implements Serializable {

  @SerializedName("streamId")
  @Expose
  private String streamId;
  @SerializedName("userId")
  @Expose
  private String userId;
  @SerializedName("viewerId")
  @Expose
  private String viewerId;
  @SerializedName("action")
  @Expose
  private String action;
  @SerializedName("intentToStop")
  @Expose
  private boolean intentToStop;
  @SerializedName("streamData")
  @Expose
  private StreamData streamData;


  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getViewerId() {
    return viewerId;
  }

  public void setViewerId(String viewerId) {
    this.viewerId = viewerId;
  }

  public String getStreamId() {
    return streamId;
  }

  public void setStreamId(String streamId) {
    this.streamId = streamId;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public boolean isIntentToStop() {
    return intentToStop;
  }

  public void setIntentToStop(boolean intentToStop) {
    this.intentToStop = intentToStop;
  }

  public StreamData getStreamData() {
    return streamData;
  }

  public void setStreamData(StreamData streamData) {
    this.streamData = streamData;
  }

  public class StreamData implements Serializable {

    @SerializedName("inviteId")
    @Expose
    private String inviteId;

    public String getInviteId() {
      return inviteId;
    }

    public void setInviteId(String inviteId) {
      this.inviteId = inviteId;
    }
  }
}
