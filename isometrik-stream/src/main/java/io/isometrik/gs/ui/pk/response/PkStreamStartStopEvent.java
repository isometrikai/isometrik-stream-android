package io.isometrik.gs.ui.pk.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import io.isometrik.gs.ui.streams.event.LiveStreamsResponse;

public class PkStreamStartStopEvent implements Serializable {


    @SerializedName("createdTs")
    @Expose
    private String timeStamp;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("timeInMin")
    @Expose
    private int timeInMin;
    @SerializedName("pkId")
    @Expose
    private String pkId;
    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("streamData")
    @Expose
    private List<LiveStreamsResponse.Stream> streamDataList;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTimeInMin() {
        return timeInMin;
    }

    public void setTimeInMin(int timeInMin) {
        this.timeInMin = timeInMin;
    }

    public String getPkId() {
        return pkId;
    }

    public void setPkId(String pkId) {
        this.pkId = pkId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public LiveStreamsResponse.Stream getStreamData() {
        if (!streamDataList.isEmpty()) {
            return streamDataList.get(0);
        } else {
           return new LiveStreamsResponse.Stream();
        }
    }

    public void setStreamData(List<LiveStreamsResponse.Stream> streamData) {
        this.streamDataList = streamData;
    }
}
