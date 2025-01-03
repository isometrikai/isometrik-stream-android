package io.isometrik.gs.listeners;

import java.util.ArrayList;
import java.util.List;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.callbacks.ViewerEventCallback;
import io.isometrik.gs.events.viewer.ViewerJoinEvent;
import io.isometrik.gs.events.viewer.ViewerLeaveEvent;
import io.isometrik.gs.events.viewer.ViewerRemoveEvent;
import io.isometrik.gs.events.viewer.ViewerTimeoutEvent;

/**
 * The class containing methods to add or remove listeners for the ViewerEventCallback{@link
 * ViewerEventCallback } and to
 * broadcast the ViewerJoinEvent{@link ViewerJoinEvent},
 * ViewerRemoveEvent{@link ViewerRemoveEvent},
 * ViewerTimeoutEvent{@link ViewerTimeoutEvent} or
 * ViewerLeaveEvent{@link
 * ViewerLeaveEvent} events to all the registered
 * listeners.
 *
 * @see ViewerEventCallback
 * @see ViewerJoinEvent
 * @see ViewerLeaveEvent
 * @see ViewerRemoveEvent
 * @see ViewerTimeoutEvent
 */
public class ViewerListenerManager {

  private final List<ViewerEventCallback> listeners;
  private final Isometrik isometrik;

  /**
   * Instantiates a new Viewer listener manager.
   *
   * @param isometrikInstance the isometrik instance
   * @see Isometrik
   */
  public ViewerListenerManager(Isometrik isometrikInstance) {
    this.listeners = new ArrayList<>();
    this.isometrik = isometrikInstance;
  }

  /**
   * Add listener.
   *
   * @param listener the ViewerEventCallback{@link ViewerEventCallback}
   * listener to be added
   * @see ViewerEventCallback
   */
  public void addListener(ViewerEventCallback listener) {
    synchronized (listeners) {
      listeners.add(listener);
    }
  }

  /**
   * Remove listener.
   *
   * @param listener the ViewerEventCallback{@link ViewerEventCallback}
   * listener to be removed
   * @see ViewerEventCallback
   */
  public void removeListener(ViewerEventCallback listener) {
    synchronized (listeners) {
      listeners.remove(listener);
    }
  }

  /**
   * @return list of ViewerEventCallback{@link ViewerEventCallback}
   * listeners currently registered
   * @see ViewerEventCallback
   */
  private List<ViewerEventCallback> getListeners() {
    List<ViewerEventCallback> tempCallbackList;
    synchronized (listeners) {
      tempCallbackList = new ArrayList<>(listeners);
    }
    return tempCallbackList;
  }

  /**
   * announce a ViewerJoinEvent to listeners.
   *
   * @param viewerJoinEvent ViewerJoinEvent{@link ViewerJoinEvent}
   * which will be broadcast to listeners.
   * @see ViewerJoinEvent
   */
  public void announce(ViewerJoinEvent viewerJoinEvent) {
    for (ViewerEventCallback viewerEventCallback : getListeners()) {
      viewerEventCallback.viewerJoined(this.isometrik, viewerJoinEvent);
    }
  }

  /**
   * announce a ViewerLeaveEvent to listeners.
   *
   * @param viewerLeaveEvent ViewerLeaveEvent{@link ViewerLeaveEvent}
   * which will be broadcast to listeners.
   * @see ViewerLeaveEvent
   */
  public void announce(ViewerLeaveEvent viewerLeaveEvent) {
    for (ViewerEventCallback viewerEventCallback : getListeners()) {
      viewerEventCallback.viewerLeft(this.isometrik, viewerLeaveEvent);
    }
  }

  /**
   * announce a ViewerRemoveEvent to listeners.
   *
   * @param viewerRemoveEvent ViewerRemoveEvent{@link ViewerRemoveEvent}
   * which will be broadcast to listeners.
   * @see ViewerRemoveEvent
   */
  public void announce(ViewerRemoveEvent viewerRemoveEvent) {
    for (ViewerEventCallback viewerEventCallback : getListeners()) {
      viewerEventCallback.viewerRemoved(this.isometrik, viewerRemoveEvent);
    }
  }

  /**
   * announce a ViewerTimeoutEvent to listeners.
   *
   * @param viewerTimeoutEvent ViewerTimeoutEvent{@link ViewerTimeoutEvent}
   * which will be broadcast to listeners.
   * @see ViewerTimeoutEvent
   */
  public void announce(ViewerTimeoutEvent viewerTimeoutEvent) {
    for (ViewerEventCallback viewerEventCallback : getListeners()) {
      viewerEventCallback.viewerTimedOut(this.isometrik, viewerTimeoutEvent);
    }
  }
}
