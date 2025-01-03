
package io.isometrik.gs.ui.pk.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PkStreamStopResponse implements Serializable {

    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("pkId")
    @Expose
    private String pkId;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPkId() {
        return pkId;
    }

    public void setPkId(String pkId) {
        this.pkId = pkId;
    }

}
