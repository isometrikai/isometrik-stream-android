package io.isometrik.gs.response.subscription;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.subscription.AddSubscriptionQuery;

/**
 * The class to parse add subscription result of the subscribing to receiving stream presence
 * events for stream start or stop query AddSubscriptionQuery{@link AddSubscriptionQuery}.
 *
 * @see AddSubscriptionQuery
 */
public class AddSubscriptionResult implements Serializable {

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