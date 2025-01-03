package io.isometrik.gs.listeners;

import java.util.ArrayList;
import java.util.List;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.callbacks.PresenceEventCallback;
import io.isometrik.gs.events.presence.PresenceStreamStartEvent;
import io.isometrik.gs.events.presence.PresenceStreamStopEvent;

/**
 * The class containing methods to add or remove listeners for the PresenceEventCallback{@link
 * PresenceEventCallback } and to
 * broadcast the PresenceStreamStartEvent{@link PresenceStreamStartEvent}
 * or
 * PresenceStreamStopEvent{@link
 * PresenceStreamStopEvent} events to all the registered
 * listeners.
 *
 * @see PresenceEventCallback
 * @see PresenceStreamStartEvent
 * @see PresenceStreamStopEvent
 */
public class PresenceListenerManager {

  private final List<PresenceEventCallback> listeners;
  private final Isometrik isometrik;

  /**
   * Instantiates a new Presence listener manager.
   *
   * @param isometrikInstance the isometrik instance
   * @see Isometrik
   */
  public PresenceListenerManager(Isometrik isometrikInstance) {
    this.listeners = new ArrayList<>();
    this.isometrik = isometrikInstance;
  }

  /**
   * Add listener.
   *
   * @param listener the PresenceEventCallback{@link PresenceEventCallback}
   * listener to be added
   * @see PresenceEventCallback
   */
  public void addListener(PresenceEventCallback listener) {
    synchronized (listeners) {
      listeners.add(listener);
    }
  }

  /**
   * Remove listener.
   *
   * @param listener the PresenceEventCallback{@link PresenceEventCallback}
   * listener to be removed
   * @see PresenceEventCallback
   */
  public void removeListener(PresenceEventCallback listener) {
    synchronized (listeners) {
      listeners.remove(listener);
    }
  }

  /**
   * @return list of PresenceEventCallback{@link PresenceEventCallback}
   * listeners currently registered
   * @see PresenceEventCallback
   */
  private List<PresenceEventCallback> getListeners() {
    List<PresenceEventCallback> tempCallbackList;
    synchronized (listeners) {
      tempCallbackList = new ArrayList<>(listeners);
    }
    return tempCallbackList;
  }

  /**
   * announce a PresenceStreamStartEvent to listeners.
   *
   * @param presenceStreamStartEvent PresenceStreamStartEvent{@link PresenceStreamStartEvent}
   * which will be broadcast to listeners.
   * @see PresenceStreamStartEvent
   */
  public void announce(PresenceStreamStartEvent presenceStreamStartEvent) {
    for (PresenceEventCallback streamEventCallback : getListeners()) {
      streamEventCallback.streamStarted(this.isometrik, presenceStreamStartEvent);
    }
  }

  /**
   * announce a PresenceStreamStopEvent to listeners.
   *
   * @param presenceStreamStopEvent PresenceStreamStopEvent{@link PresenceStreamStopEvent}
   * which will be broadcast to listeners.
   * @see PresenceStreamStopEvent
   */
  public void announce(PresenceStreamStopEvent presenceStreamStopEvent) {
    for (PresenceEventCallback streamEventCallback : getListeners()) {
      streamEventCallback.streamStopped(this.isometrik, presenceStreamStopEvent);
    }
  }
}
