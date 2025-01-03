package io.isometrik.gs.events.connection;

import org.eclipse.paho.client.mqttv3.MqttException;

public class ConnectionFailedEvent {

  private final String reason;
  private final int errorCode;
  private final Throwable throwable;

  /**
   * Instantiates a new Disconnect event.
   *
   * @param mqttException the cause of the connection drop
   */
  public ConnectionFailedEvent(MqttException mqttException) {
    this.errorCode = mqttException.getReasonCode();
    this.reason = mqttException.getMessage();
    this.throwable = mqttException.getCause();
  }

  public String getReason() {
    return reason;
  }

  public int getErrorCode() {
    return errorCode;
  }

  public Throwable getThrowable() {
    return throwable;
  }
}
