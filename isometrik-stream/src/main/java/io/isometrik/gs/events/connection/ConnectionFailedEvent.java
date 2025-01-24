package io.isometrik.gs.events.connection;


public class ConnectionFailedEvent {

  private final String reason;
  private final int errorCode;
  private final Throwable throwable;

  /**
   * Instantiates a new Disconnect event.
   *
   * @param mqttException the cause of the connection drop
   */
  public ConnectionFailedEvent(Exception mqttException) {
    this.errorCode = 0;
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
