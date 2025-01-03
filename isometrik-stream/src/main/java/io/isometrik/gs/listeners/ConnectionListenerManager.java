package io.isometrik.gs.listeners;

import java.util.ArrayList;
import java.util.List;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.callbacks.ConnectionEventCallback;
import io.isometrik.gs.events.connection.ConnectEvent;
import io.isometrik.gs.events.connection.ConnectionFailedEvent;
import io.isometrik.gs.events.connection.DisconnectEvent;
import io.isometrik.gs.response.error.IsometrikError;

/**
 * The class containing methods to add or remove listeners for the ConnectionEventCallback{@link
 * ConnectionEventCallback} and to
 * broadcast the ConnectEvent{@link ConnectEvent},
 * DisconnectEvent{@link DisconnectEvent } or
 * IsometrikError{@link
 * IsometrikError} events to all the registered listeners.
 *
 * @see ConnectionEventCallback
 * @see ConnectEvent
 * @see DisconnectEvent
 * @see IsometrikError
 */
public class ConnectionListenerManager {
  private final List<ConnectionEventCallback> listeners;
  private final Isometrik isometrik;

  /**
   * Instantiates a new Connection listener manager.
   *
   * @param isometrikInstance the isometrik instance
   * @see Isometrik
   */
  public ConnectionListenerManager(Isometrik isometrikInstance) {
    this.listeners = new ArrayList<>();
    this.isometrik = isometrikInstance;
  }

  /**
   * Add listener.
   *
   * @param listener the ConnectionEventCallback{@link ConnectionEventCallback}
   * listener to be added
   * @see ConnectionEventCallback
   */
  public void addListener(ConnectionEventCallback listener) {
    synchronized (listeners) {
      listeners.add(listener);
    }
  }

  /**
   * Remove listener.
   *
   * @param listener the ConnectionEventCallback{@link ConnectionEventCallback}
   * listener to be added
   * @see ConnectionEventCallback
   */
  public void removeListener(ConnectionEventCallback listener) {
    synchronized (listeners) {
      listeners.remove(listener);
    }
  }

  /**
   * @return list of ConnectionEventCallback{@link ConnectionEventCallback}
   * listeners currently registered
   * @see ConnectionEventCallback
   */
  private List<ConnectionEventCallback> getListeners() {
    List<ConnectionEventCallback> tempCallbackList;
    synchronized (listeners) {
      tempCallbackList = new ArrayList<>(listeners);
    }
    return tempCallbackList;
  }

  /**
   * announce a ConnectEvent to listeners.
   *
   * @param connectEvent ConnectEvent{@link ConnectEvent} which
   * will be broadcast to listeners.
   * @see ConnectEvent
   */
  public void announce(ConnectEvent connectEvent) {
    for (ConnectionEventCallback connectionEventCallback : getListeners()) {
      connectionEventCallback.connected(this.isometrik, connectEvent);
    }
  }

  /**
   * announce a DisconnectEvent to listeners.
   *
   * @param disconnectEvent DisconnectEvent{@link DisconnectEvent}
   * which will be broadcast to listeners.
   * @see DisconnectEvent
   */
  public void announce(DisconnectEvent disconnectEvent) {
    for (ConnectionEventCallback connectionEventCallback : getListeners()) {
      connectionEventCallback.disconnected(this.isometrik, disconnectEvent);
    }
  }

  /**
   * announce a ConnectionFailedEvent to listeners.
   *
   * @param connectionFailedEvent ConnectionFailedEvent{@link ConnectionFailedEvent}
   * which
   * will be broadcast to listeners.
   * @see ConnectionFailedEvent
   */
  public void announce(ConnectionFailedEvent connectionFailedEvent) {
    for (ConnectionEventCallback connectionEventCallback : getListeners()) {
      connectionEventCallback.failedToConnect(this.isometrik, connectionFailedEvent);
    }
  }

  /**
   * announce a IsometrikError to listeners.
   *
   * @param isometrikError IsometrikError{@link IsometrikError} which
   * will be broadcast to listeners.
   * @see IsometrikError
   */
  public void announce(IsometrikError isometrikError) {
    for (ConnectionEventCallback connectionEventCallback : getListeners()) {
      connectionEventCallback.connectionFailed(this.isometrik, isometrikError);
    }
  }
}
