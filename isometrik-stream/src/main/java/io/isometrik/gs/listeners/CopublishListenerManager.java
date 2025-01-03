package io.isometrik.gs.listeners;

import java.util.ArrayList;
import java.util.List;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.callbacks.CopublishEventCallback;
import io.isometrik.gs.events.copublish.CopublishRequestAcceptEvent;
import io.isometrik.gs.events.copublish.CopublishRequestAddEvent;
import io.isometrik.gs.events.copublish.CopublishRequestDenyEvent;
import io.isometrik.gs.events.copublish.CopublishRequestRemoveEvent;
import io.isometrik.gs.events.copublish.CopublishRequestSwitchProfileEvent;
import io.isometrik.gs.response.error.IsometrikError;

/**
 * The class containing methods to add or remove listeners for the CopublishEventCallback{@link
 * CopublishEventCallback} and to
 * broadcast the CopublishRequestAcceptEvent{@link CopublishRequestAcceptEvent},
 * CopublishRequestAddEvent{@link CopublishRequestAddEvent },
 * CopublishRequestDenyEvent{@link CopublishRequestDenyEvent },
 * CopublishRequestRemoveEvent{@link CopublishRequestRemoveEvent
 * },
 * CopublishRequestSwitchProfileEvent{@link CopublishRequestSwitchProfileEvent
 * } or
 * IsometrikError{@link
 * IsometrikError} events to all the registered listeners.
 *
 * @see CopublishEventCallback
 * @see CopublishRequestAcceptEvent
 * @see CopublishRequestAddEvent
 * @see CopublishRequestDenyEvent
 * @see CopublishRequestRemoveEvent
 * @see CopublishRequestSwitchProfileEvent
 * @see IsometrikError
 */
public class CopublishListenerManager {

  private final List<CopublishEventCallback> listeners;
  private final Isometrik isometrik;

  /**
   * Instantiates a new copublish listener manager.
   *
   * @param isometrikInstance the isometrik instance
   * @see Isometrik
   */
  public CopublishListenerManager(Isometrik isometrikInstance) {
    this.listeners = new ArrayList<>();
    this.isometrik = isometrikInstance;
  }

  /**
   * Add listener.
   *
   * @param listener the CopublishEventCallback{@link CopublishEventCallback}
   * listener to be added
   * @see CopublishEventCallback
   */
  public void addListener(CopublishEventCallback listener) {
    synchronized (listeners) {
      listeners.add(listener);
    }
  }

  /**
   * Remove listener.
   *
   * @param listener the CopublishEventCallback{@link CopublishEventCallback}
   * listener to be removed
   * @see CopublishEventCallback
   */
  public void removeListener(CopublishEventCallback listener) {
    synchronized (listeners) {
      listeners.remove(listener);
    }
  }

  /**
   * @return list of CopublishEventCallback{@link CopublishEventCallback}
   * listeners currently registered
   * @see CopublishEventCallback
   */
  private List<CopublishEventCallback> getListeners() {
    List<CopublishEventCallback> tempCallbackList;
    synchronized (listeners) {
      tempCallbackList = new ArrayList<>(listeners);
    }
    return tempCallbackList;
  }

  /**
   * announce a CopublishRequestAcceptEvent to listeners.
   *
   * @param copublishRequestAcceptEvent CopublishRequestAcceptEvent{@link
   * CopublishRequestAcceptEvent}
   * which will be broadcast to listeners.
   * @see CopublishRequestAcceptEvent
   */
  public void announce(CopublishRequestAcceptEvent copublishRequestAcceptEvent) {
    for (CopublishEventCallback copublishEventCallback : getListeners()) {
      copublishEventCallback.copublishRequestAccepted(this.isometrik, copublishRequestAcceptEvent);
    }
  }

  /**
   * announce a CopublishRequestAddEvent to listeners.
   *
   * @param copublishRequestAddEvent CopublishRequestAddEvent{@link
   * CopublishRequestAddEvent}
   * which will be broadcast to listeners.
   * @see CopublishRequestAddEvent
   */
  public void announce(CopublishRequestAddEvent copublishRequestAddEvent) {
    for (CopublishEventCallback copublishEventCallback : getListeners()) {
      copublishEventCallback.copublishRequestAdded(this.isometrik, copublishRequestAddEvent);
    }
  }

  /**
   * announce a CopublishRequestDenyEvent to listeners.
   *
   * @param copublishRequestDenyEvent CopublishRequestDenyEvent{@link
   * CopublishRequestDenyEvent}
   * which will be broadcast to listeners.
   * @see CopublishRequestDenyEvent
   */
  public void announce(CopublishRequestDenyEvent copublishRequestDenyEvent) {
    for (CopublishEventCallback copublishEventCallback : getListeners()) {
      copublishEventCallback.copublishRequestDenied(this.isometrik, copublishRequestDenyEvent);
    }
  }

  /**
   * announce a CopublishRequestRemoveEvent to listeners.
   *
   * @param copublishRequestRemoveEvent CopublishRequestRemoveEvent{@link
   * CopublishRequestRemoveEvent}
   * which will be broadcast to listeners.
   * @see CopublishRequestRemoveEvent
   */
  public void announce(CopublishRequestRemoveEvent copublishRequestRemoveEvent) {
    for (CopublishEventCallback copublishEventCallback : getListeners()) {
      copublishEventCallback.copublishRequestRemoved(this.isometrik, copublishRequestRemoveEvent);
    }
  }

  /**
   * announce a CopublishRequestSwitchProfileEvent to listeners.
   *
   * @param copublishRequestSwitchProfileEvent CopublishRequestSwitchProfileEvent{@link
   * CopublishRequestSwitchProfileEvent}
   * which will be broadcast to listeners.
   * @see CopublishRequestSwitchProfileEvent
   */
  public void announce(CopublishRequestSwitchProfileEvent copublishRequestSwitchProfileEvent) {
    for (CopublishEventCallback copublishEventCallback : getListeners()) {
      copublishEventCallback.switchProfile(this.isometrik, copublishRequestSwitchProfileEvent);
    }
  }
}
