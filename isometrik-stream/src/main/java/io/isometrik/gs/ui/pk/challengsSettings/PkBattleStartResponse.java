package io.isometrik.gs.ui.pk.challengsSettings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PkBattleStartResponse implements Serializable {

  @SerializedName("message")
  @Expose
  private String message;
  @SerializedName("pkId")
  @Expose
  private String pkId;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getPkId() {
    return pkId;
  }

  public void setPkId(String pkId) {
    this.pkId = pkId;
  }
}
