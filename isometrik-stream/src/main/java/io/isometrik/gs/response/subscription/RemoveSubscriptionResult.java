package io.isometrik.gs.response.subscription;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.subscription.RemoveSubscriptionQuery;

/**
 * The class to parse remove subscription result of the unsubscribe from receiving stream presence
 * events for stream start or stop query RemoveSubscriptionQuery{@link
 * RemoveSubscriptionQuery}.
 *
 * @see RemoveSubscriptionQuery
 */
public class RemoveSubscriptionResult implements Serializable {

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