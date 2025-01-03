package io.isometrik.gs.callbacks;

import org.jetbrains.annotations.NotNull;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.events.connection.ConnectEvent;
import io.isometrik.gs.events.connection.ConnectionFailedEvent;
import io.isometrik.gs.events.connection.DisconnectEvent;
import io.isometrik.gs.response.error.IsometrikError;

/**
 * The abstract class for the connection event callback, with methods for connected made, connection
 * fail and connection drop.
 */
public abstract class ConnectionEventCallback {

  /**
   * Disconnected.
   *
   * @param isometrik the isometrik instance
   * @param disconnectEvent the disconnect event
   * @see Isometrik
   * @see DisconnectEvent
   */
  public abstract void disconnected(@NotNull Isometrik isometrik,
      @NotNull DisconnectEvent disconnectEvent);

  /**
   * Connected.
   *
   * @param isometrik the isometrik instance
   * @param connectEvent the connect event
   * @see Isometrik
   * @see ConnectEvent
   */
  public abstract void connected(@NotNull Isometrik isometrik, @NotNull ConnectEvent connectEvent);

  /**
   * Connection failed.
   *
   * @param isometrik the isometrik instance
   * @param isometrikError the isometrik error instance
   * @see Isometrik
   * @see IsometrikError
   */
  public abstract void connectionFailed(@NotNull Isometrik isometrik,
      @NotNull IsometrikError isometrikError);

  /**
   * Connected.
   *
   * @param isometrik the isometrik instance
   * @param connectionFailedEvent the connection failed event
   * @see Isometrik
   * @see ConnectionFailedEvent
   */
  public abstract void failedToConnect(@NotNull Isometrik isometrik,
      @NotNull ConnectionFailedEvent connectionFailedEvent);
}
